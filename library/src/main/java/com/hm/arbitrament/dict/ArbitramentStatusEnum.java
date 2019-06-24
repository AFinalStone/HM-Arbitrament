package com.hm.arbitrament.dict;

/**
 * Created by syl on 2019/6/11.
 */

public enum ArbitramentStatusEnum {
    HAVE_NOT_APPLY(0, "还未申请过或重新申请"),
    HAVE_APPLY_MAKE_BOOK_NOT_PAY(1, "已申请制作仲裁申请书，未付款"),
    HAVE_APPLY_MAKE_BOOK_WAIT_RESULT(2, "已付款，等待仲裁申请书的制作结果"),
    HAVE_APPLY_MAKE_BOOK_SUCCESS(3, "提交材料,仲裁申请书成功生成"),
    HAVE_SUBMIT_FIRST_TRIAL_FAILED_CAN_RETRY(4, "提交材料-预审失败-允许重试"),
    HAVE_SUBMIT_FIRST_TRIAL_FAILED_CAN_NOT_RETRY(5, "提交材料-预审失败-不可重试"),
    HAVE_SUBMIT_PROGRESS_CAN_CANCEL(6, "初审通过进度页面-有撤销按钮"),
    HAVE_SUBMIT_PROGRESS_CAN_NOT_CANCEL(7, "初审通过进度页面-有退款进度按钮"),
    HAVE_SUBMIT_PROGRESS_HAVE_FINISH(8, "进度页面-结束"),
    LENDER_NO_APPLY_ARBITRAMENT(9, "当前用户借款人，出借人未申请仲裁"),
    LENDER_HAVE_APPLY_ARBITRAMENT(10, "当前用户借款人，出借人已申请仲裁"),
    LENDER_ONLY_SUPPORT_ALIPAY(11, "当前只支持支付宝对支付宝转账记录"),
    PROCESS_RULE(12, "进度页面-退款规则");

    private int code;
    private String statusDesc;

    ArbitramentStatusEnum(int code, String statusDesc) {
        this.code = code;
        this.statusDesc = statusDesc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
