package com.hm.arbitrament.business.progress.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;

import com.hm.arbitrament.R;
import com.hm.arbitrament.business.progress.MoneyBackProgressContract;
import com.hm.arbitrament.business.progress.view.ProgressAdapter;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.RouterUtil;

import java.util.ArrayList;
import java.util.List;

import static com.hm.arbitrament.Constants.H5_URL_TUI_KUAN;

public class MoneyBackProgressPresenter extends MvpActivityPresenter<MoneyBackProgressContract.View> implements MoneyBackProgressContract.Presenter {

    public MoneyBackProgressPresenter(@NonNull Context context, @NonNull MoneyBackProgressContract.View view) {
        super(context, view);
    }

    @Override
    public void loadProgressData() {
        mView.showDataLoading();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ProgressAdapter.IProgressItem> list = new ArrayList<>();

                list.add(new ProgressAdapter.IProgressItem() {
                    @Override
                    public String getTitle() {
                        return "组庭成功";
                    }

                    @Override
                    public String getTime() {
                        return "2016.08.09 12:00";
                    }

                    @Override
                    public int getPositionFlag() {
                        return 1;
                    }

                    @Override
                    public String getSubTitle() {
                        return "组庭通知书";
                    }

                    @Override
                    public String getLink() {
                        return null;
                    }

                    @Override
                    public int getCheckedIcon() {
                        return R.mipmap.uikit_icon_check_black;
                    }
                });

                list.add(new ProgressAdapter.IProgressItem() {
                    @Override
                    public String getTitle() {
                        return "仲裁审理中";
                    }

                    @Override
                    public String getTime() {
                        return "2016.08.09 12:00";
                    }

                    @Override
                    public int getPositionFlag() {
                        return 0;
                    }

                    @Override
                    public String getSubTitle() {
                        return "书面审理通知书";
                    }

                    @Override
                    public String getLink() {
                        return null;
                    }

                    @Override
                    public int getCheckedIcon() {
                        return R.mipmap.uikit_icon_check_black;
                    }
                });

                list.add(new ProgressAdapter.IProgressItem() {
                    @Override
                    public String getTitle() {
                        return "取消仲裁申请中";
                    }

                    @Override
                    public String getTime() {
                        return "2016.08.09 12:00";
                    }

                    @Override
                    public String getSubTitle() {
                        return null;
                    }

                    @Override
                    public int getPositionFlag() {
                        return 2;
                    }

                    @Override
                    public String getLink() {
                        return null;
                    }

                    @Override
                    public int getCheckedIcon() {
                        return R.mipmap.uikit_icon_check_black;
                    }
                });

                mView.showProgressList(list);
                mView.showBackMoney("￥122.02");
                mView.addFooterTips("如何在第三方支付平台账户查看退款款项", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RouterUtil.clickMenuLink(mContext, H5_URL_TUI_KUAN);
                    }
                });
            }
        }, 2000);
    }

}
