package com.hm.arbitrament.business.fail;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.FailReasonResBean;
import com.hm.arbitrament.business.apply.presenter.BasePresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

public class AuditFailPresenter extends BasePresenter<AuditFailContract.View> implements AuditFailContract.Presenter {

    public AuditFailPresenter(@NonNull Context context, @NonNull AuditFailContract.View view) {
        super(context, view);
    }

    @Override
    public void getFailReason(String arbNo) {
        ArbitramentApi.getFailReason(arbNo)
                .compose(getProvider().<BaseResponse<FailReasonResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<FailReasonResBean>handleResponse())
                .subscribeWith(new CommSubscriber<FailReasonResBean>(mView) {
                    @Override
                    public void handleResult(FailReasonResBean data) {
                        if (data.isCanRetry()) {
                            mView.showDocCompletion();
                        }
                        List<String> list = data.getReasonList();
                        StringBuilder sb = new StringBuilder();
                        if (list != null && !list.isEmpty()) {
                            for (String s : list) {
                                sb.append(s).append("\n");
                            }
                        }
                        mView.showFailReason(sb.toString());
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String msg) {
                        mView.showDataLoadFail(msg);
                    }

                    @Override
                    public boolean isShowCommError() {
                        return false;
                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }
                });
    }

    @Override
    public void cancelArbitrament(final String arbApplyNo, int type, String reason) {
        mView.showLoadingView();
        ArbitramentApi.cancelArbitrament(arbApplyNo, type, reason)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object o) {
                        mView.dismissLoadingView();
                        //取消之后，进入仲裁进度页面
                        mView.closeCurrPage();
                        NavigationHelper.toArbitramentProgressPage(mContext, arbApplyNo,true);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();

                    }
                });
    }


}
