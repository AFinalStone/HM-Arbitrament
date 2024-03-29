package com.hm.arbitrament.business.apply.view;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.R;
import com.hm.arbitrament.R2;
import com.hm.arbitrament.business.apply.FiveAdvantageContract;
import com.hm.arbitrament.business.apply.presenter.FiveAdvantagePresenter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.router.Router;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 申请仲裁5大优点
 */
public class FiveAdvantageActivity extends BaseActivity<FiveAdvantagePresenter> implements FiveAdvantageContract.View {

    public static final String EXTRA_KEY_IOU_ID = "iou_id";
    public static final String EXTRA_KEY_JUST_ID = "just_id";
    public static final String EXTRA_KEY_IS_CAN_ARB = "is_can_arb";

    private String mIouId;
    private String mJustId;
    private String mIsCanArb;

    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.btn_ok)
    Button mBtnOk;

    @Override
    protected int getLayoutId() {
        return R.layout.arbitrament_activity_five_advantage;
    }

    @Override
    protected FiveAdvantagePresenter initPresenter() {
        return new FiveAdvantagePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {

        mIouId = getIntent().getStringExtra(EXTRA_KEY_IOU_ID);
        mJustId = getIntent().getStringExtra(EXTRA_KEY_JUST_ID);
        mIsCanArb = getIntent().getStringExtra(EXTRA_KEY_IS_CAN_ARB);
        if (bundle != null) {
            mIouId = bundle.getString(EXTRA_KEY_IOU_ID);
            mJustId = bundle.getString(EXTRA_KEY_JUST_ID);
            mIsCanArb = bundle.getString(EXTRA_KEY_IS_CAN_ARB);
        }
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                NavigationHelper.toNeedKnowByArbitrament(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        if ("false".equals(mIsCanArb)) {
            mBtnOk.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_IOU_ID, mIouId);
        outState.putString(EXTRA_KEY_JUST_ID, mJustId);
        outState.putString(EXTRA_KEY_IS_CAN_ARB, mIsCanArb);
    }

    @OnClick(R2.id.btn_ok)
    public void onClick() {
        mPresenter.checkArbitramentApplyStatus(mIouId, mJustId);
    }

    @Override
    public void showKnowDialog(String msg) {
        new HMAlertDialog.Builder(mContext)
                .setTitle("温馨提示")
                .setMessage(msg)
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButton("知道了")
                .create()
                .show();
    }

    @Override
    public void showNeedUploadElecEvidenceDialog(String msg) {
        new HMAlertDialog.Builder(mContext)
                .setTitle("汇款凭证")
                .setMessage(msg)
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButton("如何上传")
                .setNegativeButton("稍后上传")
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        NavigationHelper.toUploadEvidence(mContext);
                    }

                    @Override
                    public void onNegClick() {

                    }
                })
                .create()
                .show();
    }

    @Override
    public void showNeedUpdateIDCardDialog() {
        new HMAlertDialog.Builder(mContext)
                .setTitle("温馨提示")
                .setMessage("您的身份证有效期不足一个月，为保证仲裁顺利进行，请先更新您的身份证信息")
                .setPositiveButton("马上更新")
                .setNegativeButton("稍后更新")
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/facecheck/update_idcard").navigation(mContext);
                    }

                    @Override
                    public void onNegClick() {
                    }
                })
                .create()
                .show();
    }
}
