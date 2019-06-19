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
import com.hm.arbitrament.event.ClosePageEvent;
import com.hm.arbitrament.util.CacheDataUtil;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.logger.Logger;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * @author syl
 * @time 2019/6/11 3:36 PM
 */

public class InputApplyInfoPresenter extends BasePresenter<InputApplyInfoContract.View> implements InputApplyInfoContract.Presenter {

    public InputApplyInfoPresenter(@NonNull Context context, @NonNull InputApplyInfoContract.View view) {
        super(context, view);
    }

    @Override
    public void getInputApplyInfoData(String iouId, String justId) {
        mView.showLoadingView();
        ArbitramentApi.getArbitramentInputApplyData(iouId, justId)
                .compose(getProvider().<BaseResponse<GetArbitramentInputApplyDataResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<GetArbitramentInputApplyDataResBean>handleResponse())
                .subscribeWith(new CommSubscriber<GetArbitramentInputApplyDataResBean>(mView) {
                    @Override
                    public void handleResult(GetArbitramentInputApplyDataResBean resBean) {
                        mView.dismissLoadingView();
                        if (resBean == null) {
                            mView.closeCurrPage();
                            return;
                        }
                        mView.showData(resBean);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Override
    public void getArbitramentCost(String iouId, String justiceId, double totalMoney) {
        mView.showLoadingView();
        ArbitramentApi.getArbitramentCost(iouId, justiceId, totalMoney)
                .compose(getProvider().<BaseResponse<GetArbCostResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<GetArbCostResBean>handleResponse())
                .subscribeWith(new CommSubscriber<GetArbCostResBean>(mView) {
                    @Override
                    public void handleResult(GetArbCostResBean bean) {
                        mView.dismissLoadingView();
                        Logger.d("" + bean.toString());
                        mView.showArbCost(bean.getArbCost().toString());
                        mView.showArbMoney(bean.getArbMoney().toString());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
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
        mView.showLoadingView();
        ArbitramentApi.getArbServerAgreement(iouId, justId)
                .compose(getProvider().<BaseResponse<GetArbServerAgreementResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<GetArbServerAgreementResBean>handleResponse())
                .subscribeWith(new CommSubscriber<GetArbServerAgreementResBean>(mView) {
                    @Override
                    public void handleResult(GetArbServerAgreementResBean bean) {
                        mView.dismissLoadingView();
                        if (bean == null) {
                            return;
                        }
                        String url = bean.getContractUrl();
                        NavigationHelper.toArbitramentServerAgreement((Activity) mContext, url, InputApplyInfoActivity.REQ_GET_ARB_AGREEMENT_SERVER);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });

    }

    @Override
    public void createOrder(final CreateArbOrderReqBean reqBean) {
        mView.showLoadingView();
        ArbitramentApi.createArbApplyBookOrder(reqBean)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String orderId) {
                        mView.dismissLoadingView();
                        NavigationHelper.toPay(mContext, reqBean.getIouId(), reqBean.getJusticeId(), orderId);
                        CacheDataUtil.clearInputApplyInfoCacheData(mContext);
                        EventBus.getDefault().post(new ClosePageEvent());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });

    }

    @Override
    public void resubmitOrder(CreateArbOrderReqBean reqBean) {
        mView.showLoadingView();
        ArbitramentApi.resubmitArbApplyBookOrder(reqBean)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String orderId) {
                        mView.dismissLoadingView();
                        NavigationHelper.toWaitMakeArbitramentApplyBook(mContext);
                        CacheDataUtil.clearInputApplyInfoCacheData(mContext);
                        EventBus.getDefault().post(new ClosePageEvent());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

}
