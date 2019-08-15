package com.hm.arbitrament.business.evidence.view

import android.os.Bundle
import android.view.Gravity
import com.hm.arbitrament.EXTRA_KEY_IOU_ID
import com.hm.arbitrament.EXTRA_KEY_JUST_ID
import com.hm.arbitrament.R
import com.hm.arbitrament.business.evidence.EvidenceApplyDescContract
import com.hm.arbitrament.business.evidence.presenter.EvidenceApplyDescPresenter
import com.hm.iou.base.BaseActivity
import com.hm.iou.base.utils.RouterUtil
import com.hm.iou.tools.kt.click
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import com.hm.iou.uikit.dialog.HMAlertDialog
import kotlinx.android.synthetic.main.arbitrament_activity_evidence_apply_desc.*

/**
 * created by hjy on 2019/8/12
 *
 * 证据链申请入信息介绍描述页面
 */
class EvidenceApplyDescActivity : BaseActivity<EvidenceApplyDescPresenter>(), EvidenceApplyDescContract.View {

    private var mIouId: String? by extraDelegate(EXTRA_KEY_IOU_ID, null)
    private var mJusticeId: String? by extraDelegate(EXTRA_KEY_JUST_ID, null)

    override fun initPresenter(): EvidenceApplyDescPresenter = EvidenceApplyDescPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.arbitrament_activity_evidence_apply_desc

    override fun initEventAndData(bundle: Bundle?) {
        bundle?.let {
            mIouId = bundle.getValue(EXTRA_KEY_IOU_ID)
            mJusticeId = bundle.getValue(EXTRA_KEY_JUST_ID)
        }

        btn_ok.click {
            mPresenter.checkStatusBeforeApply(mIouId, mJusticeId)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_IOU_ID, mIouId)
        outState?.putValue(EXTRA_KEY_JUST_ID, mJusticeId)
    }

    override fun showEmailNotAuthDialog() {
        HMAlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("你还没有认证邮箱，请先认证邮箱后再次获取")
                .setPositiveButton("立即认证")
                .setNegativeButton("稍后认证")
                .setOnClickListener(object : HMAlertDialog.OnClickListener {
                    override fun onNegClick() {
                    }

                    override fun onPosClick() {
                        RouterUtil.clickMenuLink(this@EvidenceApplyDescActivity,
                                "hmiou://m.54jietiao.com/login/bindemail")
                    }
                })
                .create().show()
    }

    override fun showRepaymentDateTipsDialog() {
        HMAlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("还没有到约定的还款时间哦！")
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButton("知道了")
                .create().show()
    }
}