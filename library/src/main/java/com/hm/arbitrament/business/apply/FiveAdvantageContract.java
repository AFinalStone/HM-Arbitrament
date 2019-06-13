package com.hm.arbitrament.business.apply;

import com.hm.iou.base.mvp.BaseContract;

/**
 * 申请仲裁的5大优点
 */

public class FiveAdvantageContract {

    public interface View extends BaseContract.BaseView {

        /**
         * 显示知道了对话框
         *
         * @param msg
         */
        void showKnowDialog(String msg);

        /**
         * 需要上传有效电子凭证
         */
        void showNeedUploadElecEvidenceDialog();

        /**
         * 需要更新身份证
         */
        void showNeedUpdateIDCardDialog();
    }

    public interface Presenter extends BaseContract.BasePresenter {
        /**
         * 校验仲裁状态
         */
        void checkArbitramentApplyStatus(String iouId, String justId);
    }
}
