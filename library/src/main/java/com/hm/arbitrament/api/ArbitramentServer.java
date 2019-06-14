package com.hm.arbitrament.api;

import com.hm.arbitrament.bean.ElecEvidenceResBean;
import com.hm.arbitrament.bean.GetArbApplyBookOrderResBean;
import com.hm.arbitrament.bean.GetArbCostResBean;
import com.hm.arbitrament.bean.GetArbServerAgreementResBean;
import com.hm.arbitrament.bean.GetArbitramentInputApplyDataResBean;
import com.hm.arbitrament.bean.GetArbitramentStatusResBean;
import com.hm.arbitrament.bean.GetCollectionProveResBean;
import com.hm.arbitrament.bean.PayArbApplyBookOrderResBean;
import com.hm.arbitrament.bean.ProgressResBean;
import com.hm.arbitrament.bean.req.CancelArbReqBean;
import com.hm.arbitrament.bean.req.CheckArbitramentApplyStatusReqBean;
import com.hm.arbitrament.bean.req.CreateArbOrderReqBean;
import com.hm.arbitrament.bean.req.GetArbApplyBookOrderReqBean;
import com.hm.arbitrament.bean.req.GetArbCostReqBean;
import com.hm.arbitrament.bean.req.GetArbServerAgreementReqBean;
import com.hm.arbitrament.bean.req.GetArbitramentInputApplyDataReqBean;
import com.hm.arbitrament.bean.req.GetArbitramentStatusReqBean;
import com.hm.arbitrament.bean.req.GetElecEvidenceListDetailReqBean;
import com.hm.arbitrament.bean.req.PayArbApplyBookOrderReqBean;
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

    @POST("/api/arb/v1/getArbPageIndex")
    Flowable<BaseResponse<GetArbitramentStatusResBean>> getArbitramentStatus(@Body GetArbitramentStatusReqBean req);

    @POST("/api/arb/v1/getRemittanceReceipt")
    Flowable<BaseResponse<List<ElecEvidenceResBean>>> getElecEvidenceList(@Body GetElecEvidenceListDetailReqBean req);

    @POST("/api/arb/v1/checkArbApply")
    Flowable<BaseResponse<List<ElecEvidenceResBean>>> checkArbitramentApplyStatus(@Body CheckArbitramentApplyStatusReqBean req);

    @POST("/api/arb/v1/getArbMoneyData")
    Flowable<BaseResponse<GetArbitramentInputApplyDataResBean>> getArbitramentInputApplyData(@Body GetArbitramentInputApplyDataReqBean req);

    @POST("/api/arb/v1/getArbCost")
    Flowable<BaseResponse<GetArbCostResBean>> getArbitramentCost(@Body GetArbCostReqBean reqBean);

    @POST("/api/arb/v1/getUrgeEvidenceState")
    Flowable<BaseResponse<List<GetCollectionProveResBean>>> getCollectionProvelist();

    @POST("/api/arb/v1/getArbProtocol")
    Flowable<BaseResponse<GetArbServerAgreementResBean>> getArbServerAgreement(@Body GetArbServerAgreementReqBean reqBean);

    @POST("/api/arb/v1/order/createHmOrder")
    Flowable<BaseResponse<Integer>> createArbApplyBookOrder(@Body CreateArbOrderReqBean reqBean);

    @POST("/api/arb/v1/order/getHmPackage")
    Flowable<BaseResponse<GetArbApplyBookOrderResBean>> getArbApplyBookOrderDetail(@Body GetArbApplyBookOrderReqBean reqBean);

    @POST("/api/arb/v1/order/arouseHmOrder")
    Flowable<BaseResponse<PayArbApplyBookOrderResBean>> payArbApplyBookOrder(@Body PayArbApplyBookOrderReqBean reqBean);

    @GET("/api/arb/v1/getProgress")
    Flowable<BaseResponse<ProgressResBean>> getProgress(@Query("arbApplyNo") String arbApplyNo);

    @GET("/api/arb/v1/getArbApplyDoc")
    Flowable<BaseResponse<String>> getArbApplyDoc(@Query("arbApplyNo") String arbApplyNo);

    @POST("/api/arb/v1/cancel")
    Flowable<BaseResponse<Object>> cancelArbitrament(@Body CancelArbReqBean reqBean);

}
