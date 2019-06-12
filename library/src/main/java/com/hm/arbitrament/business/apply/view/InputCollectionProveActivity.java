package com.hm.arbitrament.business.apply.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.bean.GetArbitramentInputApplyDataResBean;
import com.hm.arbitrament.business.apply.InputCollectionProveContract;
import com.hm.arbitrament.business.apply.presenter.InputCollectionProvePresenter;
import com.hm.iou.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 催收证明
 */
public class InputCollectionProveActivity extends BaseActivity<InputCollectionProvePresenter> implements InputCollectionProveContract.View {

    public static final int REQ_SELECT_PIC = 100;

    public static final String EXTRA_KEY_ITEM = "item";

    @BindView(R2.id.rv_collection_prove)
    RecyclerView mRvCollectionProve;

    CollectionProveAdapter mAdapter;
    GetArbitramentInputApplyDataResBean.UrgeExidenceListBean mBean;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_input_collection_prove;
    }

    @Override
    protected InputCollectionProvePresenter initPresenter() {
        return new InputCollectionProvePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mBean = (GetArbitramentInputApplyDataResBean.UrgeExidenceListBean) getIntent().getSerializableExtra(EXTRA_KEY_ITEM);
        if (bundle != null) {
            mBean = (GetArbitramentInputApplyDataResBean.UrgeExidenceListBean) bundle.getSerializable(EXTRA_KEY_ITEM);
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
        outState.putSerializable(EXTRA_KEY_ITEM, mBean);
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

    @OnClick(R2.id.btn_ok)
    public void onClick() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_KEY_ITEM, mBean);
        setResult(RESULT_OK, intent);
        finish();
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
