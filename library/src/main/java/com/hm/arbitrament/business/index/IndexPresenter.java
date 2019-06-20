package com.hm.arbitrament.business.index;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.GetArbitramentStatusResBean;
import com.hm.arbitrament.dict.ArbitramentStatusEnum;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * @author syl
 * @time 2018/5/30 下午6:47
 */
public class IndexPresenter extends MvpActivityPresenter<IndexContract.View> implements IndexContract.Presenter {


    public IndexPresenter(@NonNull Context context, @NonNull IndexContract.View view) {
        super(context, view);
    }

    @Override
    public void getArbitramentStatus(final String iouId, final String justId) {
        mView.showLoadingView();
        ArbitramentApi.getArbitramentStatus(iouId, justId)
                .compose(getProvider().<BaseResponse<GetArbitramentStatusResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<GetArbitramentStatusResBean>handleResponse())
                .subscribeWith(new CommSubscriber<GetArbitramentStatusResBean>(mView) {
                    @Override
                    public void handleResult(GetArbitramentStatusResBean bean) {
                        mView.dismissLoadingView();
                        if (bean != null) {
                            int flag = bean.getRoute();
                            if (ArbitramentStatusEnum.HAVE_NOT_APPLY.getCode() == flag) {
                                //还未申请，或者重新申请
                                NavigationHelper.toFiveAdvantage(mContext, iouId, justId);
                                mView.closeCurrPage();
                            } else if (ArbitramentStatusEnum.HAVE_APPLY_MAKE_BOOK_NOT_PAY.getCode() == flag) {
                                //已提交嘿马帮忙制作仲裁申请书，未付款
                                NavigationHelper.toWaitPayToMakeArbitramentApplyBook(mContext, iouId, justId, bean.getArbApplyNo(), bean.getExField());
//                                NavigationHelper.toFiveAdvantage(mContext, iouId, justId);

                                mView.closeCurrPage();
                            } else if (ArbitramentStatusEnum.HAVE_APPLY_MAKE_BOOK_WAIT_RESULT.getCode() == flag) {
                                NavigationHelper.toWaitMakeArbitramentApplyBook(mContext);
//                                NavigationHelper.toFiveAdvantage(mContext, iouId, justId);
                                mView.closeCurrPage();
                            } else if (ArbitramentStatusEnum.HAVE_APPLY_MAKE_BOOK_SUCCESS.getCode() == flag) {
                                //成功生成仲裁书
                                NavigationHelper.toArbitramentApplyBookDetail(mContext, iouId, justId, bean.getArbApplyNo());
                                mView.closeCurrPage();
                            } else if (ArbitramentStatusEnum.HAVE_SUBMIT_FIRST_TRIAL_FAILED_CAN_RETRY.getCode() == flag) {
                                //初审失败，允许重新补全资料
                                NavigationHelper.toSubmitFirstTrialFailed(mContext, iouId, justId, bean.getArbApplyNo());
                                mView.closeCurrPage();
                            } else if (ArbitramentStatusEnum.HAVE_SUBMIT_FIRST_TRIAL_FAILED_CAN_NOT_RETRY.getCode() == flag) {
                                //初审失败，不允许重新补全资料
                                NavigationHelper.toSubmitFirstTrialFailed(mContext, iouId, justId, bean.getArbApplyNo());
                                mView.closeCurrPage();
                            } else if (ArbitramentStatusEnum.HAVE_SUBMIT_PROGRESS_CAN_CANCEL.getCode() == flag) {
                                //初审通过进度页面，允许取消
                                NavigationHelper.toSubmitProgressFailed(mContext, true);
                                mView.closeCurrPage();
                            } else if (ArbitramentStatusEnum.HAVE_SUBMIT_PROGRESS_CAN_NOT_CANCEL.getCode() == flag) {
                                //初审通过进度页面，不允许取消
                                NavigationHelper.toArbitramentProgressPage(mContext, bean.getArbApplyNo());
                                mView.closeCurrPage();
                            } else if (ArbitramentStatusEnum.HAVE_SUBMIT_PROGRESS_HAVE_FINISH.getCode() == flag) {
                                //初审通过进度页面，不允许取消
                                NavigationHelper.toArbitramentProgressPage(mContext, bean.getArbApplyNo());
                                mView.closeCurrPage();
                            } else if (ArbitramentStatusEnum.LENDER_NO_APPLY_ARBITRAMENT.getCode() == flag) {
                                //当前用户借款人，出借人未申请仲裁
                                mView.showDialog("温馨提示", "出借人还未申请仲裁");
                            } else if (ArbitramentStatusEnum.LENDER_HAVE_APPLY_ARBITRAMENT.getCode() == flag) {
                                //当前用户借款人，出借人已申请仲裁
                                mView.showDialog("温馨提示", "出借人已经提出申请仲裁");
                            } else {
                                mView.closeCurrPage();
                            }
                        }

                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                        mView.closeCurrPage();
                    }
                });
    }
}