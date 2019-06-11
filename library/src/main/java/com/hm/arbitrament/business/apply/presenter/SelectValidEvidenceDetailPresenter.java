package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.business.apply.InputApplyInfoContract;
import com.hm.iou.base.mvp.MvpActivityPresenter;


/**
 * Created by syl on 2019/6/10.
 */

public class SelectValidEvidenceDetailPresenter extends MvpActivityPresenter<InputApplyInfoContract.View> implements InputApplyInfoContract.Presenter {

    public SelectValidEvidenceDetailPresenter(@NonNull Context context, @NonNull InputApplyInfoContract.View view) {
        super(context, view);
    }

}
