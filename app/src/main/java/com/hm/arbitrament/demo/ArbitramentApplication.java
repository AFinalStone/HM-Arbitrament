package com.hm.arbitrament.demo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.logger.Logger;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.network.HttpRequestConfig;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;

/**
 * @author syl
 * @time 2019/4/4 2:08 PM
 */
public class ArbitramentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(this, true);
        Router.init(this);
        BaseBizAppLike appLike = new BaseBizAppLike();
        appLike.onCreate(this);
//        appLike.initServer("http:dev.54jietiao.com", "http:dev.54jietiao.com",
//                "http:dev.54jietiao.com");
//        appLike.initServer("http://re.54jietiao.com", "http://re.54jietiao.com",
//                "http://re.54jietiao.com");
        appLike.initServer("http://192.168.1.107:3000", "http://192.168.1.107:3000",
                "http://192.168.1.107:3000");
        initNetwork();
    }


    /**
     * 分包
     */
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


    private void initNetwork() {
        System.out.println("init-----------");
        HttpRequestConfig config = new HttpRequestConfig.Builder(this)
                .setDebug(true)
                .setAppChannel("guanfang")
                .setAppVersion("1.0.2")
                .setDeviceId("123abc123")
                .setBaseUrl(BaseBizAppLike.getInstance().getApiServer())
                .setUserId(UserManager.getInstance(this).getUserId())
                .setToken(UserManager.getInstance(this).getToken())
                .build();
        HttpReqManager.init(config);
    }


}