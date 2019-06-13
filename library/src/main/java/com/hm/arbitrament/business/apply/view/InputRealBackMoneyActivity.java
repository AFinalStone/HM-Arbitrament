package com.hm.arbitrament.business.apply.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.bean.BackMoneyRecordBean;
import com.hm.arbitrament.business.apply.ArbitramentServerAgreementContract;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 催收证明
 */
public class InputRealBackMoneyActivity<T extends MvpActivityPresenter> extends BaseActivity<T> implements ArbitramentServerAgreementContract.View {

    public static final int REQ_ADD_BACK_MONEY_RECORD = 100;
    public static final String EXTRA_KEY_BACK_MONEY_RECORD_LIST = "back_money_record_list";
    public static final String EXTRA_KEY_MAX_BACK_MONEY = "max_back_money";
    public static final String EXTRA_KEY_BACK_TIME_START_TIME = "back_time_start_time";

    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.rv_back_record)
    RecyclerView mRvBackRecord;

    BackMoneyRecordProveAdapter mAdapter;
    ArrayList<BackMoneyRecordBean> mListData;
    int mMaxBackMoney = -1;
    String mBackTimeStartTime;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_input_real_back_money;
    }

    @Override
    protected T initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mListData = (ArrayList<BackMoneyRecordBean>) getIntent().getSerializableExtra(EXTRA_KEY_BACK_MONEY_RECORD_LIST);
        mMaxBackMoney = getIntent().getIntExtra(EXTRA_KEY_MAX_BACK_MONEY, -1);
        mBackTimeStartTime = getIntent().getStringExtra(EXTRA_KEY_BACK_TIME_START_TIME);
        if (bundle != null) {
            mListData = (ArrayList<BackMoneyRecordBean>) bundle.getSerializable(EXTRA_KEY_BACK_MONEY_RECORD_LIST);
            mMaxBackMoney = bundle.getInt(EXTRA_KEY_MAX_BACK_MONEY, -1);
            mBackTimeStartTime = bundle.getString(EXTRA_KEY_BACK_TIME_START_TIME);
        }
        if (mListData == null) {
            mListData = new ArrayList<>();
        }

        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                startActivityForResult(new Intent(mContext, InputRealBackMoneyAddRecordActivity.class), REQ_ADD_BACK_MONEY_RECORD);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        mRvBackRecord.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BackMoneyRecordProveAdapter();
        HMLoadingView hmLoadingView = new HMLoadingView(mContext);
        hmLoadingView.showDataEmpty("");
        mAdapter.setEmptyView(hmLoadingView);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final BackMoneyRecordBean bean = mAdapter.getItem(position);
                if (bean == null) {
                    return;
                }
                if (R.id.rl_content == view.getId()) {
                    toAddRecord(bean);
                } else if (R.id.btn_delete == view.getId()) {
                    new HMAlertDialog.Builder(mContext)
                            .setMessage("您确定删除这条记录吗？")
                            .setMessageGravity(Gravity.CENTER)
                            .setPositiveButton("删除")
                            .setNegativeButton("取消")
                            .setOnClickListener(new HMAlertDialog.OnClickListener() {
                                @Override
                                public void onPosClick() {
                                    mListData.remove(bean);
                                    updateListData();
                                }

                                @Override
                                public void onNegClick() {

                                }
                            })
                            .create().show();
                }
            }
        });
        mRvBackRecord.setAdapter(mAdapter);
        if (mListData.isEmpty()) {
            toAddRecord(null);
        } else {
            updateListData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_KEY_BACK_MONEY_RECORD_LIST, mListData);
        outState.putInt(EXTRA_KEY_MAX_BACK_MONEY, mMaxBackMoney);
        outState.putString(EXTRA_KEY_BACK_TIME_START_TIME, mBackTimeStartTime);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_ADD_BACK_MONEY_RECORD == requestCode) {
            if (RESULT_OK == resultCode && data != null) {
                BackMoneyRecordBean bean = (BackMoneyRecordBean) data.getSerializableExtra(InputRealBackMoneyAddRecordActivity.EXTRA_KEY_ITEM);
                int position = mListData.indexOf(bean);
                if (position != -1) {
                    mListData.set(position, bean);
                } else {
                    mListData.add(bean);
                }
                updateListData();
            }
        }
    }


    private void toAddRecord(BackMoneyRecordBean bean) {
        Intent intent = new Intent(mContext, InputRealBackMoneyAddRecordActivity.class);
        intent.putExtra(InputRealBackMoneyAddRecordActivity.EXTRA_KEY_ITEM, bean);
        intent.putExtra(InputRealBackMoneyAddRecordActivity.EXTRA_KEY_MAX_BACK_MONEY, mMaxBackMoney);
        intent.putExtra(InputRealBackMoneyAddRecordActivity.EXTRA_KEY_BACK_TIME_START_TIME, mBackTimeStartTime);
        startActivityForResult(intent, REQ_ADD_BACK_MONEY_RECORD);
    }

    private void updateListData() {
        mAdapter.setNewData(mListData);
        int totalMoney = 0;
        for (BackMoneyRecordBean bean : mListData) {
            totalMoney = totalMoney + bean.getAmount();
        }
    }

    @OnClick(R2.id.btn_ok)
    public void onClick() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_KEY_BACK_MONEY_RECORD_LIST, mListData);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static class BackMoneyRecordProveAdapter extends BaseQuickAdapter<BackMoneyRecordBean, BaseViewHolder> {

        DecimalFormat mDecimalFormat = new DecimalFormat("¥ ,###");

        public BackMoneyRecordProveAdapter() {
            super(R.layout.arbitrament_item_real_back_money_record_list);
        }

        @Override
        protected void convert(BaseViewHolder helper, BackMoneyRecordBean item) {
            try {
                String strMoney = mDecimalFormat.format(item.getAmount());
                helper.setText(R.id.tv_money, strMoney);
            } catch (Exception e) {

            }
            String backTime = item.getRepaymentDate();
            backTime = backTime.replaceAll("-", "\\.");
            backTime = backTime.substring(0, 10);
            helper.setText(R.id.tv_time, "还款时间：" + backTime);
            helper.addOnClickListener(R.id.rl_content);
            helper.addOnClickListener(R.id.btn_delete);
        }

    }


}
