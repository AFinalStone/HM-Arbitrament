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
import com.hm.arbitrament.bean.GetArbitramentInputApplyDataResBean;
import com.hm.arbitrament.business.apply.InputApplyInfoContract;
import com.hm.arbitrament.business.apply.presenter.InputApplyInfoPresenter;
import com.hm.arbitrament.dict.CollectionProveEnum;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 填写仲裁申请信息
 *
 * @param
 */
public class InputApplyInfoActivity extends BaseActivity<InputApplyInfoPresenter> implements InputApplyInfoContract.View {

    public static final int REQ_INPUT_REAL_BACK_MONEY_RECORD = 100;//实际归还记录
    public static final int REQ_INPUT_COLLECTION_PROVE = 101;//催收证明

    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";

    @BindView(R2.id.tv_total_back_money)
    TextView mTvTotalBackMoney;//合计应还
    @BindView(R2.id.tv_real_back_money)
    TextView mTvRealBackMoney;//实际归还
    @BindView(R2.id.iv_real_back_money)
    ImageView mIvRealBackMoney;//实际归还
    @BindView(R2.id.ll_arbitrament_money)
    LinearLayout mLlArbitramentMoney;//仲裁金额
    @BindView(R2.id.tv_arbitrament_money)
    TextView mTvArbitramentMoney;//仲裁金额
    @BindView(R2.id.tv_collection_prove)
    TextView mTvCollectionProve;//催收证明
    @BindView(R2.id.iv_collection_prove)
    ImageView mIvCollectionProve;//催收证明
    @BindView(R2.id.tv_purpose_interest_rate)
    TextView mTvPurposeInterestRate;//利息意向
    @BindView(R2.id.ll_purpose_interest_rate)
    LinearLayout llPurposeInterestRate;//利息意向
    @BindView(R2.id.tv_out_time_interest)
    TextView mTvOutTimeInterest;//逾期利息
    @BindView(R2.id.tv_arbitrament_cost)
    TextView mTvArbitramentCost;//仲裁费用
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;

    private String mIouId;
    private String mJustId;

