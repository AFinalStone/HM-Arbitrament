package com.hm.arbitrament.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class CollectionProveBean implements Serializable {
    /**
     * fileId : string
     * urgeEvidenceType : 0
     */

    private String fileId;
    private String fileUrl;//本地缓存使用
    private int urgeEvidenceType;
    private String description;

}