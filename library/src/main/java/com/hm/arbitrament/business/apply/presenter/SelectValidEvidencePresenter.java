package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.ElecEvidenceResBean;
import com.hm.arbitrament.business.apply.SelectValidEvidenceContract;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.event.CommBizEvent;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author syl
 * @time 2019/6/11 9:41 AM
 */
public class SelectValidEvidencePresenter extends BasePresenter<SelectValidEvidenceContract.View> implements SelectValidEvidenceContract.Presenter {

    private boolean mIsNeedRefresh = true;//是否需要刷新数据

    public SelectValidEvidencePresenter(@NonNull Context context, @NonNull SelectValidEvidenceContract.View view) {
        super(context, view);
    }

    @Override
    public void init(String iouId, String justId) {
        if (mIsNeedRefresh) {
            mView.showInit();
            ArbitramentApi.getElecEvidenceList(iouId, justId)
                    .compose(getProvider().<BaseResponse<List<ElecEvidenceResBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                    .map(RxUtil.<List<ElecEvidenceResBean>>handleResponse())
                    .subscribeWith(new CommSubscriber<List<ElecEvidenceResBean>>(mView) {
                        @Override
                        public void handleResult(List<ElecEvidenceResBean> list) {
                            mIsNeedRefresh = false;
                            mView.hideInit();
                            if (list == null || list.isEmpty()) {
                                mView.showDataEmpty();
                                return;
                            }
                            for (ElecEvidenceResBean bean : list) {
                                try {
                                    String createTime = bean.getCreateTime();
                                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime);
                                    createTime = new SimpleDateFormat("yyyy.MM.dd").format(date);
                                    bean.setCreateTime(createTime);
                                } catch (Exception e) {

                                }
                            }
                            mView.showEvidenceList(list);
                            mView.hideInit();
                        }

                        @Override
                        public void handleException(Throwable throwable, String s, String s1) {
                            mView.showInitFailed(s1);
                        }

                        @Override
                        public boolean isShowBusinessError() {
                            return false;
                        }

                        @Override
                        public boolean isShowCommError() {
                            return false;
                        }
                    });
        }
    }

    @Override
    public void refresh(String iouId, String justId) {
        ArbitramentApi.getElecEvidenceList(iouId, justId)
                .compose(getProvider().<BaseResponse<List<ElecEvidenceResBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<ElecEvidenceResBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<ElecEvidenceResBean>>(mView) {
                    @Override
                    public void handleResult(List<ElecEvidenceResBean> list) {
                        mIsNeedRefresh = false;
                        mView.hidePullDownView();
                        if (list == null || list.isEmpty()) {
                            mView.showDataEmpty();
                            return;
                        }
                        for (ElecEvidenceResBean bean : list) {
                            String createTime = bean.getCreateTime();
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime);
                                createTime = new SimpleDateFormat("yyyy.MM.dd").format(date);
                                bean.setCreateTime(createTime);
                            } catch (Exception e) {

                            }
                        }
                        mView.showEvidenceList(list);
                        mView.hideInit();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.hidePullDownView();
                    }
                });
    }


    /**
     * 存证删除成功
     *
     * @param commBizEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDeleteEvidenceSuccess(CommBizEvent commBizEvent) {
        if ("jietiao_event_delete_elec_evidence_success".equals(commBizEvent.key)) {
            mIsNeedRefresh = true;
        }
    }

    /**
     * 存证名称修改
     *
     * @param commBizEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChangeEvidenceName(CommBizEvent commBizEvent) {
        if ("jietiao_event_change_elec_evidence_name".equals(commBizEvent.key)) {
            mIsNeedRefresh = true;
        }
    }

}
