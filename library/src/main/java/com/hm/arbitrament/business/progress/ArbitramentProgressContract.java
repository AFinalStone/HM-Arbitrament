package com.hm.arbitrament.business.progress;

import com.hm.arbitrament.business.progress.view.ProgressAdapter;
import com.hm.iou.base.mvp.BaseContract;

import java.util.List;

/**
 * 仲裁进度
 */
public interface ArbitramentProgressContract {

    interface View extends BaseContract.BaseView {

        void showDataLoading();

        void showDataLoadFailed();

        void showProgressList(List<ProgressAdapter.IProgressItem> list);

        void addFooterTips(CharSequence tips, android.view.View.OnClickListener listener);

        /**
         * 底部导航栏上显示"取消仲裁"按钮操作
         */
        void showBottomCancelArbMenu();

        /**
         * 底部导航栏上显示"退款规则"按钮操作
         */
        void showBottomBackMoneyRule(int progress);

        /**
         * 底部导航栏上显示"退款进度"按钮操作
         */
        void showBottomBackMoneyProgressMenu();
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 加载仲裁进度
         *
         * @param arbNo
         */
        void loadProgressData(String arbNo);

    }

}
