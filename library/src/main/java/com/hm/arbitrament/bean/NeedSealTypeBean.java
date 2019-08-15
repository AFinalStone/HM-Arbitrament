package com.hm.arbitrament.bean;

import lombok.Data;

/**
 * Created by hjy on 2019/3/21.
 */
@Data
public class NeedSealTypeBean {

    public boolean hasSeal;            //当限制为【个人】时，需要判断该字段，有无手写签名 ,
    public int sealLimitType;          //0-不限制，1-系统，2-个人，3-无需选择签章

}
