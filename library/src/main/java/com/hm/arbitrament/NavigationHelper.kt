package com.hm.arbitrament

import android.content.Context
import com.hm.arbitrament.business.evidence.view.EvidenceApplyDescActivity
import com.hm.arbitrament.business.evidence.view.EvidenceApplyEmailConfirmActivity
import com.hm.arbitrament.business.evidence.view.EvidenceApplyRecordActivity
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
 * 进入证据链申请进度页面
 *
 * @param context
 * @param iouId     借条id
 */
fun toEvidenceApplyProgressPage(context: Context, iouId: String) {

}