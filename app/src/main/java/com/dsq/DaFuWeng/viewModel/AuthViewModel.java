//
//package com.dsq.DaFuWeng.viewModel;
//
//import android.app.Application;
//import android.text.TextUtils;
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import com.dsq.DaFuWeng.model.ApiResponse;
//import com.dsq.DaFuWeng.model.User;
//import com.dsq.DaFuWeng.repository.UserRepository;
//import com.dsq.DaFuWeng.utils.PhoneUtil;
//import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//
//
//
//public class AuthViewModel extends AndroidViewModel {
//    private UserRepository userRepository;
//
//    public AuthViewModel(@NonNull Application application) {
//        super(application);
//        userRepository = UserRepository.getInstance(); // 使用单例获取
//    }
//
//    // 手机号码登录
//    public LiveData<ApiResponse<User>> login(String phone, String password) {
//        MutableLiveData<ApiResponse<User>> liveData = new MutableLiveData<>();
//
//        if (!PhoneUtil.isPhoneValid(phone) || TextUtils.isEmpty(password)) {
//            ApiResponse<User> response = new ApiResponse<>();
//            response.setSuccess(false);
//            response.setMessage("请输入正确的手机号码和密码");
//            liveData.setValue(response);
//            return liveData;
//        }
//
//        // 异步调用UserRepository的login方法
//        new Thread(() -> {
//            try {
//                ApiResponse<User> response = userRepository.login(phone, password).get();
//                liveData.postValue(response);
//            } catch (InterruptedException | ExecutionException e) {
//                ApiResponse<User> errorResponse = new ApiResponse<>();
//                errorResponse.setSuccess(false);
//                errorResponse.setMessage("登录处理异常: " + e.getMessage());
//                liveData.postValue(errorResponse);
//            }
//        }).start();
//
//        return liveData;
//    }
//
//    // 手机号码注册
//    public LiveData<ApiResponse<User>> register(String phone, String password, String verifyCode) {
//        MutableLiveData<ApiResponse<User>> liveData = new MutableLiveData<>();
//
//        if (!PhoneUtil.isPhoneValid(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(verifyCode)) {
//            ApiResponse<User> response = new ApiResponse<>();
//            response.setSuccess(false);
//            response.setMessage("请输入完整信息");
//            liveData.setValue(response);
//            return liveData;
//        }
//
//        User user = new User();
//        user.setPhone(phone);
//        user.setPassword(password);
//
//        // 异步调用UserRepository的register方法
//        new Thread(() -> {
//            try {
//                ApiResponse<User> response = userRepository.register(user, verifyCode).get();
//                liveData.postValue(response);
//            } catch (InterruptedException | ExecutionException e) {
//                ApiResponse<User> errorResponse = new ApiResponse<>();
//                errorResponse.setSuccess(false);
//                errorResponse.setMessage("注册处理异常: " + e.getMessage());
//                liveData.postValue(errorResponse);
//            }
//        }).start();
//
//        return liveData;
//    }
//
//    public LiveData<Boolean> logout() {
//        MutableLiveData<Boolean> result = new MutableLiveData<>();
//        try {
//            SharedPrefsUtil.getInstance(getApplication()).onLogout();
//            result.setValue(true);
//        } catch (Exception e) {
//            result.setValue(false);
//        }
//        return result;
//    }
//
//
//}

package com.dsq.DaFuWeng.viewModel;

import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.repository.UserRepository;
import com.dsq.DaFuWeng.utils.PhoneUtil;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AuthViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(); // 使用单例获取
    }

    // 手机号码登录
    public LiveData<ApiResponse<User>> login(String phone, String password) {
        MutableLiveData<ApiResponse<User>> liveData = new MutableLiveData<>();

        if (!PhoneUtil.isPhoneValid(phone) || TextUtils.isEmpty(password)) {
            ApiResponse<User> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("请输入正确的手机号码和密码");
            liveData.setValue(response);
            return liveData;
        }

        // 异步调用UserRepository的login方法
        new Thread(() -> {
            try {
                ApiResponse<User> response = userRepository.login(phone, password).get();
                liveData.postValue(response);
            } catch (InterruptedException | ExecutionException e) {
                ApiResponse<User> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("登录处理异常: " + e.getMessage());
                liveData.postValue(errorResponse);
            }
        }).start();

        return liveData;
    }

    // 手机号码注册
    public LiveData<ApiResponse<User>> register(String phone, String password, String verifyCode) {
        MutableLiveData<ApiResponse<User>> liveData = new MutableLiveData<>();

        if (!PhoneUtil.isPhoneValid(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(verifyCode)) {
            ApiResponse<User> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("请输入完整信息");
            liveData.setValue(response);
            return liveData;
        }

        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);

        // 异步调用UserRepository的register方法
        new Thread(() -> {
            try {
                ApiResponse<User> response = userRepository.register(user, verifyCode).get();
                liveData.postValue(response);
            } catch (InterruptedException | ExecutionException e) {
                ApiResponse<User> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("注册处理异常: " + e.getMessage());
                liveData.postValue(errorResponse);
            }
        }).start();

        return liveData;
    }

    // 获取手机验证码
    public LiveData<ApiResponse<Void>> getVerifyCode(String phone) {
        MutableLiveData<ApiResponse<Void>> liveData = new MutableLiveData<>();

        if (!PhoneUtil.isPhoneValid(phone)) {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("请输入正确的11位手机号码");
            liveData.setValue(response);
            return liveData;
        }

        // 异步调用UserRepository的getVerifyCode方法
        new Thread(() -> {
            try {
                ApiResponse<Void> response = userRepository.getVerifyCode(phone).get();
                liveData.postValue(response);
            } catch (InterruptedException | ExecutionException e) {
                ApiResponse<Void> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("获取验证码异常: " + e.getMessage());
                liveData.postValue(errorResponse);
            }
        }).start();

        return liveData;
    }

    // 重置密码
    public LiveData<ApiResponse<Void>> resetPassword(String phone, String newPassword, String verifyCode) {
        MutableLiveData<ApiResponse<Void>> liveData = new MutableLiveData<>();

        if (!PhoneUtil.isPhoneValid(phone) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(verifyCode)) {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("请输入完整信息");
            liveData.setValue(response);
            return liveData;
        }

        if (newPassword.length() < 8) {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("密码长度不能小于8位");
            liveData.setValue(response);
            return liveData;
        }

        // 异步调用UserRepository的resetPassword方法
        new Thread(() -> {
            try {
                ApiResponse<Void> response = userRepository.resetPassword(phone, newPassword, verifyCode).get();
                liveData.postValue(response);
            } catch (InterruptedException | ExecutionException e) {
                ApiResponse<Void> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("重置密码异常: " + e.getMessage());
                liveData.postValue(errorResponse);
            }
        }).start();

        return liveData;
    }

    // 退出登录
    public LiveData<Boolean> logout() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        try {
            SharedPrefsUtil.getInstance(getApplication()).onLogout();
            result.setValue(true);
        } catch (Exception e) {
            result.setValue(false);
        }
        return result;
    }
}