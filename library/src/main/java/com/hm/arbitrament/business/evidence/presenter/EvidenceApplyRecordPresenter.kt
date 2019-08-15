package com.hm.arbitrament.business.evidence.presenter

import android.content.Context
import android.graphics.Color
import com.hm.arbitrament.api.ArbitramentApi
import com.hm.arbitrament.bean.EvidenceApplyHistoryItemBean
import com.hm.arbitrament.bean.EvidenceStatusEnum
import com.hm.arbitrament.business.evidence.EvidenceApplyRecordContract
import com.hm.arbitrament.business.evidence.view.IEvidenceApplyRecord
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * created by hjy on 2019/8/12
 *
 * 证据链申请记录
 */
class EvidenceApplyRecordPresenter(context: Context, view: EvidenceApplyRecordContract.View) :
        MvpActivityPresenter<EvidenceApplyRecordContract.View>(context, view),
        EvidenceApplyRecordContract.Presenter {

    override fun refreshApplyHistoryList(iouId: String, justiceId: String) {
        ArbitramentApi.getEvidenceApplyHistory(iouId, justiceId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<List<EvidenceApplyHistoryItemBean>>(mView) {
                    override fun handleResult(dataList: List<EvidenceApplyHistoryItemBean>?) {
                        mView.finishRefresh()
                        val list = mutableListOf<IEvidenceApplyRecord>()
                        dataList?.let {
                            for (item in dataList) {
                                list.add(convertData(item))
                            }
                        }
                        mView.showApplyList(list)
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.finishRefresh()
                    }
                })
    }

    fun convertData(item: EvidenceApplyHistoryItemBean): IEvidenceApplyRecord {
        return object : IEvidenceApplyRecord {
            override fun getApplyTime(): String? = item.applyDateStr

            override fun getApplyEmail(): String? = item.destMailAddr

            override fun getApplyStatus(): String = EvidenceStatusEnum.parse(item.applyStatus).desc

            override fun getApplyStatusTextColor(): Int {
                return when (EvidenceStatusEnum.parse(item.applyStatus)) {
                    EvidenceStatusEnum.APPLY_SUCCESS -> Color.parseColor("#ffef5350")
                    EvidenceStatusEnum.COMPLETE -> Color.parseColor("#ff9b9b9b")
                    else -> Color.parseColor("#ff2782e2")
                }
            }

            override fun getApplyId(): String? = item.applyId

            override fun getStatus(): Int = item.applyStatus
        }
    }

}