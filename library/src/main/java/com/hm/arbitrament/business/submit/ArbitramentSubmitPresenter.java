package com.hm.arbitrament.business.submit;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class ArbitramentSubmitPresenter extends MvpActivityPresenter<ArbitramentSubmitContract.View> implements ArbitramentSubmitContract.Presenter {

    public ArbitramentSubmitPresenter(@NonNull Context context, @NonNull ArbitramentSubmitContract.View view) {
        super(context, view);
    }

    @Override
    public void getArbApplyDoc(String arbApplyNo) {
        mView.showLoadingView();
        ArbitramentApi.getArbApplyDoc(arbApplyNo)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String pdfUrl) {
                        mView.dismissLoadingView();
                        mView.showArbApplyDoc(pdfUrl);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Override
    public void cancelArbitrament(String arbApplyNo) {

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
