package com.hm.arbitrament.dict;

/**
 * Created by syl on 2019/6/12.
 */

public enum CollectionProveEnum {
    BOOK(0, "已使用书面催收"), VERBAL(1, "我已口头说明催收"),
    EMAIL(2, "我已使用电子邮件催收"), CAN_NOT_CONNECT(2, "无法联系到借款人");
    private int type;
    private String desc;

    CollectionProveEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static CollectionProveEnum getInstance(int type) {
        if (BOOK.getType() == type) {
            return BOOK;
        } else if (VERBAL.getType() == type) {
            return VERBAL;
        } else if (EMAIL.getType() == type) {
            return EMAIL;
        } else if (CAN_NOT_CONNECT.getType() == type) {
            return CAN_NOT_CONNECT;
        }
        return null;
    }
}
