package com.hm.arbitrament.business.pay.applysubmit;

import com.hm.iou.base.mvp.BaseContract;

/**
 * 催收证明
 */

public class ArbApplySubmitPayContract {

    public interface View extends BaseContract.BaseView {

        void toProgressPage();

    }

    public interface Presenter extends BaseContract.BasePresenter {
        /**
         * 更加仲裁编号和短信验证码，创建订单
         * String arbApplyNo 仲裁编号
         */
        void createApplyOrderInfo(String arbApplyNo, String msgCode);

        /**
         * 进行订单支付
         */
        void payOrder();

    }
}
