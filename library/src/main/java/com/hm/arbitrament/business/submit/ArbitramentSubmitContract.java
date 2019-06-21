package com.hm.arbitrament.business.submit;

import com.hm.iou.base.mvp.BaseContract;

public interface ArbitramentSubmitContract {

    interface View extends BaseContract.BaseView {

        void showArbApplyDoc(String pdfUrl);

        void startCountDown();

        void toPay(String code);
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 查询仲裁申请书pdf地址
         *
         * @param arbApplyNo
         */
        void getArbApplyDoc(String arbApplyNo);

        /**
         * 取消仲裁申请
         *
         * @param arbApplyNo 仲裁申请编号
         * @param type       1-已履行，2-已和解，3-其他
         * @param reason     其他取消原因
         */
        void cancelArbitrament(String arbApplyNo, int type, String reason);

        /**
         * 发送验证码
         */
        void sendVerifyCode();

        void verifySmsCode(String code);
    }

}
