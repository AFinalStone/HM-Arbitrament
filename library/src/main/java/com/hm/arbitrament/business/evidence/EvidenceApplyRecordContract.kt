package com.hm.arbitrament.business.evidence

import com.hm.arbitrament.business.evidence.view.IEvidenceApplyRecord
import com.hm.iou.base.mvp.BaseContract

/**
 * created by hjy on 2019/8/12
 *
 * 证据链申请记录
 */
interface EvidenceApplyRecordContract {

    interface View : BaseContract.BaseView {

        fun finishRefresh()

        /**
         * 显示申请记录列表
         */
        fun showApplyList(list: List<IEvidenceApplyRecord>)

    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 刷新证据链申请列表
         */
        fun refreshApplyHistoryList(iouId: String, justiceId: String)

    }

}