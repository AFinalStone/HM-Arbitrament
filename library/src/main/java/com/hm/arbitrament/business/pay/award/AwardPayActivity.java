package com.hm.arbitrament.business.pay.award;

import android.os.Bundle;

import com.hm.arbitrament.business.pay.base.BasePayActivity;

/**
 * Created by syl on 2019/6/19.
 */

public class AwardPayActivity extends BasePayActivity<AwardPayPresenter> implements AwardPayContract.View {

    public static final String EXTRA_KEY_ORDER_ID = "order_id";
    public static final String EXTRA_KEY_ARBPAPER_ID = "arbpaper_id";

    private String mOrderId;
    private String mArbPaperId;

    @Override
    protected AwardPayPresenter initPresenter() {
        return new AwardPayPresenter(this, this);
    }

    @Override
    protected void init(Bundle bundle) {
        mOrderId = getIntent().getStringExtra(EXTRA_KEY_ORDER_ID);
        mArbPaperId = getIntent().getStringExtra(EXTRA_KEY_ARBPAPER_ID);
        if (bundle != null) {
            mOrderId = bundle.getString(EXTRA_KEY_ORDER_ID);
            mArbPaperId = bundle.getString(EXTRA_KEY_ARBPAPER_ID);
        }
        mPresenter.getArbPaperApplyOrderInfo(mArbPaperId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_ORDER_ID, mOrderId);
        outState.putString(EXTRA_KEY_ARBPAPER_ID, mArbPaperId);
    }

    @Override
    protected void refresh() {
        mPresenter.getArbPaperApplyOrderInfo(mArbPaperId);
    }

    @Override
    protected void pay() {
        mPresenter.payOrder(mOrderId);
    }
}
