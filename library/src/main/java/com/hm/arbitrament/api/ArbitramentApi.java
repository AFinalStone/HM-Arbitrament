package com.hm.arbitrament.api;

import com.hm.arbitrament.bean.ElecEvidenceRes;
import com.hm.arbitrament.bean.IOUExtResult;
import com.hm.arbitrament.bean.req.GetElecEvidenceListDetailBean;
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
     * 获取有效凭证列表
     *
     * @return
     */
    public static Flowable<BaseResponse<IOUExtResult>> getElecExDetails(String iouId) {
        return getService().getElecExDetails(iouId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 获取有效凭证列表
     *
     * @param iouId
     * @param justiceId
     * @return
     */
    public static Flowable<BaseResponse<List<ElecEvidenceRes>>> getElecEvidenceList(String iouId, String justiceId) {
        GetElecEvidenceListDetailBean data = new GetElecEvidenceListDetailBean();
        data.setIouId(iouId);
        data.setJusticeId(justiceId);
        return getService().getElecEvidenceList(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询仲裁状态
     *
     * @param iouId
     * @return
     */
    public static Flowable<BaseResponse<Integer>> getArbitramentStatus(String iouId) {
        return getService().getArbitramentStatus(iouId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
