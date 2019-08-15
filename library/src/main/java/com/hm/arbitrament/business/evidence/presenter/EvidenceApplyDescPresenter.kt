package com.hm.arbitrament.business.evidence.presenter

import android.content.Context
import com.hm.arbitrament.business.evidence.EvidenceApplyDescContract
import com.hm.iou.base.mvp.MvpActivityPresenter

/**
 * created by hjy on 2019/8/13
 *
 * 证据链申请入信息介绍描述页面
 */
class EvidenceApplyDescPresenter(context: Context, view: EvidenceApplyDescContract.View)
    : MvpActivityPresenter<EvidenceApplyDescContract.View>(context, view), EvidenceApplyDescContract.Presenter {

}