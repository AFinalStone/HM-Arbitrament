package com.hm.arbitrament.business.progress.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.hm.arbitrament.R
import com.hm.arbitrament.business.progress.EvidenceApplyProgressContract
import com.hm.arbitrament.business.progress.presenter.EvidenceApplyProgressPresenter
import com.hm.iou.base.BaseActivity
import com.hm.iou.tools.kt.density
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import kotlinx.android.synthetic.main.arbitrament_activity_evidence_apply_progress.*

/**
 * created by hjy on 2019/8/12
 *
 * 证据链申请进度
 */
class EvidenceApplyProgressActivity : BaseActivity<EvidenceApplyProgressPresenter>(), EvidenceApplyProgressContract.View {

    companion object {
        const val EXTRA_KEY_APPLY_ID = "apply_id"
    }

    private var mApplyId: String? by extraDelegate(EXTRA_KEY_APPLY_ID, null)

    private var mAdapter: ProgressAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.arbitrament_activity_evidence_apply_progress
    }

    override fun initPresenter(): EvidenceApplyProgressPresenter {
        return EvidenceApplyProgressPresenter(this, this)
    }

    override fun initEventAndData(bundle: Bundle?) {
        if (bundle != null) {
            mApplyId = bundle.getValue(EXTRA_KEY_APPLY_ID)
        }

        rv_progress_content.layoutManager = LinearLayoutManager(this)
        mAdapter = ProgressAdapter(this)
        rv_progress_content.adapter = mAdapter

        mPresenter.loadProgressData(mApplyId ?: "")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putString(EXTRA_KEY_APPLY_ID, mApplyId)
    }

    override fun showDataLoading() {
        loading_view.showDataLoading()
        rv_progress_content.visibility = View.GONE
    }

    override fun showDataLoadFailed() {
        rv_progress_content.visibility = View.GONE
        loading_view.showDataFail { mPresenter.loadProgressData(mApplyId ?: "") }
    }

    override fun showProgressList(list: List<ProgressAdapter.IProgressItem>) {
        loading_view.visibility = View.GONE
        rv_progress_content.visibility = View.VISIBLE
        mAdapter?.setNewData(list)
    }

    override fun addFooterTips(tips: CharSequence, listener: View.OnClickListener) {
        val tvFooter = TextView(this)
        tvFooter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f)
        tvFooter.setTextColor(resources.getColor(R.color.uikit_function_remind))
        tvFooter.paint.isUnderlineText = true
        val padding = (15 * this.density).toInt()
        tvFooter.setPadding(padding, padding / 3, padding, padding)
        tvFooter.setOnClickListener(listener)
        tvFooter.text = tips
        mAdapter?.addFooterView(tvFooter)
    }

    override fun removeFooterTips() {
        mAdapter?.removeAllFooterView()
    }
}
