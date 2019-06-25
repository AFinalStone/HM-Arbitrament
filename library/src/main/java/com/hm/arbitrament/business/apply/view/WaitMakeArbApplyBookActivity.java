package com.hm.arbitrament.business.apply.view;

import android.os.Bundle;

import com.hm.arbitrament.R;
import com.hm.arbitrament.event.ClosePageEvent;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 仲裁申请书等待付款
 */
public class WaitMakeArbApplyBookActivity<T extends MvpActivityPresenter> extends BaseActivity<T> {

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_wait_make_arb_apply_book;
    }

    @Override
    protected T initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 关闭当前页面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusOpenWXResult(ClosePageEvent closePageEvent) {
        finish();
    }
}
