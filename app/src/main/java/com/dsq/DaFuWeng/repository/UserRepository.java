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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    // 在UserRepository类中添加以下方法
    public CompletableFuture<Integer> getTotalParticipants() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        apiService.getTodayActivities().enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                int total = 0;
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // 服务端返回的data是List<LotteryActivity>
                    List<HashMap<String, Object>> activities =
                            (List<HashMap<String, Object>>) response.body().getData().get("data");

                    // 累加每个活动的参与人数（假设字段为participantCount）
                    for (HashMap<String, Object> activity : activities) {
                        if (activity.get("participantCount") != null) {
                            total += ((Number) activity.get("participantCount")).intValue();
                        }
                    }
                }
                future.complete(total);
            }

            @Override
            public void onFailure(Call<ApiResponse<HashMap<String, Object>>> call, Throwable t) {
                future.complete(0); // 失败时默认显示0
            }
        });

        return future;
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

    // 在UserRepository类中添加以下方法
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
                    errorResponse.setMessage("获取参与记录失败");
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

    // 添加按类型和档次参与活动的方法
    public CompletableFuture<ApiResponse<HashMap<String, Object>>> participateByTypeAndLevel(Long userId, Integer playType, Integer level) {
        CompletableFuture<ApiResponse<HashMap<String, Object>>> future = new CompletableFuture<>();

        apiService.participateByTypeAndLevel(userId, playType, level).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
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

    // UserRepository.java
    /**
     * 获取指定活动的参与人数
     */
    public CompletableFuture<Integer> getActivityParticipantCount(Long activityId) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        apiService.getActivityParticipantCount(activityId).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Object countObj = response.body().getData().get("data");
                    int count = countObj instanceof Number ? ((Number) countObj).intValue() : 0;
                    future.complete(count);
                } else {
                    future.complete(0);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HashMap<String, Object>>> call, Throwable t) {
                future.complete(0);
            }
        });
        return future;
    }
    /**
     * 通过playType和level获取活动ID
     */
    public CompletableFuture<HashMap<String, Object>> getActivityByTypeAndLevel(int playType, int level) {
        CompletableFuture<HashMap<String, Object>> future = new CompletableFuture<>();
        apiService.getActivityByTypeAndLevel(playType, level).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    future.complete(response.body().getData());
                } else {
                    future.complete(null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HashMap<String, Object>>> call, Throwable t) {
                future.complete(null);
            }
        });
        return future;
    }

    /**
     * 获取指定活动的所有参与者
     */
    public CompletableFuture<List<HashMap<String, Object>>> getActivityParticipants(Long activityId) {
        CompletableFuture<List<HashMap<String, Object>>> future = new CompletableFuture<>();
        apiService.getActivityParticipants(activityId).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Object dataObj = response.body().getData().get("data");
                    if (dataObj instanceof List) {
                        // 强制转换为参与者列表（每个元素是包含userId、phone等的Map）
                        List<HashMap<String, Object>> participants = (List<HashMap<String, Object>>) dataObj;
                        future.complete(participants);
                    } else {
                        future.complete(new ArrayList<>());
                    }
                } else {
                    future.complete(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HashMap<String, Object>>> call, Throwable t) {
                future.complete(new ArrayList<>());
            }
        });
        return future;
    }
}