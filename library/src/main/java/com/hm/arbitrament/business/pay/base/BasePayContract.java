package com.hm.arbitrament.business.pay.base;

import com.hm.iou.base.mvp.BaseContract;

import java.util.List;

/**
 * @author syl
 * @time 2019/6/5 3:15 PM
 */

public class BasePayContract {

    public interface BasePayView extends BaseContract.BaseView {

        /**
         * 显示套餐数据
         *
         * @param list
         */
        void showData(List<IMoneyItem> list);

        /**
         * 显示总金额
         *
         * @param totalPayMoney
         * @param realPayMoney
         */
        void showTotalPayMoney(String totalPayMoney, String realPayMoney);

        /**
         * 显示折扣
         */
        void showDiscount(String discount);

        /**
         * 显示备注描述信息
         */
        void showBottomRemark(String remark);

        /**
         * 显示初始化动画
         */
        void showInitLoadingView();

        /**
         * 隐藏初始化动画
         */
        void hideInitLoadingView();

        /**
         * 初始化失败
         *
         * @param msg
         */
        void showInitLoadingFailed(String msg);

        /**
         * 显示未检测到支付结果对话框
         */
        void showNoCheckPayResultDialog();

    }

    public interface BasePayPresenter extends BaseContract.BasePresenter {

    }
}
