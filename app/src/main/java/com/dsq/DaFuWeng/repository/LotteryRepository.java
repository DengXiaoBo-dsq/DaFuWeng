package com.dsq.DaFuWeng.repository;

import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.network.ApiService;
import com.dsq.DaFuWeng.network.RetrofitClient;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class LotteryRepository {
    private static LotteryRepository instance;
    private ApiService apiService;
    private SharedPrefsUtil sharedPrefsUtil;

    private LotteryRepository() {
        apiService = RetrofitClient.getInstance().getApiService();
        sharedPrefsUtil = SharedPrefsUtil.getInstance(RetrofitClient.getApplication());
    }

    public static LotteryRepository getInstance() {
        if (instance == null) {
            instance = new LotteryRepository();
        }
        return instance;
    }

    // 获取今日活动
    public CompletableFuture<ApiResponse<HashMap<String, Object>>> getTodayActivities() {
        CompletableFuture<ApiResponse<HashMap<String, Object>>> future = new CompletableFuture<>();
        apiService.getTodayActivities().enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("获取今日活动失败");
                    future.complete(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HashMap<String, Object>>> call, Throwable t) {
                ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("网络错误: " + t.getMessage());
                future.complete(errorResponse);
            }
        });
        return future;
    }

    // 参与活动
    public CompletableFuture<ApiResponse<HashMap<String, Object>>> participate(Long userId, Long activityId, Integer playType, Integer level) {
        CompletableFuture<ApiResponse<HashMap<String, Object>>> future = new CompletableFuture<>();
        apiService.participate(userId, activityId, playType, level).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("参与活动失败");
                    future.complete(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HashMap<String, Object>>> call, Throwable t) {
                ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("网络错误: " + t.getMessage());
                future.complete(errorResponse);
            }
        });
        return future;
    }

    // 获取用户参与记录
    public CompletableFuture<ApiResponse<HashMap<String, Object>>> getUserParticipations(Long userId) {
        CompletableFuture<ApiResponse<HashMap<String, Object>>> future = new CompletableFuture<>();
        apiService.getUserParticipations(userId).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("获取用户参与记录失败");
                    future.complete(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HashMap<String, Object>>> call, Throwable t) {
                ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("网络错误: " + t.getMessage());
                future.complete(errorResponse);
            }
        });
        return future;
    }
}