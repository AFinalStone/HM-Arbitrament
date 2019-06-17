package com.hm.arbitrament.business.progress.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.hm.arbitrament.R;
import com.hm.arbitrament.api.ArbitramentApi;
import com.hm.arbitrament.bean.ProgressResBean;
import com.hm.arbitrament.business.progress.ArbitramentProgressContract;
import com.hm.arbitrament.business.progress.view.ProgressAdapter;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

public class ArbitramentProgressPresenter extends MvpActivityPresenter<ArbitramentProgressContract.View>
        implements ArbitramentProgressContract.Presenter, View.OnClickListener {

    private ProgressResBean mProgressInfo;

    public ArbitramentProgressPresenter(@NonNull Context context, @NonNull ArbitramentProgressContract.View view) {
        super(context, view);
    }

    @Override
    public void loadProgressData(String arbNo) {
        mView.showDataLoading();

        mView.showBottomBackMoneyRule(1);

        ArbitramentApi.getProgress(arbNo)
                .compose(getProvider().<BaseResponse<ProgressResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<ProgressResBean>handleResponse())
                .subscribeWith(new CommSubscriber<ProgressResBean>(mView) {
                    @Override
                    public void handleResult(ProgressResBean result) {
                        mProgressInfo = result;
                        List<ProgressAdapter.IProgressItem> list = new ArrayList<>();
                        List<ProgressResBean.ProgressItem> progressList = result.getProgressItemList();
                        if (progressList != null) {
                            int i = 0;
                            for (final ProgressResBean.ProgressItem item : progressList) {
                                int flag = 0;
                                if (i == 0)
                                    flag = 1;
                                else if (i == progressList.size() - 1)
                                    flag = 2;
                                list.add(convert(item, flag));
                                i++;
                            }
                        }
                        mView.showProgressList(list);

                        if (!TextUtils.isEmpty(result.getOperName())) {
                            mView.addFooterTips(result.getOperName(), ArbitramentProgressPresenter.this);
                        }

                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.showDataLoadFailed();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (mProgressInfo == null)
            return;
    }

    /**
     * @param item
     * @param positionFlag 1-表示第一条数据，2-表示最后一条数据，0及其他-表示在中间位置
     * @return
     */
    private ProgressAdapter.IProgressItem convert(final ProgressResBean.ProgressItem item, final int positionFlag) {
        return new ProgressAdapter.IProgressItem() {
            @Override
            public String getTitle() {
                return item.getProgressDesc();
            }

            @Override
            public String getTime() {
                return item.getProgressDate();
            }

            @Override
            public String getSubTitle() {
                return item.getDocName();
            }

            @Override
            public String getLink() {
                return item.getDocUrl();
            }

            @Override
            public int getCheckedIcon() {
                if ("1".equals(item.getProgressIconType()))
                    return R.mipmap.uikit_icon_check_black;
                if ("2".equals(item.getProgressIconType()))
                    return R.mipmap.arbitrament_ic_progress_canceld;
                return R.mipmap.uikit_icon_check_black;
            }

            @Override
            public int getPositionFlag() {
                return positionFlag;
            }
        };
    }

}
