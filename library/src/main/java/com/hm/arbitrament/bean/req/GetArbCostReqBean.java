package com.hm.arbitrament.bean.req;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 11:27 AM
 */
@Data
public class GetArbCostReqBean {

    private String iouId;
    private String justiceId;
    private double repayAmount;

}
