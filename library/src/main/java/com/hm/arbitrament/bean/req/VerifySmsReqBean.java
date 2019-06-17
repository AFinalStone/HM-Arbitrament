package com.hm.arbitrament.bean.req;

import lombok.Data;

@Data
public class VerifySmsReqBean {

    private int purpose;
    private String mobile;
    private String message;

}
