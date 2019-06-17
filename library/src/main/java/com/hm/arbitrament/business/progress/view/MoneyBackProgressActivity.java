package com.hm.arbitrament.business.progress.view;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.business.progress.ArbitramentProgressContract;
import com.hm.arbitrament.business.progress.MoneyBackProgressContract;
import com.hm.arbitrament.business.progress.presenter.ArbitramentProgressPresenter;
import com.hm.arbitrament.business.progress.presenter.MoneyBackProgressPresenter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.base.utils.RouterUtil;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.dialog.HMBottomDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.hm.arbitrament.Constants.H5_URL_RETURN_MONEY_RULE;

/**
 * 退款进度
 */
public class MoneyBackProgressActivity extends BaseActivity<MoneyBackProgressPresenter> implements MoneyBackProgressContract.View, View.OnClickListener {

    public static final String EXTRA_KEY_ARB_NO = "arb_no";

    @BindView(R2.id.ll_backmoney_content)
    LinearLayout mLlContent;
    @BindView(R2.id.tv_backmoney_amount)
    TextView mTvAmount;
    @BindView(R2.id.rv_progress_content)
    RecyclerView mRvProgress;
    @BindView(R2.id.loading_view)
    HMLoadingView mLoadingView;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;

    private ProgressAdapter mAdapter;
    private String mArbNo;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_money_back_progress;
    }

    @Override
    protected MoneyBackProgressPresenter initPresenter() {
        return new MoneyBackProgressPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mArbNo = getIntent().getStringExtra(EXTRA_KEY_ARB_NO);
        if (bundle != null) {
            mArbNo = bundle.getString(EXTRA_KEY_ARB_NO);
        }

        mRvProgress.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProgressAdapter(this);
        mRvProgress.setAdapter(mAdapter);

        findViewById(R.id.ll_backmoney_amount).setOnClickListener(this);

        mPresenter.loadProgressData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_ARB_NO, mArbNo);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_backmoney_amount) {
            View contentView = getLayoutInflater().inflate(R.layout.arbitrament_dialog_money_back_detail, null);
            RecyclerView recyclerView = contentView.findViewById(R.id.rv_money_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            BackMoneyDetailAdapter detailAdapter = new BackMoneyDetailAdapter();
            List<BackMoneyDetailAdapter.IBackMoneyItem> list = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                list.add(new BackMoneyDetailAdapter.IBackMoneyItem() {
                    @Override
                    public String getTitle() {
                        return "律师费";
                    }

                    @Override
                    public String getMoney() {
                        return "￥100.00";
                    }
                });
            }
            detailAdapter.setNewData(list);
            recyclerView.setAdapter(detailAdapter);
            TextView tvTotal = contentView.findViewById(R.id.tv_progress_total);
            tvTotal.setText("￥400.00");
            HMBottomDialog dialog = new HMBottomDialog.Builder(this)
                    .setTitle("退款明细")
                    .setBottomView(contentView)
                    .create();
            dialog.show();
        }
    }

    @Override
    public void showDataLoading() {
        mLoadingView.showDataLoading();
    }

    @Override
    public void showDataLoadFailed() {
        mLoadingView.showDataFail(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadProgressData();
            }
        });
    }

    @Override
    public void showProgressList(List<ProgressAdapter.IProgressItem> list) {
        mLlContent.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mRvProgress.setVisibility(View.VISIBLE);
        mAdapter.setNewData(list);
    }

    @Override
    public void addFooterTips(CharSequence tips, View.OnClickListener listener) {
        TextView tvFooter = new TextView(this);
        tvFooter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tvFooter.setTextColor(getResources().getColor(R.color.uikit_function_remind));
        tvFooter.getPaint().setUnderlineText(true);
        int padding = (int) (15 * getResources().getDisplayMetrics().density);
        tvFooter.setPadding(padding, padding / 3, padding, padding);
        tvFooter.setOnClickListener(listener);
        tvFooter.setText(tips);
        mAdapter.addFooterView(tvFooter);
    }

    @Override
    public void showBackMoney(String txt) {
        mTvAmount.setText(txt);
        mBottomBar.updateTitle("退款规则");
        mBottomBar.setTitleVisible(true);
        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                //TODO 当前退款进度到哪了
                NavigationHelper.toReturnMoneyRulePage(MoneyBackProgressActivity.this, 2);
            }
        });
    }

}
