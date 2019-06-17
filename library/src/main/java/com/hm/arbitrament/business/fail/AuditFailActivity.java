package com.hm.arbitrament.business.fail;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.business.CancelArbDialog;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.dialog.HMActionSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AuditFailActivity extends BaseActivity<AuditFailPresenter> implements AuditFailContract.View {

    public static final String EXTRA_KEY_ARB_NO = "arb_no";
    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUSTICE_ID = "justice_id";

    @BindView(R2.id.tv_fail_reason)
    TextView mTvFailReason;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBarView;
    @BindView(R2.id.ll_fail_content)
    View mViewFailContent;
    @BindView(R2.id.loading_view)
    HMLoadingView mLoadingView;

    private String mArbNo;
    private String mIouId;
    private String mJusticeId;

    private HMActionSheetDialog mCancelActionSheetDialog;
    private CancelArbDialog mCancelArbDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_audit_fail;
    }

    @Override
    protected AuditFailPresenter initPresenter() {
        return new AuditFailPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mArbNo = getIntent().getStringExtra(EXTRA_KEY_ARB_NO);
        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJusticeId = getIntent().getStringExtra(EXTRA_KEY_JUSTICE_ID);
        if (bundle != null) {
            mArbNo = bundle.getString(EXTRA_KEY_ARB_NO);
            mIouId = bundle.getString(EXTRA_KEY_IOU_ID);
            mJusticeId = bundle.getString(EXTRA_KEY_JUSTICE_ID);
        }

        mBottomBarView.setOnTitleIconClickListener(new HMBottomBarView.OnTitleIconClickListener() {
            @Override
            public void onClickIcon() {
                showMoreActionSheet();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_ARB_NO, mArbNo);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUSTICE_ID, mJusticeId);
    }

    @Override
    public void showDataLoadFail(String msg) {
        mLoadingView.showDataFail(msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingView.showDataLoading();
                mPresenter.getFailReason(mArbNo);
            }
        });
    }

    @Override
    public void showDocCompletion() {
        mBottomBarView.updateTitle("资料补全");
        mBottomBarView.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                NavigationHelper.toDocCompletionPage(AuditFailActivity.this, mIouId, mJusticeId, mArbNo);
            }
        });
    }

    @Override
    public void showFailReason(String msg) {
        mLoadingView.setVisibility(View.GONE);
        mViewFailContent.setVisibility(View.VISIBLE);
        mTvFailReason.setText(msg);
    }

    private void showMoreActionSheet() {
        if (mCancelActionSheetDialog == null) {
            List<String> list = new ArrayList<>();
            list.add("取消仲裁");
            mCancelActionSheetDialog = new HMActionSheetDialog.Builder(AuditFailActivity.this)
                    .setActionSheetList(list)
                    .setCanSelected(false)
                    .setOnItemClickListener(new HMActionSheetDialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(int i, String s) {
                            if (mCancelArbDialog == null) {
                                mCancelArbDialog = new CancelArbDialog(AuditFailActivity.this);
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
                    })
                    .create();
        }
        mCancelActionSheetDialog.show();
    }

}
