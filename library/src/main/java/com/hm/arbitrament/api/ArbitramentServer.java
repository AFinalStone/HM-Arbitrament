package com.hm.arbitrament.api;

import com.hm.arbitrament.bean.IOUExtResult;
import com.hm.iou.sharedata.model.BaseResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by syl on 2019/6/5.
 */

public interface ArbitramentServer {

    @GET("/api/iou/front/v1/exDetails")
    Flowable<BaseResponse<IOUExtResult>> getElecExDetails(@Query("iouId") String iouId);

}
