package com.hm.arbitrament.bean;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 11:27 AM
 */
@Data
public class GetArbServerAgreementResBean {

    /**
     * contractId : 0
     * hasPaid : true
     */

    private Integer contractId;
    private boolean hasPaid;

}
