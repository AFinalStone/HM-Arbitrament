package com.hm.arbitrament.business.pay.award;

import com.hm.iou.base.mvp.BaseContract;

/**
 * 催收证明
 */

public class AwardPayContract {

    public interface View extends BaseContract.BaseView {

    }

    public interface Presenter extends BaseContract.BasePresenter {

        void getArbPaperApplyOrderInfo();

        void payOrderByWeiXin(String orderId);

    }
}
