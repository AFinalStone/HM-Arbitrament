package com.hm.arbitrament.api;

import com.hm.iou.sharedata.model.BaseResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by syl on 2019/6/5.
 */

public interface ArbitramentServer {

    @GET("/api/message/messageCenter/v2/unReadMessages")
    Flowable<BaseResponse<Integer>> getUnReadMsgNum(@Query("tab") int tab);

}
