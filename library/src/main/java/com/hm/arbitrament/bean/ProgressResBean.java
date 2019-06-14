package com.hm.arbitrament.bean;

import java.util.List;

import lombok.Data;

@Data
public class ProgressResBean {

    private String operName;
    private String operType;
    private List<ProgressItem> progressItemList;

    @Data
    public static class ProgressItem {

        private String docName;
        private String progressDesc;
        private String progressDate;
        private String docUrl;
        private String progressIconType;

    }

}
