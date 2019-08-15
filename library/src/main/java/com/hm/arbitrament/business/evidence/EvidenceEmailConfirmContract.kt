package com.hm.arbitrament.business.evidence

import com.hm.iou.base.mvp.BaseContract

/**
 * created by hjy on 2019/8/12
 *
 * 证据链邮箱确认界面
 */
interface EvidenceEmailConfirmContract {

    interface View : BaseContract.BaseView {

        /**
         * 显示用户当前绑定的邮箱账号
         *
         * @param email 用户当前绑定的邮箱
         */
        fun showUserEmail(email: String)

        /**
         * 验证码发送后开始倒计时
         */
        fun startCountDown()

    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 初始化
         */
        fun init(iouId: String?)

        /**
         * 发送验证码
         */
        fun sendVerifyCode()

        /**
         * 验证验证码是否正确
         *
         * @param code 验证码
         */
        fun verifySmsCode(code: String)

    }

}