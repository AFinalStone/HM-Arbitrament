package com.hm.arbitrament.dict;

/**
 * Created by syl on 2019/6/11.
 */

public enum ArbBookEditRemarkTypeEnum {

    arb001("arb001", "争议金额"), arb002("arb002", "逾期利息"), arb003("arb003", "证据链"),
    arb004("arb004", "利息意向"), arb005("arb005", "合计应还"), arb006("arb006", "仲裁费用");

    private String typeCode;
    private String typeName;

    ArbBookEditRemarkTypeEnum(String code, String name) {
        this.typeCode = code;
        this.typeName = name;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }
}
