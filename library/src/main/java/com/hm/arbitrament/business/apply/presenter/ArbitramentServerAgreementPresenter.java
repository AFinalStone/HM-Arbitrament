package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.business.apply.ArbitramentServerAgreementContract;


/**
 * @author syl
 * @time 2019/6/11 3:36 PM
 */

public class ArbitramentServerAgreementPresenter extends BasePresenter<ArbitramentServerAgreementContract.View> implements ArbitramentServerAgreementContract.Presenter {

    public ArbitramentServerAgreementPresenter(@NonNull Context context, @NonNull ArbitramentServerAgreementContract.View view) {
        super(context, view);
    }
}
