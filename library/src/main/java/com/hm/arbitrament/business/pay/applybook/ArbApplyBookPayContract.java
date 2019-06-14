package com.hm.arbitrament.business.pay.applybook;

import com.hm.iou.base.mvp.BaseContract;

/**
 * 催收证明
 */

public class ArbApplyBookPayContract {

    public interface View extends BaseContract.BaseView {

    }

    public interface Presenter extends BaseContract.BasePresenter {
        /**
         * 获取仲裁申请书订单信息
         */
        void getArbApplyBookOrderInfo(String iouId, String justId);

        /**
         * 通过微信进行订单支付
         *
         * @param orderId 订单id
         */
        void payOrderByWeiXin(Integer orderId);
    }
}
