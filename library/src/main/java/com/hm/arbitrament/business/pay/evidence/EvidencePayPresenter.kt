package com.hm.arbitrament.business.pay.evidence

import android.content.Context
import com.hm.arbitrament.KEY_EVIDENCE_WX_PAY_CODE
import com.hm.iou.base.event.OpenWxResultEvent
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.tencent.mm.opensdk.openapi.IWXAPI
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * created by hjy on 2019/8/12
 *
 * 证据链支付
 */
class EvidencePayPresenter(context: Context, view: EvidencePayContract.View) :
        MvpActivityPresenter<EvidencePayContract.View>(context, view),
        EvidencePayContract.Presenter {

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
//                checkWeiXinCallBackResult()
            }
        }
    }

}