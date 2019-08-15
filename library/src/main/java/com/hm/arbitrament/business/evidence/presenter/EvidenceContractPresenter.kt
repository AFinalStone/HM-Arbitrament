package com.hm.arbitrament.business.evidence.presenter

import android.content.Context
import com.hm.arbitrament.business.evidence.EvidenceContractContract
import com.hm.iou.base.mvp.MvpActivityPresenter

/**
 * created by hjy on 2019/8/12
 *
 * 证据链合同签署
 */
class EvidenceContractPresenter(context: Context, view: EvidenceContractContract.View) :
        MvpActivityPresenter<EvidenceContractContract.View>(context, view),
        EvidenceContractContract.Presenter {

    override fun getEvidenceContractDoc(iouId: String) {

    }
}