package com.hm.arbitrament.business.evidence.presenter

import android.app.Activity
import android.content.Context
import com.hm.arbitrament.NavigationHelper
import com.hm.arbitrament.api.ArbitramentApi
import com.hm.arbitrament.bean.EvidenceApplyDocResBean
import com.hm.arbitrament.bean.NeedSealTypeBean
import com.hm.arbitrament.business.evidence.EvidenceContractContract
import com.hm.arbitrament.business.evidence.view.EvidenceContractActivity
import com.hm.arbitrament.event.EvidenceSignSuccEvent
import com.hm.arbitrament.toEvidenceApplyProgressPage
import com.hm.arbitrament.toSetHandlerSignature
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.tools.Md5Util
import com.trello.rxlifecycle2.android.ActivityEvent
import org.greenrobot.eventbus.EventBus

/**
 * created by hjy on 2019/8/12
 *
 * 证据链合同签署
 */
class EvidenceContractPresenter(context: Context, view: EvidenceContractContract.View) :
        MvpActivityPresenter<EvidenceContractContract.View>(context, view),
        EvidenceContractContract.Presenter {

    private var mContractId: String? = null

    override fun getEvidenceContractDoc(applyId: String) {
        mView.showDataLoading()
        ArbitramentApi.getEvidenceApplyDoc(applyId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<EvidenceApplyDocResBean>(mView) {
                    override fun handleResult(data: EvidenceApplyDocResBean?) {
                        data?.let {
                            mView.showEvidenceContractDoc(data.contractUrl ?: "")
                            mView.showBottomButtonText("立即确认")
                            mContractId = it.contractId
                        }
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.showLoadFailed(p2 ?: "数据加载出错")
                    }

                    override fun isShowBusinessError(): Boolean = false

                    override fun isShowCommError(): Boolean = false

                })
    }

    override fun doSignContract(applyId: String, sealId: String, pwd: String) {
        mView.showLoadingView()
        ArbitramentApi.signEvidenceContract(applyId, sealId, Md5Util.getMd5ByString(pwd))
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<EvidenceApplyDocResBean>(mView) {
                    override fun handleResult(p0: EvidenceApplyDocResBean?) {
                        mView.dismissLoadingView()
                        EventBus.getDefault().post(EvidenceSignSuccEvent())
                        //申请成功之后，进入进度页面
                        toEvidenceApplyProgressPage(mContext, applyId)
                        mView.closeCurrPage()
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.dismissLoadingView()
                    }
                })
    }

    override fun judgeNeedSealType() {
        if (mContractId.isNullOrEmpty()) {
            return
        }
        mView.showLoadingView()
        ArbitramentApi.getNeedSealType(mContractId, 0)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<NeedSealTypeBean>(mView) {
                    override fun handleResult(data: NeedSealTypeBean?) {
                        mView.dismissLoadingView()
                        data?.let {
                            //如果需要手写签章，但是没有设置过
                            if (it.sealLimitType == 2 && !it.hasSeal) {
                                toSetHandlerSignature(mContext)
                                return
                            }
                            NavigationHelper.toSignContractCheckSignPwd(mContext as Activity, "输入签约密码",
                                    it.sealLimitType, true, EvidenceContractActivity.REQ_CHECK_SIGN_PWD)
                        }
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.dismissLoadingView()
                    }
                })
    }
}