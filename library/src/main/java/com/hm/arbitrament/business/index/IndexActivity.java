package com.hm.arbitrament.business.index;

import android.os.Bundle;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.dialog.HMAlertDialog;

/**
 * 起始页面
 */
public class IndexActivity extends BaseActivity<IndexPresenter> implements IndexContract.View {

    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";
    public static final String EXTRA_KEY_IS_BORROWER = "is_borrower";

    private String mIouId;
    private String mJustId;
    private String mIsBorrower;//是否是借款人

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected IndexPresenter initPresenter() {
        return new IndexPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        mIsBorrower = getIntent().getStringExtra(EXTRA_KEY_IS_BORROWER);
        if (bundle != null) {
            mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
            mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
            mIsBorrower = getIntent().getStringExtra(EXTRA_KEY_IS_BORROWER);
        }
        boolean isBorrower = false;
        if ("1".equals(mIsBorrower)) {//是否是借款人
            isBorrower = true;
        }
        mPresenter.getArbitramentStatus(mIouId, mJustId, isBorrower);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
        outState.putString(EXTRA_KEY_IS_BORROWER, mIsBorrower);
    }

    @Override
    public void showDialog(String title, String msg) {
        new HMAlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("知道了")
                .setCanceledOnTouchOutside(false)
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        finish();
                    }

                    @Override
                    public void onNegClick() {
                        finish();
                    }
                })
                .create().show();
    }
}
