package com.hm.arbitrament.business.evidence.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hm.arbitrament.*
import com.hm.arbitrament.bean.EvidenceStatusEnum
import com.hm.arbitrament.business.evidence.EvidenceApplyRecordContract
import com.hm.arbitrament.business.evidence.presenter.EvidenceApplyRecordPresenter
import com.hm.iou.base.BaseActivity
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import kotlinx.android.synthetic.main.arbitrament_activity_evidence_apply_record.*

/**
 * created by hjy on 2019/8/12
 *
 * 证据链申请记录
 */
class EvidenceApplyRecordActivity : BaseActivity<EvidenceApplyRecordPresenter>(), EvidenceApplyRecordContract.View {

    private var mIouId: String? by extraDelegate(EXTRA_KEY_IOU_ID, null)
    private var mJusticeId: String? by extraDelegate(EXTRA_KEY_JUST_ID, null)

    private var mAdapter: EvidenceApplyRecordAdapter? = null

    override fun initPresenter(): EvidenceApplyRecordPresenter = EvidenceApplyRecordPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.arbitrament_activity_evidence_apply_record

    override fun initEventAndData(bundle: Bundle?) {
        bundle?.let {
            mIouId = bundle.getValue(EXTRA_KEY_IOU_ID)
            mJusticeId = bundle.getValue(EXTRA_KEY_JUST_ID)
        }

        mAdapter = EvidenceApplyRecordAdapter()
        rv_evidence_content.layoutManager = LinearLayoutManager(this)
        rv_evidence_content.adapter = mAdapter
        smartrl_evidence_list.setOnRefreshListener {
            mPresenter.refreshApplyHistoryList(mIouId ?: "", mJusticeId ?: "")
        }
        mAdapter?.setOnItemChildClickListener { adapter, _, position ->
            val applyRecord = adapter.getItem(position) as? IEvidenceApplyRecord
            applyRecord?.let {
                val evidenceStatusEnum = EvidenceStatusEnum.parse(it.getStatus())
                if (evidenceStatusEnum == EvidenceStatusEnum.HAS_PAID) {
                    //如果已经支付
                    toSignEvidenceContractPage(mContext, it.getApplyId() ?: "")
                } else {
                    toEvidenceApplyProgressPage(mContext, it.getApplyId() ?: "")
                }
            }
        }

        bottomBar.setOnTitleClickListener {
            toEvidenceApplyEmailPage(this, mIouId ?: "", mJusticeId ?: "")
        }

        smartrl_evidence_list.autoRefresh()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_IOU_ID, mIouId)
        outState?.putValue(EXTRA_KEY_JUST_ID, mJusticeId)
    }

    override fun finishRefresh() {
        smartrl_evidence_list.finishRefresh()
    }

    override fun showApplyList(list: List<IEvidenceApplyRecord>) {
        mAdapter?.setNewData(list)
    }
}