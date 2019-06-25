package com.hm.arbitrament.bean;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 9:04 PM
 */

@Data
public class GetArbitramentInputApplyDataResBean implements Serializable {


    /**
     * amount : 0
     * dailyRate : 0
     * overdueInterestType : 0
     * repaymentRecordList : [{"amount":0,"repaymentDate":"2019-06-10T10:13:56.077Z"}]
     * urgeExidenceList : [{"fileId":"string","urgeEvidenceType":0}]
     */

    private Double amount;
    private String dailyRate;
    private String contractStartDate;//合同开始时间
    private Integer overdueInterestType;
    private ArrayList<BackMoneyRecordBean> repaymentRecordList;//还款记录
    private ArrayList<CollectionProveBean> urgeExidenceList;//催收证明
    private ArrayList<Remark> remarkList;//弹窗列表

    @Data
    public static class Remark {
        private String code;
        private String remark;
    }

}