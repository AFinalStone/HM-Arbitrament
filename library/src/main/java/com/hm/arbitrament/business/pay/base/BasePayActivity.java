package com.hm.arbitrament.business.pay.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.uikit.HMGrayDividerItemDecoration;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.dialog.HMAlertDialog;

import java.util.List;

import butterknife.BindView;

/**
 * 起始页面
 */
public abstract class BasePayActivity<T extends MvpActivityPresenter> extends BaseActivity<T> implements BasePayContract.BasePayView {

    @BindView(R2.id.rv_money_list)
    RecyclerView mRvMoneyList;
    @BindView(R2.id.tv_bottom_remark)
    TextView mTvBottomRemark;
    @BindView(R2.id.tv_total_pay_money)
    TextView mTvTotalPayMoney;
    @BindView(R2.id.tv_time_count_down)
    TextView mTvTimeCountDown;
    @BindView(R2.id.btn_ok)
    Button mBtnOk;
    @BindView(R2.id.tv_discount)
    TextView mTvDiscount;
    @BindView(R2.id.tv_real_total_pay_money)
    TextView tvRealTotalPayMoney;
    @BindView(R2.id.init_loading)
    HMLoadingView mInitLoading;

    MoneyListAdapter mAdapter;

    private HMAlertDialog mExitDialog;

    /**
     * 初始化
     */
    protected abstract void init(Bundle bundle);

    /**
     * 刷新数据
     */
    protected abstract void refresh();

    /**
     * 支付
     */
    protected abstract void pay();


    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_base_pay;
    }


    @Override
    protected void initEventAndData(Bundle bundle) {
        mRvMoneyList.setLayoutManager(new LinearLayoutManager(this));
        mRvMoneyList.addItemDecoration(new HMGrayDividerItemDecoration(mContext, HMGrayDividerItemDecoration.VERTICAL));
        mAdapter = new MoneyListAdapter();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IMoneyItem item = mAdapter.getItem(position);
                if (item != null && !TextUtils.isEmpty(item.getWarnDialogContent())) {
                    new HMAlertDialog.Builder(mContext)
                            .setTitle(item.getName())
                            .setMessage(item.getWarnDialogContent())
                            .setPositiveButton("知道了")
                            .create()
                            .show();
                }
            }
        });
        mRvMoneyList.setAdapter(mAdapter);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });
        init(bundle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        if (mExitDialog == null) {
            mExitDialog = new HMAlertDialog.Builder(mContext)
                    .setTitle("放弃支付")
                    .setMessage("您要稍后再支付吗？")
                    .setMessageGravity(Gravity.CENTER)
                    .setPositiveButton("继续支付")
                    .setNegativeButton("稍后支付")
                    .setOnClickListener(new HMAlertDialog.OnClickListener() {
                        @Override
                        public void onPosClick() {

                        }

                        @Override
                        public void onNegClick() {
                            finish();
                        }
                    })
                    .create();
        }
        mExitDialog.show();
    }

    public void showData(List<IMoneyItem> list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void showTotalPayMoney(String totalPayMoney, String realPayMoney) {
        if (!TextUtils.isEmpty(totalPayMoney)) {
            SpannableString msp = new SpannableString(totalPayMoney);
            msp.setSpan(new StrikethroughSpan(), 0, totalPayMoney.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvTotalPayMoney.setText(msp);
        }
        if (!TextUtils.isEmpty(totalPayMoney)) {
            tvRealTotalPayMoney.setText(totalPayMoney);
        }
    }

    @Override
    public void showDiscount(String discount) {
        if (TextUtils.isEmpty(discount)) {
            mTvDiscount.setVisibility(View.INVISIBLE);
        } else {
            mTvDiscount.setVisibility(View.VISIBLE);
            mTvDiscount.setText(discount);
        }
    }

    @Override
    public void showBottomRemark(String remark) {
        mTvBottomRemark.setText(remark);
    }

    @Override
    public void showInitLoadingView() {
        mInitLoading.setVisibility(View.VISIBLE);
        mInitLoading.showDataLoading();
    }

    @Override
    public void hideInitLoadingView() {
        mInitLoading.stopLoadingAnim();
        mInitLoading.setVisibility(View.GONE);
    }

    @Override
    public void showInitLoadingFailed(String msg) {
        mInitLoading.setVisibility(View.VISIBLE);
        mInitLoading.showDataFail(msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }
}
