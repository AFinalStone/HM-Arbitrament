package com.hm.arbitrament.business.submit;

import com.hm.iou.base.mvp.BaseContract;

public interface ArbitramentSubmitContract {

    interface View extends BaseContract.BaseView {

        void startCountDown();

    }

    interface Presenter extends BaseContract.BasePresenter {

        void cancelArbitrament(String iouId, String justiceId);

        void sendVerifyCode();
    }

}
