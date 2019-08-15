package com.hm.arbitrament.business.progress

import com.hm.arbitrament.business.progress.view.ProgressAdapter
import com.hm.iou.base.mvp.BaseContract

/**
 * created by hjy on 2019/8/12
 *
 * 证据链申请进度
 */
interface EvidenceApplyProgressContract {

    interface View : BaseContract.BaseView {

        fun showDataLoading()

        fun showDataLoadFailed()

        fun showProgressList(list: List<ProgressAdapter.IProgressItem>)

        fun addFooterTips(tips: CharSequence, listener: android.view.View.OnClickListener)

        fun removeFooterTips()
    }

    interface Presenter : BaseContract.BasePresenter {

        fun loadProgressData(evidenceApplyId: String)

    }

}
