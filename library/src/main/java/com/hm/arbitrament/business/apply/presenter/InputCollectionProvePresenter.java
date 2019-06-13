package com.hm.arbitrament.business.apply.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.GetCollectionProveResBean;
import com.hm.arbitrament.business.apply.InputCollectionProveContract;
import com.hm.iou.base.file.FileApi;
import com.hm.iou.base.file.FileBizType;
import com.hm.iou.base.file.FileUploadResult;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
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
    public void getCollectionProvelist() {
        mView.showLoadingView();
        ArbitramentApi.getCollectionProvelist()
                .compose(getProvider().<BaseResponse<List<GetCollectionProveResBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<GetCollectionProveResBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<GetCollectionProveResBean>>(mView) {
                    @Override
                    public void handleResult(List<GetCollectionProveResBean> list) {
                        mView.dismissLoadingView();
                        if (list == null || list.isEmpty()) {
                            mView.closeCurrPage();
                            return;
                        }
                        mView.showProveList(list);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.closeCurrPage();
                    }
                });
    }

    @Override
    public void uploadImage(File file) {
        mView.showLoadingView();
        FileApi.uploadImage(file, FileBizType.Arbitration_Collect_Certificate)
                .compose(getProvider().<BaseResponse<FileUploadResult>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<FileUploadResult>handleResponse())
                .subscribeWith(new CommSubscriber<FileUploadResult>(mView) {
                    @Override
                    public void handleResult(FileUploadResult result) {
                        mView.dismissLoadingView();
                        if (result == null) {
                            return;
                        }
                        mView.showImage(result.getFileId(), result.getFileUrl());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }
}
