package com.dsq.DaFuWeng.repository;

import androidx.lifecycle.MutableLiveData;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.UserParticipation;
import com.dsq.DaFuWeng.network.ApiService;
import com.dsq.DaFuWeng.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class ParticipationRepository {
    private static ParticipationRepository instance;
    private ApiService apiService;
    private MutableLiveData<ApiResponse<List<UserParticipation>>> participationListLiveData;

    private ParticipationRepository() {
        apiService = RetrofitClient.getInstance().getApiService();
        participationListLiveData = new MutableLiveData<>();
    }

    public static ParticipationRepository getInstance() {
        if (instance == null) {
            instance = new ParticipationRepository();
        }
        return instance;
    }

    public MutableLiveData<ApiResponse<List<UserParticipation>>> getParticipationList() {
        // 从API获取参与记录
        apiService.getParticipationList().enqueue(new Callback<ApiResponse<List<UserParticipation>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<UserParticipation>>> call, Response<ApiResponse<List<UserParticipation>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    participationListLiveData.setValue(response.body());
                } else {
                    ApiResponse<List<UserParticipation>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("获取参与记录失败");
                    participationListLiveData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<UserParticipation>>> call, Throwable t) {
                ApiResponse<List<UserParticipation>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("网络错误: " + t.getMessage());
                participationListLiveData.setValue(errorResponse);
            }
        });
        return participationListLiveData;
    }
}