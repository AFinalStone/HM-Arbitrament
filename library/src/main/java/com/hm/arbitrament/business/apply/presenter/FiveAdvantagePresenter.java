package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.ElecEvidenceResBean;
import com.hm.arbitrament.business.apply.FiveAdvantageContract;
import com.hm.arbitrament.business.base.BasePresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;


/**
 * Created by syl on 2019/6/10.
 */

public class FiveAdvantagePresenter extends BasePresenter<FiveAdvantageContract.View> implements FiveAdvantageContract.Presenter {

    private static final String CODE_IS_NOT_UP_TO_APPOINTED_TIME = "1801008";//还没有到约定的还款时间哦！
    private static final String CODE_TIME_ARBITRAMENT_OUT_TIME = "1801009";//仲裁有效期为3年哦！
    private static final String CODE_TIME_CURRENT_ONLE_SUPPORT_YZ = "1801009";//目前仅支持衢州仲裁委在线申请
    private static final String CODE_TIME_NEED_UPLOAD_VALID_EVIDENCE = "1801011";//请先上传有效电子汇款凭证
    private static final String CODE_TIME_ID_CART_WILL_OUT_TIME = "1801012";//您的身份证有效期不足一个月，为保证仲裁顺利进行，请先更新您的身份证信息


    public FiveAdvantagePresenter(@NonNull Context context, @NonNull FiveAdvantageContract.View view) {
        super(context, view);
    }

    @Override
    public void checkArbitramentApplyStatus(final String iouId, final String justId) {
        ArbitramentApi.checkArbitramentApplyStatus(iouId, justId)
                .compose(getProvider().<BaseResponse<List<ElecEvidenceResBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<ElecEvidenceResBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<ElecEvidenceResBean>>(mView) {
                    @Override
                    public void handleResult(List<ElecEvidenceResBean> elecEvidenceResBeans) {
                        NavigationHelper.toSelectValidEvidenceActivity(mContext, iouId, justId);
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String s1) {
                        if (CODE_IS_NOT_UP_TO_APPOINTED_TIME.equals(code)) {
                            mView.showKnowDialog("还没有到约定的还款时间哦！");
                        } else if (CODE_TIME_ARBITRAMENT_OUT_TIME.equals(code)) {
                            mView.showKnowDialog("仲裁有效期为3年哦！");
                        } else if (CODE_TIME_CURRENT_ONLE_SUPPORT_YZ.equals(code)) {
                            mView.showKnowDialog("目前仅支持衢州仲裁委在线申请");
                        } else if (CODE_TIME_NEED_UPLOAD_VALID_EVIDENCE.equals(code)) {
                            mView.showNeedUploadElecEvidenceDialog();
                        } else if (CODE_TIME_ID_CART_WILL_OUT_TIME.equals(code)) {
                            mView.showNeedUpdateIDCardDialog();
                        }
                    }
                });
    }
}
