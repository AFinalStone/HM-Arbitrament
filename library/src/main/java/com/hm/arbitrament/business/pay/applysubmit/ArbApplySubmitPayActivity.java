package com.hm.arbitrament.business.pay.applysubmit;

import android.os.Bundle;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.business.pay.base.BasePayActivity;

/**
 * Created by syl on 2019/6/14.
 */

public class ArbApplySubmitPayActivity extends BasePayActivity<ArbApplySubmitPayPresenter> implements ArbApplySubmitPayContract.View {

    public static final String EXTRA_KEY_ARB_NO = "arb_no";
    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";
    public static final String EXTRA_KEY_ORDER_ID = "order_id";

    private String mArbNo;
    private String mIouId;
    private String mJustId;
    private String mOrderId;

    @Override
    protected ArbApplySubmitPayPresenter initPresenter() {
        return new ArbApplySubmitPayPresenter(this, this);
    }

    @Override
    protected void init(Bundle bundle) {
        mArbNo = getIntent().getStringExtra(EXTRA_KEY_ARB_NO);
        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        mOrderId = getIntent().getStringExtra(EXTRA_KEY_ORDER_ID);
        if (bundle != null) {
            mArbNo = bundle.getString(EXTRA_KEY_ARB_NO);
            mIouId = bundle.getString(EXTRA_KEY_IOU_ID);
            mJustId = bundle.getString(EXTRA_KEY_JUST_ID);
            mOrderId = bundle.getString(EXTRA_KEY_ORDER_ID);
        }
        mPresenter.getArbApplySubmitOrderInfo(mIouId, mJustId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_ARB_NO, mArbNo);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
        outState.putString(EXTRA_KEY_ORDER_ID, mOrderId);
    }

    @Override
    protected void refresh() {
        mPresenter.getArbApplySubmitOrderInfo(mIouId, mJustId);
    }

    @Override
    protected void pay() {
        mPresenter.payOrderByWeiXin(mJustId, mOrderId);
    }

    @Override
    public void toProgressPage() {
        NavigationHelper.toArbitramentProgressPage(this, mArbNo);
    }
}
