package com.hm.arbitrament.api;

import com.hm.arbitrament.bean.ElecEvidenceResBean;
import com.hm.arbitrament.bean.GetArbitramentInputApplyDataResBean;
import com.hm.arbitrament.bean.GetArbitramentStatusResBean;
import com.hm.arbitrament.bean.req.CheckArbitramentApplyStatusReqBean;
import com.hm.arbitrament.bean.req.GetArbitramentInputApplyDataReqBean;
import com.hm.arbitrament.bean.req.GetArbitramentStatusReqBean;
import com.hm.arbitrament.bean.req.GetElecEvidenceListDetailReqBean;
import com.hm.iou.sharedata.model.BaseResponse;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
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
    Flowable<BaseResponse<String>> getArbitramentCost(@Body GetArbitramentInputApplyDataReqBean reqBean);
}
