package com.hm.arbitrament.business.pay.award;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.GetArbApplyBookOrderResBean;
import com.hm.arbitrament.bean.PayArbApplyBookOrderResBean;
import com.hm.arbitrament.business.pay.base.IMoneyItem;
import com.hm.arbitrament.dict.OrderPayStatusEnumBean;
import com.hm.arbitrament.event.AwardPaySuccEvent;
import com.hm.iou.base.event.OpenWxResultEvent;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.SystemUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hjy
 * @time 2019/6/19
 */
public class AwardPayPresenter extends MvpActivityPresenter<AwardPayActivity> implements AwardPayContract.Presenter {

    private static final String KEY_WX_PAY_CODE = "awardpay.wxpay";
    private static final String PACKAGE_NAME_OF_WX_CHAT = "com.tencent.mm";

    private IWXAPI mWXApi;

    private String mOrderId;
    private boolean mWeiXinCallBackPaySuccess = false;

    private PayArbApplyBookOrderResBean mPayInfo;

    public AwardPayPresenter(@NonNull Context context, @NonNull AwardPayActivity view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mWXApi != null) {
            mWXApi.detach();
        }
    }

    @Override
    public void getArbPaperApplyOrderInfo() {
        mView.showInitLoadingView();
        ArbitramentApi.getArbPaperPackage()
                .compose(getProvider().<BaseResponse<GetArbApplyBookOrderResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<GetArbApplyBookOrderResBean>handleResponse())
                .subscribeWith(new CommSubscriber<GetArbApplyBookOrderResBean>(mView) {
                    @Override
                    public void handleResult(GetArbApplyBookOrderResBean bean) {
                        if (bean == null) {
                            mView.showInitLoadingFailed("数据异常");
                            return;
                        }
                        mView.hideInitLoadingView();
                        //折扣
                        String discount = bean.getDiscountStr();
                        mView.showDiscount(discount);
                        //付款金额
                        String totalMoney = bean.getShowAmountStr();
                        String realTotalMoney = bean.getActualAmountStr();
                        mView.showTotalPayMoney(totalMoney, realTotalMoney);
                        //底部备注
                        String bottomRemark = bean.getComment();
                        mView.showBottomRemark(bottomRemark);
                        //费用列表
                        List<IMoneyItem> list = changeData(bean.getItemList());
                        mView.showData(list);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.showInitLoadingFailed(s1);
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
    public void payOrder(String orderId) {
        if (mWeiXinCallBackPaySuccess) {
            checkPayResult();
            return;
        }
        boolean flag = SystemUtil.isAppInstalled(mContext, PACKAGE_NAME_OF_WX_CHAT);
        if (flag) {
            mOrderId = orderId;
            if (mPayInfo != null) {
                callWxPay(mPayInfo);
            } else {
                payOrderByWeiXin();
            }
        } else {
            mView.toastMessage("当前手机未安装微信");
        }
    }

    private String getAppId() {
        PlatformConfig.APPIDPlatform weixin = (PlatformConfig.APPIDPlatform) PlatformConfig.configs.get(SHARE_MEDIA.WEIXIN);
        return weixin.appId;
    }

    private void payOrderByWeiXin() {
        mView.showLoadingView();
        ArbitramentApi.createPreparePayOrder(1, mOrderId)
                .compose(getProvider().<BaseResponse<PayArbApplyBookOrderResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<PayArbApplyBookOrderResBean>handleResponse())
                .subscribeWith(new CommSubscriber<PayArbApplyBookOrderResBean>(mView) {
                    @Override
                    public void handleResult(PayArbApplyBookOrderResBean bean) {
                        mView.dismissLoadingView();
                        mPayInfo = bean;
                        callWxPay(bean);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }


    private void checkPayResult() {
        mView.showLoadingView();
        ArbitramentApi.queryOrderPayState(mOrderId)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String code) {
                        mView.dismissLoadingView();
                        if (OrderPayStatusEnumBean.PaySuccess.getStatus().equals(code)) {
                            EventBus.getDefault().post(new AwardPaySuccEvent());
                            mView.closeCurrPage();
                        } else {
                            mView.showNoCheckPayResultDialog();
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errorMsg) {
                        mView.dismissLoadingView();
                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }
                });
    }


    /**
     * 唤起微信支付
     *
     * @param bean
     */
    private void callWxPay(PayArbApplyBookOrderResBean bean) {
        mWeiXinCallBackPaySuccess = false;
        if (mWXApi == null) {
            mWXApi = WXAPIFactory.createWXAPI(mContext.getApplicationContext(), null);
            mWXApi.registerApp(getAppId());
        }

        String partnerId = bean.getPartnerid();
        String prepayid = bean.getPrepayid();
        String packageValue = bean.getPackageX();
        String nonceStr = bean.getNoncestr();
        String timeStamp = bean.getTimestamp();
        String sign = bean.getSign();

        PayReq request = new PayReq();
        request.appId = getAppId();
        request.partnerId = partnerId;
        request.prepayId = prepayid;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;
        request.extData = KEY_WX_PAY_CODE;
        mWXApi.sendReq(request);
    }

    /**
     * 微信回调提示支付成功，这里调用接口再检验一遍
     */
    private void checkWeiXinCallBackResult() {
        mView.showLoadingView();
        Flowable.just(mOrderId)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<String, Publisher<BaseResponse<String>>>() {
                    @Override
                    public Publisher<BaseResponse<String>> apply(String orderId) throws Exception {
                        return ArbitramentApi.queryOrderPayState(orderId);
                    }
                })
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String code) {
                        mView.dismissLoadingView();
                        if (OrderPayStatusEnumBean.PaySuccess.getStatus().equals(code)) {
                            NavigationHelper.toWaitMakeArbitramentApplyBook(mContext);
                            mView.closeCurrPage();
                        } else {
                            mView.showNoCheckPayResultDialog();
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errorMsg) {
                        mView.dismissLoadingView();
                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }
                });
    }

    private List<IMoneyItem> changeData(List<GetArbApplyBookOrderResBean.ItemListBean> list) {
        List<IMoneyItem> listResult = new ArrayList<>();
        if (list != null) {
            for (final GetArbApplyBookOrderResBean.ItemListBean bean : list) {
                IMoneyItem iMoneyItem = new IMoneyItem() {
                    @Override
                    public String getName() {
                        return bean.getName();
                    }

                    @Override
                    public String getContent() {
                        return bean.getAmountStr();
                    }


                    @Override
                    public String getWarnDialogContent() {
                        return bean.getComment();
                    }
                };
                listResult.add(iMoneyItem);
            }
        }
        return listResult;
    }


    /**
     * 微信支付成功，关闭当前页面
     *
     * @param openWxResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusOpenWXResult(OpenWxResultEvent openWxResultEvent) {
        if (KEY_WX_PAY_CODE.equals(openWxResultEvent.getKey())) {
            if (openWxResultEvent.getIfPaySuccess()) {
                mWeiXinCallBackPaySuccess = true;
                checkWeiXinCallBackResult();
            }
        }
    }

}