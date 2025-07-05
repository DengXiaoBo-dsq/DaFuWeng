package com.dsq.DaFuWeng.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.network.ApiService;
import com.dsq.DaFuWeng.network.RetrofitClient;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
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

    public MutableLiveData<ApiResponse<Double>> recharge(double amount) {
        final MutableLiveData<ApiResponse<Double>> rechargeData = new MutableLiveData<>();
        // 通过实例获取Token
        String token = sharedPrefsUtil.getToken();

        apiService.recharge(token, amount).enqueue(new Callback<ApiResponse<Double>>() {
            @Override
            public void onResponse(Call<ApiResponse<Double>> call, Response<ApiResponse<Double>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rechargeData.setValue(response.body());
                } else {
                    ApiResponse<Double> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("充值失败：" + response.message());
                    rechargeData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Double>> call, Throwable t) {
                Log.e(TAG, "充值失败: " + t.getMessage());
                ApiResponse<Double> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("充值失败：网络错误");
                rechargeData.setValue(errorResponse);
            }
        });

        return rechargeData;
    }

    public MutableLiveData<ApiResponse<Double>> withdraw(double amount) {
        final MutableLiveData<ApiResponse<Double>> withdrawData = new MutableLiveData<>();
        // 通过实例获取Token
        String token = sharedPrefsUtil.getToken();

        apiService.withdraw(token, amount).enqueue(new Callback<ApiResponse<Double>>() {
            @Override
            public void onResponse(Call<ApiResponse<Double>> call, Response<ApiResponse<Double>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    withdrawData.setValue(response.body());
                } else {
                    ApiResponse<Double> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("提现失败：" + response.message());
                    withdrawData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Double>> call, Throwable t) {
                Log.e(TAG, "提现失败: " + t.getMessage());
                ApiResponse<Double> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("提现失败：网络错误");
                withdrawData.setValue(errorResponse);
            }
        });

        return withdrawData;
    }
}