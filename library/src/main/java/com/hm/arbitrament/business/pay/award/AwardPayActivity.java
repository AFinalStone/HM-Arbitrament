package com.hm.arbitrament.business.pay.award;

import android.os.Bundle;

import com.hm.arbitrament.business.pay.base.BasePayActivity;

/**
 * Created by syl on 2019/6/19.
 */

public class AwardPayActivity extends BasePayActivity<AwardPayPresenter> implements AwardPayContract.View {

    public static final String EXTRA_KEY_ORDER_ID = "order_id";

    private String mOrderId;

    @Override
    protected AwardPayPresenter initPresenter() {
        return new AwardPayPresenter(this, this);
    }

    @Override
    protected void init(Bundle bundle) {
        mOrderId = getIntent().getStringExtra(EXTRA_KEY_ORDER_ID);
        if (bundle != null) {
            mOrderId = bundle.getString(EXTRA_KEY_ORDER_ID);
        }
        mPresenter.getArbPaperApplyOrderInfo();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_ORDER_ID, mOrderId);
    }

    @Override
    protected void refresh() {
        mPresenter.getArbPaperApplyOrderInfo();
    }

    @Override
    protected void pay() {
        mPresenter.payOrderByWeiXin(mOrderId);
    }
}
