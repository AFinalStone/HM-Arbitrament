package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.ElecEvidenceResBean;
import com.hm.arbitrament.business.apply.FiveAdvantageContract;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;


/**
 * Created by syl on 2019/6/10.
 */

public class FiveAdvantagePresenter extends MvpActivityPresenter<FiveAdvantageContract.View> implements FiveAdvantageContract.Presenter {

    public FiveAdvantagePresenter(@NonNull Context context, @NonNull FiveAdvantageContract.View view) {
        super(context, view);
    }

    @Override
    public void checkArbitramentApplyStatus(final String iouId, final String justId) {
        ArbitramentApi.checkArbitramentApplyStatus(iouId, justId)
                .compose(getProvider().<BaseResponse<List<ElecEvidenceResBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<ElecEvidenceResBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<ElecEvidenceResBean>>(mView) {
                    @Override
                    public void handleResult(List<ElecEvidenceResBean> elecEvidenceResBeans) {
                        NavigationHelper.toSelectValidEvidenceActivity(mContext, iouId, justId);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {

                    }
                });
    }
}
