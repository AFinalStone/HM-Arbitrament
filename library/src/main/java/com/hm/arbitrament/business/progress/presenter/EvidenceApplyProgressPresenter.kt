package com.hm.arbitrament.business.progress.presenter

import android.content.Context
import android.view.View
import com.hm.arbitrament.R
import com.hm.arbitrament.api.ArbitramentApi
import com.hm.arbitrament.bean.EvidenceProgressResBean
import com.hm.arbitrament.business.progress.EvidenceApplyProgressContract
import com.hm.arbitrament.business.progress.view.ProgressAdapter
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * created by hjy on 2019/8/12
 *
 * 证据链申请进度
 */
class EvidenceApplyProgressPresenter(context: Context, view: EvidenceApplyProgressContract.View) :
        MvpActivityPresenter<EvidenceApplyProgressContract.View>(context, view),
        EvidenceApplyProgressContract.Presenter {

    private lateinit var mEvidenceApplyId: String
    private var mEvidenceProgressResBean: EvidenceProgressResBean? = null

    override fun loadProgressData(evideneApplyId: String) {
        mEvidenceApplyId = evideneApplyId
        mView.showDataLoading()
        ArbitramentApi.getEvidenceApplyProgress(evideneApplyId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<EvidenceProgressResBean>(mView) {
                    override fun handleResult(data: EvidenceProgressResBean?) {
                        mEvidenceProgressResBean = data
                        val list = mutableListOf<ProgressAdapter.IProgressItem>()
                        mEvidenceProgressResBean?.progressItemList?.let {
                            for (i in 0 until it.size) {
                                var flag = 0
                                if (i == 0) {
                                    flag = 1
                                } else if (i == it.size - 1) {
                                    flag = 2
                                }
                                list.add(object : ProgressAdapter.IProgressItem {
                                    override fun getTitle(): String {
                                        return it[i].progressDesc ?: ""
                                    }

                                    override fun getTime(): String? {
                                        var date: String? = it[i].progressDateStr
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

                        if (data?.nextOperType == 1) {
                            mView.addFooterTips("申请补发", View.OnClickListener {
                                applyEvidenceAgain()
                            })
                        } else {
                            mView.removeFooterTips()
                        }
                    }

                    override fun handleException(throwable: Throwable?, code: String?, msg: String?) {
                        mView.showDataLoadFailed()
                    }
                })
    }

    /**
     * 申请补发
     */
    private fun applyEvidenceAgain() {
        mView.showLoadingView()
        ArbitramentApi.resendEvidence(mEvidenceApplyId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<Any>(mView) {
                    override fun handleResult(p0: Any?) {
                        mView.dismissLoadingView()
                        mView.toastMessage("申请补发成功")
                        loadProgressData(mEvidenceApplyId)
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.dismissLoadingView()
                    }
                })
    }

}
