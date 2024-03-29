package com.hm.arbitrament.demo;

import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.UserInfo;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hjy on 2018/5/29.
 */

public interface LoginService {

    @POST("/api/iou/user/v1/mobileLogin")
    Flowable<BaseResponse<UserInfo>> mobileLogin(@Body MobileLoginReqBean mobileLoginReqBean);

    @POST("/api/arb/v1/testUpdateStauts")
    Flowable<BaseResponse<Object>> testUpdateStauts(@Query("arbApplyNo") String arbApplyNo, @Query("status") String status);

}
