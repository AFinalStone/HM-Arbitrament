package com.hm.arbitrament.business.progress.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.hm.arbitrament.R;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.RefundInfo;
import com.hm.arbitrament.business.progress.MoneyBackProgressContract;
import com.hm.arbitrament.business.progress.view.BackMoneyDetailAdapter;
import com.hm.arbitrament.business.progress.view.ProgressAdapter;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RouterUtil;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.hm.arbitrament.Constants.H5_URL_TUI_KUAN;

public class MoneyBackProgressPresenter extends MvpActivityPresenter<MoneyBackProgressContract.View> implements MoneyBackProgressContract.Presenter {

    private RefundInfo mRefundInfo;

    public MoneyBackProgressPresenter(@NonNull Context context, @NonNull MoneyBackProgressContract.View view) {
        super(context, view);
    }

    @Override
    public void loadProgressData(String arbNo) {
        mView.showDataLoading();
        ArbitramentApi.getRefundStep(arbNo)
                .compose(getProvider().<BaseResponse<RefundInfo>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<RefundInfo>handleResponse())
                .subscribeWith(new CommSubscriber<RefundInfo>(mView) {
                    @Override
                    public void handleResult(RefundInfo refundInfo) {
                        mRefundInfo = refundInfo;
                        List<ProgressAdapter.IProgressItem> list = new ArrayList<>();
                        if (refundInfo.getArbRefundList() != null) {
                            for (int i = 0; i < refundInfo.getArbRefundList().size(); i++) {
                                final RefundInfo.RefundStep step = refundInfo.getArbRefundList().get(i);
                                int f = 0;
                                if (i == 0) {
                                    f = 1;
                                } else if (i == refundInfo.getArbRefundList().size() - 1) {
                                    f = 2;
                                }
                                final int flag = f;
                                list.add(new ProgressAdapter.IProgressItem() {
                                    @Override
                                    public String getTitle() {
                                        return step.getDescription();
                                    }

                                    @Override
                                    public String getTime() {
                                        String date = step.getOperateDate();
                                        if (date != null) {
                                            date = date.replace("-", ".");
                                        }
                                        return date;
                                    }

                                    @Override
                                    public int getPositionFlag() {
                                        return flag;
                                    }

                                    @Override
                                    public String getSubTitle() {
                                        return null;
                                    }

                                    @Override
                                    public String getLink() {
                                        return null;
                                    }

                                    @Override
                                    public int getCheckedIcon() {
                                        return R.mipmap.uikit_icon_check_black;
                                    }
                                });
                            }
                        }

                        mView.showProgressList(list);

                        mView.showBackMoney(refundInfo.getRefundAll());
                        mView.showBackRule(refundInfo.getRefundStep());
                        mView.addFooterTips("如何在第三方支付平台账户查看退款款项", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RouterUtil.clickMenuLink(mContext, H5_URL_TUI_KUAN);
                            }
                        });
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String msg) {
                        mView.showDataLoadFailed();
                    }
                });
    }

    @Override
    public void clickBackMoneyDetail() {
        if (mRefundInfo == null)
            return;
        List<RefundInfo.RefundMoney> dataList = mRefundInfo.getRefundDetailList();
        List<BackMoneyDetailAdapter.IBackMoneyItem> list = new ArrayList<>();
        if (dataList != null) {
            for (final RefundInfo.RefundMoney data : dataList) {
                list.add(new BackMoneyDetailAdapter.IBackMoneyItem() {
                    @Override
                    public String getTitle() {
                        return data.getRefundItem();
                    }

                    @Override
                    public String getMoney() {
                        return data.getCost();
                    }
                });
            }
        }
        mView.showBackMoneyDetailList(list, mRefundInfo.getRefundAll());
    }
}
