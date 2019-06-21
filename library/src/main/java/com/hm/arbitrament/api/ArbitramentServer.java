package com.hm.arbitrament.api;

import com.hm.arbitrament.bean.ArbPaperApplyInfo;
import com.hm.arbitrament.bean.CreateArbApplyOrderResBean;
import com.hm.arbitrament.bean.ElecEvidenceResBean;
import com.hm.arbitrament.bean.FailReasonResBean;
import com.hm.arbitrament.bean.GetArbApplyBookOrderResBean;
import com.hm.arbitrament.bean.GetArbApplyDocResBean;
import com.hm.arbitrament.bean.GetArbCostResBean;
import com.hm.arbitrament.bean.GetArbServerAgreementResBean;
import com.hm.arbitrament.bean.GetArbitramentInputApplyDataResBean;
import com.hm.arbitrament.bean.GetArbitramentStatusResBean;
import com.hm.arbitrament.bean.GetCollectionProveResBean;
import com.hm.arbitrament.bean.PayArbApplyBookOrderResBean;
import com.hm.arbitrament.bean.ProgressResBean;
import com.hm.arbitrament.bean.req.ArbPaperReqBean;
import com.hm.arbitrament.bean.req.CancelArbReqBean;
import com.hm.arbitrament.bean.req.CheckArbitramentApplyStatusReqBean;
import com.hm.arbitrament.bean.req.CreateApplyOrderReqBean;
import com.hm.arbitrament.bean.req.CreateArbOrderReqBean;
import com.hm.arbitrament.bean.req.CreateArbPaperOrderReqBean;
import com.hm.arbitrament.bean.req.CreatePreparePayReqBean;
import com.hm.arbitrament.bean.req.GetArbApplyBookOrderReqBean;
import com.hm.arbitrament.bean.req.GetArbCostReqBean;
import com.hm.arbitrament.bean.req.GetArbServerAgreementReqBean;
import com.hm.arbitrament.bean.req.GetArbitramentInputApplyDataReqBean;
import com.hm.arbitrament.bean.req.GetArbitramentStatusReqBean;
import com.hm.arbitrament.bean.req.GetElecEvidenceListDetailReqBean;
import com.hm.arbitrament.bean.req.PayArbApplyBookOrderReqBean;
import com.hm.arbitrament.bean.req.VerifySmsReqBean;
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
    Flowable<BaseResponse<String>> createArbApplyBookOrder(@Body CreateArbOrderReqBean reqBean);

    @POST("/api/arb/v1/resubmit")
    Flowable<BaseResponse<String>> resubmitArbApplyBookOrder(@Body CreateArbOrderReqBean reqBean);

    @POST("/api/arb/v1/order/getHmPackage")
    Flowable<BaseResponse<GetArbApplyBookOrderResBean>> getArbApplyBookOrderDetail(@Body GetArbApplyBookOrderReqBean reqBean);

    @POST("/api/arb/v1/order/arouseHmOrder")
    Flowable<BaseResponse<PayArbApplyBookOrderResBean>> payArbApplyBookOrder(@Body PayArbApplyBookOrderReqBean reqBean);

    @GET("/api/arb/v1/getProgress")
    Flowable<BaseResponse<ProgressResBean>> getProgress(@Query("arbApplyNo") String arbApplyNo);

    @GET("/api/arb/v1/getArbApplyDoc")
    Flowable<BaseResponse<GetArbApplyDocResBean>> getArbApplyDoc(@Query("arbApplyNo") String arbApplyNo);

    @POST("/api/arb/v1/cancel")
    Flowable<BaseResponse<Object>> cancelArbitrament(@Body CancelArbReqBean reqBean);

    @POST("/api/base/msg/v1/verifyMessage")
    Flowable<BaseResponse<Object>> verfySmsCode(@Body VerifySmsReqBean reqBean);

    @GET("/api/arb/v1/getSubmitBackFailReason")
    Flowable<BaseResponse<FailReasonResBean>> getFailReason(@Query("arbApplyNo") String arbApplyNo);

    @POST("/api/arb/v1/applyArbPaper")
    Flowable<BaseResponse<String>> applyArbPaper(@Body ArbPaperReqBean reqBean);

    @GET("/api/arb/v1/getArbPaperList")
    Flowable<BaseResponse<List<ArbPaperApplyInfo>>> getArbPaperList(@Query("arbApplyNo") String arbApplyNo);

    @GET("/pay/iou/v1/queryOrderWhilePaying")
    Flowable<BaseResponse<String>> queryOrderPayState(@Query("orderId") String orderId);

    @POST("/api/arb/v1/order/getApplyPackage")
    Flowable<BaseResponse<GetArbApplyBookOrderResBean>> getApplyPackage(@Body GetArbApplyBookOrderReqBean reqBean);

    @POST("/api/arb/v1/order/createApplyOrder")
    Flowable<BaseResponse<CreateArbApplyOrderResBean>> createArbApplyOrder(@Body CreateApplyOrderReqBean reqBean);

    @GET("/api/arb/v1/order/getArbPaperPackage")
    Flowable<BaseResponse<GetArbApplyBookOrderResBean>> getArbPaperPackage();

    @POST("/api/arb/v1/order/createArbPaperOrder")
    Flowable<BaseResponse<String>> createArbPaperOrder(@Body CreateArbPaperOrderReqBean reqBean);

    @POST("/pay/iou/v1/unifiedOrder")
    Flowable<BaseResponse<PayArbApplyBookOrderResBean>> createPreparePayOrder(@Body CreatePreparePayReqBean reqBean);

    @GET("/api/arb/v1/revokeArbPaper")
    Flowable<BaseResponse<Object>> revokeArbPaper(@Query("arbPaperId") String arbPaperId);

}
