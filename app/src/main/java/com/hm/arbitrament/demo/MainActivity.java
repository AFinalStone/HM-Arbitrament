package com.hm.arbitrament.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hm.arbitrament.business.apply.view.FiveAdvantageActivity;
import com.hm.arbitrament.business.apply.view.InputCollectionProveActivity;
import com.hm.arbitrament.business.award.ArbitralAwardActivity;
import com.hm.arbitrament.business.pay.applybook.ArbApplyBookPayActivity;
import com.hm.arbitrament.business.progress.view.ArbitramentProgressActivity;
import com.hm.arbitrament.NavigationHelper;
import com.hm.arbitrament.business.apply.view.FiveAdvantageActivity;
import com.hm.arbitrament.business.apply.view.InputCollectionProveActivity;
import com.hm.arbitrament.business.award.ArbitralAwardActivity;
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
        findViewById(R.id.btn_pay_arb_apply_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArbApplyBookPayActivity.class);
                intent.putExtra(ArbApplyBookPayActivity.EXTRA_KEY_IOU_ID, "e67ff445193545bab4b44d14e8eb5705");
                intent.putExtra(ArbApplyBookPayActivity.EXTRA_KEY_JUST_ID, "190523143827000448");
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_index).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Router.getInstance()
//                        .buildWithUrl("hmiou://m.54jietiao.com/arbitrament/index")
//                        .withString("iou_id", "d5a6ae1ebcc5431da75c1c17d9d75527")
//                        .withString("just_id", "190523143827000448")
//                        .withString("is_borrower", "0")
//                        .navigation(MainActivity.this);
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/arbitrament/index")
                        .withString("iou_id", "4c4e721d68c54d879b5d5c763e188bc9")
                        .withString("just_id", "190227170537000385")
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

    private void login() {
        String pwd = MD5.hexdigest("123456".getBytes());
        MobileLoginReqBean reqBean = new MobileLoginReqBean();
//        reqBean.setMobile("13186975702");
        reqBean.setMobile("15967132742");
//        reqBean.setMobile("15267163669");
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
