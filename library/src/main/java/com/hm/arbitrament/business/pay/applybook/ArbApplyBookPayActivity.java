package com.hm.arbitrament.business.pay.applybook;

import android.os.Bundle;

import com.hm.arbitrament.business.pay.base.BasePayActivity;

/**
 * Created by syl on 2019/6/14.
 */

public class ArbApplyBookPayActivity extends BasePayActivity<ArbApplyBookPayPresenter> implements ArbApplyBookPayContract.View {

    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";

    private String mIouId;
    private String mJustId;

    @Override
    protected ArbApplyBookPayPresenter initPresenter() {
        return new ArbApplyBookPayPresenter(this, this);
    }

    @Override
    protected void init(Bundle bundle) {
        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        if (bundle != null) {
            mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
            mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        }
        mPresenter.getArbApplyBookOrderInfo(mIouId, mJustId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
    }

    @Override
    protected void refresh() {
        mPresenter.getArbApplyBookOrderInfo(mIouId, mJustId);
    }

    @Override
    protected void pay() {

    }
}
