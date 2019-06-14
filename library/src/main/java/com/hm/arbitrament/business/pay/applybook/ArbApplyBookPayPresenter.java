package com.hm.arbitrament.business.pay.applybook;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.GetArbApplyBookOrderResBean;
import com.hm.arbitrament.business.pay.base.IMoneyItem;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author syl
 * @time 2018/5/30 下午6:47
 */
public class ArbApplyBookPayPresenter extends MvpActivityPresenter<ArbApplyBookPayActivity> implements ArbApplyBookPayContract.Presenter {


    public ArbApplyBookPayPresenter(@NonNull Context context, @NonNull ArbApplyBookPayActivity view) {
        super(context, view);
    }

    @Override
    public void getArbApplyBookOrderInfo(String iouId, String justId) {
        mView.showInitLoadingView();
        ArbitramentApi.getArbApplyBookOrderDetail(iouId, justId)
                .compose(getProvider().<BaseResponse<GetArbApplyBookOrderResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<GetArbApplyBookOrderResBean>handleResponse())
                .subscribeWith(new CommSubscriber<GetArbApplyBookOrderResBean>(mView) {
                    @Override
                    public void handleResult(GetArbApplyBookOrderResBean bean) {
                        if (bean == null) {
                            mView.showInitLoadingFailed("数据异常");
                            return;
                        }
                        mView.hideInitLoadingView();
                        //折扣
                        String discount = bean.getDiscountStr();
                        mView.showDiscount(discount);
                        //付款金额
                        String totalMoney = bean.getShowAmountStr();
                        String realTotalMoney = bean.getActualAmountStr();
                        mView.showTotalPayMoney(totalMoney, realTotalMoney);
                        //底部备注
                        String bottomRemark = bean.getComment();
                        mView.showBottomRemark(bottomRemark);
                        //费用列表
                        List<IMoneyItem> list = changeData(bean.getItemList());
                        mView.showData(list);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.showInitLoadingFailed(s1);
                    }
                });
    }

    private List<IMoneyItem> changeData(List<GetArbApplyBookOrderResBean.ItemListBean> list) {
        List<IMoneyItem> listResult = new ArrayList<>();
        if (list != null) {
            for (final GetArbApplyBookOrderResBean.ItemListBean bean : list) {
                IMoneyItem iMoneyItem = new IMoneyItem() {
                    @Override
                    public String getName() {
                        return bean.getName();
                    }

                    @Override
                    public String getContent() {
                        return bean.getAmountStr();
                    }


                    @Override
                    public String getWarnDialogContent() {
                        return bean.getComment();
                    }
                };
                listResult.add(iMoneyItem);
            }
        }
        return listResult;
    }
}