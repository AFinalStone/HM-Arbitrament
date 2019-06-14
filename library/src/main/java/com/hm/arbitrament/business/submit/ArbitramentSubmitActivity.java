package com.hm.arbitrament.business.submit;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.tools.KeyboardUtil;
import com.hm.iou.tools.StatusBarUtil;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.HMCountDownTextView;
import com.hm.iou.uikit.dialog.HMActionSheetDialog;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class ArbitramentSubmitActivity extends BaseActivity<ArbitramentSubmitPresenter> implements ArbitramentSubmitContract.View {

    @BindView(R2.id.wv_pdf)
    WebView mWebView;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBarView;

    private Dialog mCancelDialog;
    private HMAlertDialog mVerifyCodeDialog;
    private EditText mEtCode;
    private HMCountDownTextView mCountDownView;

    private String mPdfUrl = "http://www.baidu.com";
    private String mIouId;
    private String mJusticeId;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_arbitrament_submit;
    }

    @Override
    protected ArbitramentSubmitPresenter initPresenter() {
        return new ArbitramentSubmitPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        initWebView();
        mBottomBarView.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                showVerifyCodeDialog();
            }
        });
        mBottomBarView.setOnTitleIconClickListener(new HMBottomBarView.OnTitleIconClickListener() {
            @Override
            public void onClickIcon() {
                showMoreActionSheet();
            }
        });

        Uri uri = Uri.parse(mPdfUrl);
        String path = uri.getPath();
        if (path.endsWith(".pdf")) {
            //如果是 pdf 文件地址，则用加载pdf的方式来打开
            mWebView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + Uri.encode(mPdfUrl));
        } else {
            //如果是普通文件地址，则直接用WebView加载
            mWebView.loadUrl(mPdfUrl);
        }
    }

    protected void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mWebView.getLayoutParams();
        params.topMargin = StatusBarUtil.getStatusBarHeight(this);
        mWebView.setLayoutParams(params);

        String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + ";HMAndroidWebView");
    }

    private void showMoreActionSheet() {
        if (mCancelDialog == null) {
            List<String> list = new ArrayList<>();
            list.add("取消仲裁");
            mCancelDialog = new HMActionSheetDialog.Builder(this)
                    .setActionSheetList(list)
                    .setCanSelected(false)
                    .setOnItemClickListener(new HMActionSheetDialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(int i, String s) {
                            showCancelConfirmDialog();
                        }
                    })
                    .create();
        }
        mCancelDialog.show();
    }

    private void showCancelConfirmDialog() {
        new HMAlertDialog.Builder(this)
                .setTitle("取消仲裁")
                .setMessage("取消仲裁后10天内将不能再次申请，是否确定取消仲裁？")
                .setPositiveButton("继续仲裁")
                .setNegativeButton("继续取消")
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {

                    }

                    @Override
                    public void onNegClick() {
                        mPresenter.cancelArbitrament(mIouId, mJusticeId);
                    }
                })
                .create().show();
    }

    private void showVerifyCodeDialog() {
        if (mVerifyCodeDialog == null) {
            View contentView = getLayoutInflater().inflate(R.layout.arbitrament_dialog_verify_code, null);
            mVerifyCodeDialog = new HMAlertDialog.Builder(this)
                    .setTitle("填写验证码")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setPositiveButton("下一步")
                    .setNegativeButton("取消")
                    .enablePostButton(false)
                    .setCustomView(contentView)
                    .setOnClickListener(new HMAlertDialog.OnClickListener() {
                        @Override
                        public void onPosClick() {

                        }

                        @Override
                        public void onNegClick() {

                        }
                    })
                    .create();
            initVerifyCodeContentView(contentView);
        }
        mVerifyCodeDialog.show();
        KeyboardUtil.toggleKeyboard(this);
    }

    private void initVerifyCodeContentView(View contentView) {
        TextView tvMsg = contentView.findViewById(R.id.tv_dialog_msg);
        mEtCode = contentView.findViewById(R.id.et_dialog_code);
        mCountDownView = contentView.findViewById(R.id.tv_dialog_countdown);
        String mobile = UserManager.getInstance(this).getUserInfo().getMobile();
        if (mobile != null && mobile.length() >= 3)
            mobile = mobile.substring(0, 3) + "****";
        tvMsg.setText(String.format("为确保账户资金安全，请输入%s收到的验证码", mobile));
        mCountDownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.sendVerifyCode();
            }
        });
        RxTextView.textChanges(mEtCode).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence code) throws Exception {
                if (mVerifyCodeDialog == null)
                    return;
                if (TextUtils.isEmpty(code) || code.length() < 4) {
                    mVerifyCodeDialog.enablePosButton(false);
                } else {
                    mVerifyCodeDialog.enablePosButton(true);
                }
            }
        });
    }

    @Override
    public void startCountDown() {
        if (mCountDownView != null)
            mCountDownView.startCountDown();
        KeyboardUtil.showKeyboard(mEtCode);
    }
}