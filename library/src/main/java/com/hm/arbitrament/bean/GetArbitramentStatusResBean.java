package com.hm.arbitrament.bean;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 9:04 PM
 */

@Data
public class GetArbitramentStatusResBean {


    /**
     * arbApplyNo : string
     * route : 0
     */

    private String arbApplyNo;
    private int route;
    private String exField;
    private int role;//1申请人，2非申请人
}