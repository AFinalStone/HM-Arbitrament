package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.business.apply.InputCollectionProveContract;
import com.hm.iou.base.mvp.MvpActivityPresenter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author syl
 * @time 2019/6/11 3:36 PM
 */

public class InputCollectionProvePresenter extends MvpActivityPresenter<InputCollectionProveContract.View> implements InputCollectionProveContract.Presenter {

    public InputCollectionProvePresenter(@NonNull Context context, @NonNull InputCollectionProveContract.View view) {
        super(context, view);
    }

    @Override
    public void init() {
        List<String> list = new ArrayList<>();
        list.add("已使用书面催收");
        list.add("我已口头说明催收");
        list.add("我已使用电子邮件催收");
        list.add("无法联系到借款人");
        mView.showData(list);
    }
}
