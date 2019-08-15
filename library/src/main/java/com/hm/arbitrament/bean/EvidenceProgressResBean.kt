package com.hm.arbitrament.bean

/**
 * Created by hjy on 19/8/14.<br>
 */
class EvidenceProgressResBean {

    var nextOperName: String? = null
    var nextOperType: Int? = 0
    var progressItemList: List<EvidenceApplyProgressItem>? = null

}

class EvidenceApplyProgressItem {

    var progressDateStr: String? = null
    var progressDesc: String? = null
    var progressIconType: Int = 0

}