package com.dsq.DaFuWeng.repository;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.LotteryActivity;
import com.dsq.DaFuWeng.model.UserParticipation;
import com.dsq.DaFuWeng.network.ApiService;
import com.dsq.DaFuWeng.network.RetrofitClient;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class LotteryRepository {

    private static final String TAG = "LotteryRepository";
    private ApiService apiService;
    private static LotteryRepository instance;

    private LotteryRepository() {
        apiService = RetrofitClient.getInstance().getApiService();
    }

    public static synchronized LotteryRepository getInstance() {
        if (instance == null) {
            instance = new LotteryRepository();
        }
        return instance;
    }


    public MutableLiveData<ApiResponse<List<LotteryActivity>>> getActiveLotteries() {
        final MutableLiveData<ApiResponse<List<LotteryActivity>>> lotteriesData = new MutableLiveData<>();

        apiService.getActiveLotteries().enqueue(new Callback<ApiResponse<List<LotteryActivity>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<LotteryActivity>>> call, Response<ApiResponse<List<LotteryActivity>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lotteriesData.setValue(response.body());
                } else {
                    ApiResponse<List<LotteryActivity>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("获取活动列表失败：" + response.message());
                    lotteriesData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<LotteryActivity>>> call, Throwable t) {
                Log.e(TAG, "获取活动列表失败: " + t.getMessage());
                ApiResponse<List<LotteryActivity>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("获取活动列表失败：网络错误");
                lotteriesData.setValue(errorResponse);
            }
        });

        return lotteriesData;
    }

    public MutableLiveData<ApiResponse<List<LotteryActivity>>> getHistoryLotteries() {
        final MutableLiveData<ApiResponse<List<LotteryActivity>>> lotteriesData = new MutableLiveData<>();

        apiService.getHistoryLotteries().enqueue(new Callback<ApiResponse<List<LotteryActivity>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<LotteryActivity>>> call, Response<ApiResponse<List<LotteryActivity>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lotteriesData.setValue(response.body());
                } else {
                    ApiResponse<List<LotteryActivity>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("获取历史活动失败：" + response.message());
                    lotteriesData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<LotteryActivity>>> call, Throwable t) {
                Log.e(TAG, "获取历史活动失败: " + t.getMessage());
                ApiResponse<List<LotteryActivity>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("获取历史活动失败：网络错误");
                lotteriesData.setValue(errorResponse);
            }
        });

        return lotteriesData;
    }

    public MutableLiveData<ApiResponse<UserParticipation>> joinLottery(String userId, String lotteryId) {
        final MutableLiveData<ApiResponse<UserParticipation>> participationData = new MutableLiveData<>();

        // 从RetrofitClient获取Context
        Context context = RetrofitClient.getApplication().getApplicationContext();
        String token = SharedPrefsUtil.getInstance(context).getToken();

        UserParticipation participation = new UserParticipation();
        participation.setUserId(userId);
        participation.setLotteryId(lotteryId);

        apiService.joinLottery(token, participation).enqueue(new Callback<ApiResponse<UserParticipation>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserParticipation>> call, Response<ApiResponse<UserParticipation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    participationData.setValue(response.body());
                } else {
                    ApiResponse<UserParticipation> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("参与活动失败：" + response.message());
                    participationData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserParticipation>> call, Throwable t) {
                Log.e(TAG, "参与活动失败: " + t.getMessage());
                ApiResponse<UserParticipation> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("参与活动失败：网络错误");
                participationData.setValue(errorResponse);
            }
        });

        return participationData;
    }

    public MutableLiveData<ApiResponse<List<UserParticipation>>> getUserParticipations(String userId) {
        final MutableLiveData<ApiResponse<List<UserParticipation>>> participationsData = new MutableLiveData<>();

        // 从RetrofitClient获取Context
        Context context = RetrofitClient.getApplication().getApplicationContext();
        String token = SharedPrefsUtil.getInstance(context).getToken();

        apiService.getUserParticipations(token, userId).enqueue(new Callback<ApiResponse<List<UserParticipation>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<UserParticipation>>> call, Response<ApiResponse<List<UserParticipation>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    participationsData.setValue(response.body());
                } else {
                    ApiResponse<List<UserParticipation>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("获取参与记录失败：" + response.message());
                    participationsData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<UserParticipation>>> call, Throwable t) {
                Log.e(TAG, "获取参与记录失败: " + t.getMessage());
                ApiResponse<List<UserParticipation>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("获取参与记录失败：网络错误");
                participationsData.setValue(errorResponse);
            }
        });

        return participationsData;
    }
}    