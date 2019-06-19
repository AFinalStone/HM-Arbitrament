package com.hm.arbitrament;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hm.arbitrament.bean.BackMoneyRecordBean;
import com.hm.arbitrament.bean.CollectionProveBean;
import com.hm.arbitrament.bean.ElecEvidenceResBean;
import com.hm.arbitrament.business.apply.view.ArbApplyBookWaitPayActivity;
import com.hm.arbitrament.business.apply.view.ArbitramentServerAgreementActivity;
import com.hm.arbitrament.business.apply.view.FiveAdvantageActivity;
import com.hm.arbitrament.business.apply.view.InputApplyInfoActivity;
import com.hm.arbitrament.business.apply.view.InputCollectionProveActivity;
import com.hm.arbitrament.business.apply.view.InputRealBackMoneyActivity;
import com.hm.arbitrament.business.apply.view.SelectValidEvidenceActivity;
import com.hm.arbitrament.business.apply.view.SelectValidEvidenceDetailActivity;
import com.hm.arbitrament.business.apply.view.WaitMakeArbApplyBookActivity;
import com.hm.arbitrament.business.award.ArbitralAwardActivity;
import com.hm.arbitrament.business.fail.AuditFailActivity;
import com.hm.arbitrament.business.pay.applybook.ArbApplyBookPayActivity;
import com.hm.arbitrament.business.pay.applysubmit.ArbApplySubmitPayActivity;
import com.hm.arbitrament.business.pay.award.AwardPayActivity;
import com.hm.arbitrament.business.progress.view.ArbitramentProgressActivity;
import com.hm.arbitrament.business.progress.view.MoneyBackProgressActivity;
import com.hm.arbitrament.business.submit.ArbitramentSubmitActivity;
import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.base.utils.RouterUtil;
import com.hm.iou.router.Router;

import java.util.ArrayList;

import static com.hm.arbitrament.Constants.H5_URL_RETURN_MONEY_RULE;
import static com.hm.arbitrament.Constants.H5_URL_ZHONGCAI_XUZHI;

/**
 * Created by syl on 2019/6/6.
 */

public class NavigationHelper {


