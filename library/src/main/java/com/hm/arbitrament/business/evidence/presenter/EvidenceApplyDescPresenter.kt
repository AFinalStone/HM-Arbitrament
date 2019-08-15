package com.hm.arbitrament.business.evidence.presenter

import android.content.Context
import com.hm.arbitrament.api.ArbitramentApi
import com.hm.arbitrament.business.evidence.EvidenceApplyDescContract
import com.hm.arbitrament.toEvidenceApplyEmailPage
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * created by hjy on 2019/8/13
 *
 * 证据链申请入信息介绍描述页面
 */
class EvidenceApplyDescPresenter(context: Context, view: EvidenceApplyDescContract.View)
    : MvpActivityPresenter<EvidenceApplyDescContract.View>(context, view), EvidenceApplyDescContract.Presenter {

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

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.dismissLoadingView()
                    }
                })
    }
}