package com.hm.arbitrament.business.apply.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.bean.BackMoneyRecordBean;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.comm.HMTextChangeListener;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.datepicker.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;

/**
 * 催收证明
 */
public class InputRealBackMoneyAddRecordActivity<T extends MvpActivityPresenter> extends BaseActivity<T> {

    public static final String EXTRA_KEY_ITEM = "item";
    public static final String EXTRA_KEY_MAX_BACK_MONEY = "max_back_money";
    public static final String EXTRA_KEY_BACK_TIME_START_TIME = "back_time_start_time";


    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.et_money)
    EditText mEvMoney;
    @BindView(R2.id.tv_time)
    TextView mTvTime;
    @BindView(R2.id.bottomBar)
    HMBottomBarView mBottomBar;

    private BackMoneyRecordBean mItem;
    private Dialog mDatePicker;

    Double mMaxBackMoney;
    String mBackTimeStartTime;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_input_real_back_money_add_record;
    }

    @Override
    protected T initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mItem = (BackMoneyRecordBean) getIntent().getSerializableExtra(EXTRA_KEY_ITEM);
        mMaxBackMoney = getIntent().getDoubleExtra(EXTRA_KEY_MAX_BACK_MONEY, -1);
        mBackTimeStartTime = getIntent().getStringExtra(EXTRA_KEY_BACK_TIME_START_TIME);
        if (bundle != null) {
            mItem = (BackMoneyRecordBean) bundle.getSerializable(EXTRA_KEY_ITEM);
            mMaxBackMoney = bundle.getDouble(EXTRA_KEY_MAX_BACK_MONEY, -1);
            mBackTimeStartTime = bundle.getString(EXTRA_KEY_BACK_TIME_START_TIME);
        }
        mBottomBar.setOnBackClickListener(new HMBottomBarView.OnBackClickListener() {
            @Override
            public void onClickBack() {
                onBackPressed();
            }
        });
        mBottomBar.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                String strBackMoney = mEvMoney.getText().toString();
                String backTime = mTvTime.getText().toString();
                backTime = backTime.replaceAll("\\.", "-") + " 00:00:00";
                if (mItem == null) {
                    mItem = new BackMoneyRecordBean();
                    mItem.setCreateTime(System.currentTimeMillis());
                }
                Double backMoney = Double.parseDouble(strBackMoney);
                mItem.setAmount(backMoney);
                mItem.setRepaymentDate(backTime);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_KEY_ITEM, mItem);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        mEvMoney.addTextChangedListener(new HMTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String strBackMoney = charSequence.toString();
                int posDot = charSequence.toString().indexOf(".");
                if (posDot != -1 && posDot < strBackMoney.length() - 3) {
                    strBackMoney = strBackMoney.substring(0, posDot + 3);
                    mEvMoney.setText(strBackMoney);
                    mEvMoney.setSelection(mEvMoney.length());
                    return;
                }
                if (mMaxBackMoney != -1) {
                    try {
                        Double backMoney = Double.parseDouble(strBackMoney);
                        if (backMoney > mMaxBackMoney) {
                            mEvMoney.setText(String.valueOf(mMaxBackMoney));
                            mEvMoney.setSelection(mEvMoney.length());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                checkValue();
            }
        });
        mTvTime.addTextChangedListener(new HMTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                checkValue();
            }
        });
        if (mItem == null) {
            showSoftKeyboard();
            return;
        }

        if (mItem.getAmount()  != null) {
            //有小数点的
            if (mItem.getAmount() - mItem.getAmount().intValue() > 0) {
                String backMoney = StringUtil.doubleToString01(mItem.getAmount());
                //还款金额
                if (!TextUtils.isEmpty(backMoney)) {
                    mEvMoney.setText(backMoney);
                    mEvMoney.setSelection(mEvMoney.length());
                }
            } else {
                //整数
                int v = mItem.getAmount().intValue();
                if (v > 0) {
                    mEvMoney.setText(v + "");
                    mEvMoney.setSelection(mEvMoney.length());
                }
            }
        }

        //还款时间
        String backTime = String.valueOf(mItem.getRepaymentDate());
        if (!TextUtils.isEmpty(backTime)) {
            backTime = backTime.replaceAll("-", "\\.");
            backTime = backTime.substring(0, 10);
            mTvTime.setText(backTime);
        }
        showSoftKeyboard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_KEY_ITEM, mItem);
        outState.putDouble(EXTRA_KEY_MAX_BACK_MONEY, mMaxBackMoney);
        outState.putString(EXTRA_KEY_BACK_TIME_START_TIME, mBackTimeStartTime);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void checkValue() {
        String strBackMoney = mEvMoney.getText().toString();
        Double backMoney = 0.0;
        try {
            backMoney = Double.parseDouble(strBackMoney);
        } catch (Exception e) {
        }
        if (backMoney <= 0) {
            mBottomBar.setEnabled(false);
            return;
        }
        if (mTvTime.length() == 0) {
            mBottomBar.setEnabled(false);
            return;
        }
        mBottomBar.setEnabled(true);
    }

    private void showDatePicker() {
        if (mDatePicker == null) {
            long now = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            calendar.setTimeInMillis(now);
            calendar.add(Calendar.YEAR, -10);
            String startTime = sdf.format(calendar.getTime());
            if (!TextUtils.isEmpty(mBackTimeStartTime)) {
                startTime = mBackTimeStartTime;
            }

            calendar.setTimeInMillis(now);
            String endTime = sdf.format(calendar.getTime());

            String value = mTvTime.getText().toString();
            if (!TextUtils.isEmpty(value)) {
                value = value.replace("\\.", "-") + " 00:00:00";
            }

            mDatePicker = new TimePickerDialog.Builder(this)
                    .setTitle("还款时间")
                    .setScrollType(TimePickerDialog.SCROLL_TYPE.DAY)
                    .setTimeRange(startTime, endTime)
                    .setSelectedTime(value)
                    .setOnPickerConfirmListener(new TimePickerDialog.OnTimeConfirmListener() {
                        @Override
                        public void onConfirm(int year, int month, int day, int i3, int i4) {
                            month++;
                            String appTime = String.format("%d.%s.%s", year, patchZero(month), patchZero(day));
                            mTvTime.setText(appTime);
                        }
                    })
                    .create();
        }
        mDatePicker.show();
    }

    private String patchZero(int m) {
        return m < 10 ? "0" + m : "" + m;
    }


}
