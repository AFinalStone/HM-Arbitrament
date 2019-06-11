package com.hm.arbitrament.business.apply.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.github.chrisbanes.photoview.PhotoView;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.bean.IOUExtResult;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.tools.StatusBarUtil;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.uikit.HMBottomBarView;

import butterknife.BindView;

/**
 * 有效凭证
 *
 * @param <T>
 */
public class SelectValidEvidenceDetailActivity<T extends MvpActivityPresenter> extends BaseActivity<T> {

    public static final String EXTRA_KEY_ITEM = "item";
    public static final String EXTRA_KEY_IS_SELECT = "is_select";
    private static final int FILE_TYPE_IMAGE = 1;
    private static final int FILE_TYPE_PDF = 2;

    @BindView(R2.id.view_statusbar_placeholder)
    View mViewStatusBarPlaceHolder;
    @BindView(R2.id.viewStub_detail_image)
    ViewStub mViewStubDetailImage;
    @BindView(R2.id.viewStub_detail_pdf)
    ViewStub mViewStubDetailPdf;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;

    private IOUExtResult.ExtEvidence mItem;
    private boolean mIsSelect;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_select_valid_evidence_detail;
    }

    @Override
    protected T initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mItem = getIntent().getParcelableExtra(EXTRA_KEY_ITEM);
        mIsSelect = getIntent().getBooleanExtra(EXTRA_KEY_IS_SELECT, false);
        if (bundle != null) {
            mItem = bundle.getParcelable(EXTRA_KEY_ITEM);
            mIsSelect = bundle.getBoolean(EXTRA_KEY_IS_SELECT, false);
        }
        if (mItem == null) {
            onBackPressed();
            return;
        }
        //初始化View
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
        if (statusBarHeight > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewStatusBarPlaceHolder.getLayoutParams();
            params.height = statusBarHeight;
            mViewStatusBarPlaceHolder.setLayoutParams(params);
        }
        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                mIsSelect = !mIsSelect;
                if (mIsSelect) {
                    mBottomBar.setTitleIconDrawable(R.mipmap.uikit_icon_check_black);
                } else {
                    mBottomBar.setTitleIconDrawable(R.mipmap.uikit_icon_check_default);
                }
            }
        });
        //是否选中
        if (mIsSelect) {
            mBottomBar.setTitleIconDrawable(R.mipmap.uikit_icon_check_black);
        } else {
            mBottomBar.setTitleIconDrawable(R.mipmap.uikit_icon_check_default);
        }
        //名称
        String name = mItem.getName();
        mBottomBar.updateBackText(StringUtil.getUnnullString(name));
        int fileType = mItem.getFileType();
        if (FILE_TYPE_IMAGE == fileType) {
            initDetailImageView();
        } else if (FILE_TYPE_PDF == fileType) {
            initDetailPDFView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_KEY_ITEM, mItem);
        outState.putBoolean(EXTRA_KEY_IS_SELECT, mIsSelect);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        int flag = mIsSelect ? RESULT_OK : RESULT_CANCELED;
        setResult(flag, intent);
        finish();
    }

    private void initDetailImageView() {
        View view = mViewStubDetailImage.inflate();
        PhotoView pv = view.findViewById(R.id.iv_photo);
        //文件内容
        String url = mItem.getUrl();
        ImageLoader.getInstance(mContext).displayImage(url, pv);
    }

    private void initDetailPDFView() {
        View view = mViewStubDetailPdf.inflate();
        WebView wv = view.findViewById(R.id.wv_pdf);
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        wv.setWebViewClient(new WebViewClient());

        String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + ";HMAndroidWebView");
        //文件内容
        String url = mItem.getUrl();
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            String path = uri.getPath();
            if (path.endsWith(".pdf")) {
                //如果是 pdf 文件地址，则用加载pdf的方式来打开
                wv.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + Uri.encode(url));
            } else {
                //如果是普通文件地址，则直接用WebView加载
                wv.loadUrl(url);
            }
        }
    }

}
