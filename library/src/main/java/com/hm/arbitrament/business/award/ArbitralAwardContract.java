package com.hm.arbitrament.business.award;

import com.hm.iou.base.mvp.BaseContract;

import java.util.List;

/**
 * 裁决书申请页面
 */
public interface ArbitralAwardContract {

    interface View extends BaseContract.BaseView {

        void finishRefresh();

        void showApplyList(List<ArbitralAwardListAdapter.IArbitralAwardListItem> list);

        /**
         * 显示裁决书申请资料填写页面
         */
        void showApplyInputView();

        void applySucc();

        void removeData(String applyPaperId);
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 刷新裁决书申请历史记录
         *
         * @param arbNo
         */
        void refreshApplyHistoryList(String arbNo);

        /**
         * 提交信息
         *
         * @param arbNo
         * @param name
         * @param count  数量
         * @param mobile
         * @param city
         * @param addr
         */
        void submitApplyInfo(String arbNo, String name, Integer count, String mobile, String city, String addr);

        void toCancelApply(String applyId);

        void toPayApply(String applyId);
    }

}
