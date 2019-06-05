package com.hm.arbitrament.business.test;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;

/**
 * @author syl
 * @time 2018/5/30 下午6:47
 */
public class ApplyPresenter extends MvpActivityPresenter<ApplyContract.View> implements ApplyContract.Presenter {


    public ApplyPresenter(@NonNull Context context, @NonNull ApplyContract.View view) {
        super(context, view);
    }
}