package com.hm.arbitrament.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class CreateArbApplyOrderResBean {


    /**
     * arbPackageResp : {"actualAmountStr":"string","comment":"string","discountStr":"string","expireSeconds":0,"itemList":[{"amountStr":"string","comment":"string","name":"string","order":0}],"name":"string","packageId":0,"pageRoute":0,"showAmountStr":"string"}
     * orderId : string
     * wxPayAppParamResp : {"appid":"string","noncestr":"string","orderId":"string","package":"string","partnerid":"string","prepayid":"string","sign":"string","timestamp":0}
     */

    private ArbPackageRespBean arbPackageResp;
    private String orderId;
    private WxPayAppParamRespBean wxPayAppParamResp;

    @Data
    public static class ArbPackageRespBean {
        /**
         * actualAmountStr : string
         * comment : string
         * discountStr : string
         * expireSeconds : 0
         * itemList : [{"amountStr":"string","comment":"string","name":"string","order":0}]
         * name : string
         * packageId : 0
         * pageRoute : 0
         * showAmountStr : string
         */
        private String actualAmountStr;
        private String comment;
        private String discountStr;
        private int expireSeconds;
        private String name;
        private int packageId;
        private int pageRoute;
        private String showAmountStr;
        private List<ItemListBean> itemList;
    }

    @Data
    public static class ItemListBean {
        /**
         * amountStr : string
         * comment : string
         * name : string
         * order : 0
         */
        private String amountStr;
        private String comment;
        private String name;
        private int order;

    }

    @Data
    public static class WxPayAppParamRespBean {
        /**
         * appid : string
         * noncestr : string
         * orderId : string
         * package : string
         * partnerid : string
         * prepayid : string
         * sign : string
         * timestamp : 0
         */
        private String appid;
        private String noncestr;
        private String orderId;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

    }
}
