package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.business.apply.InputRealBackMoneyContract;
import com.hm.iou.base.mvp.MvpActivityPresenter;


/**
 * @author syl
 * @time 2019/6/11 3:36 PM
 */

public class InputRealBackMoneyPresenter extends MvpActivityPresenter<InputRealBackMoneyContract.View> implements InputRealBackMoneyContract.Presenter {


    public InputRealBackMoneyPresenter(@NonNull Context context, @NonNull InputRealBackMoneyContract.View view) {
        super(context, view);
    }

}
