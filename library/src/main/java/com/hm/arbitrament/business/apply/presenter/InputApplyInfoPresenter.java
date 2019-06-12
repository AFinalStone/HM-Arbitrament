package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.GetArbitramentInputApplyDataResBean;
import com.hm.arbitrament.business.apply.InputApplyInfoContract;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;


/**
 * @author syl
 * @time 2019/6/11 3:36 PM
 */

public class InputApplyInfoPresenter extends MvpActivityPresenter<InputApplyInfoContract.View> implements InputApplyInfoContract.Presenter {

    public InputApplyInfoPresenter(@NonNull Context context, @NonNull InputApplyInfoContract.View view) {
        super(context, view);
    }

    @Override
    public void getInputApplyInfoData(String iouId, String justId) {
        ArbitramentApi.getArbitramentInputApplyData(iouId, justId)
                .compose(getProvider().<BaseResponse<GetArbitramentInputApplyDataResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<GetArbitramentInputApplyDataResBean>handleResponse())
                .subscribeWith(new CommSubscriber<GetArbitramentInputApplyDataResBean>(mView) {
                    @Override
                    public void handleResult(GetArbitramentInputApplyDataResBean resBean) {
                        if (resBean == null) {
                            mView.closeCurrPage();
                            return;
                        }
                        mView.showData(resBean);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {

                    }
                });
    }

    @Override
    public void getArbitramentCost(String arbAmount) {

    }
}
