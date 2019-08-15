package com.hm.arbitrament.business.evidence.presenter

import android.content.Context
import com.hm.arbitrament.api.ArbitramentApi
import com.hm.arbitrament.business.evidence.EvidenceEmailConfirmContract
import com.hm.arbitrament.event.CloseEvidencePage
import com.hm.arbitrament.toEvidenceApplyPayPage
import com.hm.iou.base.comm.CommApi
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.sharedata.UserManager
import com.hm.iou.sharedata.model.BaseResponse
import com.trello.rxlifecycle2.android.ActivityEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * created by hjy on 2019/8/12
 *
 * 证据链邮箱确认界面
 */
class EvidenceEmailConfirmPresenter(context: Context, view: EvidenceEmailConfirmContract.View) :
        MvpActivityPresenter<EvidenceEmailConfirmContract.View>(context, view),
        EvidenceEmailConfirmContract.Presenter {

    private lateinit var mIouId: String
    private lateinit var mJusticeId: String

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun init(iouId: String?, justiceId: String?) {
        if (iouId.isNullOrEmpty() || justiceId.isNullOrEmpty()) {
            mView.closeCurrPage()
            return
        }
        mIouId = iouId
        mJusticeId = justiceId
        val userInfo = UserManager.getInstance(mContext).userInfo
        mView.showUserEmail(userInfo.mailAddr ?: "")
    }

    override fun sendVerifyCode() {
        mView.showLoadingView()
        val mobile = UserManager.getInstance(mContext).userInfo.mobile
        CommApi.sendMessage(14, mobile, null)
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
        ArbitramentApi.verfySmsCode(14, mobile, code)
                .compose(provider.bindUntilEvent<BaseResponse<Any>>(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<Any>())
                .subscribeWith(object : CommSubscriber<Any>(mView) {
                    override fun handleResult(o: Any?) {
                        mView.dismissLoadingView()
                        toEvidenceApplyPayPage(mContext, mIouId, mJusticeId, code)
                    }

                    override fun handleException(throwable: Throwable?, s: String?, s1: String?) {
                        mView.dismissLoadingView()
                    }
                })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventClosePage(event: CloseEvidencePage) {
        mView.closeCurrPage()
    }

}