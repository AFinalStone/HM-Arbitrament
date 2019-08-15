package com.hm.arbitrament.business.evidence.view

import android.os.Bundle
import com.hm.arbitrament.EXTRA_KEY_IOU_ID
import com.hm.arbitrament.EXTRA_KEY_JUST_ID
import com.hm.arbitrament.business.evidence.EvidenceIndexContract
import com.hm.arbitrament.business.evidence.presenter.EvidenceIndexPresenter
import com.hm.iou.base.BaseActivity
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue

/**
 * created by hjy on 2019/8/12
 *
 * 证据链入口界面，透明界面，在这里判断入口逻辑
 */
class EvidenceIndexActivity : BaseActivity<EvidenceIndexPresenter>(), EvidenceIndexContract.View {

    private var mIouId: String? by extraDelegate(EXTRA_KEY_IOU_ID, null)
    private var mJusticeId: String? by extraDelegate(EXTRA_KEY_JUST_ID, null)

    override fun initPresenter(): EvidenceIndexPresenter = EvidenceIndexPresenter(this, this)

    override fun getLayoutId(): Int = 0

    override fun initEventAndData(bundle: Bundle?) {
        bundle?.let {
            mIouId = bundle.getValue(EXTRA_KEY_IOU_ID)
            mJusticeId = bundle.getValue(EXTRA_KEY_JUST_ID)
        }
        mPresenter.getEvidenceStatus(mIouId ?: "", mJusticeId ?: "")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_IOU_ID, mIouId)
        outState?.putValue(EXTRA_KEY_JUST_ID, mJusticeId)
    }

}