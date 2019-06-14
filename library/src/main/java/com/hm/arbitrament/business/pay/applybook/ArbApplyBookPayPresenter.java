package com.hm.arbitrament.business.pay.applybook;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.GetArbApplyBookOrderResBean;
import com.hm.arbitrament.business.pay.base.IMoneyItem;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author syl
 * @time 2018/5/30 下午6:47
 */
public class ArbApplyBookPayPresenter extends MvpActivityPresenter<ArbApplyBookPayActivity> implements ArbApplyBookPayContract.Presenter {


    public ArbApplyBookPayPresenter(@NonNull Context context, @NonNull ArbApplyBookPayActivity view) {
        super(context, view);
    }

    @Override
    public void getArbApplyBookOrderInfo(String iouId, String justId) {
        ArbitramentApi.getArbApplyBookOrderDetail(iouId, justId)
                .compose(getProvider().<BaseResponse<GetArbApplyBookOrderResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<GetArbApplyBookOrderResBean>handleResponse())
                .subscribeWith(new CommSubscriber<GetArbApplyBookOrderResBean>(mView) {
                    @Override
                    public void handleResult(GetArbApplyBookOrderResBean getArbApplyBookOrderResBean) {

                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {

                    }
                });
    }

    private List<IMoneyItem> changeData() {
        List<IMoneyItem> list = new ArrayList<>();

        return list;
    }
}