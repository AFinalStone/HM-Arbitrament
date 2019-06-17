package com.hm.arbitrament.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 9:04 PM
 */

@Data
public class ElecEvidenceResBean implements Serializable {


    /**
     * exEvidenceId : string
     * fileType : 0
     * name : string
     * url : string
     */

    private String exEvidenceId;
    private int fileType;
    private String name;
    private String url;
    private String createTime;

}