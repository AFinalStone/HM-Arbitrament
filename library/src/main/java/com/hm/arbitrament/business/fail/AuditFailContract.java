package com.hm.arbitrament.business.fail;

import com.hm.iou.base.mvp.BaseContract;

public interface AuditFailContract {

    interface View extends BaseContract.BaseView {

        void showDataLoadFail(String msg);

        /**
         * 显示资料补全
         */
        void showDocCompletion();

        void showFailReason(String msg);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void getFailReason(String arbNo);

        /**
         * 取消仲裁申请
         *
         * @param arbApplyNo 仲裁申请编号
         * @param type       1-已履行，2-已和解，3-其他
         * @param reason     其他取消原因
         */
        void cancelArbitrament(String arbApplyNo, int type, String reason);

    }

}
