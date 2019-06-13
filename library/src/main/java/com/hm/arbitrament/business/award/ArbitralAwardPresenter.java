package com.hm.arbitrament.business.award;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.iou.base.constants.HMConstants;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.tools.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ArbitralAwardPresenter extends MvpActivityPresenter<ArbitralAwardContract.View> implements ArbitralAwardContract.Presenter {

    public ArbitralAwardPresenter(@NonNull Context context, @NonNull ArbitralAwardContract.View view) {
        super(context, view);
    }

    @Override
    public void refreshApplyHistoryList() {
        System.out.println("auto refresh ======");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ArbitralAwardListAdapter.IArbitralAwardListItem> list = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    list.add(new ArbitralAwardListAdapter.IArbitralAwardListItem() {
                        @Override
                        public String getTime() {
                            return "2018.12.25 12:12";
                        }

                        @Override
                        public String getStatus() {
                            return "申请成功";
                        }

                        @Override
                        public String getName() {
                            return "王二小";
                        }

                        @Override
                        public String getMobile() {
                            return "15967132742";
                        }

                        @Override
                        public String getAddress() {
                            return "浙江省 杭州市 余杭区 天目山西路222号竹海水韵7幢2单元1111";
                        }
                    });
                }
                mView.finishRefresh();
                mView.showApplyList(list);
            }
        }, 2000);
    }

    @Override
    public void submitApplyInfo(String name, String mobile, String city, String addr) {
        if (TextUtils.isEmpty(name)) {
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
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.dismissLoadingView();
                mView.applySucc();
            }
        }, 2000);
    }
}
