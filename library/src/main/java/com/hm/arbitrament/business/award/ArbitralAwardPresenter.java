package com.hm.arbitrament.business.award;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.ArbPaperApplyInfo;
import com.hm.arbitrament.bean.req.ArbPaperReqBean;
import com.hm.arbitrament.event.AwardPaySuccEvent;
import com.hm.iou.base.constants.HMConstants;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.StringUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ArbitralAwardPresenter extends MvpActivityPresenter<ArbitralAwardContract.View> implements ArbitralAwardContract.Presenter {

    public ArbitralAwardPresenter(@NonNull Context context, @NonNull ArbitralAwardContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void refreshApplyHistoryList(String arbNo) {
        ArbitramentApi.getArbPaperList(arbNo)
                .compose(getProvider().<BaseResponse<List<ArbPaperApplyInfo>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<ArbPaperApplyInfo>>handleResponse())
                .subscribeWith(new CommSubscriber<List<ArbPaperApplyInfo>>(mView) {
                    @Override
                    public void handleResult(List<ArbPaperApplyInfo> dataList) {
                        mView.finishRefresh();
                        List<ArbitralAwardListAdapter.IArbitralAwardListItem> list = new ArrayList<>();
                        if (dataList != null) {
                            for (ArbPaperApplyInfo data : dataList) {
                                list.add(convertData(data));
                            }
                        }
                        mView.showApplyList(list);
                        if (list.isEmpty()) {
                            mView.showApplyInputView();
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.finishRefresh();
                    }
                });
    }

    @Override
    public void submitApplyInfo(final String arbNo, String name, String mobile, String city, String addr) {
        if (TextUtils.isEmpty(name) || name.length() < 2) {
            mView.toastMessage("请输入收件人姓名");
            return;
        }
        if (!StringUtil.matchRegex(mobile, HMConstants.REG_MOBILE)) {
            mView.toastMessage("请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(city)) {
            mView.toastMessage("请选择城市/区域");
            return;
        }
        if (TextUtils.isEmpty(addr)) {
            mView.toastMessage("请填写详细地址");
            return;
        }
        mView.showLoadingView();
        ArbPaperReqBean reqBean = new ArbPaperReqBean();
        reqBean.setArbApplyNo(arbNo);
        reqBean.setName(name);
        reqBean.setMobile(mobile);
        reqBean.setCityDetail(city);
        reqBean.setDetailAddress(addr);
        ArbitramentApi.applyArbPaper(reqBean)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String arbPaperId) {
                        createOrder(arbNo, arbPaperId);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    private void createOrder(String arbApplyNo, String arbPaperId) {
        ArbitramentApi.createArbPaperOrder(arbApplyNo, arbPaperId)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String orderId) {
                        mView.dismissLoadingView();
                        NavigationHelper.toAwardPagePage(mContext, orderId);
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String msg) {
                        mView.dismissLoadingView();
                    }
                });
    }

    private ArbitralAwardListAdapter.IArbitralAwardListItem convertData(final ArbPaperApplyInfo data) {
        return new ArbitralAwardListAdapter.IArbitralAwardListItem() {

            String time;

            @Override
            public String getTime() {
                if (TextUtils.isEmpty(time)) {
                    time = data.getCreateTime();
                    if (time != null && time.length() >= 16) {
                        time = time.replace("-", ".").substring(0, 16);
                    }
                }
                return time;
            }

            @Override
            public String getStatus() {
                return "申请成功";
            }

            @Override
            public String getName() {
                return data.getName();
            }

            @Override
            public String getMobile() {
                return data.getMobile();
            }

            @Override
            public String getAddress() {
                return data.getCityDetail() +  data.getDetailAddress();
            }
        };
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPaySucc(AwardPaySuccEvent event) {
        mView.applySucc();
    }

}
