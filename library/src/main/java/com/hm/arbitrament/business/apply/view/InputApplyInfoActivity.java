package com.hm.arbitrament.business.apply.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.bean.BackMoneyRecordBean;
import com.hm.arbitrament.bean.CollectionProveBean;
import com.hm.arbitrament.bean.GetArbitramentInputApplyDataResBean;
import com.hm.arbitrament.bean.req.CreateArbOrderReqBean;
import com.hm.arbitrament.business.apply.InputApplyInfoContract;
import com.hm.arbitrament.business.apply.presenter.InputApplyInfoPresenter;
import com.hm.arbitrament.util.CacheDataUtil;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.logger.Logger;
import com.hm.iou.tools.Md5Util;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.hm.iou.uikit.dialog.HMBottomDialog;

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
    public static final int REQ_GET_ARB_AGREEMENT_SERVER = 102;//催收证明
    public static final int REQ_CHECK_SIGNATURE_PSD = 103;//校验签约密码
    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";
    public static final String EXTRA_KEY_LIST = "list";
    public static final String EXTRA_KEY_IS_RESUBMIT = "is_resubmit";
    private static final String BACK_NOTHING = "全部未还";


    @BindView(R2.id.tv_total_back_money)
    TextView mTvTotalBackMoney;//合计应还
    @BindView(R2.id.tv_real_back_money_left)
    TextView mTvRealBackMoneyLeft;//实际归还金钱标记
    @BindView(R2.id.tv_real_back_money)
    TextView mTvRealBackMoney;//实际归还
    @BindView(R2.id.iv_real_back_money)
    ImageView mIvRealBackMoney;//实际归还

    @BindView(R2.id.ll_arbitrament_money)
    LinearLayout mLlArbitramentMoney;//仲裁金额
    @BindView(R2.id.view_divider_arbitrament_money)
    View mViewDividerArbitramentMoney;//仲裁金额分割线
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
    @BindView(R2.id.ll_arbitrament_cost)
    LinearLayout mLlArbitramentCost;////仲裁费用
    @BindView(R2.id.view_arbitrament_cost_divider)
    View mViewArbitramentCostDivider;////仲裁费用分割线

    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;

    private String mIouId;
    private String mJustId;
    private ArrayList<String> mListElecEvidence;//有效凭证列表
    private boolean mIsSubmit;//是否提交

    private HMBottomDialog mBottomAddBackRecordDialog;//还款记录
    private boolean mIsBackNothing = false;//是否全部未还
    private HMAlertDialog mArbitramentCostDialog;
    private CollectionProveBean mCollectionProveBean;//催收证明
    private ArrayList<BackMoneyRecordBean> mBackMoneyRecordList;//还款记录
    private String mContractStartTime;//合同开始时间

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
        mListElecEvidence = getIntent().getStringArrayListExtra(EXTRA_KEY_LIST);
        mIsSubmit = getIntent().getBooleanExtra(EXTRA_KEY_IS_RESUBMIT, false);
        if (bundle != null) {
            mIouId = bundle.getString(EXTRA_KEY_IOU_ID);
            mJustId = bundle.getString(EXTRA_KEY_JUST_ID);
            mListElecEvidence = bundle.getStringArrayList(EXTRA_KEY_LIST);
            mIsSubmit = bundle.getBoolean(EXTRA_KEY_IS_RESUBMIT, false);
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
                double arbMoney = 0;
                try {
                    String strArbMoney = mTvArbitramentMoney.getText().toString();
                    arbMoney = Double.parseDouble(strArbMoney);
                } catch (Exception e) {

                }
                if (arbMoney < 300) {
                    showArbitramentMoneyTooSmallDialog();
                    return;
                }
                if (mIsSubmit) {
                    CreateArbOrderReqBean reqBean = new CreateArbOrderReqBean();
                    reqBean.setIouId(mIouId);//合同id
                    reqBean.setJusticeId(mJustId);//合同公证id
                    reqBean.setExEvidenceIdList(mListElecEvidence);
                    if (mBackMoneyRecordList == null) {
                        mBackMoneyRecordList = new ArrayList<>();
                    }
                    reqBean.setRepaymentRecordList(mBackMoneyRecordList);//归还记录
                    ArrayList<CollectionProveBean> list = new ArrayList<>();
                    list.add(mCollectionProveBean);
                    reqBean.setUrgeExidenceList(list);
                    mPresenter.resubmitOrder(reqBean);
                } else {
                    mPresenter.getAgreement(mIouId, mJustId);
                }
            }
        });
        mPresenter.getInputApplyInfoData(mIouId, mJustId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
        outState.putStringArrayList(EXTRA_KEY_LIST, mListElecEvidence);
        outState.putBoolean(EXTRA_KEY_IS_RESUBMIT, mIsSubmit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CacheDataUtil.setIsBackNothing(mContext, mIsBackNothing);
        CacheDataUtil.setBackMoneyRecordList(mContext, mBackMoneyRecordList);
        CacheDataUtil.setCollectionProveBean(mContext, mCollectionProveBean);
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
                ArrayList<BackMoneyRecordBean> list = (ArrayList<BackMoneyRecordBean>) data.getSerializableExtra(InputRealBackMoneyActivity.EXTRA_KEY_BACK_MONEY_RECORD_LIST);
                mTvRealBackMoney.setText("");
                //归还记录
                showRealBackRecord(list);
                checkValue();
            }
        } else if (REQ_INPUT_COLLECTION_PROVE == requestCode) {
            if (RESULT_OK == resultCode) {
                if (data == null) {
                    return;
                }
                mIvCollectionProve.setImageResource(R.mipmap.uikit_ic_arrow_right);
                mCollectionProveBean = (CollectionProveBean) data.getSerializableExtra(InputCollectionProveActivity.EXTRA_KEY_ITEM);
                //催收证明
                showCollectionProve(mCollectionProveBean);
                checkValue();
            }

        } else if (REQ_GET_ARB_AGREEMENT_SERVER == requestCode) {
            if (RESULT_OK == resultCode) {
                NavigationHelper.toCheckSignPwd(mContext, "输入签约密码", REQ_CHECK_SIGNATURE_PSD);
            }
        } else if (REQ_CHECK_SIGNATURE_PSD == requestCode) {
            CacheDataUtil.clearInputApplyInfoCacheData(mContext);
            if (RESULT_OK == resultCode) {
                String signPwd = data.getStringExtra("pwd");
                String signId = data.getStringExtra("sign_id");
                Logger.d("SignPwd = " + signPwd);
                Logger.d("SignId = " + signId);

                CreateArbOrderReqBean reqBean = new CreateArbOrderReqBean();
                reqBean.setIouId(mIouId);//合同id
                reqBean.setJusticeId(mJustId);//合同公证id
                reqBean.setSealId(signId);//签章id
                reqBean.setTransPswd(Md5Util.getMd5ByString(signPwd));//签约密码
                reqBean.setExEvidenceIdList(mListElecEvidence);
                if (mBackMoneyRecordList == null) {
                    mBackMoneyRecordList = new ArrayList<>();
                }
                reqBean.setRepaymentRecordList(mBackMoneyRecordList);//归还记录
                ArrayList<CollectionProveBean> list = new ArrayList<>();
                list.add(mCollectionProveBean);
                reqBean.setUrgeExidenceList(list);
                mPresenter.createOrder(reqBean);
            }
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

            if (mBottomAddBackRecordDialog == null) {
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.arbitrament_dialog_select_real_back_money_type, null, false);
                contentView.findViewById(R.id.btn_add_back_money_record).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomAddBackRecordDialog.dismiss();
                        String strTotalMoney = mTvTotalBackMoney.getText().toString();
                        NavigationHelper.toAddBackMoneyRecord(mContext, mBackMoneyRecordList, mContractStartTime, strTotalMoney, REQ_INPUT_REAL_BACK_MONEY_RECORD);
                    }
                });
                contentView.findViewById(R.id.btn_set_back_nothing).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomAddBackRecordDialog.dismiss();
                        mTvRealBackMoney.setText(BACK_NOTHING);
                        showRealBackRecord(null);
                        checkValue();
                    }
                });
                mBottomAddBackRecordDialog = new HMBottomDialog.Builder(mContext)
                        .setTitle("实际归还")
                        .setBottomView(contentView)
                        .create();
            }
            //判断是否填写了实际归还记录
            String realBackMoney = mTvRealBackMoney.getText().toString();
            if (TextUtils.isEmpty(realBackMoney) || BACK_NOTHING.equals(realBackMoney)) {
                mBottomAddBackRecordDialog.show();
            } else {
                String strTotalMoney = mTvTotalBackMoney.getText().toString();
                NavigationHelper.toAddBackMoneyRecord(mContext, mBackMoneyRecordList, mContractStartTime, strTotalMoney, REQ_INPUT_REAL_BACK_MONEY_RECORD);
            }
        } else if (R.id.ll_arbitrament_money == id) {
            showKnowDialog("仲裁金额", "仲裁金额=未还金额+逾期利息\n仲裁时未还部分的利息最高支持为年化24%；已还款部分最高支持年化36%；仲裁时逾期利息最高支持为年化24%");
        } else if (R.id.tv_collection_prove == id || R.id.iv_collection_prove == id) {
            NavigationHelper.toAddCollectionProve(mContext, mCollectionProveBean, REQ_INPUT_COLLECTION_PROVE);
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
        //合同开始时间
        mContractStartTime = resBean.getContractStartDate();
        //合计应还
        Double totalBackMoney = resBean.getAmount();
        mTvTotalBackMoney.setText(StringUtil.doubleToString01(totalBackMoney));
        //利息意向
        Double purposeInterestRate = resBean.getDailyRate();
        if (0 != purposeInterestRate) {
            mTvPurposeInterestRate.setText("年利率" + purposeInterestRate + "%");
        }
        //逾期利息
        int outTimeInterestRateType = resBean.getOverdueInterestType();
        if (0 == outTimeInterestRateType) {
            mTvOutTimeInterest.setText("国家规定万分之6.5");
        } else {
            mTvOutTimeInterest.setText("未还金额的万分之" + outTimeInterestRateType);
        }
        //归还记录
        mTvRealBackMoney.setText("");
        ArrayList<BackMoneyRecordBean> backMoneyRecordList = resBean.getRepaymentRecordList();
        if (backMoneyRecordList == null || backMoneyRecordList.isEmpty()) {
            //本地缓存
            boolean isBackNothing = CacheDataUtil.getIsBackNothing(mContext);
            if (isBackNothing) {
                mTvRealBackMoney.setText(BACK_NOTHING);
            } else {
                backMoneyRecordList = CacheDataUtil.getBackMoneyRecordList(mContext);
                if (mIsSubmit) {//如果是重复提交
                    if (backMoneyRecordList == null || backMoneyRecordList.isEmpty()) {
                        mTvRealBackMoney.setText(BACK_NOTHING);
                    }
                }
            }
        }
        showRealBackRecord(backMoneyRecordList);
        //催收证明
        List<CollectionProveBean> list = resBean.getUrgeExidenceList();
        CollectionProveBean collectionProveBean = null;
        if (list != null && list.size() > 0) {
            collectionProveBean = list.get(0);
        }
        if (collectionProveBean == null) {
            //本地缓存
            collectionProveBean = CacheDataUtil.getCollectionProveBean(mContext);
        }
        showCollectionProve(collectionProveBean);
        checkValue();
    }

    @Override
    public void showArbMoney(String strMoney) {
        mLlArbitramentMoney.setVisibility(View.VISIBLE);
        mViewDividerArbitramentMoney.setVisibility(View.VISIBLE);
        mTvArbitramentMoney.setText(strMoney);
    }

    @Override
    public void showArbCost(String strCost) {
        mLlArbitramentCost.setVisibility(View.VISIBLE);
        mViewArbitramentCostDivider.setVisibility(View.VISIBLE);
        mTvArbitramentCost.setText(strCost);
    }

    /**
     * 实际归还
     *
     * @param list
     */

    private void showRealBackRecord(ArrayList<BackMoneyRecordBean> list) {
        mBackMoneyRecordList = list;
        if (BACK_NOTHING.equals(mTvRealBackMoney.getText().toString())) {
            mIsBackNothing = true;
            mTvRealBackMoneyLeft.setVisibility(View.GONE);
            getArbitramentCost();
            return;
        }
        mIsBackNothing = false;
        //初始化或者用户未添加实际归还记录
        if (list == null || list.isEmpty()) {
            mLlArbitramentCost.setVisibility(View.GONE);
            mLlArbitramentMoney.setVisibility(View.GONE);
            return;
        }
        //计算仲裁金额和仲裁费用
        double realBackMoney = 0;
        for (int i = 0; i < list.size(); i++) {
            BackMoneyRecordBean bean = list.get(i);
            realBackMoney = realBackMoney + bean.getAmount();
            bean.setCreateTime(System.currentTimeMillis());
            String backTime = bean.getRepaymentDate();
            //修改归还时间格式
            backTime = backTime.replaceAll("\\.", "-");
            bean.setRepaymentDate(backTime);
        }
        mTvRealBackMoney.setText(StringUtil.doubleToString01(realBackMoney));
        mTvRealBackMoneyLeft.setVisibility(View.VISIBLE);
        getArbitramentCost();
    }

    /**
     * 催收证明
     *
     * @param bean
     */
    private void showCollectionProve(CollectionProveBean bean) {
        if (bean == null) {
            return;
        }
        mCollectionProveBean = bean;
        String desc = mCollectionProveBean.getDescription();
        if (!TextUtils.isEmpty(desc)) {
            mTvCollectionProve.setText(desc);
            getArbitramentCost();
        }
    }

    /**
     * 计算仲裁金额和仲裁费率
     */
    private void getArbitramentCost() {
        String strTealBackMoney = mTvRealBackMoney.getText().toString();
        String strCollectionProve = mTvCollectionProve.getText().toString();
        if (!TextUtils.isEmpty(strTealBackMoney) && !TextUtils.isEmpty(strCollectionProve)) {
            double realBackMoney = 0f;
            if (!mIsBackNothing) {
                try {
                    realBackMoney = Double.parseDouble(strTealBackMoney);
                } catch (Exception e) {

                }
            }
            mPresenter.getArbitramentCost(mIouId, mJustId, realBackMoney);
        }
    }
}
