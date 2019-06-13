package com.hm.arbitrament.api;

import com.hm.arbitrament.bean.ElecEvidenceResBean;
import com.hm.arbitrament.bean.GetArbCostResBean;
import com.hm.arbitrament.bean.GetArbServerAgreementResBean;
import com.hm.arbitrament.bean.GetArbitramentInputApplyDataResBean;
import com.hm.arbitrament.bean.GetArbitramentStatusResBean;
import com.hm.arbitrament.bean.GetCollectionProveResBean;
import com.hm.arbitrament.bean.req.CheckArbitramentApplyStatusReqBean;
import com.hm.arbitrament.bean.req.CreateArbOrderReqBean;
import com.hm.arbitrament.bean.req.GetArbCostReqBean;
import com.hm.arbitrament.bean.req.GetArbServerAgreementReqBean;
import com.hm.arbitrament.bean.req.GetArbitramentInputApplyDataReqBean;
import com.hm.arbitrament.bean.req.GetArbitramentStatusReqBean;
import com.hm.arbitrament.bean.req.GetElecEvidenceListDetailReqBean;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.sharedata.model.BaseResponse;

import java.util.List;

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
     * 查询仲裁状态
     *
     * @param iouId
     * @return
     */
    public static Flowable<BaseResponse<GetArbitramentStatusResBean>> getArbitramentStatus(String iouId, String justId) {
        GetArbitramentStatusReqBean req = new GetArbitramentStatusReqBean();
        req.setIouId(iouId);
        req.setJusticeId(justId);
        return getService().getArbitramentStatus(req).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取有效凭证列表
     *
     * @param iouId
     * @param justiceId
     * @return
     */
    public static Flowable<BaseResponse<List<ElecEvidenceResBean>>> getElecEvidenceList(String iouId, String justiceId) {
        GetElecEvidenceListDetailReqBean data = new GetElecEvidenceListDetailReqBean();
        data.setIouId(iouId);
        data.setJusticeId(justiceId);
        return getService().getElecEvidenceList(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 校验仲裁申请状态
     *
     * @param iouId
     * @param justiceId
     * @return
     */
    public static Flowable<BaseResponse<List<ElecEvidenceResBean>>> checkArbitramentApplyStatus(String iouId, String justiceId) {
        CheckArbitramentApplyStatusReqBean data = new CheckArbitramentApplyStatusReqBean();
        data.setIouId(iouId);
        data.setJusticeId(justiceId);
        return getService().checkArbitramentApplyStatus(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取仲裁申请页面的预填充数据
     *
     * @param iouId
     * @param justiceId
     * @return
     */
    public static Flowable<BaseResponse<GetArbitramentInputApplyDataResBean>> getArbitramentInputApplyData(String iouId, String justiceId) {
        GetArbitramentInputApplyDataReqBean data = new GetArbitramentInputApplyDataReqBean();
        data.setIouId(iouId);
        data.setJusticeId(justiceId);
        return getService().getArbitramentInputApplyData(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 计算仲裁费用
     *
     * @param iouId
     * @param justiceId
     * @return
     */
    public static Flowable<BaseResponse<GetArbCostResBean>> getArbitramentCost(String iouId, String justiceId, Double totalMoney) {
        GetArbCostReqBean data = new GetArbCostReqBean();
        data.setIouId(iouId);
        data.setJusticeId(justiceId);
        data.setRepayAmount(totalMoney);
        return getService().getArbitramentCost(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取催收证明描述列表
     *
     * @return
     */
    public static Flowable<BaseResponse<List<GetCollectionProveResBean>>> getCollectionProvelist() {
        return getService().getCollectionProvelist().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取仲裁服务协议
     *
     * @return
     */
    public static Flowable<BaseResponse<GetArbServerAgreementResBean>> getArbServerAgreement(String iouId, String justiceId) {
        GetArbServerAgreementReqBean reqBean = new GetArbServerAgreementReqBean();
        reqBean.setIouId(iouId);
        reqBean.setJusticeId(justiceId);
        return getService().getArbServerAgreement(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建仲裁申请书的支付订单
     *
     * @return
     */
    public static Flowable<BaseResponse<Object>> createArbApplyBookOrder(CreateArbOrderReqBean reqBean) {
        return getService().createArbApplyBookOrder(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
