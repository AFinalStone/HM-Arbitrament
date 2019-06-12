package com.hm.arbitrament.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 9:04 PM
 */

@Data
public class GetArbitramentInputApplyDataResBean {


    /**
     * amount : 0
     * dailyRate : 0
     * overdueInterestType : 0
     * repaymentRecordList : [{"amount":0,"repaymentDate":"2019-06-10T10:13:56.077Z"}]
     * urgeExidenceList : [{"fileId":"string","urgeEvidenceType":0}]
     */

    private Number amount;
    private Number dailyRate;
    private int overdueInterestType;
    private List<RepaymentRecordListBean> repaymentRecordList;//还款记录
    private List<UrgeExidenceListBean> urgeExidenceList;//催收证明

    @Data
    public static class RepaymentRecordListBean implements Serializable {
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

            RepaymentRecordListBean that = (RepaymentRecordListBean) o;

            return createTime == that.createTime;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (int) (createTime ^ (createTime >>> 32));
            return result;
        }
    }

    @Data
    public static class UrgeExidenceListBean implements Serializable {
        /**
         * fileId : string
         * urgeEvidenceType : 0
         */

        private String fileId;
        private int urgeEvidenceType;

    }
}