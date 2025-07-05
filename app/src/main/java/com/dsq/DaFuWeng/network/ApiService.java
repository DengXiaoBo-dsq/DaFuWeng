package com.dsq.DaFuWeng.network;

import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.LotteryActivity;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.model.UserParticipation;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // 修改login方法参数为String
    @FormUrlEncoded
    @POST("api/user/login")
    Call<ApiResponse<User>> login(
            @Field("phone") String phone,
            @Field("password") String password
    );

    // 修改register方法参数
    @FormUrlEncoded
    @POST("api/user/register")
    Call<ApiResponse<User>> register(
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("verifyCode") String verifyCode
    );

    @FormUrlEncoded
    @POST("api/user/info")
    Call<ApiResponse<User>> getUserInfo(
            @Field("token") String token
    );

    // 彩票活动相关接口
    @GET("api/lottery/active")
    Call<ApiResponse<List<LotteryActivity>>> getActiveLotteries();

    @GET("api/lottery/history")
    Call<ApiResponse<List<LotteryActivity>>> getHistoryLotteries();

    @GET("api/lottery/{id}")
    Call<ApiResponse<LotteryActivity>> getLotteryById(@Path("id") String id);

    // 用户参与相关接口
    @POST("api/participation/join")
    Call<ApiResponse<UserParticipation>> joinLottery(@Header("Authorization") String token, @Body UserParticipation participation);

    @GET("api/participation/user/{userId}")
    Call<ApiResponse<List<UserParticipation>>> getUserParticipations(@Header("Authorization") String token, @Path("userId") String userId);

    // 用户账户相关接口
    @GET("api/user/{id}")
    Call<ApiResponse<User>> getUserById(@Header("Authorization") String token, @Path("id") String id);

    @POST("api/user/recharge")
    Call<ApiResponse<Double>> recharge(@Header("Authorization") String token, @Body Double amount);

    @POST("api/user/withdraw")
    Call<ApiResponse<Double>> withdraw(@Header("Authorization") String token, @Body Double amount);

    @GET("api/participation/list")
    Call<ApiResponse<List<UserParticipation>>> getParticipationList();

    @FormUrlEncoded
    @POST("api/user/sendVerifyCode")
    Call<ApiResponse<Void>> getVerifyCode(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/user/resetPassword")
    Call<ApiResponse<Void>> resetPassword(
            @Field("phone") String phone,
            @Field("newPassword") String newPassword,
            @Field("verifyCode") String verifyCode
    );
}    