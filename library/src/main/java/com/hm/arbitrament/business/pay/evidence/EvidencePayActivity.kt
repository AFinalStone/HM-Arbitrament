package com.hm.arbitrament.business.pay.evidence

import android.os.Bundle
import com.hm.arbitrament.EXTRA_KEY_IOU_ID
import com.hm.arbitrament.EXTRA_KEY_SMS_CODE
import com.hm.arbitrament.business.pay.base.BasePayActivity
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue

/**
 * created by hjy on 2019/8/12
 *
 * 证据链支付
 */
class EvidencePayActivity : BasePayActivity<EvidencePayPresenter>(), EvidencePayContract.View {

    private var mIouId: String? by extraDelegate(EXTRA_KEY_IOU_ID, null)
    private var mMsgCode: String? by extraDelegate(EXTRA_KEY_SMS_CODE, null)

    override fun initPresenter(): EvidencePayPresenter = EvidencePayPresenter(this, this)

    override fun init(bundle: Bundle?) {
        bundle?.let {
            mIouId = bundle.getValue(EXTRA_KEY_IOU_ID)
            mMsgCode = bundle.getValue(EXTRA_KEY_SMS_CODE)
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_IOU_ID, mIouId)
        outState?.putValue(EXTRA_KEY_SMS_CODE, mMsgCode)
    }

    override fun pay() {

    }

    override fun refresh() {

    }


}