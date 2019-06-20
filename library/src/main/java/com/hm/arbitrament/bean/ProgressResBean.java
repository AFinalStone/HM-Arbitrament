package com.hm.arbitrament.bean;

import java.util.List;

import lombok.Data;

@Data
public class ProgressResBean {

    private String nextOperName;                    //进度条下方的操作名称：如【申请纸质裁决书】
    private int nextOperType;                       //1-如何补充，2-申请裁决书
    private String pageOperName;                    //页面的操作按钮名称：如【取消仲裁】
    private int pageOperType;                       //0-无操作，1-取消仲裁，2-退款规则，3-退款进度
    private List<ProgressItem> progressItemList;
    private String exField;                         //pageOperType =2,返回退款时的步骤

    @Data
    public static class ProgressItem {

        private String docName;
        private String progressDesc;
        private String progressDate;
        private String docUrl;
        private String progressIconType;

    }

}