    /**
     * 上传凭证
     *
     * @param context
     */
    public static void toUploadEvidence(Context context) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                .withString("url", BaseBizAppLike.getInstance().getH5Server() + Constants.H5_URL_UPLOAD_EVIDENCE)
                .navigation(context);
    }

    /**
     * 仲裁须知
     *
     * @param context
     */
    public static void toNeedKnowByArbitrament(Context context) {
        RouterUtil.clickMenuLink(context, H5_URL_ZHONGCAI_XUZHI);
    }

    /**
     * 进入申请仲裁的5大优点
     *
     * @param context
     */
    public static void toFiveAdvantage(Context context, String iouId, String justId) {
        Intent intent = new Intent(context, FiveAdvantageActivity.class);
        intent.putExtra(FiveAdvantageActivity.EXTRA_KEY_IOU_ID, iouId);
        intent.putExtra(FiveAdvantageActivity.EXTRA_KEY_JUST_ID, justId);
        context.startActivity(intent);
    }

    /**
     * 输入申请信息
     *
     * @param context
     */
    public static void toInputApplyInfo(Context context, String iouId, String justId, ArrayList<String> list, boolean mIsSubmit) {
        Intent intent = new Intent(context, InputApplyInfoActivity.class);
        intent.putExtra(InputApplyInfoActivity.EXTRA_KEY_IOU_ID, iouId);
        intent.putExtra(InputApplyInfoActivity.EXTRA_KEY_JUST_ID, justId);
        intent.putExtra(InputApplyInfoActivity.EXTRA_KEY_LIST, list);
        intent.putExtra(InputApplyInfoActivity.EXTRA_KEY_IS_RESUBMIT, mIsSubmit);
        context.startActivity(intent);
    }

    /**
     * 选择有效凭证
     *
     * @param context
     */
    public static void toSelectValidEvidenceActivity(Context context, String iouId, String justId) {
        Intent intent = new Intent(context, SelectValidEvidenceActivity.class);
        intent.putExtra(SelectValidEvidenceActivity.EXTRA_KEY_IOU_ID, iouId);
        intent.putExtra(SelectValidEvidenceActivity.EXTRA_KEY_JUST_ID, justId);
        context.startActivity(intent);
    }

    /**
     * 有效凭证详情
     *
     * @param activity
     */
    public static void toSelectValidEvidenceDetailActivity(Activity activity, int requestCode, ElecEvidenceResBean item, boolean isSelect) {
        Intent intent = new Intent(activity, SelectValidEvidenceDetailActivity.class);
        intent.putExtra(SelectValidEvidenceDetailActivity.EXTRA_KEY_ITEM, item);
        intent.putExtra(SelectValidEvidenceDetailActivity.EXTRA_KEY_IS_SELECT, isSelect);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 校验签约密码
     *
     * @param context
     * @param title
     * @param reqCode
     */
    public static void toCheckSignPwd(Activity context, String title, int reqCode) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/signature/check_sign_psd_v2")
                .withString("title", title)
                .navigation(context, reqCode);
    }

    /**
     * 仲裁服务协议
     *
     * @param activity
     */
    public static void toArbitramentServerAgreement(Activity activity, String url, int reqCode) {
        Intent intent = new Intent(activity, ArbitramentServerAgreementActivity.class);
        intent.putExtra(ArbitramentServerAgreementActivity.EXTRA_KEY_URL, url);
        activity.startActivityForResult(intent, reqCode);
    }

    /**
     * 等待付款去生成仲裁申请书
     *
     * @param context
     * @param iouId
     * @param justId
     * @param orderId
     * @param arbNo
     */
    public static void toWaitPayToMakeArbitramentApplyBook(Context context, String iouId, String justId, String arbNo, String orderId) {
        Intent intent = new Intent(context, ArbApplyBookWaitPayActivity.class);
        intent.putExtra(ArbApplyBookWaitPayActivity.EXTRA_KEY_IOU_ID, iouId);
        intent.putExtra(ArbApplyBookWaitPayActivity.EXTRA_KEY_JUST_ID, justId);
        intent.putExtra(ArbApplyBookWaitPayActivity.EXTRA_KEY_ARB_NO, arbNo);
        intent.putExtra(ArbApplyBookWaitPayActivity.EXTRA_KEY_ORDER_ID, orderId);
        context.startActivity(intent);
    }

    /**
     * 等待生成仲裁申请书
     *
     * @param context
     */
    public static void toWaitMakeArbitramentApplyBook(Context context) {
        Intent intent = new Intent(context, WaitMakeArbApplyBookActivity.class);
        context.startActivity(intent);
    }

    /**
     * 仲裁申请书详情页面，准备正式提交申请
     *
     * @param activity
     * @param iouId
     * @param justId
     * @param arbNo
     */
    public static void toArbitramentApplyBookDetail(Context activity, String iouId, String justId, String arbNo) {
        Intent intent = new Intent(activity, ArbitramentSubmitActivity.class);
        intent.putExtra(ArbitramentSubmitActivity.EXTRA_KEY_IOU_ID, iouId);
        intent.putExtra(ArbitramentSubmitActivity.EXTRA_KEY_JUSTICE_ID, justId);
        intent.putExtra(ArbitramentSubmitActivity.EXTRA_KEY_ARB_NO, arbNo);
        activity.startActivity(intent);
    }


    /**
     * 提交初审失败
     *
     * @param activity
     * @param iouId
     * @param justiceId
     * @param arbApplyNo
     */
    public static void toSubmitFirstTrialFailed(Context activity, String iouId, String justiceId, String arbApplyNo) {
        Intent intent = new Intent(activity, AuditFailActivity.class);
        intent.putExtra(AuditFailActivity.EXTRA_KEY_IOU_ID, iouId);
        intent.putExtra(AuditFailActivity.EXTRA_KEY_JUSTICE_ID, justiceId);
        intent.putExtra(AuditFailActivity.EXTRA_KEY_ARB_NO, arbApplyNo);
        activity.startActivity(intent);
    }

    /**
     * 提交仲裁审核进度失败
     *
     * @param activity
     * @param isCanReTry 允许重试
     */
    public static void toSubmitProgressFailed(Context activity, boolean isCanReTry) {
//        Intent intent = new Intent(activity, Wai.class);
//        activity.startActivity(intent);
    }

    /**
     * 进入仲裁进度页面
     *
     * @param context
     * @param arbNo   仲裁申请编号
     */
    public static void toArbitramentProgressPage(Context context, String arbNo) {
        Intent intent = new Intent(context, ArbitramentProgressActivity.class);
        intent.putExtra(ArbitramentProgressActivity.EXTRA_KEY_ARB_NO, arbNo);
        context.startActivity(intent);
    }

    /**
     * 在线支付页面
     *
     * @param context
     */
    public static void toPay(Context context, String iouId, String justId, String orderId) {
        Intent intent = new Intent(context, ArbApplyBookPayActivity.class);
        intent.putExtra(ArbApplyBookPayActivity.EXTRA_KEY_IOU_ID, iouId);
        intent.putExtra(ArbApplyBookPayActivity.EXTRA_KEY_JUST_ID, justId);
        context.startActivity(intent);
    }

    /**
     * 添加催收证明
     *
     * @param activity
     * @param reqCode
     */
    public static void toAddCollectionProve(Activity activity, CollectionProveBean proveBean, int reqCode) {
        Intent intent = new Intent(activity, InputCollectionProveActivity.class);
        intent.putExtra(InputCollectionProveActivity.EXTRA_KEY_ITEM, proveBean);
        activity.startActivityForResult(intent, reqCode);
    }

    /**
     * 添加还款记录
     *
     * @param activity
     * @param reqCode
     */
    public static void toAddBackMoneyRecord(Activity activity, ArrayList<BackMoneyRecordBean> list,
                                            String startTime, String strTotalMoney, int reqCode) {
        Intent intent = new Intent(activity, InputRealBackMoneyActivity.class);
        intent.putExtra(InputRealBackMoneyActivity.EXTRA_KEY_BACK_MONEY_RECORD_LIST, list);
        intent.putExtra(InputRealBackMoneyActivity.EXTRA_KEY_BACK_TIME_START_TIME, startTime);
        try {
            Double totalMoney = Double.parseDouble(strTotalMoney);
            intent.putExtra(InputRealBackMoneyActivity.EXTRA_KEY_MAX_BACK_MONEY, totalMoney);
        } catch (Exception e) {

        }
        activity.startActivityForResult(intent, reqCode);
    }

    /**
     * 查看PDF
     *
     * @param context
     * @param pdfUrl
     */
    public static void toPdfPage(Context context, String pdfUrl) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/iou/contract_pdf_detail")
                .withString("pdf_url", pdfUrl)
                .navigation(context);
    }

    /**
     * 退款规则页面
     *
     * @param context
     * @param progress [0, 4]
     */
    public static void toReturnMoneyRulePage(Context context, int progress) {
        String url = BaseBizAppLike.getInstance().getH5Server() + H5_URL_RETURN_MONEY_RULE + progress;
        RouterUtil.clickMenuLink(context, url);
    }

    /**
     * 进入仲裁申请书提交页面
     *
     * @param context
     * @param arbApplyNo 仲裁申请编号
     */
    public static void toArbitramentSubmitPage(Context context, String arbApplyNo) {
        Intent intent = new Intent(context, ArbitramentSubmitActivity.class);
        intent.putExtra(ArbitramentSubmitActivity.EXTRA_KEY_ARB_NO, arbApplyNo);
        context.startActivity(intent);
    }

    /**
     * 进入退款进度页面
     *
     * @param context
     * @param arbApplyNo 仲裁申请编号
     */
    public static void toMoneyBackProgressPage(Context context, String arbApplyNo) {
        Intent intent = new Intent(context, MoneyBackProgressActivity.class);
        intent.putExtra(MoneyBackProgressActivity.EXTRA_KEY_ARB_NO, arbApplyNo);
        context.startActivity(intent);
    }

    /**
     * 进入资料补全界面
     *
     * @param context
     * @param iouId
     * @param justiceId
     * @param arbApplyNo
     */
    public static void toDocCompletionPage(Context context, String iouId, String justiceId, String arbApplyNo) {

    }

    /**
     * 进入仲裁申请支付页面
     *
     * @param context
     * @param iouId
     * @param justId
     * @param arbApplyNo
     * @param orderId
     */
    public static void toArbApplyPayPage(Context context, String iouId, String justId, String arbApplyNo, String orderId) {
        Intent intent = new Intent(context, ArbApplySubmitPayActivity.class);
        intent.putExtra(ArbApplySubmitPayActivity.EXTRA_KEY_ARB_NO, arbApplyNo);
        intent.putExtra(ArbApplySubmitPayActivity.EXTRA_KEY_IOU_ID, iouId);
        intent.putExtra(ArbApplySubmitPayActivity.EXTRA_KEY_JUST_ID, justId);
        intent.putExtra(ArbApplySubmitPayActivity.EXTRA_KEY_ORDER_ID, orderId);
        context.startActivity(intent);
    }

    /**
     * 进入裁决书申请页面
     *
     * @param context
     * @param arbNo
     */
    public static void toArbAwardPage(Context context, String arbNo) {
        Intent intent = new Intent(context, ArbitralAwardActivity.class);
        intent.putExtra(ArbitralAwardActivity.EXTRA_KEY_ARB_NO, arbNo);
        context.startActivity(intent);
    }

    /**
     * 进入裁决书申请支付页面
     *
     * @param context
     * @param orderId
     */
    public static void toAwardPagePage(Context context, String orderId) {
        Intent intent = new Intent(context, AwardPayActivity.class);
        intent.putExtra(AwardPayActivity.EXTRA_KEY_ORDER_ID, orderId);
        context.startActivity(intent);
    }

}
