package com.dsq.DaFuWeng.network;

import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.LotteryActivity;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.model.UserParticipation;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // 登录
    @POST("api/auth/login")
    Call<ApiResponse<HashMap<String, Object>>> login(
            @Body HashMap<String, String> request
    );

    // 注册
    @POST("api/auth/register")
    Call<ApiResponse<HashMap<String, Object>>> register(
            @Body HashMap<String, String> request
    );

    // 发送验证码
    @GET("api/auth/send-code")
    Call<ApiResponse<HashMap<String, Object>>> sendVerificationCode(
            @Query("phone") String phone,
            @Query("type") Integer type
    );

    // 重置密码
    @POST("api/auth/reset-password")
    Call<ApiResponse<HashMap<String, Object>>> resetPassword(
            @Body HashMap<String, String> request
    );

    // 获取今日活动
    @GET("api/lottery/today")
    Call<ApiResponse<HashMap<String, Object>>> getTodayActivities();

    // 参与活动
    @POST("api/lottery/participate")
    Call<ApiResponse<HashMap<String, Object>>> participate(
            @Query("userId") Long userId,
            @Query("activityId") Long activityId,
            @Query("playType") Integer playType,
            @Query("level") Integer level
    );

    // 充值接口（匹配服务端：需要userId和amount）
    @POST("api/lottery/recharge")
    Call<ApiResponse<HashMap<String, Object>>> recharge(
            @Query("userId") Long userId,  // 服务端需要的用户ID（Long类型）
            @Query("amount") BigDecimal amount  // 服务端需要的金额（BigDecimal类型）
    );

    // 获取用户参与记录（匹配服务端接口）
    @GET("api/lottery/participations/user")
    Call<ApiResponse<HashMap<String, Object>>> getUserParticipations(
            @Query("userId") Long userId  // 服务端必需的用户ID参数
    );
    // 新增提现接口（仿照充值接口格式）
    @POST("api/lottery/withdraw")
    Call<ApiResponse<HashMap<String, Object>>> withdraw(
            @Query("userId") Long userId,
            @Query("amount") BigDecimal amount
    );
    // 获取用户信息接口
    @POST("api/auth/user/info")
    Call<ApiResponse<HashMap<String, Object>>> getUserInfo(
            @Query("userId") Long userId
    );



}