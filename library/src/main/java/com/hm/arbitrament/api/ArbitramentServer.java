package com.hm.arbitrament.api;

import com.hm.arbitrament.bean.ElecEvidenceRes;
import com.hm.arbitrament.bean.IOUExtResult;
import com.hm.arbitrament.bean.req.GetElecEvidenceListDetailBean;
import com.hm.iou.sharedata.model.BaseResponse;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by syl on 2019/6/5.
 */

public interface ArbitramentServer {

    @GET("/api/iou/front/v1/getArbitramentStatus")
    Flowable<BaseResponse<Integer>> getArbitramentStatus(@Query("iouId") String iouId);

    @GET("/api/iou/front/v1/exDetails")
    Flowable<BaseResponse<IOUExtResult>> getElecExDetails(@Query("iouId") String iouId);

    @POST("/api/arb/v1/getRemittanceReceipt")
    Flowable<BaseResponse<List<ElecEvidenceRes>>> getElecEvidenceList(@Body GetElecEvidenceListDetailBean data);
}
