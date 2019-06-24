package com.hm.arbitrament.business.apply.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.bean.ElecEvidenceResBean;
import com.hm.arbitrament.business.apply.SelectValidEvidenceContract;
import com.hm.arbitrament.business.apply.presenter.SelectValidEvidencePresenter;
import com.hm.arbitrament.event.ClosePageEvent;
import com.hm.arbitrament.util.CacheDataUtil;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.router.Router;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 有效凭证
 */
public class SelectValidEvidenceActivity extends BaseActivity<SelectValidEvidencePresenter> implements SelectValidEvidenceContract.View {

    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";
    public static final String EXTRA_KEY_IS_RESUBMIT = "is_resubmit";


    private String mIouId;
    private String mJustId;
    private boolean mIsSubmit;//是否提交

    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R2.id.init_loading)
    HMLoadingView mInitLoading;
    @BindView(R2.id.rv_evidence)
    RecyclerView mRvEvidence;
    @BindView(R2.id.btn_ok)
    Button mBtnOk;

    EvidenceAdapter mAdapter;
    private HMAlertDialog mExitDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_select_valid_evidence;
    }

    @Override
    protected SelectValidEvidencePresenter initPresenter() {
        return new SelectValidEvidencePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        mIsSubmit = getIntent().getBooleanExtra(EXTRA_KEY_IS_RESUBMIT, false);
        if (bundle != null) {
            mIouId = bundle.getString(EXTRA_KEY_IOU_ID);
            mJustId = bundle.getString(EXTRA_KEY_JUST_ID);
            mIsSubmit = bundle.getBoolean(EXTRA_KEY_IS_RESUBMIT, false);
        }
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                NavigationHelper.toUploadEvidence(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        mAdapter = new EvidenceAdapter();
        mRvEvidence.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ElecEvidenceResBean> list = mAdapter.getData();
                if (list != null) {
                    ElecEvidenceResBean bean = list.get(position);
                    if (bean != null) {
                        if (1 == bean.getFileType()) {
                            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/iou/contract_evidence_image_detail")
                                    .withString("iou_id", mIouId)
                                    .withString("url", bean.getUrl())
                                    .withString("evidence_name", bean.getName())
                                    .withString("evidence_id", bean.getExEvidenceAutoId())
                                    .navigation(mContext);
                        } else if (2 == bean.getFileType()) {
                            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/iou/contract_evidence_pdf_detail")
                                    .withString("pdf_url", bean.getUrl())
                                    .withString("evidence_name", bean.getName())
                                    .withString("evidence_id", bean.getExEvidenceAutoId())
                                    .navigation(mContext);
                        }
                    }
                }
            }
        });
        mRvEvidence.setAdapter(mAdapter);
        mInitLoading.showDataLoading();
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.refresh(mIouId, mJustId);
            }
        });
        //清除编辑页面的缓存数据
        CacheDataUtil.clearInputApplyInfoCacheData(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.init(mIouId, mJustId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
        outState.putBoolean(EXTRA_KEY_IS_RESUBMIT, mIsSubmit);
    }

    @Override
    public void onBackPressed() {
        if (mExitDialog == null) {
            mExitDialog = new HMAlertDialog.Builder(mContext)
                    .setTitle("放弃仲裁")
                    .setMessage("是否要放弃仲裁申请？")
                    .setMessageGravity(Gravity.CENTER)
                    .setPositiveButton("取消")
                    .setNegativeButton("放弃")
                    .setOnClickListener(new HMAlertDialog.OnClickListener() {
                        @Override
                        public void onPosClick() {

                        }

                        @Override
                        public void onNegClick() {
                            CacheDataUtil.clearInputApplyInfoCacheData(mContext);
                            EventBus.getDefault().post(new ClosePageEvent());
                        }
                    })
                    .create();
        }
        mExitDialog.show();
    }


    @OnClick(R2.id.btn_ok)
    public void onClick() {
        NavigationHelper.toInputApplyInfo(mContext, mIouId, mJustId, mAdapter.getIdList(), mIsSubmit);
    }

    @Override
    public void showInit() {
        mInitLoading.setVisibility(View.VISIBLE);
        mInitLoading.startLoadingAnim();
    }

    @Override
    public void hideInit() {
        mInitLoading.setVisibility(View.GONE);
        mInitLoading.stopLoadingAnim();
    }

    @Override
    public void showInitFailed(String msg) {
        mInitLoading.setVisibility(View.VISIBLE);
        mInitLoading.showDataFail(msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.refresh(mIouId, mJustId);
            }
        });
    }

    @Override
    public void showDataEmpty() {
        mInitLoading.setVisibility(View.VISIBLE);
        mInitLoading.showDataEmpty("");
    }

    @Override
    public void hidePullDownView() {
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showEvidenceList(List<ElecEvidenceResBean> listEvidence) {
        if (listEvidence == null || listEvidence.isEmpty()) {
            mBtnOk.setEnabled(false);
        } else {
            mBtnOk.setEnabled(true);
        }
        mAdapter.setNewData(listEvidence);
    }

    public static class EvidenceAdapter extends BaseQuickAdapter<ElecEvidenceResBean, BaseViewHolder> {

        public EvidenceAdapter() {
            super(R.layout.arbitrament_item_select_valid_evidence);
        }

        @Override
        protected void convert(BaseViewHolder helper, ElecEvidenceResBean item) {
            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_time, item.getCreateTime());
            if (1 == item.getFileType()) {
                helper.setImageResource(R.id.iv_logo, R.mipmap.arbitrament_ic_elec_evidence_flag_image);
            } else {
                helper.setImageResource(R.id.iv_logo, R.mipmap.arbitrament_ic_elec_evidence_flag_pdf);
            }
        }

        public ArrayList<String> getIdList() {
            ArrayList<String> idList = new ArrayList<>();
            List<ElecEvidenceResBean> list = getData();
            if (list != null) {
                for (ElecEvidenceResBean bean : list) {
                    idList.add(bean.getExEvidenceId());
                }
            }
            return idList;
        }

    }
}
