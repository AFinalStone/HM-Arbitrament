package com.hm.arbitrament.business.index;

import android.os.Bundle;

import com.hm.iou.base.BaseActivity;

/**
 * 起始页面
 */
public class IndexActivity extends BaseActivity<IndexPresenter> implements IndexContract.View {

    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    private String mIouId;

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
        if (bundle != null) {
            mIouId = bundle.getString(EXTRA_KEY_IOU_ID);
        }
        mPresenter.getArbitramentStatus(mIouId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
    }
}
