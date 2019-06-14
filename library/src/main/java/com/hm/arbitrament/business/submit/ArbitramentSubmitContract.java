package com.hm.arbitrament.business.submit;

import com.hm.iou.base.mvp.BaseContract;

public interface ArbitramentSubmitContract {

    interface View extends BaseContract.BaseView {

        void showArbApplyDoc(String pdfUrl);

        void startCountDown();
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 查询仲裁申请书pdf地址
         *
         * @param arbApplyNo
         */
        void getArbApplyDoc(String arbApplyNo);

        void cancelArbitrament(String arbApplyNo);

        void sendVerifyCode();
    }

}
