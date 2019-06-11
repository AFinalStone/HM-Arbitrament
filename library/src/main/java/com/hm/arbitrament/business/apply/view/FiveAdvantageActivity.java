package com.hm.arbitrament.business.apply.view;

import android.os.Bundle;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.business.apply.FiveAdvantageContract;
import com.hm.arbitrament.business.apply.presenter.FiveAdvantagePresenter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMTopBarView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 申请仲裁5大优点
 */
public class FiveAdvantageActivity extends BaseActivity<FiveAdvantagePresenter> implements FiveAdvantageContract.View {

    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";

    private String mIouId;
    private String mJustId;


    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;


    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_five_advantage;
    }

    @Override
    protected FiveAdvantagePresenter initPresenter() {
        return new FiveAdvantagePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {

        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        if (bundle != null) {
            mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
            mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
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
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
    }

    @OnClick(R2.id.btn_ok)
    public void onClick() {
        mPresenter.checkArbitramentApplyStatus(mIouId, mJustId);
    }
}
