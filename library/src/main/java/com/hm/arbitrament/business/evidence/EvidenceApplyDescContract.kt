package com.hm.arbitrament.business.evidence

import com.hm.iou.base.mvp.BaseContract

/**
 * created by hjy on 2019/8/13
 *
 * 证据链申请入信息介绍描述页面
 */
interface EvidenceApplyDescContract {

    interface View : BaseContract.BaseView {

        /**
         * 显示邮箱还没有认证的弹窗
         */
        fun showEmailNotAuthDialog()

        /**
         * 显示还没有到约定还款时间的弹窗
         */
        fun showRepaymentDateTipsDialog()
    }

    interface Presenter : BaseContract.BasePresenter {

    }

}