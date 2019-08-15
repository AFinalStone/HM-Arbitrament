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

    private String mArbNo;

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
        mArbNo = arbNo;
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
    public void submitApplyInfo(final String arbNo, String name, Integer count, String mobile, String city, String addr) {
        if (TextUtils.isEmpty(name) || name.length() < 2) {
            mView.toastMessage("请输入收件人姓名");
            return;
        }
        if (count == null || count <= 0) {
            mView.toastMessage("请选择申请份数");
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
        reqBean.setPaperNum(count);
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

    @Override
    public void toCancelApply(final String applyId) {
        mView.showLoadingView();
        ArbitramentApi.revokeArbPaper(applyId)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object o) {
                        mView.dismissLoadingView();
                        mView.removeData(applyId);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Override
    public void toPayApply(String applyId) {
        mView.showLoadingView();
        createOrder(mArbNo, applyId);
    }

    private void createOrder(String arbApplyNo, final String arbPaperId) {
        ArbitramentApi.createArbPaperOrder(arbApplyNo, arbPaperId)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String orderId) {
                        mView.dismissLoadingView();
                        NavigationHelper.toAwardPagePage(mContext, orderId, arbPaperId);
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
            public String getApplyId() {
                return data.getArbPaperId();
            }

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
            public String getStatusStr() {
                if (data.getStatus() == 1) {
                    return "申请成功";
                } else if (data.getStatus() == 2) {
                    return "申领完成";
                }
                return "等待支付";
            }

            @Override
            public int getStatus() {
                return data.getStatus();
            }

            @Override
            public int getStatusColor() {
                if (data.getStatus() == 1) {
                    return 0xff2782e2;
                } else if (data.getStatus() == 2) {
                    return 0xff111111;
                }
                return 0xffef5350;
            }

            @Override
            public String getDesc() {
                return data.getDescription();
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
                return data.getCityDetail() + data.getDetailAddress();
            }
        };
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPaySucc(AwardPaySuccEvent event) {
        mView.applySucc();
    }

}
