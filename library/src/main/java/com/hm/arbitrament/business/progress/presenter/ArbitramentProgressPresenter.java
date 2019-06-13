package com.hm.arbitrament.business.progress.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;

import com.hm.arbitrament.R;
import com.hm.arbitrament.business.progress.ArbitramentProgressContract;
import com.hm.arbitrament.business.progress.view.ProgressAdapter;
import com.hm.iou.base.mvp.MvpActivityPresenter;

import java.util.ArrayList;
import java.util.List;

public class ArbitramentProgressPresenter extends MvpActivityPresenter<ArbitramentProgressContract.View> implements ArbitramentProgressContract.Presenter {

    public ArbitramentProgressPresenter(@NonNull Context context, @NonNull ArbitramentProgressContract.View view) {
        super(context, view);
    }

    int i = 0;

    @Override
    public void loadProgressData() {
        mView.showDataLoading();

        if (i == 0) {
            i++;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mView.showDataLoadFailed();
                }
            }, 2000);
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ProgressAdapter.IProgressItem> list = new ArrayList<>();
                list.add(new ProgressAdapter.IProgressItem() {
                    @Override
                    public String getTitle() {
                        return "仲裁委托申请成功";
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
                    public String getLink() {
                        return null;
                    }

                    @Override
                    public int getPositionFlag() {
                        return 1;
                    }

                    @Override
                    public int getCheckedIcon() {
                        return R.mipmap.uikit_icon_check_black;
                    }
                });

                list.add(new ProgressAdapter.IProgressItem() {
                    @Override
                    public String getTitle() {
                        return "仲裁申请成功";
                    }

                    @Override
                    public String getTime() {
                        return "2016.08.09 12:00";
                    }

                    @Override
                    public String getSubTitle() {
                        return "仲裁申请书";
                    }

                    @Override
                    public int getPositionFlag() {
                        return 0;
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
                        return "仲裁受理成功";
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
                        return "受理通知书";
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
                        return "组庭成功";
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
                        return R.mipmap.arbitrament_ic_progress_canceld;
                    }
                });

                mView.showProgressList(list);

                mView.addFooterTips("申请纸质裁决书", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }, 2000);
    }
}
