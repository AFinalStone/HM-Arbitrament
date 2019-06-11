package com.hm.arbitrament.business.apply.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.uikit.HMBottomBarView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 填写仲裁申请信息
 *
 * @param
 */
public class InputApplyInfoActivity<T extends MvpActivityPresenter> extends BaseActivity<T> {


    @BindView(R2.id.tv_real_back_money)
    TextView mTvRealBackMoney;
    @BindView(R2.id.tv_evidence)
    TextView mTvEvidence;
    @BindView(R2.id.tv_purpose_interest_rate)
    TextView mTvPurposeInterestRate;
    @BindView(R2.id.ll_purpose_interest_rate)
    LinearLayout llPurposeInterestRate;
    @BindView(R2.id.tv_out_time_interest)
    TextView mTvOutTimeInterest;
    @BindView(R2.id.tv_arbitrament_cost)
    TextView mTvArbitramentCost;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_input_apply_info;
    }

    @Override
    protected T initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                NavigationHelper.toArbitramentServerAgreement(mContext);
            }
        });
    }

    @OnClick({R2.id.ll_evidence_link, R2.id.ll_total_back_money, R2.id.tv_real_back_money,
            R2.id.iv_real_back_money, R2.id.tv_evidence, R2.id.iv_evidence,
            R2.id.ll_purpose_interest_rate, R2.id.ll_out_time_interest, R2.id.ll_arbitrament_cost})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.ll_evidence_link == id) {

        } else if (R.id.ll_total_back_money == id) {

        } else if (R.id.tv_real_back_money == id || R.id.iv_real_back_money == id) {

        } else if (R.id.tv_evidence == id || R.id.iv_evidence == id) {

        } else if (R.id.ll_purpose_interest_rate == id) {

        } else if (R.id.ll_out_time_interest == id) {

        } else if (R.id.ll_arbitrament_cost == id) {

        }
    }
}
