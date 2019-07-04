package com.hm.arbitrament.business.submit;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.business.CancelArbDialog;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.logger.Logger;
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

    public static final String EXTRA_KEY_ARB_NO = "arb_no";
    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUSTICE_ID = "justice_id";
    public static final String EXTRA_KEY_IS_APPLY_PERSON = "is_apply_person";

    @BindView(R2.id.view_statusbar_placeholder)
    View mViewStatusBarPlaceHolder;
    @BindView(R2.id.wv_pdf)
    WebView mWebView;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBarView;

    private Dialog mCancelDialog;
    private CancelArbDialog mCancelArbDialog;

    private HMAlertDialog mVerifyCodeDialog;
    private EditText mEtCode;
    private HMCountDownTextView mCountDownView;

    private String mArbNo;
    private String mIouId;
    private String mJusticeId;
    private String mIsApplyPerson;

    private String mPdfUrl;

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
        mArbNo = getIntent().getStringExtra(EXTRA_KEY_ARB_NO);
        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJusticeId = getIntent().getStringExtra(EXTRA_KEY_JUSTICE_ID);
        mIsApplyPerson = getIntent().getStringExtra(EXTRA_KEY_IS_APPLY_PERSON);
        if (bundle != null) {
            mArbNo = bundle.getString(EXTRA_KEY_ARB_NO);
            mIouId = bundle.getString(EXTRA_KEY_IOU_ID);
            mJusticeId = bundle.getString(EXTRA_KEY_JUSTICE_ID);
            mIsApplyPerson = bundle.getString(EXTRA_KEY_IS_APPLY_PERSON);
        }
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
        if (statusBarHeight > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewStatusBarPlaceHolder.getLayoutParams();
            params.height = statusBarHeight;
            mViewStatusBarPlaceHolder.setLayoutParams(params);
        }
        initWebView();
        mBottomBarView.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                if (checkIsApplyPerson()) {
                    showVerifyCodeDialog();
                }
            }
        });
        mBottomBarView.setOnTitleIconClickListener(new HMBottomBarView.OnTitleIconClickListener() {
            @Override
            public void onClickIcon() {
                showMoreActionSheet();
            }
        });

        mPresenter.getArbApplyDoc(mArbNo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_ARB_NO, mArbNo);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUSTICE_ID, mJusticeId);
        outState.putString(EXTRA_KEY_IS_APPLY_PERSON, mIsApplyPerson);
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

        String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + ";HMAndroidWebView");
    }

    /**
     * 检验是否是仲裁申请账号
     *
     * @return
     */
    public boolean checkIsApplyPerson() {
        if ("true".equals(mIsApplyPerson)) {
            return true;
        }
        new HMAlertDialog
                .Builder(mContext)
                .setTitle("温馨提示")
                .setMessage("请使用仲裁申请账号进行操作")
                .setPositiveButton("知道了")
                .create()
                .show();
        return false;
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
                            if (checkIsApplyPerson()) {
                                if (mCancelArbDialog == null) {
                                    mCancelArbDialog = new CancelArbDialog(ArbitramentSubmitActivity.this);
                                    mCancelArbDialog.setOnCancelArbListener(new CancelArbDialog.OnCancelArbListener() {
                                        @Override
                                        public void onCanceled(int index, String reason) {
                                            int type = 0;
                                            if (index == 0) {
                                                type = 2;
                                            } else if (index == 1) {
                                                type = 1;
                                            } else if (index == 2) {
                                                type = 3;
                                            }
                                            mPresenter.cancelArbitrament(mArbNo, type, reason);
                                        }
                                    });
                                }
                                mCancelArbDialog.show();
                            }
                        }
                    })
                    .create();
        }
        mCancelDialog.show();
    }

    private void showVerifyCodeDialog() {
        if (mVerifyCodeDialog == null) {
            View contentView = getLayoutInflater().inflate(R.layout.arbitrament_dialog_verify_code, null);
            mVerifyCodeDialog = new HMAlertDialog.Builder(this)
                    .setTitle("安全验证")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setPositiveButton("下一步")
                    .setNegativeButton("取消")
                    .enablePostButton(false)
                    .setCustomView(contentView)
                    .setOnClickListener(new HMAlertDialog.OnClickListener() {
                        @Override
                        public void onPosClick() {
                            mPresenter.verifySmsCode(mEtCode.getText().toString());
                        }

                        @Override
                        public void onNegClick() {

                        }
                    })
                    .create();
            initVerifyCodeContentView(contentView);
        }
        if (mEtCode != null) {
            mEtCode.setText("");
        }
        if (mCountDownView != null) {
            mCountDownView.resume();
        }
        mVerifyCodeDialog.show();
        KeyboardUtil.toggleKeyboard(this);
    }

    private void initVerifyCodeContentView(View contentView) {
        TextView tvMsg = contentView.findViewById(R.id.tv_dialog_msg);
        mEtCode = contentView.findViewById(R.id.et_dialog_code);
        mCountDownView = contentView.findViewById(R.id.tv_dialog_countdown);
        String mobile = UserManager.getInstance(this).getUserInfo().getMobile();
        if (mobile != null && mobile.length() >= 4)
            mobile = mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
        tvMsg.setText(String.format("为确保账户安全，请输入%s收到的验证码", mobile));
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

    @Override
    public void showArbApplyDoc(String pdfUrl) {
        Logger.d("pdfurl == " + pdfUrl);
        mPdfUrl = pdfUrl;
        //文件内容
        if (!TextUtils.isEmpty(mPdfUrl)) {
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
    }

    @Override
    public void toPay(String code) {
        NavigationHelper.toArbApplyPayPage(this, mIouId, mJusticeId, mArbNo, code);
    }
}