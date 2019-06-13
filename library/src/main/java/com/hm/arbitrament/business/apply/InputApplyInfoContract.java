package com.hm.arbitrament.business.apply;

import com.hm.arbitrament.bean.GetArbitramentInputApplyDataResBean;
import com.hm.arbitrament.bean.req.CreateArbOrderReqBean;
import com.hm.iou.base.mvp.BaseContract;

/**
 * 仲裁申请信息填写页面
 */

public class InputApplyInfoContract {

    public interface View extends BaseContract.BaseView {

        /**
         * 显示仲裁的预加载信息
         *
         * @param resBean
         */
        void showData(GetArbitramentInputApplyDataResBean resBean);

        /**
         * 显示仲裁金额
         */
        void showArbMoney(String strMoney);

        /**
         * 仲裁费用
         */
        void showArbCost(String strCost);
    }

    public interface Presenter extends BaseContract.BasePresenter {

        /**
         * 获取预填充数据
         */
        void getInputApplyInfoData(String iouId, String justId);


        /**
         * 仲裁费用计算
         */
        void getArbitramentCost(String iouId, String justiceId, double totalMoney);

        /**
         * 获取仲裁协议
         */
        void getAgreement(String iouId, String justId);

        /**
         * 创建订单
         */
        void createOrder(CreateArbOrderReqBean reqBean);

    }
}
