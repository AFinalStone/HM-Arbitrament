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

    private String mIouId;
    private String mJustId;

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
        if (bundle != null) {
            mIouId = bundle.getString(EXTRA_KEY_IOU_ID);
            mJustId = bundle.getString(EXTRA_KEY_JUST_ID);
        }
        mPresenter.getArbitramentStatus(mIouId, mJustId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
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
