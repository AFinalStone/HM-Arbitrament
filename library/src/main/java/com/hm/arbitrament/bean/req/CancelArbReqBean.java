package com.hm.arbitrament.bean.req;

import lombok.Data;

@Data
public class CancelArbReqBean {

    private String arbApplyNo;
    private int withdrawType;
    private String withdrawReason;

}
