package com.hm.arbitrament.business.apply.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.GetArbCostResBean;
import com.hm.arbitrament.bean.GetArbServerAgreementResBean;
import com.hm.arbitrament.bean.GetArbitramentInputApplyDataResBean;
import com.hm.arbitrament.bean.req.CreateArbOrderReqBean;
import com.hm.arbitrament.business.apply.InputApplyInfoContract;
import com.hm.arbitrament.business.apply.view.InputApplyInfoActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.logger.Logger;
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
    public void getArbitramentCost(String iouId, String justiceId, double totalMoney) {
        ArbitramentApi.getArbitramentCost(iouId, justiceId, totalMoney)
                .compose(getProvider().<BaseResponse<GetArbCostResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<GetArbCostResBean>handleResponse())
                .subscribeWith(new CommSubscriber<GetArbCostResBean>(mView) {
                    @Override
                    public void handleResult(GetArbCostResBean bean) {
                        Logger.d("" + bean.toString());
                        mView.showArbCost(bean.getArbCost().toString());
                        mView.showArbMoney(bean.getArbMoney().toString());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {

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

    @Override
    public void getAgreement(String iouId, String justId) {
        ArbitramentApi.getArbServerAgreement(iouId, justId)
                .compose(getProvider().<BaseResponse<GetArbServerAgreementResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<GetArbServerAgreementResBean>handleResponse())
                .subscribeWith(new CommSubscriber<GetArbServerAgreementResBean>(mView) {
                    @Override
                    public void handleResult(GetArbServerAgreementResBean getCollectionProveResBeans) {
                        NavigationHelper.toArbitramentServerAgreement((Activity) mContext, "", InputApplyInfoActivity.REQ_GET_ARB_AGREEMENT_SERVER);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        NavigationHelper.toArbitramentServerAgreement((Activity) mContext, "", InputApplyInfoActivity.REQ_GET_ARB_AGREEMENT_SERVER);
                    }
                });

    }

    @Override
    public void createOrder(CreateArbOrderReqBean reqBean) {
        ArbitramentApi.createArbApplyBookOrder(reqBean)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object o) {

                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {

                    }
                });
    }
}