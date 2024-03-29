package com.hm.arbitrament.business.evidence.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.hm.arbitrament.EXTRA_EVIDENCE_APPLY_ID
import com.hm.arbitrament.R
import com.hm.arbitrament.business.evidence.EvidenceContractContract
import com.hm.arbitrament.business.evidence.presenter.EvidenceContractPresenter
import com.hm.iou.base.BaseActivity
import com.hm.iou.logger.Logger
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import kotlinx.android.synthetic.main.arbitrament_activity_evidence_contract.*

/**
 * created by hjy on 2019/8/12
 *
 * 证据链合同签署
 */
class EvidenceContractActivity : BaseActivity<EvidenceContractPresenter>(), EvidenceContractContract.View {

    companion object {
        const val REQ_CHECK_SIGN_PWD = 100
    }

    private var mApplyId: String? by extraDelegate(EXTRA_EVIDENCE_APPLY_ID, null)

    override fun initPresenter(): EvidenceContractPresenter = EvidenceContractPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.arbitrament_activity_evidence_contract

    override fun initEventAndData(bundle: Bundle?) {
        bundle?.let {
            mApplyId = bundle.getValue(EXTRA_EVIDENCE_APPLY_ID)
        }

        initWebView()

        mApplyId?.let {
            mPresenter.getEvidenceContractDoc(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_EVIDENCE_APPLY_ID, mApplyId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CHECK_SIGN_PWD) {
            if (resultCode == Activity.RESULT_OK) {
                val signPwd = data?.getStringExtra("pwd") ?: ""
                val signId = data?.getStringExtra("sign_id") ?: ""
                mPresenter.doSignContract(mApplyId ?: "", signId, signPwd)
            }
        }
    }

    private fun initWebView() {
        val settings = wv_pdf.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.builtInZoomControls = true
        wv_pdf.webViewClient = WebViewClient()
        settings.userAgentString = "${settings.userAgentString};HMAndroidWebView"
    }

    override fun showDataLoading() {
        loading_view.showDataLoading()
        wv_pdf.visibility = View.GONE
    }

    override fun showLoadFailed(msg: CharSequence) {
        loading_view.showDataFail(msg) {
            mPresenter.getEvidenceContractDoc(mApplyId ?: "")
        }
    }

    override fun showEvidenceContractDoc(pdfUrl: String) {
        Logger.d("pdfurl == $pdfUrl")
        if (pdfUrl.isNullOrEmpty()) {
            return
        }
        loading_view.visibility = View.GONE
        wv_pdf.visibility = View.VISIBLE

        val uri = Uri.parse(pdfUrl)
        val path = uri.path
        if (path.endsWith(".pdf")) {
            //如果是 pdf 文件地址，则用加载pdf的方式来打开
            wv_pdf.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=${Uri.encode(pdfUrl)}")
        } else {
            //如果是普通文件地址，则直接用WebView加载
            wv_pdf.loadUrl(pdfUrl)
        }
    }

    override fun showBottomButtonText(text: String) {
        bottomBar.setTitleVisible(true)
        bottomBar.updateTitle(text)
        bottomBar.setOnTitleClickListener {
            mPresenter.judgeNeedSealType()
        }
    }
}