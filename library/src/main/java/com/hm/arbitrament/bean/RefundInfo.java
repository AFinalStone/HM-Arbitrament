package com.hm.arbitrament.bean;

import java.util.List;

import lombok.Data;

@Data
public class RefundInfo {

    private List<RefundStep> arbRefundList;
    private List<RefundMoney> refundDetailList;
    private String refundAll;
    private int refundStep;

    @Data
    public static class RefundStep {
        private String description;
        private String operateDate;
    }

    @Data
    public static class RefundMoney {
        private String cost;
        private String refundItem;
    }

}
