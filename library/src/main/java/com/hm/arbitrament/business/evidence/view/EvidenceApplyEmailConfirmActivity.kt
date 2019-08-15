package com.hm.arbitrament.business.evidence.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.hm.arbitrament.EXTRA_KEY_IOU_ID
import com.hm.arbitrament.EXTRA_KEY_JUST_ID
import com.hm.arbitrament.R
import com.hm.arbitrament.business.evidence.EvidenceEmailConfirmContract
import com.hm.arbitrament.business.evidence.presenter.EvidenceEmailConfirmPresenter
import com.hm.iou.base.BaseActivity
import com.hm.iou.sharedata.UserManager
import com.hm.iou.tools.KeyboardUtil
import com.hm.iou.tools.kt.*
import com.hm.iou.uikit.HMCountDownTextView
import com.hm.iou.uikit.dialog.HMAlertDialog
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.arbitrament_activity_evidence_email_confirm.*

/**
 * created by hjy on 2019/8/12
 *
 * 证据链邮箱确认页面
 */
class EvidenceApplyEmailConfirmActivity : BaseActivity<EvidenceEmailConfirmPresenter>(), EvidenceEmailConfirmContract.View {

    private var mIouId: String? by extraDelegate(EXTRA_KEY_IOU_ID, null)
    private var mJusticeId: String? by extraDelegate(EXTRA_KEY_JUST_ID, null)

    private var mVerifyCodeDialog: HMAlertDialog? = null
    private var mEtCode: EditText? = null
    private var mCountDownView: HMCountDownTextView? = null

    override fun getLayoutId(): Int = R.layout.arbitrament_activity_evidence_email_confirm

    override fun initPresenter(): EvidenceEmailConfirmPresenter = EvidenceEmailConfirmPresenter(this, this)
    override fun initEventAndData(bundle: Bundle?) {
        bundle?.let {
            mIouId = bundle.getValue(EXTRA_KEY_IOU_ID)
            mJusticeId = bundle.getValue(EXTRA_KEY_JUST_ID)
        }

        ll_content.clickWithDuration {
            showTipsDialog()
        }
        btn_ok.click {
            showVerifyCodeDialog()
        }

        mPresenter.init(mIouId)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_IOU_ID, mIouId)
        outState?.putValue(EXTRA_KEY_JUST_ID, mJusticeId)
    }

    override fun showUserEmail(email: String) {
        tv_evidence_email.text = email
    }

    override fun startCountDown() {
        mCountDownView?.startCountDown()
        KeyboardUtil.showKeyboard(mEtCode)
    }

    private fun showTipsDialog() {
        HMAlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("为保证数据安全性，仅支持发送至已认证邮箱")
                .setPositiveButton("知道了")
                .create().show()
    }

    /**
     * 显示验证码弹窗
     */
    private fun showVerifyCodeDialog() {
        if (mVerifyCodeDialog == null) {
            val contentView = layoutInflater.inflate(R.layout.arbitrament_dialog_verify_code, null)
            mVerifyCodeDialog = HMAlertDialog.Builder(this)
                    .setTitle("安全验证")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setPositiveButton("下一步")
                    .setNegativeButton("取消")
                    .enablePostButton(false)
                    .setCustomView(contentView)
                    .setOnClickListener(object : HMAlertDialog.OnClickListener {
                        override fun onPosClick() {
                            mPresenter.verifySmsCode(mEtCode?.text.toString())
                        }

                        override fun onNegClick() {

                        }
                    })
                    .create()
            initVerifyCodeContentView(contentView)
        }

        mEtCode?.setText("")
        mCountDownView?.resume()
        mVerifyCodeDialog?.show()
        KeyboardUtil.toggleKeyboard(this)
    }

    private fun initVerifyCodeContentView(contentView: View) {
        val tvMsg: TextView = contentView.findViewById(R.id.tv_dialog_msg)
        mEtCode = contentView.findViewById(R.id.et_dialog_code)
        mCountDownView = contentView.findViewById(R.id.tv_dialog_countdown)
        var mobile: String? = UserManager.getInstance(this).userInfo.mobile
        if (mobile != null && mobile.length >= 4)
            mobile = "${mobile.substring(0, 3)}****${mobile.substring(mobile.length - 4)}"
        tvMsg.text = "为确保账户安全，请输入${mobile}收到的验证码"
        mCountDownView?.click {
            mPresenter.sendVerifyCode()
        }
        mEtCode?.let {
            RxTextView.textChanges(it).subscribe { code ->
                if (TextUtils.isEmpty(code) || code.length < 4) {
                    mVerifyCodeDialog?.enablePosButton(false)
                } else {
                    mVerifyCodeDialog?.enablePosButton(true)
                }
            }
        }
    }

}