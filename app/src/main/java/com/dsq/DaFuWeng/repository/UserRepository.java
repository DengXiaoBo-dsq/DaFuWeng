package com.dsq.DaFuWeng.repository;

import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.network.ApiService;
import com.dsq.DaFuWeng.network.RetrofitClient;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class UserRepository {
    private static UserRepository instance;
    private ApiService apiService;
    private SharedPrefsUtil sharedPrefsUtil;

    private UserRepository() {
        apiService = RetrofitClient.getInstance().getApiService();
        sharedPrefsUtil = SharedPrefsUtil.getInstance(RetrofitClient.getApplication());
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    public CompletableFuture<ApiResponse<HashMap<String, Object>>> login(String phone, String password) {
        CompletableFuture<ApiResponse<HashMap<String, Object>>> future = new CompletableFuture<>();
        HashMap<String, String> request = new HashMap<>();
        request.put("phone", phone);
        request.put("password", password);
        apiService.login(request).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                    if (response.body().isSuccess() && response.body().getData() != null) {
                        User user = new User();
                        HashMap<String, Object> data = response.body().getData();

                        // 处理id（非空判断）
                        user.setId(data.get("id") != null ? String.valueOf(data.get("id")) : "");
                        // 处理phone
                        user.setPhone((String) data.get("phone"));
                        // 处理balance（非空判断）
                        user.setBalance(data.get("balance") != null ? (Double) data.get("balance") : 0.0);
                        // 彻底删除level和isVip的处理代码

                        sharedPrefsUtil.saveUser(user);

                    }
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("登录失败");
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
    // 注册
    public CompletableFuture<ApiResponse<HashMap<String, Object>>> register(String phone, String password, String verifyCode) {
        CompletableFuture<ApiResponse<HashMap<String, Object>>> future = new CompletableFuture<>();
        HashMap<String, String> request = new HashMap<>();
        request.put("phone", phone);
        request.put("password", password);
        request.put("verifyCode", verifyCode);
        apiService.register(request).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("注册失败");
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

    // 发送验证码
    public CompletableFuture<ApiResponse<HashMap<String, Object>>> sendVerificationCode(String phone, Integer type) {
        CompletableFuture<ApiResponse<HashMap<String, Object>>> future = new CompletableFuture<>();
        apiService.sendVerificationCode(phone, type).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("发送验证码失败");
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

    // 重置密码
    public CompletableFuture<ApiResponse<HashMap<String, Object>>> resetPassword(String phone, String newPassword, String verifyCode) {
        CompletableFuture<ApiResponse<HashMap<String, Object>>> future = new CompletableFuture<>();
        HashMap<String, String> request = new HashMap<>();
        request.put("phone", phone);
        request.put("newPassword", newPassword);
        request.put("verifyCode", verifyCode);
        apiService.resetPassword(request).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("重置密码失败");
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
    // 新增：获取用户信息方法（解决第一个错误）
    public CompletableFuture<ApiResponse<HashMap<String, Object>>> getUserInfo() {
        CompletableFuture<ApiResponse<HashMap<String, Object>>> future = new CompletableFuture<>();

        // 获取当前登录用户ID
        Long userId = sharedPrefsUtil.getUserIdAsLong();
        if (userId == null) {
            ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("用户未登录，无法获取信息");
            future.complete(errorResponse);
            return future;
        }

        // 调用API获取用户信息
        apiService.getUserInfo(userId).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("获取用户信息失败");
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

    // 充值
    public CompletableFuture<ApiResponse<HashMap<String, Object>>> recharge(Long userId, BigDecimal amount) {
        CompletableFuture<ApiResponse<HashMap<String, Object>>> future = new CompletableFuture<>();
        apiService.recharge(userId, amount).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("充值失败");
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