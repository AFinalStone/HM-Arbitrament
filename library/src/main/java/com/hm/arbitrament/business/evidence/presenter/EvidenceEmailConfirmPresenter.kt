package com.hm.arbitrament.business.evidence.presenter

import android.content.Context
import com.hm.arbitrament.api.ArbitramentApi
import com.hm.arbitrament.business.evidence.EvidenceEmailConfirmContract
import com.hm.iou.base.comm.CommApi
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.sharedata.UserManager
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * created by hjy on 2019/8/12
 *
 * 证据链邮箱确认界面
 */
class EvidenceEmailConfirmPresenter(context: Context, view: EvidenceEmailConfirmContract.View) :
        MvpActivityPresenter<EvidenceEmailConfirmContract.View>(context, view),
        EvidenceEmailConfirmContract.Presenter {

    private lateinit var mIouId: String

    override fun init(iouId: String?) {
        if (iouId.isNullOrEmpty()) {
            mView.closeCurrPage()
            return
        }
        mIouId = iouId
        val userInfo = UserManager.getInstance(mContext).userInfo
        mView.showUserEmail(userInfo.mailAddr ?: "")
    }

    override fun sendVerifyCode() {
        mView.showLoadingView()
        val mobile = UserManager.getInstance(mContext).userInfo.mobile
        CommApi.sendMessage(13, mobile, null)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<String>(mView) {
                    override fun handleResult(s: String?) {
                        mView.dismissLoadingView()
                        mView.startCountDown()
                    }

                    override fun handleException(throwable: Throwable?, s: String?, s1: String?) {
                        mView.dismissLoadingView()
                    }
                })
    }

    override fun verifySmsCode(code: String) {
        mView.showLoadingView()
        val mobile = UserManager.getInstance(mContext).userInfo.mobile
        ArbitramentApi.verfySmsCode(13, mobile, code)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<Any>(mView) {
                    override fun handleResult(o: Any?) {
                        mView.dismissLoadingView()
                        //                   mView.toPay(code)
                    }

                    override fun handleException(throwable: Throwable?, s: String?, s1: String?) {
                        mView.dismissLoadingView()
                    }
                })
    }

}