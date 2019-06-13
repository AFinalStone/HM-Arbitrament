package com.hm.arbitrament.business.apply.view;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.tools.StatusBarUtil;
import com.hm.iou.uikit.HMBottomBarView;

import butterknife.BindView;

/**
 * 仲裁服务协议
 *
 * @param <T>
 */
public class ArbitramentServerAgreementActivity<T extends MvpActivityPresenter> extends BaseActivity<T> {

    public static final String EXTRA_KEY_URL = "url";
    private String mUrl;

    @BindView(R2.id.view_statusbar_placeholder)
    View mViewStatusBarPlaceHolder;
    @BindView(R2.id.wv_pdf)
    WebView mWvPdf;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;


    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_arbirament_server_agreement;
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
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewStatusBarPlaceHolder.getLayoutParams();
            params.height = statusBarHeight;
            mViewStatusBarPlaceHolder.setLayoutParams(params);
        }
        initWebView();
        //
        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_URL, mUrl);
    }


    private void initWebView() {
        WebSettings settings = mWvPdf.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        mWvPdf.setWebViewClient(new WebViewClient());

        String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + ";HMAndroidWebView");
        //文件内容
        if (!TextUtils.isEmpty(mUrl)) {
            Uri uri = Uri.parse(mUrl);
            String path = uri.getPath();
            if (path.endsWith(".pdf")) {
                //如果是 pdf 文件地址，则用加载pdf的方式来打开
                mWvPdf.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + Uri.encode(mUrl));
            } else {
                //如果是普通文件地址，则直接用WebView加载
                mWvPdf.loadUrl(mUrl);
            }
        }
    }
}
