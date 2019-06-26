package com.hm.arbitrament.business;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.arbitrament.R;
import com.hm.iou.tools.KeyboardUtil;
import com.hm.iou.tools.ToastUtil;
import com.hm.iou.uikit.dialog.HMAlertDialog;

public class CancelArbDialog {

    public interface OnCancelArbListener {
        /**
         * 确认取消
         *
         * @param index  0-已和解，1-已履行，2-其他
         * @param reason 取消原因
         */
        void onCanceled(int index, String reason);
    }

    private Context mContext;
    private ImageView mIvOption1;
    private ImageView mIvOption2;
    private ImageView mIvOption3;
    private EditText mEtReason;
    private TextView mTvBottomTip;

    private int mSelected = -1;
    private HMAlertDialog mDialog;
    private OnCancelArbListener mListener;
    private boolean mIsCanRestartArbNextTenDay = false;

    public CancelArbDialog(Context context) {
        mContext = context;
    }

    public void setOnCancelArbListener(OnCancelArbListener listener) {
        mListener = listener;
    }

    /**
     * 未来10天是否可以重新申请仲裁
     */
    public void setCanRestartArbNextTenDay(boolean isCan) {
        mIsCanRestartArbNextTenDay = isCan;
    }

    public void show() {
        if (mDialog != null) {
            mDialog.show();
            return;
        }
        final View contentView = LayoutInflater.from(mContext).inflate(R.layout.arbitrament_dialog_cancel_arb, null);
        mIvOption1 = contentView.findViewById(R.id.iv_cancel_option1);
        mIvOption2 = contentView.findViewById(R.id.iv_cancel_option2);
        mIvOption3 = contentView.findViewById(R.id.iv_cancel_option3);
        mEtReason = contentView.findViewById(R.id.et_cancel_reason);
        mTvBottomTip = contentView.findViewById(R.id.tv_bottom_tip);
        if (mIsCanRestartArbNextTenDay) {
            mTvBottomTip.setText("请选择取消仲裁原因");
        } else {
            mTvBottomTip.setText("取消仲裁后10天内将不能再次申请，是否确定取消仲裁？");
        }

        contentView.findViewById(R.id.ll_cancel_option1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelected = 0;
                updateCheckState();
                mEtReason.setVisibility(View.GONE);
                hideSoftInput(mEtReason);
            }
        });
        contentView.findViewById(R.id.ll_cancel_option2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelected = 1;
                updateCheckState();
                mEtReason.setVisibility(View.GONE);
                hideSoftInput(mEtReason);
            }
        });
        contentView.findViewById(R.id.ll_cancel_option3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelected = 2;
                updateCheckState();
                mEtReason.setVisibility(View.VISIBLE);
                mEtReason.requestFocus();
                KeyboardUtil.showKeyboard(mEtReason);
            }
        });

        mDialog = new HMAlertDialog.Builder(mContext)
                .setTitle("取消仲裁")
                .setCustomView(contentView)
                .setPositiveButton("继续仲裁")
                .setNegativeButton("继续取消")
                .setDismessedOnClickBtn(false)
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        mDialog.dismiss();
                    }

                    @Override
                    public void onNegClick() {
                        if (mSelected == -1) {
                            ToastUtil.showMessage(mContext, "请选择取消原因");
                            return;
                        } else if (mSelected == 2) {
                            if (mEtReason.getText().toString().isEmpty()) {
                                ToastUtil.showMessage(mContext, "请输入取消原因");
                                return;
                            }
                        }
                        if (mListener != null) {
                            mListener.onCanceled(mSelected, mEtReason.getText().toString());
                            mDialog.dismiss();
                        }
                    }
                })
                .create();
        mDialog.show();
    }

    private void updateCheckState() {
        mIvOption1.setImageResource(R.mipmap.uikit_icon_check_default);
        mIvOption2.setImageResource(R.mipmap.uikit_icon_check_default);
        mIvOption3.setImageResource(R.mipmap.uikit_icon_check_default);
        if (mSelected == 0) {
            mIvOption1.setImageResource(R.mipmap.uikit_icon_check_black);
        } else if (mSelected == 1) {
            mIvOption2.setImageResource(R.mipmap.uikit_icon_check_black);
        } else if (mSelected == 2) {
            mIvOption3.setImageResource(R.mipmap.uikit_icon_check_black);
        }
    }

    private void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
