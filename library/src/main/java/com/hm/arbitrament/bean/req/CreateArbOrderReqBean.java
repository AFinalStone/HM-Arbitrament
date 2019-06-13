package com.hm.arbitrament.bean.req;

import com.hm.arbitrament.bean.BackMoneyRecordBean;
import com.hm.arbitrament.bean.CollectionProveBean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 11:27 AM
 */
@Data
public class CreateArbOrderReqBean {


    /**
     * exEvidenceIdList : ["string"]
     * iouId : string
     * justiceId : string
     * repaymentRecordList : [{"amount":0,"repaymentDate":"2019-06-13T10:28:50.209Z"}]
     * sealId : string
     * transPswd : string
     * urgeExidenceList : [{"fileId":"string","urgeEvidenceType":0}]
     */

    private String iouId;
    private String justiceId;
    private String sealId;
    private String transPswd;
    private List<String> exEvidenceIdList;
    private ArrayList<BackMoneyRecordBean> repaymentRecordList;//还款记录
    private ArrayList<CollectionProveBean> urgeExidenceList;//催收证明

}
