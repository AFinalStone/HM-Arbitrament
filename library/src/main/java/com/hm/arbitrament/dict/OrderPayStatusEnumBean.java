package com.hm.arbitrament.dict;

import java.io.Serializable;

public enum OrderPayStatusEnumBean implements Serializable {

    PayWait("1", "创建订单，待支付"), Paying("3", "支付中"), PaySuccess("4", "支付成功"),
    PayFailed("5", "支付失败"), PayFinish("6", "支付关闭"), RefundMoney("7", "转入退款");

    String status;
    String name;

    OrderPayStatusEnumBean(String status, String name) {
        this.status = status;
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

}
