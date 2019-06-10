package com.hm.arbitrament;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hm.arbitrament.bean.IOUExtResult;
import com.hm.arbitrament.business.apply.view.SelectValidEvidenceActivity;
import com.hm.arbitrament.business.apply.view.SelectValidEvidenceDetailActivity;
import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.router.Router;

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
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                .withString("url", BaseBizAppLike.getInstance().getH5Server() + Constants.H5_URL_NEED_KNOW_BY_ARBIRAMENT)
                .navigation(context);
    }

    /**
     * 选择有效凭证
     *
     * @param context
     */
    public static void toSelectValidEvidenceActivity(Context context, String iouId) {
        Intent intent = new Intent(context, SelectValidEvidenceActivity.class);
        intent.putExtra(SelectValidEvidenceActivity.EXTRA_KEY_IOU_ID, iouId);
        context.startActivity(intent);
    }

    /**
     * 有效凭证详情
     *
     * @param activity
     */
    public static void toSelectValidEvidenceDetailActivity(Activity activity, int requestCode, IOUExtResult.ExtEvidence item, boolean isSelect) {
        Intent intent = new Intent(activity, SelectValidEvidenceDetailActivity.class);
        intent.putExtra(SelectValidEvidenceDetailActivity.EXTRA_KEY_ITEM, item);
        intent.putExtra(SelectValidEvidenceDetailActivity.EXTRA_KEY_IS_SELECT, isSelect);
        activity.startActivityForResult(intent, requestCode);
    }
}
