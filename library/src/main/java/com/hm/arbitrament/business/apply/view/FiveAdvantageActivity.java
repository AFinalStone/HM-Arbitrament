package com.hm.arbitrament.business.apply.view;

import android.os.Bundle;

import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.uikit.HMTopBarView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 申请仲裁5大优点
 *
 * @param <T>
 */
public class FiveAdvantageActivity<T extends MvpActivityPresenter> extends BaseActivity<T> {


    @BindView(R2.id.TopBar)
    HMTopBarView mTopBar;

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
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {

            }

            @Override
            public void onClickImageMenu() {

            }
        });
    }

    @OnClick(R2.id.btn_ok)
    public void onClick() {
    }
}
