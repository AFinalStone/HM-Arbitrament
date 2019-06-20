package com.hm.arbitrament.bean;

import lombok.Data;

@Data
public class ArbPaperApplyInfo {

    private String arbPaperId;
    private String name;
    private String mobile;
    private String cityDetail;
    private String detailAddress;
    private String createTime;
    //1=申请成功，2=已发货，0-未支付
    private int status;
    private String description;

}
