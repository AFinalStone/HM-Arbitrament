package com.hm.arbitrament.business.pay;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.business.pay.view.IMoneyItem;
import com.hm.iou.base.mvp.MvpActivityPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author syl
 * @time 2018/5/30 下午6:47
 */
public class PayPresenter extends MvpActivityPresenter<PayContract.View> implements PayContract.Presenter {


    public PayPresenter(@NonNull Context context, @NonNull PayContract.View view) {
        super(context, view);
    }

    @Override
    public void init() {
        IMoneyItem item = new IMoneyItem() {
            @Override
            public String getName() {
                return "仲裁受理费";
            }

            @Override
            public String getContent() {
                return "10.00";
            }

            @Override
            public String getWarnDialogContent() {
                return "仲裁受理费提示内容";
            }
        };
        List<IMoneyItem> list = new ArrayList<>();
        list.add(item);
        item = new IMoneyItem() {
            @Override
            public String getName() {
                return "仲裁服务费";
            }

            @Override
            public String getContent() {
                return "10.00";
            }

            @Override
            public String getWarnDialogContent() {
                return "仲裁服务费提示内容";
            }
        };
        list.add(item);
        mView.showData(list);
    }
}