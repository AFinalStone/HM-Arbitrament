package com.hm.arbitrament.api;

import com.hm.arbitrament.bean.IOUExtResult;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.sharedata.model.BaseResponse;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by syl on 2019/6/5.
 */

public class ArbitramentApi {

    private static ArbitramentServer getService() {
        return HttpReqManager.getInstance().getService(ArbitramentServer.class);
    }

    /**
     * 获取消息中心未读消息数量
     *
     * @return
     */
    public static Flowable<BaseResponse<IOUExtResult>> getElecExDetails(String iouId) {
        return getService().getElecExDetails(iouId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
