package com.hm.arbitrament.business.apply;

import com.hm.iou.base.mvp.BaseContract;

/**
 * 申请仲裁的5大优点
 */

public class FiveAdvantageContract {

    public interface View extends BaseContract.BaseView {

    }

    public interface Presenter extends BaseContract.BasePresenter {
        /**
         * 校验仲裁状态
         */
        void checkArbitramentApplyStatus(String iouId, String justId);
    }
}
