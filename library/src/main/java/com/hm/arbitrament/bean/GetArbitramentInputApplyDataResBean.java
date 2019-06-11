package com.hm.arbitrament.bean;

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

    private int amount;
    private int dailyRate;
    private int overdueInterestType;
    private List<RepaymentRecordListBean> repaymentRecordList;
    private List<UrgeExidenceListBean> urgeExidenceList;

    @Data
    public static class RepaymentRecordListBean {
        /**
         * amount : 0
         * repaymentDate : 2019-06-10T10:13:56.077Z
         */

        private int amount;
        private String repaymentDate;

    }

    @Data
    public static class UrgeExidenceListBean {
        /**
         * fileId : string
         * urgeEvidenceType : 0
         */

        private String fileId;
        private int urgeEvidenceType;

    }
}