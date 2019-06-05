package com.hm.arbitrament.api;

import com.hm.iou.network.HttpReqManager;

/**
 * Created by syl on 2019/6/5.
 */

public class ArbitramentApi {

    private static ArbitramentServer getService() {
        return HttpReqManager.getInstance().getService(ArbitramentServer.class);
    }

}
