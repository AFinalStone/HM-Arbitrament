package com.hm.arbitrament.business.pay.evidence

import android.content.Context
import com.hm.arbitrament.KEY_EVIDENCE_WX_PAY_CODE
import com.hm.arbitrament.PACKAGE_NAME_OF_WX_CHAT
import com.hm.arbitrament.api.ArbitramentApi
import com.hm.arbitrament.bean.EvidenceApplyOrderResBean
import com.hm.arbitrament.bean.EvidenceItemList
import com.hm.arbitrament.bean.EvidenceWxPayParams
import com.hm.arbitrament.business.pay.base.IMoneyItem
import com.hm.arbitrament.dict.OrderPayStatusEnumBean
import com.hm.arbitrament.event.CloseEvidencePage
import com.hm.arbitrament.toSignEvidenceContractPage
import com.hm.iou.base.event.OpenWxResultEvent
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.tools.SystemUtil
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.trello.rxlifecycle2.android.ActivityEvent
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.bean.SHARE_MEDIA
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

/**
 * created by hjy on 2019/8/12
 *
 * 证据链支付
 */
class EvidencePayPresenter(context: Context, view: EvidencePayActivity) :
        MvpActivityPresenter<EvidencePayActivity>(context, view),
        EvidencePayContract.Presenter {

    private var mWxPayParams: EvidenceWxPayParams? = null
    private var mOrderId: String? = null
    private var mApplyId: String? = null

    private var mWXApi: IWXAPI? = null
    private var mWeiXinCallBackPaySuccess = false

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        mWXApi?.detach()
    }

    override fun getEvidenceApplyOrder(iouId: String, justiceId: String, code: String) {
        mView.showInitLoadingView()
        ArbitramentApi.createEvidenceApplyOrder(iouId, justiceId, code)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<EvidenceApplyOrderResBean>(mView) {
                    override fun handleResult(data: EvidenceApplyOrderResBean?) {
                        if (data == null) {
                            mView.showInitLoadingFailed("数据异常")
                            return
                        }
                        mView.hideInitLoadingView()

                        mView.showDiscount("")
                        //付款金额
                        val realTotalMoney = data.actualAmountStr
                        mView.showTotalPayMoney("", realTotalMoney)
                        //底部备注
                        mView.showBottomRemark("")
                        //费用列表
                        val list = changeData(data.itemList)
                        mView.showData(list)

                        mWxPayParams = data.wxPayAppParamResp
                        mOrderId = data.orderId
                        mApplyId = data.applyId
                    }

                    override fun handleException(p0: Throwable?, p1: String?, msg: String?) {
                        mView.showInitLoadingFailed(msg)
                    }

                    override fun isShowBusinessError() = false

                    override fun isShowCommError() = false
                })
    }

    private fun changeData(list: List<EvidenceItemList>?): List<IMoneyItem> {
        val listResult = mutableListOf<IMoneyItem>()
        list?.let {
            for (bean in list) {
                val iMoneyItem = object : IMoneyItem {
                    override fun getName(): String = bean.name ?: ""

                    override fun getContent(): String = bean.amountStr ?: ""

                    override fun getWarnDialogContent(): String = bean.comment ?: ""
                }
                listResult.add(iMoneyItem)
            }
        }
        return listResult
    }

    override fun payOrder() {
        val flag = SystemUtil.isAppInstalled(mContext, PACKAGE_NAME_OF_WX_CHAT)
        if (!flag) {
            mView.toastMessage("当前手机未安装微信")
            return
        }

        if (mWxPayParams == null)
            return

        if (mWeiXinCallBackPaySuccess) {
            checkPayResult()
            return
        }

        if (mWXApi == null) {
            mWXApi = WXAPIFactory.createWXAPI(mContext.applicationContext, null)
            mWXApi?.registerApp(getAppId())
        }

        val partnerId = mWxPayParams?.partnerid
        val prepayid = mWxPayParams?.prepayid
        val packageValue = mWxPayParams?.packageX
        val nonceStr = mWxPayParams?.noncestr
        val timeStamp = mWxPayParams?.timestamp
        val sign = mWxPayParams?.sign

        val request = PayReq()
        request.appId = getAppId()
        request.partnerId = partnerId
        request.prepayId = prepayid
        request.packageValue = packageValue
        request.nonceStr = nonceStr
        request.timeStamp = timeStamp
        request.sign = sign
        request.extData = KEY_EVIDENCE_WX_PAY_CODE
        mWXApi?.sendReq(request)
    }

    private fun getAppId(): String {
        val weixin = PlatformConfig.configs[SHARE_MEDIA.WEIXIN] as PlatformConfig.APPIDPlatform
        return weixin.appId
    }

    /**
     * 微信回调提示支付成功，这里调用接口再检验一遍
     */
    private fun checkWeiXinCallBackResult() {
        mView.showLoadingView()
        Flowable.just<String>(mOrderId)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { orderId -> ArbitramentApi.queryOrderPayState(orderId) }
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<String>(mView) {
                    override fun handleResult(code: String) {
                        mView.dismissLoadingView()
                        if (OrderPayStatusEnumBean.PaySuccess.status == code) {
                            toSignContractPage()
                            EventBus.getDefault().post(CloseEvidencePage())
                            mView.closeCurrPage()
                        } else {
                            mView.showNoCheckPayResultDialog()
                        }
                    }

                    override fun handleException(throwable: Throwable, code: String, errorMsg: String) {
                        mView.dismissLoadingView()
                    }

                    override fun isShowBusinessError(): Boolean {
                        return false
                    }
                })
    }

    private fun checkPayResult() {
        mView.showLoadingView()
        ArbitramentApi.queryOrderPayState(mOrderId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<String>(mView) {
                    override fun handleResult(code: String) {
                        mView.dismissLoadingView()
                        if (OrderPayStatusEnumBean.PaySuccess.status == code) {
                            toSignContractPage()
                            EventBus.getDefault().post(CloseEvidencePage())
                            mView.closeCurrPage()
                        } else {
                            mView.showNoCheckPayResultDialog()
                        }
                    }

                    override fun handleException(throwable: Throwable, code: String, errorMsg: String) {
                        mView.dismissLoadingView()
                    }

                    override fun isShowBusinessError(): Boolean {
                        return false
                    }
                })
    }

    /**
     * 开始签署合同
     */
    private fun toSignContractPage() {
        toSignEvidenceContractPage(mContext,mApplyId ?: "")
    }

    /**
     * 微信支付成功，关闭当前页面
     *
     * @param openWxResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvenBusOpenWXResult(openWxResultEvent: OpenWxResultEvent) {
        if (KEY_EVIDENCE_WX_PAY_CODE == openWxResultEvent.key) {
            if (openWxResultEvent.ifPaySuccess) {
                mWeiXinCallBackPaySuccess = true
                checkWeiXinCallBackResult()
            }
        }
    }

}