package com.hm.arbitrament.business.evidence.presenter

import android.content.Context
import com.hm.arbitrament.api.ArbitramentApi
import com.hm.arbitrament.business.evidence.EvidenceIndexContract
import com.hm.arbitrament.toEvidenceApplyDescPage
import com.hm.arbitrament.toEvidenceApplyRecordListPage
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * created by hjy on 2019/8/12
 *
 * 证据链入口界面
 */
class EvidenceIndexPresenter(context: Context, view: EvidenceIndexContract.View) :
        MvpActivityPresenter<EvidenceIndexContract.View>(context, view),
        EvidenceIndexContract.Presenter {

    override fun getEvidenceStatus(iouId: String, justiceId: String) {
        mView.showLoadingView()
        ArbitramentApi.hasEvidenceApplyRecord(iouId, justiceId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<Boolean>(mView) {
                    override fun handleResult(hasRecord: Boolean?) {
                        mView.dismissLoadingView()
                        if (hasRecord == true) {
                            //如果有记录，则跳转到申请记录列表界面
                            toEvidenceApplyRecordListPage(mContext, iouId, justiceId)
                        } else {
                            //如果无记录，则跳转到申请界面
                            toEvidenceApplyDescPage(mContext, iouId, justiceId)
                        }
                        mView.closeCurrPage()
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.dismissLoadingView()
                        mView.closeCurrPage()
                    }
                })
    }

}