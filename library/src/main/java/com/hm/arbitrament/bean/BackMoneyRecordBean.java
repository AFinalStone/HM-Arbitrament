package com.hm.arbitrament.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class BackMoneyRecordBean implements Serializable {
    /**
     * amount : 0
     * repaymentDate : 2019-06-10T10:13:56.077Z
     */
    private long createTime;//创建时间
    private int amount;
    private String repaymentDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BackMoneyRecordBean that = (BackMoneyRecordBean) o;

        return createTime == that.createTime;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (createTime ^ (createTime >>> 32));
        return result;
    }
}