    private HMAlertDialog mArbitramentCostDialog;
    private String mContractStartTime;//合同开始时间
    private ArrayList<GetArbitramentInputApplyDataResBean.RepaymentRecordListBean> mListDataBackMoneyRecord;//实际归还记录
    private GetArbitramentInputApplyDataResBean.UrgeExidenceListBean mCollectionProveBean;//催收证明

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
        if (REQ_INPUT_REAL_BACK_MONEY_RECORD == requestCode) {
            if (RESULT_OK == resultCode) {
                if (data == null) {
                    return;
                }
                mIvRealBackMoney.setImageResource(R.mipmap.uikit_ic_arrow_right);
                mListDataBackMoneyRecord = (ArrayList<GetArbitramentInputApplyDataResBean.RepaymentRecordListBean>) data.getSerializableExtra(InputRealBackMoneyActivity.EXTRA_KEY_BACK_MONEY_RECORD_LIST);
                //归还记录
                showRealBackRecord(mListDataBackMoneyRecord);
                checkValue();
            }
        } else if (REQ_INPUT_COLLECTION_PROVE == requestCode) {
            if (RESULT_OK == resultCode) {
                if (data == null) {
                    return;
                }
            }
            mIvCollectionProve.setImageResource(R.mipmap.uikit_ic_arrow_right);
            mCollectionProveBean = (GetArbitramentInputApplyDataResBean.UrgeExidenceListBean) data.getSerializableExtra(InputCollectionProveActivity.EXTRA_KEY_ITEM);
            //催收证明
            showCollectionProve(mCollectionProveBean);
            checkValue();
        }
    }

    @OnClick({R2.id.ll_evidence_link, R2.id.ll_total_back_money, R2.id.tv_real_back_money,
            R2.id.iv_real_back_money, R2.id.ll_arbitrament_money, R2.id.tv_collection_prove, R2.id.iv_collection_prove,
            R2.id.ll_purpose_interest_rate, R2.id.ll_out_time_interest, R2.id.ll_arbitrament_cost})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.ll_evidence_link == id) {
            showKnowDialog("证据链", "证据链为借款双方，签署相关借条的全流程信息保证借条的有效性，证据链由条管家收集并整理成功后提交至仲裁委。");
        } else if (R.id.ll_total_back_money == id) {
            showKnowDialog("合计应还", "借款本金+借款利息+逾期利息");
        } else if (R.id.tv_real_back_money == id || R.id.iv_real_back_money == id) {
            Intent intent = new Intent(mContext, InputRealBackMoneyActivity.class);
            intent.putExtra(InputRealBackMoneyActivity.EXTRA_KEY_BACK_MONEY_RECORD_LIST, mListDataBackMoneyRecord);
            intent.putExtra(InputRealBackMoneyActivity.EXTRA_KEY_BACK_TIME_START_TIME, mContractStartTime);
            String strMaxMoney = mTvTotalBackMoney.getText().toString();
            try {
                int maxMoney = Integer.parseInt(strMaxMoney);
                intent.putExtra(InputRealBackMoneyActivity.EXTRA_KEY_MAX_BACK_MONEY, maxMoney);
            } catch (Exception e) {

            }
            startActivityForResult(intent, REQ_INPUT_REAL_BACK_MONEY_RECORD);
        } else if (R.id.ll_arbitrament_money == id) {
            showKnowDialog("仲裁金额", "仲裁金额=未还金额+逾期利息\n仲裁时未还部分的利息最高支持为年化24%；已还款部分最高支持年化36%；仲裁时逾期利息最高支持为年化24%");
        } else if (R.id.tv_collection_prove == id || R.id.iv_collection_prove == id) {
            Intent intent = new Intent(mContext, InputCollectionProveActivity.class);
            intent.putExtra(InputCollectionProveActivity.EXTRA_KEY_ITEM, mCollectionProveBean);
            startActivityForResult(intent, REQ_INPUT_COLLECTION_PROVE);
        } else if (R.id.ll_purpose_interest_rate == id) {
            showKnowDialog("利息意向", "依据相关法律规定，借款利息最高支持24%");
        } else if (R.id.ll_out_time_interest == id) {
            showKnowDialog("逾期利息", "仲裁时逾期利息最高支持为年化24% 即日利率万分之6.5");
        } else if (R.id.ll_arbitrament_cost == id) {
            showKnowDialog("仲裁费用", "仲裁费用");
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

    /**
     * 显示我知道了提示对话框
     *
     * @param title
     * @param msg
     */
    private void showKnowDialog(String title, String msg) {
        new HMAlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("知道了")
                .create().show();

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

    @Override
    public void showData(GetArbitramentInputApplyDataResBean resBean) {
        //合计应还
        Number totalBackMoney = resBean.getAmount();
        mTvTotalBackMoney.setText(String.valueOf(totalBackMoney.doubleValue()));
        //利息意向
        Number purposeInterestRate = resBean.getDailyRate();
        if (0 != purposeInterestRate.intValue()) {
            mTvPurposeInterestRate.setText("年利率" + purposeInterestRate.intValue() + "%");
        }
        //逾期利息
        int outTimeInterestRateType = resBean.getOverdueInterestType();
        if (0 == outTimeInterestRateType) {
            mTvOutTimeInterest.setText("国家规定万分之6.5");
        } else {
            mTvOutTimeInterest.setText("未还金额的万分之" + outTimeInterestRateType);
        }
        //归还记录
        showRealBackRecord(resBean.getRepaymentRecordList());
        //催收证明
        List<GetArbitramentInputApplyDataResBean.UrgeExidenceListBean> list = resBean.getUrgeExidenceList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                showCollectionProve(list.get(i));
            }
        }
    }

    /**
     * 归还记录
     *
     * @param list
     */

    private void showRealBackRecord(List<GetArbitramentInputApplyDataResBean.RepaymentRecordListBean> list) {
        if (list != null) {
            int realBackMoney = 0;
            for (int i = 0; i < list.size(); i++) {
                realBackMoney = realBackMoney + list.get(i).getAmount();
            }
            mTvRealBackMoney.setText(String.valueOf(realBackMoney));
        }
    }

    /**
     * 催收证明
     *
     * @param bean
     */
    private void showCollectionProve(GetArbitramentInputApplyDataResBean.UrgeExidenceListBean bean) {
        if (bean == null) {
            return;
        }
        mCollectionProveBean = bean;
        CollectionProveEnum collectionProveEnum = CollectionProveEnum.getInstance(mCollectionProveBean.getUrgeEvidenceType());
        if (collectionProveEnum != null) {
            mTvCollectionProve.setText(collectionProveEnum.getDesc());
        }
    }
}
