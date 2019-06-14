package com.hm.arbitrament.bean.req;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 11:27 AM
 */
@Data
public class PayArbApplyBookOrderReqBean {


    /**
     * channel : 0
     * justiceId : string
     * openId : string
     * orderId : 0
     * sceneCode : string
     * tradeType : string
     */

    private int channel;
    private String justiceId;
    private String openId;
    private int orderId;
    private String sceneCode;
    private String tradeType;


}
