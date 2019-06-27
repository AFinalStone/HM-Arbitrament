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
import com.hm.arbitrament.bean.RefundInfo;
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
    public static Flowable<BaseResponse<GetArbitramentStatusResBean>> getArbitramentStatus(String iouId, String justId, String arbNo) {
        GetArbitramentStatusReqBean req = new GetArbitramentStatusReqBean();
        req.setIouId(iouId);
        req.setJusticeId(justId);
        req.setArbApplyNo(arbNo);
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
    public static Flowable<BaseResponse<String>> createArbApplyBookOrder(CreateArbOrderReqBean reqBean) {
        return getService().createArbApplyBookOrder(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 重新提交仲裁申请书的支付订单
     *
     * @return
     */
    public static Flowable<BaseResponse<String>> resubmitArbApplyBookOrder(CreateArbOrderReqBean reqBean) {
        return getService().resubmitArbApplyBookOrder(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取仲裁申请书的订单详情
     *
     * @return
     */
    public static Flowable<BaseResponse<GetArbApplyBookOrderResBean>> getArbApplyBookOrderDetail(String iouId, String justiceId) {
        GetArbApplyBookOrderReqBean reqBean = new GetArbApplyBookOrderReqBean();
        reqBean.setIouId(iouId);
        reqBean.setJusticeId(justiceId);
        return getService().getArbApplyBookOrderDetail(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 对仲裁申请书的订单进行付款
     *
     * @return
     */
    public static Flowable<BaseResponse<PayArbApplyBookOrderResBean>> payArbApplyBookOrder(PayArbApplyBookOrderReqBean reqBean) {
        return getService().payArbApplyBookOrder(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取仲裁进度
     *
     * @param arbApplyNo 仲裁申请编号
     * @return
     */
    public static Flowable<BaseResponse<ProgressResBean>> getProgress(String arbApplyNo) {
        return getService().getProgress(arbApplyNo).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取仲裁申请书
     *
     * @param arbApplyNo
     * @return
     */
    public static Flowable<BaseResponse<GetArbApplyDocResBean>> getArbApplyDoc(String arbApplyNo) {
        return getService().getArbApplyDoc(arbApplyNo).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消仲裁
     *
     * @param arbApplyNo 仲裁申请编号
     * @param type       1-已履行，2-已和解，3-其他
     * @param reason     撤案原因描述：withdrawType=3时必传
     * @return
     */
    public static Flowable<BaseResponse<Object>> cancelArbitrament(String arbApplyNo, int type, String reason) {
        CancelArbReqBean reqBean = new CancelArbReqBean();
        reqBean.setArbApplyNo(arbApplyNo);
        reqBean.setWithdrawType(type);
        reqBean.setWithdrawReason(reason);
        return getService().cancelArbitrament(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<Object>> verfySmsCode(int purpose, String mobile, String msg) {
        VerifySmsReqBean reqBean = new VerifySmsReqBean();
        reqBean.setPurpose(purpose);
        reqBean.setMobile(mobile);
        reqBean.setMessage(msg);
        return getService().verfySmsCode(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取失败原因
     *
     * @param arbApplyNo
     * @return
     */
    public static Flowable<BaseResponse<FailReasonResBean>> getFailReason(String arbApplyNo) {
        return getService().getFailReason(arbApplyNo).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 裁决书申请
     *
     * @param reqBean
     * @return
     */
    public static Flowable<BaseResponse<String>> applyArbPaper(ArbPaperReqBean reqBean) {
        return getService().applyArbPaper(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<List<ArbPaperApplyInfo>>> getArbPaperList(String arbApplyNo) {
        return getService().getArbPaperList(arbApplyNo).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询支付订单状态
     *
     * @param orderId
     * @return
     */
    public static Flowable<BaseResponse<String>> queryOrderPayState(String orderId) {
        return getService().queryOrderPayState(orderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<GetArbApplyBookOrderResBean>> getApplyPackage(String iouId, String justId) {
        GetArbApplyBookOrderReqBean reqBean = new GetArbApplyBookOrderReqBean();
        reqBean.setIouId(iouId);
        reqBean.setJusticeId(justId);
        return getService().getApplyPackage(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<CreateArbApplyOrderResBean>> createArbApplyOrder(String arbNo, String msgCode) {
        CreateApplyOrderReqBean reqBean = new CreateApplyOrderReqBean();
        reqBean.setArbApplyNo(arbNo);
        reqBean.setMessage(msgCode);
        return getService().createArbApplyOrder(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<GetArbApplyBookOrderResBean>> getArbPaperPackage() {
        return getService().getArbPaperPackage().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<String>> createArbPaperOrder(String arbNo, String arbPaperId) {
        CreateArbPaperOrderReqBean reqBean = new CreateArbPaperOrderReqBean();
        reqBean.setArbApplyNo(arbNo);
        reqBean.setArbPaperId(arbPaperId);
        return getService().createArbPaperOrder(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建预付款订单
     *
     * @param chanel  平台 1微信支付 2苹果支付
     * @param orderId
     * @return
     */
    public static Flowable<BaseResponse<PayArbApplyBookOrderResBean>> createPreparePayOrder(int chanel, String orderId) {
        CreatePreparePayReqBean reqBean = new CreatePreparePayReqBean();
        reqBean.setChannel(1);
        reqBean.setOrderId(orderId);
        return getService().createPreparePayOrder(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<Object>> revokeArbPaper(String arbPaperId) {
        return getService().revokeArbPaper(arbPaperId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<RefundInfo>> getRefundStep(String arbApplyNo) {
        return getService().getRefundStep(arbApplyNo).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
