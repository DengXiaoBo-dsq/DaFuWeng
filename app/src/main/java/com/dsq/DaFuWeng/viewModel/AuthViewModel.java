package com.dsq.DaFuWeng.viewModel;

import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.repository.UserRepository;
import com.dsq.DaFuWeng.utils.PhoneUtil;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AuthViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
    }

    // 登录
    public LiveData<ApiResponse<HashMap<String, Object>>> login(String phone, String password) {
        MutableLiveData<ApiResponse<HashMap<String, Object>>> liveData = new MutableLiveData<>();
        if (!PhoneUtil.isPhoneValid(phone) || TextUtils.isEmpty(password)) {
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("请输入正确的手机号码和密码");
            liveData.setValue(response);
            return liveData;
        }
        new Thread(() -> {
            try {
                ApiResponse<HashMap<String, Object>> response = userRepository.login(phone, password).get();
                liveData.postValue(response);
            } catch (InterruptedException | ExecutionException e) {
                ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("登录处理异常: " + e.getMessage());
                liveData.postValue(errorResponse);
            }
        }).start();
        return liveData;
    }

    // 注册
    public LiveData<ApiResponse<HashMap<String, Object>>> register(String phone, String password, String verifyCode) {
        MutableLiveData<ApiResponse<HashMap<String, Object>>> liveData = new MutableLiveData<>();
        if (!PhoneUtil.isPhoneValid(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(verifyCode)) {
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("请输入完整信息");
            liveData.setValue(response);
            return liveData;
        }
        new Thread(() -> {
            try {
                ApiResponse<HashMap<String, Object>> response = userRepository.register(phone, password, verifyCode).get();
                liveData.postValue(response);
            } catch (InterruptedException | ExecutionException e) {
                ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("注册处理异常: " + e.getMessage());
                liveData.postValue(errorResponse);
            }
        }).start();
        return liveData;
    }

    // 发送验证码
    public LiveData<ApiResponse<HashMap<String, Object>>> sendVerificationCode(String phone, Integer type) {
        MutableLiveData<ApiResponse<HashMap<String, Object>>> liveData = new MutableLiveData<>();
        if (!PhoneUtil.isPhoneValid(phone)) {
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("请输入正确的11位手机号码");
            liveData.setValue(response);
            return liveData;
        }
        new Thread(() -> {
            try {
                ApiResponse<HashMap<String, Object>> response = userRepository.sendVerificationCode(phone, type).get();
                liveData.postValue(response);
            } catch (InterruptedException | ExecutionException e) {
                ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("获取验证码异常: " + e.getMessage());
                liveData.postValue(errorResponse);
            }
        }).start();
        return liveData;
    }

    // 重置密码
    public LiveData<ApiResponse<HashMap<String, Object>>> resetPassword(String phone, String newPassword, String verifyCode) {
        MutableLiveData<ApiResponse<HashMap<String, Object>>> liveData = new MutableLiveData<>();
        if (!PhoneUtil.isPhoneValid(phone) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(verifyCode)) {
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("请输入完整信息");
            liveData.setValue(response);
            return liveData;
        }
        if (newPassword.length() < 8) {
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("密码长度不能小于8位");
            liveData.setValue(response);
            return liveData;
        }
        new Thread(() -> {
            try {
                ApiResponse<HashMap<String, Object>> response = userRepository.resetPassword(phone, newPassword, verifyCode).get();
                liveData.postValue(response);
            } catch (InterruptedException | ExecutionException e) {
                ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("重置密码异常: " + e.getMessage());
                liveData.postValue(errorResponse);
            }
        }).start();
        return liveData;
    }

    // 充值
    public LiveData<ApiResponse<HashMap<String, Object>>> recharge(Long userId, BigDecimal amount) {
        MutableLiveData<ApiResponse<HashMap<String, Object>>> liveData = new MutableLiveData<>();
        new Thread(() -> {
            try {
                ApiResponse<HashMap<String, Object>> response = userRepository.recharge(userId, amount).get();
                liveData.postValue(response);
            } catch (InterruptedException | ExecutionException e) {
                ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("充值异常: " + e.getMessage());
                liveData.postValue(errorResponse);
            }
        }).start();
        return liveData;
    }
}