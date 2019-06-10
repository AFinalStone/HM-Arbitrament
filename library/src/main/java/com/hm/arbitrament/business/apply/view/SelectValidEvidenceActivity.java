package com.hm.arbitrament.business.apply.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.IOUExtResult;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * 有效凭证
 *
 * @param <T>
 */
public class SelectValidEvidenceActivity<T extends MvpActivityPresenter> extends BaseActivity<T> {

    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final int REQ_SELECT_EVIDENCE_DETAIL = 100;

    private String mIouId;

    @BindView(R2.id.TopBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.init_loading)
    HMLoadingView mInitLoading;
    @BindView(R2.id.rv_evidence)
    RecyclerView mRvEvidence;
    @BindView(R2.id.btn_ok)
    Button mBtnOk;

    EvidenceAdapter mAdapter;
    private Disposable mDisposable;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_select_valid_evidence;
    }

    @Override
    protected T initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        if (bundle != null) {
            mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        }
        mAdapter = new EvidenceAdapter();
        mRvEvidence.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                IOUExtResult.ExtEvidence item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                if (R.id.rl_content == view.getId()) {
                    mAdapter.addOrRemoveCheck(item);
                    if (mAdapter.getSelectObjects().isEmpty()) {
                        mBtnOk.setEnabled(false);
                    } else {
                        mBtnOk.setEnabled(true);
                    }
                } else if (R.id.iv_arrow == view.getId()) {
                    NavigationHelper.toSelectValidEvidenceDetailActivity(mContext, REQ_SELECT_EVIDENCE_DETAIL
                            , item, mAdapter.getSelectObjects().contains(item));
                }
            }
        });
        mRvEvidence.setAdapter(mAdapter);
        mInitLoading.showDataLoading();
        getEvidenceList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_SELECT_EVIDENCE_DETAIL == requestCode) {
            IOUExtResult.ExtEvidence item = data.getParcelableExtra(SelectValidEvidenceDetailActivity.EXTRA_KEY_ITEM);
            if (item == null) {
                return;
            }
            HashSet<IOUExtResult.ExtEvidence> selectObjects = mAdapter.getSelectObjects();
            if (RESULT_OK == resultCode) {
                selectObjects.add(item);
            } else {
                selectObjects.remove(item);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @OnClick(R2.id.btn_ok)
    public void onClick() {
        NavigationHelper.toNeedKnowByArbitrament(mContext);
    }

    private void showEvidenceList(IOUExtResult iouExtResult) {
        if (iouExtResult == null) {
            finish();
            return;
        }
        List<IOUExtResult.ExtEvidence> list = iouExtResult.getExEvidenceList();
        if (list == null || list.isEmpty()) {
            mInitLoading.setVisibility(View.VISIBLE);
            mInitLoading.showDataEmpty("数据为空");
            return;
        }
        mAdapter.setNewData(list);
    }

    private void getEvidenceList() {
        mDisposable = ArbitramentApi.getElecExDetails(mIouId)
                .map(RxUtil.<IOUExtResult>handleResponse())
                .subscribeWith(new CommSubscriber<IOUExtResult>(this) {

                    @Override
                    public void handleResult(IOUExtResult iouExtResult) {
                        mInitLoading.setVisibility(View.GONE);
                        mInitLoading.stopLoadingAnim();
                        showEvidenceList(iouExtResult);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mInitLoading.showDataFail(s1, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getEvidenceList();
                            }
                        });
                    }
                });
    }

    public static class EvidenceAdapter extends BaseQuickAdapter<IOUExtResult.ExtEvidence, BaseViewHolder> {

        private HashSet<IOUExtResult.ExtEvidence> mSelectObject = new HashSet();

        public EvidenceAdapter() {
            super(R.layout.arbitrament_item_select_valid_evidence);
        }

        @Override
        protected void convert(BaseViewHolder helper, IOUExtResult.ExtEvidence item) {
            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_time, item.getContent());
            if (mSelectObject.contains(item)) {
                helper.setImageResource(R.id.iv_select, R.mipmap.uikit_icon_check_black);
            } else {
                helper.setImageResource(R.id.iv_select, R.mipmap.uikit_icon_check_default);
            }
            helper.addOnClickListener(R.id.rl_content);
            helper.addOnClickListener(R.id.iv_arrow);
        }

        public void addOrRemoveCheck(IOUExtResult.ExtEvidence position) {
            if (mSelectObject.contains(position)) {
                mSelectObject.remove(position);
            } else {
                mSelectObject.add(position);
            }
            notifyDataSetChanged();
        }

        public HashSet<IOUExtResult.ExtEvidence> getSelectObjects() {
            return mSelectObject;
        }
    }
}
