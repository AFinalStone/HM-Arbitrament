package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.GetArbServerAgreementResBean;
import com.hm.arbitrament.business.apply.ArbitramentServerAgreementContract;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;


/**
 * @author syl
 * @time 2019/6/11 3:36 PM
 */

public class ArbitramentServerAgreementPresenter extends MvpActivityPresenter<ArbitramentServerAgreementContract.View> implements ArbitramentServerAgreementContract.Presenter {


    public ArbitramentServerAgreementPresenter(@NonNull Context context, @NonNull ArbitramentServerAgreementContract.View view) {
        super(context, view);
    }

}
