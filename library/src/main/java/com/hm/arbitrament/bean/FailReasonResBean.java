package com.hm.arbitrament.bean;

import java.util.List;

import lombok.Data;

@Data
public class FailReasonResBean {

    private boolean canRetry;
    private List<String> reasonList;

}
