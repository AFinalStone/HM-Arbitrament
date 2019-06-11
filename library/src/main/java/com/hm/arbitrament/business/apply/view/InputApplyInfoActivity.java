package com.hm.arbitrament.business.apply.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.business.apply.InputApplyInfoContract;
import com.hm.arbitrament.business.apply.presenter.InputApplyInfoPresenter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 填写仲裁申请信息
 *
 * @param
 */
public class InputApplyInfoActivity extends BaseActivity<InputApplyInfoPresenter> implements InputApplyInfoContract.View {

    public static final int REQ_INPUT_COLLECTION_PROVE = 100;//催收证明

    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";

    @BindView(R2.id.tv_real_back_money)
    TextView mTvRealBackMoney;
    @BindView(R2.id.iv_real_back_money)
    ImageView mIvRealBackMoney;
    @BindView(R2.id.ll_arbitrament_money)
    LinearLayout mLlArbitramentMoney;
    @BindView(R2.id.tv_arbitrament_money)
    TextView mTvArbitramentMoney;
    @BindView(R2.id.tv_collection_prove)
    TextView mTvCollectionProve;
    @BindView(R2.id.iv_collection_prove)
    ImageView mIvCollectionProve;
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

    private String mIouId;
    private String mJustId;

    private HMAlertDialog mArbitramentCostDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_input_apply_info;
    }

    @Override
    protected InputApplyInfoPresenter initPresenter() {
        return new InputApplyInfoPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        if (bundle != null) {
            mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
            mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        }
        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                if (mTvRealBackMoney.length() == 0) {
                    mIvRealBackMoney.setImageResource(R.mipmap.uikit_icon_warn_red);
                }
                if (mTvCollectionProve.length() == 0) {
                    mIvCollectionProve.setImageResource(R.mipmap.uikit_icon_warn_red);
                }
                if (mTvArbitramentMoney.length() == 0) {
                    return;
                }
                int arbMoney = 0;
                try {
                    String strArbMoney = mTvArbitramentMoney.getText().toString();
                    arbMoney = Integer.parseInt(strArbMoney);
                } catch (Exception e) {

                }
                if (arbMoney < 300) {
                    showArbitramentMoneyTooSmallDialog();
                    return;
                }
                NavigationHelper.toArbitramentServerAgreement(mContext);
            }
        });
        mPresenter.getInputApplyInfoData(mIouId, mJustId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_INPUT_COLLECTION_PROVE == requestCode) {
            mIvCollectionProve.setImageResource(R.mipmap.uikit_ic_arrow_right);
            checkValue();
        }
    }

    @OnClick({R2.id.ll_evidence_link, R2.id.ll_total_back_money, R2.id.tv_real_back_money,
            R2.id.iv_real_back_money, R2.id.ll_arbitrament_money, R2.id.tv_collection_prove, R2.id.iv_collection_prove,
            R2.id.ll_purpose_interest_rate, R2.id.ll_out_time_interest, R2.id.ll_arbitrament_cost})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.ll_evidence_link == id) {

        } else if (R.id.ll_total_back_money == id) {

        } else if (R.id.tv_real_back_money == id || R.id.iv_real_back_money == id) {

        } else if (R.id.ll_arbitrament_money == id) {

        } else if (R.id.tv_collection_prove == id || R.id.iv_collection_prove == id) {
            Intent intent = new Intent(mContext, CollectionProveActivity.class);
            startActivityForResult(intent, REQ_INPUT_COLLECTION_PROVE);
        } else if (R.id.ll_purpose_interest_rate == id) {

        } else if (R.id.ll_out_time_interest == id) {

        } else if (R.id.ll_arbitrament_cost == id) {

        }
    }

    private void checkValue() {
        //实际归还
        if (mTvRealBackMoney.length() == 0) {
            mBottomBar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small);
            mBottomBar.setTitleTextColor(R.color.uikit_text_auxiliary);
            return;
        }
        //催收证明
        if (mTvCollectionProve.length() == 0) {
            mBottomBar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small);
            mBottomBar.setTitleTextColor(R.color.uikit_text_auxiliary);
            return;
        }
        mBottomBar.setTitleBackgournd(R.drawable.uikit_shape_common_btn_normal);
        mBottomBar.setTitleTextColor(R.color.uikit_text_main_content);
    }


    private void showArbitramentMoneyTooSmallDialog() {
        if (mArbitramentCostDialog == null) {
            mArbitramentCostDialog = new HMAlertDialog
                    .Builder(mContext)
                    .setTitle("温馨提示")
                    .setMessage("仲裁委支持仲裁金额最低为300元")
                    .setMessageGravity(Gravity.CENTER)
                    .setPositiveButton("我知道了")
                    .create();
        }
        mArbitramentCostDialog.show();
    }
}
