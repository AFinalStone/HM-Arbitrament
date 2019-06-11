package com.hm.arbitrament.business.pay.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.business.pay.PayContract;
import com.hm.arbitrament.business.pay.PayPresenter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMGrayDividerItemDecoration;
import com.hm.iou.uikit.dialog.HMAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 起始页面
 */
public class PayActivity extends BaseActivity<PayPresenter> implements PayContract.View {


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
    MoneyListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_pay;
    }

    @Override
    protected PayPresenter initPresenter() {
        return new PayPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mRvMoneyList.setLayoutManager(new LinearLayoutManager(this));
        mRvMoneyList.addItemDecoration(new HMGrayDividerItemDecoration(mContext, HMGrayDividerItemDecoration.VERTICAL));
        mAdapter = new MoneyListAdapter();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (R.id.iv_warn == view.getId()) {
                    IMoneyItem item = mAdapter.getItem(position);
                    if (item != null && !TextUtils.isEmpty(item.getWarnDialogContent())) {
                        new HMAlertDialog.Builder(mContext)
                                .setMessage(item.getWarnDialogContent())
                                .create()
                                .show();
                    }

                }
            }
        });
        mRvMoneyList.setAdapter(mAdapter);
        mPresenter.init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @OnClick(R2.id.btn_ok)
    public void onClick() {
    }

    @Override
    public void showData(List<IMoneyItem> list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void showTotalPayMoney(String money) {
        mTvTotalPayMoney.setText(money);
    }
}
