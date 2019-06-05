package com.hm.arbitrament.business.apply.view;

import android.os.Bundle;

import com.hm.arbitrament.R;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;

/**
 * 申请仲裁5大优点
 *
 * @param <T>
 */
public class ApplyActivity<T extends MvpActivityPresenter> extends BaseActivity<T> {


    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_five_advantage;
    }

    @Override
    protected T initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {

    }
}
