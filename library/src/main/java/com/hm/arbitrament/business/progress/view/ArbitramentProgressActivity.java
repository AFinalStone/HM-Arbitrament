package com.hm.arbitrament.business.progress.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.business.CancelArbDialog;
import com.hm.arbitrament.business.progress.ArbitramentProgressContract;
import com.hm.arbitrament.business.progress.presenter.ArbitramentProgressPresenter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.dialog.HMActionSheetDialog;
import com.hm.iou.uikit.dialog.HMAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 仲裁进度
 */
public class ArbitramentProgressActivity extends BaseActivity<ArbitramentProgressPresenter> implements ArbitramentProgressContract.View {

    //仲裁申请编号
    public static final String EXTRA_KEY_ARB_NO = "arb_no";
    public static final String EXTRA_KEY_IS_APPLY_PERSON = "is_apply_person";

    @BindView(R2.id.rv_progress_content)
    RecyclerView mRvProgress;
    @BindView(R2.id.loading_view)
    HMLoadingView mLoadingView;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;

    private ProgressAdapter mAdapter;
    private String mArbNo;
    private String mIsApplyPerson;

    private HMActionSheetDialog mCancelActionSheetDialog;
    private CancelArbDialog mCancelArbDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_arbitrament_progress;
    }

    @Override
    protected ArbitramentProgressPresenter initPresenter() {
        return new ArbitramentProgressPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mArbNo = getIntent().getStringExtra(EXTRA_KEY_ARB_NO);
        mIsApplyPerson = getIntent().getStringExtra(EXTRA_KEY_IS_APPLY_PERSON);
        if (bundle != null) {
            mArbNo = bundle.getString(EXTRA_KEY_ARB_NO);
            mIsApplyPerson = bundle.getString(EXTRA_KEY_IS_APPLY_PERSON);
        }

        mRvProgress.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProgressAdapter(this);
        mRvProgress.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_progress_title) {
                    String link = (String) view.getTag();
                    if (!TextUtils.isEmpty(link)) {
                        NavigationHelper.toPdfPage(ArbitramentProgressActivity.this, link);
                    }
                }
            }
        });

        mPresenter.loadProgressData(mArbNo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_ARB_NO, mArbNo);
        outState.putString(EXTRA_KEY_IS_APPLY_PERSON, mIsApplyPerson);
    }

    /**
     * 检验是否是仲裁申请账号
     *
     * @return
     */
    public boolean checkIsApplyPerson() {
        if ("true".equals(mIsApplyPerson)) {
            return true;
        }
        new HMAlertDialog
                .Builder(mContext)
                .setTitle("温馨提示")
                .setMessage("请使用仲裁申请账号进行操作")
                .setPositiveButton("知道了")
                .create()
                .show();
        return false;
    }

    @Override
    public void showDataLoading() {
        mLoadingView.showDataLoading();
    }

    @Override
    public void showDataLoadFailed() {
        mLoadingView.showDataFail(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadProgressData(mArbNo);
            }
        });
    }

    @Override
    public void showProgressList(List<ProgressAdapter.IProgressItem> list) {
        mLoadingView.setVisibility(View.GONE);
        mRvProgress.setVisibility(View.VISIBLE);
        mAdapter.setNewData(list);
    }

    @Override
    public void addFooterTips(CharSequence tips, View.OnClickListener listener) {
        TextView tvFooter = new TextView(this);
        tvFooter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tvFooter.setTextColor(getResources().getColor(R.color.uikit_function_remind));
        tvFooter.getPaint().setUnderlineText(true);
        int padding = (int) (15 * getResources().getDisplayMetrics().density);
        tvFooter.setPadding(padding, padding / 3, padding, padding);
        tvFooter.setOnClickListener(listener);
        tvFooter.setText(tips);
        mAdapter.addFooterView(tvFooter);
    }

    @Override
    public void showBottomCancelArbMenu() {
        mBottomBar.setTitleIconDrawable(R.mipmap.uikit_ic_more_black);
        mBottomBar.setTitleIconVisible(true);
        mBottomBar.setTitleVisible(false);
        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                if (mCancelActionSheetDialog == null) {
                    List<String> list = new ArrayList<>();
                    list.add("取消仲裁");
                    mCancelActionSheetDialog = new HMActionSheetDialog.Builder(ArbitramentProgressActivity.this)
                            .setActionSheetList(list)
                            .setCanSelected(false)
                            .setOnItemClickListener(new HMActionSheetDialog.OnItemClickListener() {
                                @Override
                                public void onItemClick(int i, String s) {
                                    if (checkIsApplyPerson()) {
                                        if (mCancelArbDialog == null) {
                                            mCancelArbDialog = new CancelArbDialog(ArbitramentProgressActivity.this);
                                            mCancelArbDialog.setOnCancelArbListener(new CancelArbDialog.OnCancelArbListener() {
                                                @Override
                                                public void onCanceled(int index, String reason) {
                                                    int type = 0;
                                                    if (index == 0) {
                                                        type = 2;
                                                    } else if (index == 1) {
                                                        type = 1;
                                                    } else if (index == 2) {
                                                        type = 3;
                                                    }
                                                    mPresenter.cancelArbitrament(mArbNo, type, reason);
                                                }
                                            });
                                        }
                                        mCancelArbDialog.show();
                                    }
                                }
                            })
                            .create();
                }
                mCancelActionSheetDialog.show();
            }
        });
    }

    @Override
    public void showBottomBackMoneyRule(final int progress) {
        mBottomBar.setTitleIconVisible(false);
        mBottomBar.setTitleVisible(true);
        mBottomBar.updateTitle("退款规则");
        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                NavigationHelper.toReturnMoneyRulePage(mContext, progress);
            }
        });
    }

    @Override
    public void showBottomBackMoneyProgressMenu() {
        mBottomBar.setTitleIconVisible(false);
        mBottomBar.setTitleVisible(true);
        mBottomBar.updateTitle("退款进度");
        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                NavigationHelper.toMoneyBackProgressPage(ArbitramentProgressActivity.this, mArbNo);
            }
        });
    }

}
