package com.hm.arbitrament.bean

enum class EvidenceStatusEnum constructor(val status: Int, val desc: String) {

    HAS_PAID(1, "等待签署协议"),      //已支付
    APPLY_SUCCESS(2, "申请成功"),
    CREATE_SUCCESS(3, "生成成功"),
    SEND_SUCCESS(4, "发送成功"),
    RESEND_SUCCESS(5, "补发成功"),
    COMPLETE(6, "已完成");

    companion object {

        fun parse(status: Int): EvidenceStatusEnum {
            return when(status) {
                1 -> HAS_PAID
                2 -> APPLY_SUCCESS
                3 -> CREATE_SUCCESS
                4 -> SEND_SUCCESS
                5 -> RESEND_SUCCESS
                6 -> COMPLETE
                else -> COMPLETE
            }
        }
    }

}