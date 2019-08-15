package com.hm.arbitrament.business.evidence.presenter

import android.content.Context
import android.graphics.Color
import com.hm.arbitrament.business.evidence.EvidenceApplyRecordContract
import com.hm.arbitrament.business.evidence.view.IApplyRecord
import com.hm.iou.base.mvp.MvpActivityPresenter

/**
 * created by hjy on 2019/8/12
 *
 * 证据链申请记录
 */
class EvidenceApplyRecordPresenter(context: Context, view: EvidenceApplyRecordContract.View) :
        MvpActivityPresenter<EvidenceApplyRecordContract.View>(context, view),
        EvidenceApplyRecordContract.Presenter {



    override fun refreshApplyHistoryList(iouId: String) {
        var list = mutableListOf<IApplyRecord>()
        for (i in 0..10) {
            list.add(object : IApplyRecord {
                override fun getApplyTime(): String {
                    return "2019-12-12 12:23"
                }

                override fun getApplyEmail(): String {
                    return "接收邮箱：hjy_0502@163.com"
                }

                override fun getApplyStatus(): String {
                    return "申请成功"
                }

                override fun getApplyStatusTextColor(): Int {
                    return Color.parseColor("#FFEF5350")
                }
            })
        }

        mView.showApplyList(list)
        mView.finishRefresh()

        /*
        ArbitramentApi.getArbPaperList(iouId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<List<ArbPaperApplyInfo>>(mView) {
                    override fun handleResult(dataList: List<ArbPaperApplyInfo>?) {
                        mView.finishRefresh()
                       // val list = ArrayList<ArbitralAwardListAdapter.IArbitralAwardListItem>()
                        /*if (dataList != null) {
                            for (data in dataList) {
                                list.add(convertData(data))
                            }
                        }
                        mView.showApplyList(list)
                        if (list.isEmpty()) {
                            mView.showApplyInputView()
                        }*/
                    }

                    override fun handleException(throwable: Throwable, s: String, s1: String) {
                        mView.finishRefresh()
                    }
                })
                */
    }

}