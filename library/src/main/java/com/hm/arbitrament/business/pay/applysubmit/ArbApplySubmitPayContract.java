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
         * 获取仲裁提交订单信息
         */
        void getArbApplySubmitOrderInfo(String iouId, String justId);

        /**
         * 通过微信进行订单支付
         *
         * @param orderId 订单id
         */
        void payOrderByWeiXin(String justId, String orderId);

    }
}
