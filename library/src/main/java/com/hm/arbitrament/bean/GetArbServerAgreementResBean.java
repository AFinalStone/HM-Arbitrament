package com.hm.arbitrament.bean;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 11:27 AM
 */
@Data
public class GetArbServerAgreementResBean {

    private int contractId;
    private String contractUrl;
    private boolean hasPaid;

}
