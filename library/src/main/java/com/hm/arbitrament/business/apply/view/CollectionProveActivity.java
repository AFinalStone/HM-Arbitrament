package com.hm.arbitrament.business.apply.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.business.apply.CollectionProveContract;
import com.hm.arbitrament.business.apply.presenter.CollectionProvePresenter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.logger.Logger;

import java.util.List;

import butterknife.BindView;

/**
 * 催收证明
 */
public class CollectionProveActivity extends BaseActivity<CollectionProvePresenter> implements CollectionProveContract.View {

    public static final int REQ_SELECT_PIC = 100;
    public static final String EXTRA_KEY_URL = "url";
    private String mUrl;

    @BindView(R2.id.rv_collection_prove)
    RecyclerView mRvCollectionProve;

    CollectionProveAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_collection_prove;
    }

    @Override
    protected CollectionProvePresenter initPresenter() {
        return new CollectionProvePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mUrl = getIntent().getStringExtra(EXTRA_KEY_URL);
        if (bundle != null) {
            mUrl = bundle.getString(EXTRA_KEY_URL);
        }
        mRvCollectionProve.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CollectionProveAdapter();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setCurrentSelectPos(position);
            }
        });
        mRvCollectionProve.setAdapter(mAdapter);
        mPresenter.init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_URL, mUrl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_SELECT_PIC == requestCode) {
        }
    }

    @Override
    public void showData(List<String> list) {
        mAdapter.setNewData(list);
    }


    public static class CollectionProveAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        private int mCurrentSelectPos = -1;

        public CollectionProveAdapter() {
            super(R.layout.arbitrament_item_collection_prove_list);
        }

        @Override
        protected void convert(BaseViewHolder helper, String name) {
            helper.setText(R.id.tv_name, name);
            if (mCurrentSelectPos == helper.getLayoutPosition()) {
                helper.setImageResource(R.id.iv_name, R.mipmap.uikit_icon_check_black);
            } else {
                helper.setImageResource(R.id.iv_name, R.mipmap.uikit_icon_check_default);
            }
        }


        /**
         * 设置当前选中的位置
         */
        public void setCurrentSelectPos(int selectPos) {
            mCurrentSelectPos = selectPos;
            notifyDataSetChanged();
        }

    }

}
