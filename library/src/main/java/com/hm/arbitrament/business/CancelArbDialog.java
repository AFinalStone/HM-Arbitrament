package com.hm.arbitrament.business;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

    private int mSelected = -1;
    private HMAlertDialog mDialog;
    private OnCancelArbListener mListener;

    public CancelArbDialog(Context context) {
        mContext = context;
    }

    public void setOnCancelArbListener(OnCancelArbListener listener) {
        mListener = listener;
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

        contentView.findViewById(R.id.ll_cancel_option1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelected = 0;
                updateCheckState();
                mEtReason.setVisibility(View.GONE);
            }
        });
        contentView.findViewById(R.id.ll_cancel_option2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelected = 1;
                updateCheckState();
                mEtReason.setVisibility(View.GONE);
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

}
