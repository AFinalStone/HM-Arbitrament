package com.hm.arbitrament.business.pay.applysubmit;

import android.os.Bundle;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.business.pay.base.BasePayActivity;

/**
 * Created by syl on 2019/6/14.
 */

public class ArbApplySubmitPayActivity extends BasePayActivity<ArbApplySubmitPayPresenter> implements ArbApplySubmitPayContract.View {

    public static final String EXTRA_KEY_ARB_NO = "arb_no";
    public static final String EXTRA_KEY_MESSAGE_CODE = "msg_code";

    private String mArbNo;
    private String mMsgCode;

    @Override
    protected ArbApplySubmitPayPresenter initPresenter() {
        return new ArbApplySubmitPayPresenter(this, this);
    }

    @Override
    protected void init(Bundle bundle) {
        mArbNo = getIntent().getStringExtra(EXTRA_KEY_ARB_NO);
        mMsgCode = getIntent().getStringExtra(EXTRA_KEY_MESSAGE_CODE);
        if (bundle != null) {
            mArbNo = bundle.getString(EXTRA_KEY_ARB_NO);
            mMsgCode = bundle.getString(EXTRA_KEY_MESSAGE_CODE);
        }
        mPresenter.createApplyOrderInfo(mArbNo, mMsgCode);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_ARB_NO, mArbNo);
        outState.putString(EXTRA_KEY_MESSAGE_CODE, mMsgCode);
    }

    @Override
    protected void refresh() {
        mPresenter.createApplyOrderInfo(mArbNo, mMsgCode);
    }

    @Override
    protected void pay() {
        mPresenter.payOrder();
    }

    @Override
    public void toProgressPage() {
        NavigationHelper.toArbitramentProgressPage(this, mArbNo);
    }
}
