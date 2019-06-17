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
    private Integer route;
    private String exField;
}