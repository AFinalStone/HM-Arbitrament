package com.hm.arbitrament.business.evidence.presenter

import android.content.Context
import com.hm.arbitrament.business.evidence.EvidenceIndexContract
import com.hm.arbitrament.toEvidenceApplyDescPage
import com.hm.iou.base.mvp.MvpActivityPresenter

/**
 * created by hjy on 2019/8/12
 *
 * 证据链入口界面
 */
class EvidenceIndexPresenter(context: Context, view: EvidenceIndexContract.View) :
        MvpActivityPresenter<EvidenceIndexContract.View>(context, view),
        EvidenceIndexContract.Presenter {

    override fun getEvidenceStatus(iouId: String) {
        mView.showLoadingView()


        toEvidenceApplyDescPage(mContext, iouId)


        mView.dismissLoadingView()
        mView.closeCurrPage()
    }
}