package com.hm.arbitrament.business.evidence

import com.hm.iou.base.mvp.BaseContract

/**
 * created by hjy on 2019/8/12
 *
 * 证据链合同书签署页面
 */
interface EvidenceContractContract {

    interface View : BaseContract.BaseView {

        fun showEvidenceContractDoc(pdfUrl: String)

    }

    interface Presenter : BaseContract.BasePresenter {

        fun getEvidenceContractDoc(iouId: String)

    }

}