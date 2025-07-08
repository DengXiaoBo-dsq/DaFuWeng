package com.dsq.DaFuWeng.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.network.ApiService;
import com.dsq.DaFuWeng.network.RetrofitClient;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;

import java.math.BigDecimal;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {
    private static final String TAG = "AccountRepository";
    private ApiService apiService;
    private static AccountRepository instance;
    private SharedPrefsUtil sharedPrefsUtil; // 添加SharedPrefsUtil实例

    private AccountRepository() {
        apiService = RetrofitClient.getInstance().getApiService();
        // 初始化SharedPrefsUtil实例
        sharedPrefsUtil = SharedPrefsUtil.getInstance(RetrofitClient.getApplication());
    }

    public static synchronized AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    // 充值方法
    public MutableLiveData<ApiResponse<HashMap<String, Object>>> recharge(double amount) {
        final MutableLiveData<ApiResponse<HashMap<String, Object>>> rechargeData = new MutableLiveData<>();

        // 获取用户ID（转换为Long）
        Long userId = sharedPrefsUtil.getUserIdAsLong();
        if (userId == null) {
            ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("用户未登录，请先登录");
            rechargeData.setValue(errorResponse);
            return rechargeData;
        }

        // 转换金额为BigDecimal
        BigDecimal rechargeAmount = new BigDecimal(amount);

        apiService.recharge(userId, rechargeAmount).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rechargeData.setValue(response.body());
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("充值失败：" + response.message());
                    rechargeData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HashMap<String, Object>>> call, Throwable t) {
                Log.e(TAG, "充值失败: " + t.getMessage());
                ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("充值失败：网络错误");
                rechargeData.setValue(errorResponse);
            }
        });

        return rechargeData;
    }
    // 新增提现方法
    public MutableLiveData<ApiResponse<HashMap<String, Object>>> withdraw(double amount) {
        final MutableLiveData<ApiResponse<HashMap<String, Object>>> withdrawData = new MutableLiveData<>();

        // 获取用户ID（转换为Long）
        Long userId = sharedPrefsUtil.getUserIdAsLong();
        if (userId == null) {
            ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("用户未登录，请先登录");
            withdrawData.setValue(errorResponse);
            return withdrawData;
        }

        // 验证金额是否合法
        if (amount <= 0) {
            ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("提现金额必须大于0");
            withdrawData.setValue(errorResponse);
            return withdrawData;
        }

        // 转换金额为BigDecimal
        BigDecimal withdrawAmount = new BigDecimal(amount);

        apiService.withdraw(userId, withdrawAmount).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    withdrawData.setValue(response.body());
                } else {
                    ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("提现失败：" + response.message());
                    withdrawData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HashMap<String, Object>>> call, Throwable t) {
                Log.e(TAG, "提现失败: " + t.getMessage());
                ApiResponse<HashMap<String, Object>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("提现失败：网络错误");
                withdrawData.setValue(errorResponse);
            }
        });

        return withdrawData;
    }
}