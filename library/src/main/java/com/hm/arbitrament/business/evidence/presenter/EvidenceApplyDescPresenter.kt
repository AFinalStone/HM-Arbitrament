package com.hm.arbitrament.business.evidence.presenter

import android.content.Context
import com.hm.arbitrament.ERR_CODE_EMAIL_NOT_AUTH
import com.hm.arbitrament.ERR_CODE_IOU_INVALID
import com.hm.arbitrament.api.ArbitramentApi
import com.hm.arbitrament.business.evidence.EvidenceApplyDescContract
import com.hm.arbitrament.event.CloseEvidencePage
import com.hm.arbitrament.toEvidenceApplyEmailPage
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * created by hjy on 2019/8/13
 *
 * 证据链申请入信息介绍描述页面
 */
class EvidenceApplyDescPresenter(context: Context, view: EvidenceApplyDescContract.View)
    : MvpActivityPresenter<EvidenceApplyDescContract.View>(context, view), EvidenceApplyDescContract.Presenter {

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun checkStatusBeforeApply(iouId: String?, justiceId: String?) {
        mView.showLoadingView()
        ArbitramentApi.checkBeforeEvidenceApply(iouId, justiceId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<Any>(mView) {
                    override fun handleResult(p0: Any?) {
                        mView.dismissLoadingView()
                        toEvidenceApplyEmailPage(mContext, iouId ?: "", justiceId ?: "")
                    }

                    override fun handleException(p0: Throwable?, code: String?, msg: String?) {
                        mView.dismissLoadingView()
                        when (code) {
                            ERR_CODE_EMAIL_NOT_AUTH -> mView.showEmailNotAuthDialog()
                            ERR_CODE_IOU_INVALID -> mView.showRepaymentDateTipsDialog()
                            else -> mView.toastErrorMessage(msg)
                        }
                    }

                    override fun isShowBusinessError(): Boolean {
                        return false
                    }
                })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventClosePage(event: CloseEvidencePage) {
        mView.closeCurrPage()
    }

}