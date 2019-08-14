package com.hm.arbitrament.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by hjy on 19/8/14.<br>
 */
class EvidenceApplyOrderResBean {

    var actualAmountStr: String? = null
    var applyId: String? = null
    var orderId: String? = null
    var wxPayAppParamResp: EvidenceWxPayParams? = null
    var itemList: List<EvidenceItemList>? = null

}

class EvidenceItemList {

    var amountStr: String? = null
    var comment: String? = null
    var name: String? = null
    var order: Int = 0
}


class EvidenceWxPayParams {

    var appid: String? = null
    var noncestr: String? = null
    var orderId: String? = null

    @SerializedName("package")
    var packageX: String? = null

    var partnerid: String? = null
    var prepayid: String? = null
    var sign: String? = null
    var timestamp: String? = null

}