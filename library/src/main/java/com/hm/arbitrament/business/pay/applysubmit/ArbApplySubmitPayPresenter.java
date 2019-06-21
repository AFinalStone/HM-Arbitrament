package com.hm.arbitrament.business.pay.applysubmit;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.CreateArbApplyOrderResBean;
import com.hm.arbitrament.business.pay.base.IMoneyItem;
import com.hm.arbitrament.dict.OrderPayStatusEnumBean;
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
 */
public class ArbApplySubmitPayPresenter extends MvpActivityPresenter<ArbApplySubmitPayActivity> implements ArbApplySubmitPayContract.Presenter {

    private static final String KEY_WX_PAY_CODE = "arbapplysubmitpay.wxpay";
    private static final String PACKAGE_NAME_OF_WX_CHAT = "com.tencent.mm";

    private IWXAPI mWXApi;
    private String mOrderId;        //订单id
    private boolean mWeiXinCallBackPaySuccess = false;
    private CreateArbApplyOrderResBean.WxPayAppParamRespBean mWxPayAppParamRespBean;

    public ArbApplySubmitPayPresenter(@NonNull Context context, @NonNull ArbApplySubmitPayActivity view) {
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
    public void createApplyOrderInfo(String arbApplyNo, String msgCode) {
        mView.showInitLoadingView();
        ArbitramentApi.createArbApplyOrder(arbApplyNo, msgCode)
                .compose(getProvider().<BaseResponse<CreateArbApplyOrderResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<CreateArbApplyOrderResBean>handleResponse())
                .subscribeWith(new CommSubscriber<CreateArbApplyOrderResBean>(mView) {
                    @Override
                    public void handleResult(CreateArbApplyOrderResBean resBean) {
                        if (resBean == null) {
                            mView.showInitLoadingFailed("数据异常");
                            return;
                        }
                        mView.hideInitLoadingView();
                        //订单编号
                        mOrderId = resBean.getOrderId();
                        //页面显示的套餐信息
                        CreateArbApplyOrderResBean.ArbPackageRespBean packageBean = resBean.getArbPackageResp();
                        if (packageBean != null) {
                            //折扣
                            String discount = packageBean.getDiscountStr();
                            mView.showDiscount(discount);
                            //付款金额
                            String totalMoney = packageBean.getShowAmountStr();
                            String realTotalMoney = packageBean.getActualAmountStr();
                            mView.showTotalPayMoney(totalMoney, realTotalMoney);
                            //底部备注
                            String bottomRemark = packageBean.getComment();
                            mView.showBottomRemark(bottomRemark);
                            //费用列表
                            List<IMoneyItem> list = changeData(packageBean.getItemList());
                            mView.showData(list);
                        }
                        //微信支付相关信息
                        mWxPayAppParamRespBean = resBean.getWxPayAppParamResp();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                        mView.closeCurrPage();
                    }
                });
    }


    private String getAppId() {
        PlatformConfig.APPIDPlatform weixin = (PlatformConfig.APPIDPlatform) PlatformConfig.configs.get(SHARE_MEDIA.WEIXIN);
        return weixin.appId;
    }

    @Override
    public void payOrder() {
        if (mWeiXinCallBackPaySuccess) {
            checkPayResult();
            return;
        }
        boolean flag = SystemUtil.isAppInstalled(mContext, PACKAGE_NAME_OF_WX_CHAT);
        if (flag) {
            callWxPay();
        } else {
            mView.toastMessage("当前手机未安装微信");
        }
    }

    /**
     * 唤起微信客户端进行付款
     */
    private void callWxPay() {
        mWeiXinCallBackPaySuccess = false;
        if (mWXApi == null) {
            mWXApi = WXAPIFactory.createWXAPI(mContext.getApplicationContext(), null);
            mWXApi.registerApp(getAppId());
        }
        if (mWxPayAppParamRespBean == null) {
            return;
        }
        PayReq request = new PayReq();
        request.appId = getAppId();
        request.partnerId = mWxPayAppParamRespBean.getPartnerid();
        request.prepayId = mWxPayAppParamRespBean.getPrepayid();
        request.packageValue = mWxPayAppParamRespBean.getPackageX();
        request.nonceStr = mWxPayAppParamRespBean.getNoncestr();
        request.timeStamp = mWxPayAppParamRespBean.getTimestamp();
        request.sign = mWxPayAppParamRespBean.getSign();
        request.extData = KEY_WX_PAY_CODE;
        mWXApi.sendReq(request);
    }

    /**
     * 校验支付结果
     */
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
                            NavigationHelper.toWaitMakeArbitramentApplyBook(mContext);
                            mView.closeCurrPage();
                        } else {
                            callWxPay();
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

    private List<IMoneyItem> changeData(List<CreateArbApplyOrderResBean.ItemListBean> list) {
        List<IMoneyItem> listResult = new ArrayList<>();
        if (list != null) {
            for (final CreateArbApplyOrderResBean.ItemListBean bean : list) {
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