package com.hm.arbitrament.business.pay;

import com.hm.arbitrament.business.pay.view.IMoneyItem;
import com.hm.iou.base.mvp.BaseContract;

import java.util.List;

/**
 * @author syl
 * @time 2019/6/5 3:15 PM
 */

public class PayContract {

    public interface View extends BaseContract.BaseView {
        /**
         * 显示数据
         *
         * @param list
         */
        void showData(List<IMoneyItem> list);

        /**
         * 显示总的支付金额
         *
         * @param money
         */
        void showTotalPayMoney(String money);
    }

    public interface Presenter extends BaseContract.BasePresenter {
        /**
         * 初始化
         */
        void init();
    }
}
