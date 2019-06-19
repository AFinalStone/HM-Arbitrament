package com.hm.arbitrament.util;

import android.content.Context;

import com.hm.arbitrament.bean.BackMoneyRecordBean;
import com.hm.arbitrament.bean.CollectionProveBean;
import com.hm.iou.tools.ACache;
import com.hm.iou.tools.SPUtil;

import java.util.ArrayList;

/**
 * Created by syl on 2019/6/18.
 */

public class CacheDataUtil {

    private static final String SP_NAME = "arbitrament_sp";
    //增量更新的时间
    private static final String KEY_COLLECTION_PROVE_BEAN = "arbitrament_collection_prove_bean";
    private static final String KEY_BACK_MONEY_RECORD_LIST = "arbitrament_back_money_record_list";
    private static final String KEY_IS_BACK_NOTHING = "arbitrament_is_back_nothing";

    /**
     * 存储催收证明对象
     *
     * @param context
     */
    public static void setCollectionProveBean(Context context, CollectionProveBean collectionProveBean) {
        ACache.get(context).put(KEY_COLLECTION_PROVE_BEAN, collectionProveBean);
    }

    /**
     * 获取催收证明对象
     *
     * @param context
     * @return
     */
    public static CollectionProveBean getCollectionProveBean(Context context) {
        return (CollectionProveBean) ACache.get(context).getAsObject(KEY_COLLECTION_PROVE_BEAN);
    }

    /**
     * 设置还款记录
     */
    public static void setBackMoneyRecordList(Context context, ArrayList<BackMoneyRecordBean> backMoneyRecordList) {
        ACache.get(context).put(KEY_BACK_MONEY_RECORD_LIST, backMoneyRecordList);
    }

    /**
     * 设置是否全部未还
     */
    public static void setIsBackNothing(Context context, boolean isBackNothing) {
        SPUtil.put(context, SP_NAME, KEY_IS_BACK_NOTHING, isBackNothing);
    }

    /**
     * 用户是否设置了全部未还
     *
     * @param context
     * @return
     */
    public static boolean getIsBackNothing(Context context) {
        return SPUtil.getBoolean(context, SP_NAME, KEY_IS_BACK_NOTHING);
    }

    /**
     * 获取还款记录
     *
     * @param context
     * @return
     */
    public static ArrayList<BackMoneyRecordBean> getBackMoneyRecordList(Context context) {
        return (ArrayList<BackMoneyRecordBean>) ACache.get(context).getAsObject(KEY_BACK_MONEY_RECORD_LIST);
    }

    /**
     * 清除创建合同页面的缓存对象
     */
    public static void clearInputApplyInfoCacheData(Context context) {
        ACache.get(context).put(KEY_COLLECTION_PROVE_BEAN, "");
        ACache.get(context).put(KEY_BACK_MONEY_RECORD_LIST, "");
        SPUtil.clear(context, SP_NAME);
    }
}
