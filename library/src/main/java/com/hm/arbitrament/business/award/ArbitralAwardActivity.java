package com.hm.arbitrament.business.award;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;

import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.tools.KeyboardUtil;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.datepicker.CityPickerDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * 裁决书页面
 */
public class ArbitralAwardActivity extends BaseActivity<ArbitralAwardPresenter> implements ArbitralAwardContract.View {

    //仲裁申请编号
    public static final String EXTRA_KEY_ARB_NO = "arb_no";

    @BindView(R2.id.topBar)
    HMTopBarView mTopBarView;
    @BindView(R2.id.smartrl_award_list)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R2.id.rv_award_content)
    RecyclerView mRvAward;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBarView;
    @BindView(R2.id.viewStub_apply_input)
    ViewStub mViewStubApplyInput;

    private View mInputContainer;
    private EditText mEtName;
    private EditText mEtMobile;
    private TextView mTvCity;
    private EditText mEtArr;
    private CityPickerDialog mCityPicker;

    private ArbitralAwardListAdapter mAdapter;

    private String mArbNo;


    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_arbitral_award;
    }

    @Override
    protected ArbitralAwardPresenter initPresenter() {
        return new ArbitralAwardPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mArbNo = getIntent().getStringExtra(EXTRA_KEY_ARB_NO);
        if (bundle != null) {
            mArbNo = bundle.getString(EXTRA_KEY_ARB_NO);
        }

        mBottomBarView.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                showApplyInputView();
            }
        });
        mAdapter = new ArbitralAwardListAdapter();
        mRvAward.setLayoutManager(new LinearLayoutManager(this));
        mRvAward.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.refreshApplyHistoryList(mArbNo);
            }
        });

        mRefreshLayout.autoRefresh();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_ARB_NO, mArbNo);
    }

    @Override
    public void finishRefresh() {
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showApplyList(List<ArbitralAwardListAdapter.IArbitralAwardListItem> list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void showApplyInputView() {
        if (mInputContainer == null) {
            mInputContainer = mViewStubApplyInput.inflate();
            mEtName = mInputContainer.findViewById(R.id.et_award_name);
            mEtMobile = mInputContainer.findViewById(R.id.et_award_mobile);
            mTvCity = mInputContainer.findViewById(R.id.tv_award_city);
            mEtArr = mInputContainer.findViewById(R.id.et_award_addr);
            mInputContainer.findViewById(R.id.ll_award_city).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCityPicker == null) {
                        mCityPicker = new CityPickerDialog.Builder(ArbitralAwardActivity.this)
                                .setTitle("城市选择")
                                .setOnCityConfirmListener(new CityPickerDialog.OnCityConfirmListener() {
                                    @Override
                                    public void onConfirm(String s, String s1, String s2) {
                                        mTvCity.setText(s + s1 + s2);
                                    }
                                })
                                .create();
                    }
                    mCityPicker.show();
                }
            });
            HMBottomBarView bottomBarView = findViewById(R.id.bottomBar_apply);
            bottomBarView.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
                @Override
                public void onClickTitle() {
                    String name = mEtName.getText().toString();
                    String mobile = mEtMobile.getText().toString();
                    String city = mTvCity.getText().toString();
                    String addr = mEtArr.getText().toString();
                    mPresenter.submitApplyInfo(mArbNo, name, mobile, city, addr);
                }
            });
            bottomBarView.setOnBackClickListener(new HMBottomBarView.OnBackClickListener() {
                @Override
                public void onClickBack() {
                    if (mAdapter.getData() == null || mAdapter.getData().isEmpty()) {
                        finish();
                    } else {
                        mInputContainer.setVisibility(View.GONE);
                    }
                }
            });
        }
        mInputContainer.setVisibility(View.VISIBLE);
        mEtName.requestFocus();
        KeyboardUtil.toggleKeyboard(this);
    }

    @Override
    public void applySucc() {
        if (mAdapter.getData() == null || mAdapter.getData().isEmpty()) {
            finish();
        } else {
            mInputContainer.setVisibility(View.GONE);
            mRefreshLayout.autoRefresh();
        }
    }

}
