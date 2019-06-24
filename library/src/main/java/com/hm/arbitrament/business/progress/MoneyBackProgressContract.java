package com.hm.arbitrament.business.progress;

import com.hm.arbitrament.business.progress.view.BackMoneyDetailAdapter;
import com.hm.arbitrament.business.progress.view.ProgressAdapter;
import com.hm.iou.base.mvp.BaseContract;

import java.util.List;

/**
 * 退款进度
 */
public interface MoneyBackProgressContract {

    interface View extends BaseContract.BaseView {

        void showDataLoading();

        void showDataLoadFailed();

        void showProgressList(List<ProgressAdapter.IProgressItem> list);

        void addFooterTips(CharSequence tips, android.view.View.OnClickListener listener);

        void showBackMoney(String txt);

        void showBackRule(int progress);

        void showBackMoneyDetailList(List<BackMoneyDetailAdapter.IBackMoneyItem> list, String totalMoneyStr);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void loadProgressData(String arbNo);

        void clickBackMoneyDetail();
    }

}
