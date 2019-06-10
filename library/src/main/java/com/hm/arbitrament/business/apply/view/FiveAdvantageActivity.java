package com.hm.arbitrament.business.apply.view;

import android.os.Bundle;

import com.hm.arbitrament.NavigationHelper;
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

    public static final String EXTRA_KEY_IOU_ID = "iou_id";

    private String mIouId;


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

        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        if (bundle != null) {
            mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        }
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                NavigationHelper.toNeedKnowByArbitrament(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
    }

    @OnClick(R2.id.btn_ok)
    public void onClick() {
        NavigationHelper.toSelectValidEvidenceActivity(mContext, mIouId);
    }
}
