package com.hm.arbitrament.business.apply.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.bean.ElecEvidenceRes;
import com.hm.arbitrament.bean.IOUExtResult;
import com.hm.arbitrament.business.apply.SelectValidEvidenceContract;
import com.hm.arbitrament.business.apply.presenter.SelectValidEvidencePresenter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 有效凭证
 */
public class SelectValidEvidenceActivity extends BaseActivity<SelectValidEvidencePresenter> implements SelectValidEvidenceContract.View {

    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";
    public static final int REQ_SELECT_EVIDENCE_DETAIL = 100;

    private String mIouId;
    private String mJustId;

    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.init_loading)
    HMLoadingView mInitLoading;
    @BindView(R2.id.rv_evidence)
    RecyclerView mRvEvidence;
    @BindView(R2.id.btn_ok)
    Button mBtnOk;

    EvidenceAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_select_valid_evidence;
    }

    @Override
    protected SelectValidEvidencePresenter initPresenter() {
        return new SelectValidEvidencePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        if (bundle != null) {
            mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
            mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        }
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                NavigationHelper.toUploadEvidence(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        mAdapter = new EvidenceAdapter();
        mRvEvidence.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ElecEvidenceRes item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                if (R.id.rl_content == view.getId()) {
                    mAdapter.addOrRemoveCheck(item);
                    if (mAdapter.getSelectObjects().isEmpty()) {
                        mBtnOk.setEnabled(false);
                    } else {
                        mBtnOk.setEnabled(true);
                    }
                } else if (R.id.iv_arrow == view.getId()) {
                    NavigationHelper.toSelectValidEvidenceDetailActivity(mContext, REQ_SELECT_EVIDENCE_DETAIL
                            , item, mAdapter.getSelectObjects().contains(item));
                }
            }
        });
        mRvEvidence.setAdapter(mAdapter);
        mInitLoading.showDataLoading();
        mPresenter.getEvidenceList(mIouId, mJustId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_SELECT_EVIDENCE_DETAIL == requestCode) {
            ElecEvidenceRes item = data.getParcelableExtra(SelectValidEvidenceDetailActivity.EXTRA_KEY_ITEM);
            if (item == null) {
                return;
            }
            HashSet<ElecEvidenceRes> selectObjects = mAdapter.getSelectObjects();
            if (RESULT_OK == resultCode) {
                selectObjects.add(item);
            } else {
                selectObjects.remove(item);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R2.id.btn_ok)
    public void onClick() {
        NavigationHelper.toInputApplyInfo(mContext);
    }

    @Override
    public void showInit() {
        mInitLoading.setVisibility(View.VISIBLE);
        mInitLoading.startLoadingAnim();
    }

    @Override
    public void hideInit() {
        mInitLoading.setVisibility(View.GONE);
        mInitLoading.stopLoadingAnim();
    }

    @Override
    public void showInitFailed(String msg) {
        mInitLoading.setVisibility(View.VISIBLE);
        mInitLoading.showDataFail(msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getEvidenceList(mIouId, mJustId);
            }
        });
    }

    @Override
    public void showDataEmpty() {
        mInitLoading.setVisibility(View.VISIBLE);
        mInitLoading.showDataEmpty("");
    }

    @Override
    public void showEvidenceList(List<ElecEvidenceRes> listEvidence) {
        mAdapter.setNewData(listEvidence);
    }

    public static class EvidenceAdapter extends BaseQuickAdapter<ElecEvidenceRes, BaseViewHolder> {

        private HashSet<ElecEvidenceRes> mSelectObject = new HashSet();

        public EvidenceAdapter() {
            super(R.layout.arbitrament_item_select_valid_evidence);
        }

        @Override
        protected void convert(BaseViewHolder helper, ElecEvidenceRes item) {
            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_time, item.getName());
            if (mSelectObject.contains(item)) {
                helper.setImageResource(R.id.iv_select, R.mipmap.uikit_icon_check_black);
            } else {
                helper.setImageResource(R.id.iv_select, R.mipmap.uikit_icon_check_default);
            }
            helper.addOnClickListener(R.id.rl_content);
            helper.addOnClickListener(R.id.iv_arrow);
        }

        public void addOrRemoveCheck(ElecEvidenceRes position) {
            if (mSelectObject.contains(position)) {
                mSelectObject.remove(position);
            } else {
                mSelectObject.add(position);
            }
            notifyDataSetChanged();
        }

        public HashSet<ElecEvidenceRes> getSelectObjects() {
            return mSelectObject;
        }
    }
}
