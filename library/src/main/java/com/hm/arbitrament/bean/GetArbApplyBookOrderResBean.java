package com.hm.arbitrament.bean;

import java.util.List;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 11:27 AM
 */
@Data
public class GetArbApplyBookOrderResBean {


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
}
