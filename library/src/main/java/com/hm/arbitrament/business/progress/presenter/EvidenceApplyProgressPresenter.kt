package com.hm.arbitrament.business.progress.presenter

import android.content.Context
import com.hm.arbitrament.bean.RefundInfo
import com.hm.arbitrament.business.progress.EvidenceApplyProgressContract
import com.hm.iou.base.mvp.MvpActivityPresenter

/**
 * created by hjy on 2019/8/12
 *
 * 证据链申请进度
 */
class EvidenceApplyProgressPresenter(context: Context, view: EvidenceApplyProgressContract.View) :
        MvpActivityPresenter<EvidenceApplyProgressContract.View>(context, view),
        EvidenceApplyProgressContract.Presenter {

    private lateinit var mEvidenceApplyId: String
    private var mRefundInfo: RefundInfo? = null

    override fun loadProgressData(evideneApplyId: String) {
        mEvidenceApplyId = evideneApplyId
        mView.showDataLoading()
        /*ArbitramentApi.getRefundStep(evideneApplyId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<RefundInfo>(mView) {
                    override fun handleResult(refundInfo: RefundInfo?) {
                        mRefundInfo = refundInfo
                        val list = mutableListOf<ProgressAdapter.IProgressItem>()
                        if (refundInfo?.arbRefundList != null) {
                            for (i in 0 until refundInfo.arbRefundList.size) {
                                val step = refundInfo.arbRefundList[i]
                                var flag = 0
                                if (i == 0) {
                                    flag = 1
                                } else if (i == refundInfo.arbRefundList.size - 1) {
                                    flag = 2
                                }
                                list.add(object : ProgressAdapter.IProgressItem {
                                    override fun getTitle(): String {
                                        return step.description
                                    }

                                    override fun getTime(): String? {
                                        var date: String? = step.operateDate
                                        if (date != null) {
                                            date = date.replace("-", ".")
                                        }
                                        return date
                                    }

                                    override fun getPositionFlag(): Int {
                                        return flag
                                    }

                                    override fun getSubTitle(): String? {
                                        return null
                                    }

                                    override fun getLink(): String? {
                                        return null
                                    }

                                    override fun getCheckedIcon(): Int {
                                        return R.mipmap.uikit_icon_check_black
                                    }
                                })
                            }
                        }

                        mView.showProgressList(list)


                        mView.addFooterTips("申请补发", View.OnClickListener {
                            applyEvidenceAgain()
                        })
                    }

                    override fun handleException(throwable: Throwable?, code: String?, msg: String?) {
                        mView.showDataLoadFailed()
                    }
                })*/
    }

    /**
     * 申请补发
     */
    private fun applyEvidenceAgain() {

    }

}
