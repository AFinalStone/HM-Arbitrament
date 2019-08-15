package com.hm.arbitrament.business.evidence.view

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.arbitrament.R

interface IEvidenceApplyRecord {

    fun getApplyTime(): String?

    fun getApplyEmail(): String?

    fun getApplyStatus(): String

    fun getApplyStatusTextColor(): Int

}

/**
 * 证据链申请记录adapter
 */
class EvidenceApplyRecordAdapter: BaseQuickAdapter<IEvidenceApplyRecord, BaseViewHolder>(R.layout.arbitrament_item_evidence_record) {

    override fun convert(helper: BaseViewHolder?, item: IEvidenceApplyRecord?) {
        helper?.let {
            it.setText(R.id.tv_evidence_time, item?.getApplyTime())
            it.setText(R.id.tv_evidence_email, item?.getApplyEmail())
            it.setText(R.id.tv_evidence_status, item?.getApplyStatus())
            it.setTextColor(R.id.tv_evidence_status, item?.getApplyStatusTextColor() ?: 0)

            it.addOnClickListener(R.id.rl_evidence_content)
        }
    }
}