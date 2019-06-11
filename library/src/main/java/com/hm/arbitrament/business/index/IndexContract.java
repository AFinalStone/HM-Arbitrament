package com.hm.arbitrament.business.index;

import com.hm.iou.base.mvp.BaseContract;

/**
 * @author syl
 * @time 2019/6/5 3:15 PM
 */

public class IndexContract {

    public interface View extends BaseContract.BaseView {

    }

    public interface Presenter extends BaseContract.BasePresenter {

        /**
         * 查询仲裁状况
         *
         * @param iouId
         */
        void getArbitramentStatus(String iouId);
    }
}