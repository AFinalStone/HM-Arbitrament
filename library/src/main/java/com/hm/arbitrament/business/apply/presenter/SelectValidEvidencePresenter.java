package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.ElecEvidenceRes;
import com.hm.arbitrament.business.apply.SelectValidEvidenceContract;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;


/**
 * @author syl
 * @time 2019/6/11 9:41 AM
 */
public class SelectValidEvidencePresenter extends MvpActivityPresenter<SelectValidEvidenceContract.View> implements SelectValidEvidenceContract.Presenter {

    public SelectValidEvidencePresenter(@NonNull Context context, @NonNull SelectValidEvidenceContract.View view) {
        super(context, view);
    }


    @Override
    public void getEvidenceList(String iouId, String justId) {
        ArbitramentApi.getElecEvidenceList(iouId, justId)
                .compose(getProvider().<BaseResponse<List<ElecEvidenceRes>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<ElecEvidenceRes>>handleResponse())
                .subscribeWith(new CommSubscriber<List<ElecEvidenceRes>>(mView) {
                    @Override
                    public void handleResult(List<ElecEvidenceRes> list) {
                        if (list == null || list.isEmpty()) {
                            mView.showDataEmpty();
                            return;
                        }
                        mView.showEvidenceList(list);
                        mView.hideInit();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.showInitFailed(s1);
                    }
                });


    }

}
