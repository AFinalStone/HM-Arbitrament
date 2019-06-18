package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.event.ClosePageEvent;
import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.base.mvp.MvpActivityPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by syl on 2019/6/17.
 */

public class BasePresenter<T extends BaseContract.BaseView> extends MvpActivityPresenter<T> {

    public BasePresenter(@NonNull Context context, @NonNull T view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 关闭当前页面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusOpenWXResult(ClosePageEvent closePageEvent) {
        mView.closeCurrPage();
    }
}
