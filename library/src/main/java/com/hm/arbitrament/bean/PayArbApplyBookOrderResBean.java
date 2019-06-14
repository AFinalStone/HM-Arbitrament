package com.hm.arbitrament.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 11:27 AM
 */
@Data
public class PayArbApplyBookOrderResBean {


    /**
     * appid : string
     * noncestr : string
     * package : string
     * partnerid : string
     * prepayid : string
     * sign : string
     * timestamp : 0
     */

    private String appid;
    private String noncestr;
    @SerializedName("package")
    private String packageX;
    private String partnerid;
    private String prepayid;
    private String sign;
    private int timestamp;
}
