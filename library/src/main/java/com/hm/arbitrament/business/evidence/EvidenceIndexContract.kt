package com.hm.arbitrament.business.evidence

import com.hm.iou.base.mvp.BaseContract

/**
 * created by hjy on 2019/8/12
 *
 * 证据链入口
 */
interface EvidenceIndexContract {

    interface View : BaseContract.BaseView {

    }

    interface Presenter : BaseContract.BasePresenter {

        fun getEvidenceStatus(iouId: String, justiceId: String)

    }

}