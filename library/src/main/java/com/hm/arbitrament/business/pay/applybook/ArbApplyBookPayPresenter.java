package com.hm.arbitrament.business.pay.applybook;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.GetArbApplyBookOrderResBean;
import com.hm.arbitrament.bean.PayArbApplyBookOrderResBean;
import com.hm.arbitrament.bean.req.PayArbApplyBookOrderReqBean;
import com.hm.arbitrament.business.pay.base.IMoneyItem;
import com.hm.arbitrament.dict.OrderPayStatusEnumBean;
import com.hm.iou.base.event.OpenWxResultEvent;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author syl
 * @time 2018/5/30 下午6:47
 */
public class ArbApplyBookPayPresenter extends MvpActivityPresenter<ArbApplyBookPayActivity> implements ArbApplyBookPayContract.Presenter {

    private IWXAPI mWXApi;
    private static final String KEY_WX_PAY_CODE = "arbapplybookpay.wxpay";
    private String mJustId;//订单公证id
    private String mOrderId;//订单id
    private Boolean mHavePaySuccess;

    public ArbApplyBookPayPresenter(@NonNull Context context, @NonNull ArbApplyBookPayActivity view) {
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
    public void getArbApplyBookOrderInfo(String iouId, String justId) {
        mView.showInitLoadingView();
        ArbitramentApi.getArbApplyBookOrderDetail(iouId, justId)
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
                });
    }

    @Override
    public void payOrderByWeiXin(String justId, String orderId) {
        if (mHavePaySuccess) {
            checkPayResult();
        }
        mJustId = justId;
        mOrderId = orderId;
        payOrder();
        return;
    }

    private String getAppId() {
        PlatformConfig.APPIDPlatform weixin = (PlatformConfig.APPIDPlatform) PlatformConfig.configs.get(SHARE_MEDIA.WEIXIN);
        return weixin.appId;
    }

    private void payOrder() {
        PayArbApplyBookOrderReqBean reqBean = new PayArbApplyBookOrderReqBean();
        reqBean.setChannel(1);//微信支付
        reqBean.setJusticeId(mJustId);
        reqBean.setOrderId(mOrderId);
        ArbitramentApi.payArbApplyBookOrder(reqBean)
                .compose(getProvider().<BaseResponse<PayArbApplyBookOrderResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<PayArbApplyBookOrderResBean>handleResponse())
                .subscribeWith(new CommSubscriber<PayArbApplyBookOrderResBean>(mView) {
                    @Override
                    public void handleResult(PayArbApplyBookOrderResBean bean) {

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
                        wxPay(mWXApi, partnerId, prepayid, packageValue, nonceStr, timeStamp, sign, KEY_WX_PAY_CODE);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {

                    }

                });
    }


    private void checkPayResult() {
        ArbitramentApi.queryOrderPayState(mOrderId)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String code) {
                        mView.dismissLoadingView();
                        if (OrderPayStatusEnumBean.PaySuccess.getStatus().equals(code)) {
                            NavigationHelper.toWaitMakeArbitramentApplyBook(mContext);
                            mView.closeCurrPage();
                        } else if (OrderPayStatusEnumBean.PayFailed.getStatus().equals(code)) {

                        } else if (OrderPayStatusEnumBean.PayWait.getStatus().equals(code)
                                || OrderPayStatusEnumBean.Paying.getStatus().equals(code)) {
                            payOrder();
                        } else if (OrderPayStatusEnumBean.PayFinish.getStatus().equals(code)) {
                            mView.toastMessage("订单已经关闭...");
                        } else if (OrderPayStatusEnumBean.RefundMoney.getStatus().equals(code)) {
                            mView.toastMessage("订单已经退款...");
                        } else {
                            mView.toastMessage("发生未知异常...");
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errorMsg) {
                        mView.dismissLoadingView();
                    }
                });
    }

    private void wxPay(IWXAPI wxApi, String partnerId, String prepayid, String packageValue, String nonceStr, String timeStamp, String sign, String key) {
        PayReq request = new PayReq();
        request.appId = getAppId();
        request.partnerId = partnerId;
        request.prepayId = prepayid;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;
        request.extData = key;
        wxApi.sendReq(request);
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
                mHavePaySuccess = true;
                checkPayResult();
            }
        }
    }

}