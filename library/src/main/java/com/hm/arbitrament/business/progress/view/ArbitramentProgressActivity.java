package com.hm.arbitrament.business.progress.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.business.progress.ArbitramentProgressContract;
import com.hm.arbitrament.business.progress.presenter.ArbitramentProgressPresenter;
import com.hm.iou.base.BaseActivity;
import com.hm.arbitrament.R2;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.HMLoadingView;

import java.util.List;

import butterknife.BindView;

/**
 * 仲裁进度
 */
public class ArbitramentProgressActivity extends BaseActivity<ArbitramentProgressPresenter> implements ArbitramentProgressContract.View {

    @BindView(R2.id.rv_progress_content)
    RecyclerView mRvProgress;
    @BindView(R2.id.loading_view)
    HMLoadingView mLoadingView;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;

    private ProgressAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_arbitrament_progress;
    }

    @Override
    protected ArbitramentProgressPresenter initPresenter() {
        return new ArbitramentProgressPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mRvProgress.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProgressAdapter(this);
        mRvProgress.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_progress_title) {
                    String link = (String) view.getTag();
                    if (!TextUtils.isEmpty(link)) {
                        NavigationHelper.toPdfPage(ArbitramentProgressActivity.this, link);
                    }
                }
            }
        });

        mPresenter.loadProgressData();
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
}
