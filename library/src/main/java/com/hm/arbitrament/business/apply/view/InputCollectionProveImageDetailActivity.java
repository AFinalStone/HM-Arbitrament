package com.hm.arbitrament.business.apply.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.github.chrisbanes.photoview.PhotoView;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.tools.StatusBarUtil;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.dialog.HMActionSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 催收证明
 */
public class InputCollectionProveImageDetailActivity<T extends MvpActivityPresenter> extends BaseActivity<T> {

    public static final String EXTRA_KEY_URL = "url";

    @BindView(R2.id.view_statusbar_placeholder)
    View mViewStatusbarPlaceholder;
    @BindView(R2.id.iv_photo)
    PhotoView mIvPhoto;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;

    private String mUrl;
    private HMActionSheetDialog mBottomDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_input_collection_prove_image_detail;
    }

    @Override
    protected T initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mUrl = getIntent().getStringExtra(EXTRA_KEY_URL);
        if (bundle != null) {
            mUrl = bundle.getString(EXTRA_KEY_URL);
        }
        //初始化View
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
        if (statusBarHeight > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewStatusbarPlaceholder.getLayoutParams();
            params.height = statusBarHeight;
            mViewStatusbarPlaceholder.setLayoutParams(params);
        }

        ImageLoader.getInstance(mContext).displayImage(mUrl, mIvPhoto);
        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                if (mBottomDialog == null) {
                    List<String> list = new ArrayList<>();
                    list.add("删除");
                    list.add("取消");
                    mBottomDialog = new HMActionSheetDialog
                            .Builder(mContext)
                            .setActionSheetList(list)
                            .setOnItemClickListener(new HMActionSheetDialog.OnItemClickListener() {
                                @Override
                                public void onItemClick(int i, String s) {
                                    if (0 == i) {
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        mBottomDialog.dismiss();
                                    }
                                }
                            }).create();
                }
                mBottomDialog.show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_URL, mUrl);
    }
}
