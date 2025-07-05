//package com.dsq.DaFuWeng.repository;
//
//import android.util.Log;
//import androidx.lifecycle.MutableLiveData;
//import com.dsq.DaFuWeng.model.ApiResponse;
//import com.dsq.DaFuWeng.model.User;
//import com.dsq.DaFuWeng.network.ApiService;
//import com.dsq.DaFuWeng.network.RetrofitClient;
//import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class UserRepository {
//    private static final String TAG = "UserRepository";
//    private ApiService apiService;
//    private static UserRepository instance;
//
//    private UserRepository() {
//        apiService = RetrofitClient.getInstance().getApiService();
//    }
//
//    public static synchronized UserRepository getInstance() {
//        if (instance == null) {
//            instance = new UserRepository();
//        }
//        return instance;
//    }
//
//    public MutableLiveData<ApiResponse<User>> login(String username, String password) {
//        final MutableLiveData<ApiResponse<User>> loginData = new MutableLiveData<>();
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//
//        apiService.login(user).enqueue(new Callback<ApiResponse<User>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    loginData.setValue(response.body());
//
//                    // 如果登录成功，保存token
//                    if (response.body().isSuccess() && response.body().getData() != null) {
//                        // 假设API返回的User对象中包含token字段
//                        String token = response.body().getData().getId(); // 这里用id作为token示例
//                        SharedPrefsUtil.getInstance().saveToken(token);
//                    }
//                } else {
//                    ApiResponse<User> errorResponse = new ApiResponse<>();
//                    errorResponse.setSuccess(false);
//                    errorResponse.setMessage("登录失败：" + response.message());
//                    loginData.setValue(errorResponse);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
//                Log.e(TAG, "登录失败: " + t.getMessage());
//                ApiResponse<User> errorResponse = new ApiResponse<>();
//                errorResponse.setSuccess(false);
//                errorResponse.setMessage("登录失败：网络错误");
//                loginData.setValue(errorResponse);
//            }
//        });
//
//        return loginData;
//    }
//
//    public MutableLiveData<ApiResponse<User>> register(String username, String password) {
//        final MutableLiveData<ApiResponse<User>> registerData = new MutableLiveData<>();
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//
//        apiService.register(user).enqueue(new Callback<ApiResponse<User>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    registerData.setValue(response.body());
//                } else {
//                    ApiResponse<User> errorResponse = new ApiResponse<>();
//                    errorResponse.setSuccess(false);
//                    errorResponse.setMessage("注册失败：" + response.message());
//                    registerData.setValue(errorResponse);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
//                Log.e(TAG, "注册失败: " + t.getMessage());
//                ApiResponse<User> errorResponse = new ApiResponse<>();
//                errorResponse.setSuccess(false);
//                errorResponse.setMessage("注册失败：网络错误");
//                registerData.setValue(errorResponse);
//            }
//        });
//
//        return registerData;
//    }
//
//    public MutableLiveData<ApiResponse<User>> getUserById(String userId) {
//        final MutableLiveData<ApiResponse<User>> userData = new MutableLiveData<>();
//        String token = SharedPrefsUtil.getInstance().getToken();
//
//        apiService.getUserById(token, userId).enqueue(new Callback<ApiResponse<User>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    userData.setValue(response.body());
//                } else {
//                    ApiResponse<User> errorResponse = new ApiResponse<>();
//                    errorResponse.setSuccess(false);
//                    errorResponse.setMessage("获取用户信息失败：" + response.message());
//                    userData.setValue(errorResponse);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
//                Log.e(TAG, "获取用户信息失败: " + t.getMessage());
//                ApiResponse<User> errorResponse = new ApiResponse<>();
//                errorResponse.setSuccess(false);
//                errorResponse.setMessage("获取用户信息失败：网络错误");
//                userData.setValue(errorResponse);
//            }
//        });
//
//        return userData;
//    }
//}
package com.dsq.DaFuWeng.repository;

import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.network.ApiService;
import com.dsq.DaFuWeng.network.RetrofitClient;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserRepository {
    private static UserRepository instance;
    private ApiService apiService;
    private SharedPrefsUtil sharedPrefsUtil;

    // 私有构造函数（单例模式）
    private UserRepository() {
        // 通过实例获取ApiService
        apiService = RetrofitClient.getInstance().getApiService();
        // 获取SharedPrefsUtil实例
        sharedPrefsUtil = SharedPrefsUtil.getInstance(RetrofitClient.getApplication());
    }

    // 单例获取方法
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    // login方法...（保持不变）
    public CompletableFuture<ApiResponse<User>> login(String phone, String password) {
        CompletableFuture<ApiResponse<User>> future = new CompletableFuture<>();

        apiService.login(phone, password).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                    if (response.body().isSuccess() && response.body().getData() != null) {
                        sharedPrefsUtil.saveUser(response.body().getData());
                    }
                } else {
                    ApiResponse<User> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("登录失败");
                    future.complete(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                ApiResponse<User> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("网络错误: " + t.getMessage());
                future.complete(errorResponse);
            }
        });

        return future;
    }

    // register方法...（保持不变）
    public CompletableFuture<ApiResponse<User>> register(User user, String verifyCode) {
        CompletableFuture<ApiResponse<User>> future = new CompletableFuture<>();

        apiService.register(user.getPhone(), user.getPassword(), verifyCode).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<User> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("注册失败");
                    future.complete(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                ApiResponse<User> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("网络错误: " + t.getMessage());
                future.complete(errorResponse);
            }
        });

        return future;
    }

    // getUserInfo方法...（保持不变）
    public CompletableFuture<ApiResponse<User>> getUserInfo() {
        CompletableFuture<ApiResponse<User>> future = new CompletableFuture<>();
        String token = sharedPrefsUtil.getToken();

        apiService.getUserInfo(token).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                    if (response.body().isSuccess() && response.body().getData() != null) {
                        sharedPrefsUtil.saveUser(response.body().getData());
                    }
                } else {
                    ApiResponse<User> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("获取用户信息失败");
                    future.complete(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                ApiResponse<User> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("网络错误: " + t.getMessage());
                future.complete(errorResponse);
            }
        });

        return future;
    }

    public CompletableFuture<ApiResponse<Void>> getVerifyCode(String phone) {
        CompletableFuture<ApiResponse<Void>> future = new CompletableFuture<>();
        apiService.getVerifyCode(phone).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<Void> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("获取验证码失败");
                    future.complete(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                ApiResponse<Void> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("网络错误: " + t.getMessage());
                future.complete(errorResponse);
            }
        });
        return future;
    }

    public CompletableFuture<ApiResponse<Void>> resetPassword(String phone, String newPassword, String verifyCode) {
        CompletableFuture<ApiResponse<Void>> future = new CompletableFuture<>();
        apiService.resetPassword(phone, newPassword, verifyCode).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    ApiResponse<Void> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("重置密码失败");
                    future.complete(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                ApiResponse<Void> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("网络错误: " + t.getMessage());
                future.complete(errorResponse);
            }
        });
        return future;
    }
}