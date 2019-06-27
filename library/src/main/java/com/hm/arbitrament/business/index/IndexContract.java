package com.hm.arbitrament.business.index;

import com.hm.iou.base.mvp.BaseContract;

/**
 * @author syl
 * @time 2019/6/5 3:15 PM
 */

public class IndexContract {

    public interface View extends BaseContract.BaseView {

        /**
         * 显示提示弹窗
         *
         * @param title
         * @param msg
         */
        void showDialog(String title, String msg);
    }

    public interface Presenter extends BaseContract.BasePresenter {

        /**
         * 查询仲裁状况
         *
         * @param iouId
         * @param justId
         * @param arbNo
         */
        void getArbitramentStatus(String iouId, String justId, String arbNo);
    }
}
