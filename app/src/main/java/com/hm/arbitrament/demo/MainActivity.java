package com.hm.arbitrament.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.business.apply.view.ArbitramentServerAgreementActivity;
import com.hm.arbitrament.business.apply.view.FiveAdvantageActivity;
import com.hm.arbitrament.business.apply.view.InputCollectionProveActivity;
import com.hm.arbitrament.business.award.ArbitralAwardActivity;
import com.hm.arbitrament.business.pay.applybook.ArbApplyBookPayActivity;
import com.hm.arbitrament.business.pay.applysubmit.ArbApplySubmitPayActivity;
import com.hm.arbitrament.business.progress.view.MoneyBackProgressActivity;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.ToastUtil;
import com.sina.weibo.sdk.utils.MD5;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.btn_five_advantage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FiveAdvantageActivity.class);
                intent.putExtra(FiveAdvantageActivity.EXTRA_KEY_IOU_ID, "e67ff445193545bab4b44d14e8eb5705");
                intent.putExtra(FiveAdvantageActivity.EXTRA_KEY_JUST_ID, "190523143827000448");
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_pay_arb_apply_book_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArbApplyBookPayActivity.class);
                intent.putExtra(ArbApplyBookPayActivity.EXTRA_KEY_IOU_ID, "a21726118d7b4ff181a8b1b60ecfbe01");
                intent.putExtra(ArbApplyBookPayActivity.EXTRA_KEY_JUST_ID, "190618204729000443");
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_pay_arb_apply_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArbApplySubmitPayActivity.class);
                intent.putExtra(ArbApplySubmitPayActivity.EXTRA_KEY_ARB_NO, "a21726118d7b4ff181a8b1b60ecfbe01");
                intent.putExtra(ArbApplySubmitPayActivity.EXTRA_KEY_MESSAGE_CODE, "190618204729000443");
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_index).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/arbitrament/index")
                        .withString("iou_id", "a21726118d7b4ff181a8b1b60ecfbe01")
                        .withString("just_id", "190618204729000443")
                        .navigation(MainActivity.this);
            }
        });
        findViewById(R.id.btn_collection_prove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InputCollectionProveActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
        findViewById(R.id.btn_arb_server_agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArbitramentServerAgreementActivity.class);
                intent.putExtra(ArbitramentServerAgreementActivity.EXTRA_KEY_URL, "http://filetest.arbexpress.cn/150217103521/15559023585432/1560936440729/0D9DC1914A90FA29BCDD680F3BDBD011.pdf");
                startActivity(intent);
            }
        });
    }

    public void toProgressPage(View v) {
        NavigationHelper.toArbitramentProgressPage(this, "123456");
    }

    public void toBackMoneyPage(View v) {
        startActivity(new Intent(this, MoneyBackProgressActivity.class));
    }

    public void toAwardPage(View v) {
        startActivity(new Intent(this, ArbitralAwardActivity.class));
    }

    public void arbitramentSubmit(View v) {
        NavigationHelper.toArbitramentSubmitPage(this, "123456");
    }

    private void test() {
        HttpReqManager.getInstance().getService(LoginService.class)
                .testUpdateStauts("", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<Object>>() {
                    @Override
                    public void accept(BaseResponse<Object> objectBaseResponse) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private void login() {
        String pwd = MD5.hexdigest("123456".getBytes());
        MobileLoginReqBean reqBean = new MobileLoginReqBean();
//        reqBean.setMobile("13186975702");
//        reqBean.setMobile("15967132742");
        reqBean.setMobile("15267163669");
//        reqBean.setMobile("18337150117");

        reqBean.setQueryPswd(pwd);
        HttpReqManager.getInstance().getService(LoginService.class)
                .mobileLogin(reqBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<UserInfo>>() {
                    @Override
                    public void accept(BaseResponse<UserInfo> userInfoBaseResponse) throws Exception {
                        if (userInfoBaseResponse.getErrorCode() != 0) {
                            ToastUtil.showMessage(MainActivity.this, userInfoBaseResponse.getMessage());
                            return;
                        }
                        ToastUtil.showMessage(MainActivity.this, "登录成功");
                        UserInfo userInfo = userInfoBaseResponse.getData();
                        UserManager.getInstance(MainActivity.this).updateOrSaveUserInfo(userInfo);
                        HttpReqManager.getInstance().setUserId(userInfo.getUserId());
                        HttpReqManager.getInstance().setToken(userInfo.getToken());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
                        t.printStackTrace();
                    }
                });
    }

}
