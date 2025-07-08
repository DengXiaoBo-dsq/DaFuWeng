package com.dsq.DaFuWeng.repository;

import androidx.lifecycle.MutableLiveData;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.UserParticipation;
import com.dsq.DaFuWeng.network.ApiService;
import com.dsq.DaFuWeng.network.RetrofitClient;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.List;
import java.util.HashMap;

public class ParticipationRepository {
    private static ParticipationRepository instance;
    private ApiService apiService;
    private MutableLiveData<ApiResponse<List<UserParticipation>>> participationListLiveData;
    private SharedPrefsUtil sharedPrefsUtil;
    private Gson gson; // 用于JSON数据转换

    private ParticipationRepository() {
        apiService = RetrofitClient.getInstance().getApiService();
        participationListLiveData = new MutableLiveData<>();
        sharedPrefsUtil = SharedPrefsUtil.getInstance(RetrofitClient.getApplication());
        gson = new Gson(); // 初始化Gson用于数据转换
    }

    public static ParticipationRepository getInstance() {
        if (instance == null) {
            instance = new ParticipationRepository();
        }
        return instance;
    }

    // 获取用户参与记录（匹配服务端接口）
    public MutableLiveData<ApiResponse<List<UserParticipation>>> getParticipationList() {
        // 1. 获取当前登录用户ID（服务端接口必需参数）
        Long userId = sharedPrefsUtil.getUserIdAsLong();
        if (userId == null) {
            ApiResponse<List<UserParticipation>> errorResponse = new ApiResponse<>();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("用户未登录，无法获取参与记录");
            participationListLiveData.setValue(errorResponse);
            return participationListLiveData;
        }

        // 2. 调用服务端接口（/lottery/participations/user）
        apiService.getUserParticipations(userId).enqueue(new Callback<ApiResponse<HashMap<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<HashMap<String, Object>>> call, Response<ApiResponse<HashMap<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<HashMap<String, Object>> serverResponse = response.body();
                    if (serverResponse.isSuccess()) {
                        // 3. 解析服务端返回的参与记录列表（服务端data字段包含列表）
                        HashMap<String, Object> data = serverResponse.getData();
                        if (data != null && data.containsKey("data")) {
                            // 转换data中的列表为List<UserParticipation>
                            Type listType = new TypeToken<List<UserParticipation>>(){}.getType();
                            List<UserParticipation> participations = gson.fromJson(
                                    gson.toJson(data.get("data")), // 将Object转为JSON字符串再解析
                                    listType
                            );

                            // 4. 封装为客户端需要的响应类型
                            ApiResponse<List<UserParticipation>> successResponse = new ApiResponse<>();
                            successResponse.setSuccess(true);
                            successResponse.setMessage(serverResponse.getMessage());
                            successResponse.setData(participations);
                            participationListLiveData.setValue(successResponse);
                        } else {
                            ApiResponse<List<UserParticipation>> errorResponse = new ApiResponse<>();
                            errorResponse.setSuccess(false);
                            errorResponse.setMessage("参与记录数据为空");
                            participationListLiveData.setValue(errorResponse);
                        }
                    } else {
                        ApiResponse<List<UserParticipation>> errorResponse = new ApiResponse<>();
                        errorResponse.setSuccess(false);
                        errorResponse.setMessage(serverResponse.getMessage());
                        participationListLiveData.setValue(errorResponse);
                    }
                } else {
                    ApiResponse<List<UserParticipation>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("获取参与记录失败：" + response.message());
                    participationListLiveData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HashMap<String, Object>>> call, Throwable t) {
                ApiResponse<List<UserParticipation>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("网络错误: " + t.getMessage());
                participationListLiveData.setValue(errorResponse);
            }
        });

        return participationListLiveData;
    }
}