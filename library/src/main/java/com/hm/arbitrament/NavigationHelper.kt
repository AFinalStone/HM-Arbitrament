package com.hm.arbitrament

import android.content.Context
import android.net.Uri
import com.hm.arbitrament.business.evidence.view.EvidenceApplyDescActivity
import com.hm.arbitrament.business.evidence.view.EvidenceApplyEmailConfirmActivity
import com.hm.arbitrament.business.evidence.view.EvidenceApplyRecordActivity
import com.hm.arbitrament.business.evidence.view.EvidenceContractActivity
import com.hm.arbitrament.business.pay.evidence.EvidencePayActivity
import com.hm.arbitrament.business.progress.view.EvidenceApplyProgressActivity
import com.hm.iou.base.BaseBizAppLike
import com.hm.iou.router.Router
import com.hm.iou.tools.kt.startActivity

/**
 * 进入证据链获取第一个描述页面
 */
fun toEvidenceApplyDescPage(context: Context, iouId: String, justiceId: String) {
    context.startActivity<EvidenceApplyDescActivity>(
            EXTRA_KEY_IOU_ID to iouId,
            EXTRA_KEY_JUST_ID to justiceId
    )
}


/**
 * 进入证据链申请记录页面
 *
 * @param context
 * @param iouId     借条id
 * @param justiceId 公证id
 *
 */
fun toEvidenceApplyRecordListPage(context: Context, iouId: String, justiceId: String) {
    context.startActivity<EvidenceApplyRecordActivity>(
            EXTRA_KEY_IOU_ID to iouId,
            EXTRA_KEY_JUST_ID to justiceId
    )
}

/**
 * 进入证据链获取"邮箱验证页面"
 *
 * @param context
 * @param iouId     借条id
 * @param justiceId 公证id
 */
fun toEvidenceApplyEmailPage(context: Context, iouId: String, justiceId: String) {
    context.startActivity<EvidenceApplyEmailConfirmActivity>(
            EXTRA_KEY_IOU_ID to iouId,
            EXTRA_KEY_JUST_ID to justiceId
    )
}

/**
 * 进入证据链申请支付页面
 *
 * @param context
 * @param iouId     借条id
 * @param justiceId 公证id
 * @param code      短信验证码
 *
 */
fun toEvidenceApplyPayPage(context: Context, iouId: String, justiceId: String, code: String) {
    context.startActivity<EvidencePayActivity>(
            EXTRA_KEY_IOU_ID to iouId,
            EXTRA_KEY_JUST_ID to justiceId,
            EXTRA_KEY_SMS_CODE to code
    )
}

/**
 * 进入证据链申请签署
 *
 */
fun toSignEvidenceContractPage(context: Context, applyId: String) {
    context.startActivity<EvidenceContractActivity>(
            EXTRA_EVIDENCE_APPLY_ID to applyId
    )
}

/**
 * 进入证据链申请进度页面
 *
 * @param context
 * @param applyId     证据链申请id
 */
fun toEvidenceApplyProgressPage(context: Context, applyId: String) {
    context.startActivity<EvidenceApplyProgressActivity>(
            EXTRA_EVIDENCE_APPLY_ID to applyId
    )
}

/**
 * 进入手写签章设置界面
 *
 * @param context
 */
fun toSetHandlerSignature(context: Context) {
    var routerUrl = "hmiou://m.54jietiao.com/signature/check_sign_psd?url="
    val setSignatureUrl = "hmiou://m.54jietiao.com/signature/set_handler_signature?url=" + Uri.encode(BaseBizAppLike.getInstance().h5Server + "/EQB/index.html")
    routerUrl = routerUrl + Uri.encode(setSignatureUrl)
    Router.getInstance().buildWithUrl(routerUrl).navigation(context)
}