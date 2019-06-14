package com.hm.arbitrament.business.apply.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.bean.CollectionProveBean;
import com.hm.arbitrament.bean.GetCollectionProveResBean;
import com.hm.arbitrament.business.apply.InputCollectionProveContract;
import com.hm.arbitrament.business.apply.presenter.InputCollectionProvePresenter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.photo.CompressPictureUtil;
import com.hm.iou.router.Router;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 催收证明
 */
public class InputCollectionProveActivity extends BaseActivity<InputCollectionProvePresenter> implements InputCollectionProveContract.View {

    public static final int REQ_OPEN_SELECT_PIC = 100;
    public static final int REQ_OPEN_PIC_DETAIL = 101;

    public static final String EXTRA_KEY_ITEM = "item";

    @BindView(R2.id.rv_collection_prove)
    RecyclerView mRvCollectionProve;
    @BindView(R2.id.ll_add_collection_prove)
    LinearLayout mLlAddCollectionProve;
    @BindView(R2.id.ll_collection_prove_detail)
    LinearLayout mLlAddCollectionProveDetail;
    @BindView(R2.id.btn_ok)
    Button mBtnOk;

    CollectionProveAdapter mAdapter;
    CollectionProveBean mBean;

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
        mBean = (CollectionProveBean) getIntent().getSerializableExtra(EXTRA_KEY_ITEM);
        if (bundle != null) {
            mBean = (CollectionProveBean) bundle.getSerializable(EXTRA_KEY_ITEM);
        }
        mRvCollectionProve.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CollectionProveAdapter();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //设置当前选中的数据
                mAdapter.setCurrentSelectPos(position);
                if (mBean == null) {
                    mBean = new CollectionProveBean();
                }
                List<GetCollectionProveResBean> list = mAdapter.getData();
                GetCollectionProveResBean selectBean = list.get(mAdapter.getCurrentSelectPos());
                if (selectBean != null) {
                    mBean.setUrgeEvidenceType(selectBean.getUrgeEvidenceType());
                    mBean.setDescription(selectBean.getDescription());
                }
                checkValue();
            }
        });
        mRvCollectionProve.setAdapter(mAdapter);

        if (mBean != null && !TextUtils.isEmpty(mBean.getFileId()) && !TextUtils.isEmpty(mBean.getImageUrl())) {
            showImage(mBean.getFileId(), mBean.getImageUrl());
        }
        //获取催收证明列表
        mPresenter.getCollectionProvelist();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_KEY_ITEM, mBean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_OPEN_SELECT_PIC == requestCode && RESULT_OK == resultCode) {
            String firstPath = data.getStringExtra("extra_result_selection_path_first");
            if (firstPath != null) {
                CompressPictureUtil.compressPic(mContext, firstPath, new CompressPictureUtil.OnCompressListener() {
                    @Override
                    public void onCompressPicSuccess(File file) {
                        mPresenter.uploadImage(file);
                    }
                });
            }
        } else if (REQ_OPEN_PIC_DETAIL == requestCode && RESULT_OK == resultCode) {
            if (mBean != null) {
                mBean.setFileId("");
                mBean.setImageUrl("");
                mLlAddCollectionProveDetail.setVisibility(View.GONE);
                mLlAddCollectionProve.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showProveList(List<GetCollectionProveResBean> list) {
        mAdapter.setNewData(list);
        if (mBean != null) {
            int proveType = mBean.getUrgeEvidenceType();
            for (int i = 0; i < list.size(); i++) {
                if (proveType == list.get(i).getUrgeEvidenceType()) {
                    mAdapter.setCurrentSelectPos(i);
                    checkValue();
                    break;
                }
            }
        }
    }

    @Override
    public void showImage(String fileId, String fileUrl) {
        if (mBean == null) {
            mBean = new CollectionProveBean();
        }
        mBean.setFileId(fileId);
        mBean.setImageUrl(fileUrl);
        mLlAddCollectionProveDetail.setVisibility(View.VISIBLE);
        mLlAddCollectionProve.setVisibility(View.GONE);
        checkValue();
    }

    @OnClick({R2.id.btn_ok, R2.id.ll_add_collection_prove, R2.id.ll_collection_prove_detail})
    public void onClick(View view) {
        if (R.id.btn_ok == view.getId()) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_KEY_ITEM, mBean);
            setResult(RESULT_OK, intent);
            finish();
        } else if (R.id.ll_add_collection_prove == view.getId()) {
            Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                    .withString("enable_select_max_num", String.valueOf(1))
                    .navigation(this, REQ_OPEN_SELECT_PIC);
        } else if (R.id.ll_collection_prove_detail == view.getId()) {
            if (mBean == null || TextUtils.isEmpty(mBean.getImageUrl())) {
                return;
            }
            Intent intent = new Intent(mContext, InputCollectionProveImageDetailActivity.class);
            intent.putExtra(InputCollectionProveImageDetailActivity.EXTRA_KEY_URL, mBean.getImageUrl());
            startActivityForResult(intent, REQ_OPEN_PIC_DETAIL);
        }

    }

    private void checkValue() {
        if (mBean == null) {
            mBtnOk.setEnabled(false);
            return;
        }
        if (TextUtils.isEmpty(mBean.getFileId())) {
            mBtnOk.setEnabled(false);
            return;
        }
        if (TextUtils.isEmpty(mBean.getImageUrl())) {
            mBtnOk.setEnabled(false);
            return;
        }
        if (0 == mBean.getUrgeEvidenceType()) {
            mBtnOk.setEnabled(false);
            return;
        }
        mBtnOk.setEnabled(true);
    }


    public static class CollectionProveAdapter extends BaseQuickAdapter<GetCollectionProveResBean, BaseViewHolder> {

        private int mCurrentSelectPos = -1;

        public CollectionProveAdapter() {
            super(R.layout.arbitrament_item_collection_prove_list);
        }

        @Override
        protected void convert(BaseViewHolder helper, GetCollectionProveResBean bean) {
            helper.setText(R.id.tv_name, bean.getDescription());
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

        /**
         * 获取当前选中的位置
         */
        public int getCurrentSelectPos() {
            return mCurrentSelectPos;
        }
    }

}
