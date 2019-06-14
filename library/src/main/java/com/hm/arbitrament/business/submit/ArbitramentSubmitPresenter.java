package com.hm.arbitrament.business.submit;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;

public class ArbitramentSubmitPresenter extends MvpActivityPresenter<ArbitramentSubmitContract.View> implements ArbitramentSubmitContract.Presenter {

    public ArbitramentSubmitPresenter(@NonNull Context context, @NonNull ArbitramentSubmitContract.View view) {
        super(context, view);
    }

    @Override
    public void cancelArbitrament(String iouId, String justiceId) {

    }

    @Override
    public void sendVerifyCode() {
        mView.showLoadingView();
        new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mView.dismissLoadingView();
                        mView.startCountDown();
                    }
                }, 2000);
    }
}
