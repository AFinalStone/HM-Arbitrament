package com.hm.arbitrament.business.apply.view;

import android.app.Dialog;
import android.os.Bundle;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.business.CancelArbDialog;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.dialog.HMActionSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * 仲裁申请书等待付款
 */
public class ArbApplyBookWaitPayActivity<T extends MvpActivityPresenter> extends BaseActivity<T> {

    public static final String EXTRA_KEY_ARB_NO = "arb_no";
    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";
    public static final String EXTRA_KEY_ORDER_ID = "order_id";

    private String mIouId;
    private String mJustId;
    private String mArbNo;
    private String mOrderId;

    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;

    private Dialog mCancelDialog;
    private CancelArbDialog mCancelArbDialog;

    private Disposable mDisposable;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_arb_apply_book_wait_pay;
    }

    @Override
    protected T initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        mArbNo = getIntent().getStringExtra(EXTRA_KEY_ARB_NO);
        mOrderId = getIntent().getStringExtra(EXTRA_KEY_ORDER_ID);
        if (bundle != null) {
            mIouId = bundle.getString(EXTRA_KEY_IOU_ID);
            mJustId = bundle.getString(EXTRA_KEY_JUST_ID);
            mArbNo = bundle.getString(EXTRA_KEY_ARB_NO);
            mOrderId = bundle.getString(EXTRA_KEY_ORDER_ID);
        }

        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                showMoreActionSheet();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
        outState.putString(EXTRA_KEY_ARB_NO, mArbNo);
        outState.putString(EXTRA_KEY_ORDER_ID, mOrderId);
    }

    @OnClick(R2.id.btn_ok)
    public void onClick() {
        NavigationHelper.toPay(mContext, mIouId, mJustId, mOrderId);
    }

    /**
     * 取消仲裁
     */
    private void cancelArb(int type, String reason) {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        showLoadingView();
        mDisposable = ArbitramentApi.cancelArbitrament(mArbNo, type, reason)
                .map(RxUtil.handleResponse())
                .subscribeWith(new CommSubscriber<Object>(this) {
                    @Override
                    public void handleResult(Object o) {
                        dismissLoadingView();
                        finish();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        dismissLoadingView();
                    }
                });
    }

    private void showMoreActionSheet() {
        if (mCancelDialog == null) {
            List<String> list = new ArrayList<>();
            list.add("取消仲裁");
            mCancelDialog = new HMActionSheetDialog.Builder(this)
                    .setActionSheetList(list)
                    .setCanSelected(false)
                    .setOnItemClickListener(new HMActionSheetDialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(int i, String s) {
                            if (mCancelArbDialog == null) {
                                mCancelArbDialog = new CancelArbDialog(mContext);
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
                                        cancelArb(type, reason);
                                    }
                                });
                            }
                            mCancelArbDialog.show();
                        }
                    })
                    .create();
        }
        mCancelDialog.show();
    }


}
