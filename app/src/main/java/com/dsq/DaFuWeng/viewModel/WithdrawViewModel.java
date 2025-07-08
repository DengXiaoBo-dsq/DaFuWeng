package com.dsq.DaFuWeng.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.repository.AccountRepository;
import com.dsq.DaFuWeng.repository.UserRepository;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class WithdrawViewModel extends ViewModel {
    private AccountRepository accountRepository;
    private UserRepository userRepository;

    private MutableLiveData<User> userLiveData;


    // 修正类型：与AccountRepository返回类型一致
    private MutableLiveData<ApiResponse<HashMap<String, Object>>> withdrawResponse;

    public WithdrawViewModel() {
        accountRepository = AccountRepository.getInstance();
        userRepository = UserRepository.getInstance();
        withdrawResponse = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();
        loadUserInfo(); // 初始化时加载用户信息
    }
    // 获取用户信息
    public LiveData<User> getUserInfo() {
        return userLiveData;
    }

    // 加载用户信息（修正调用和转换逻辑）
    private void loadUserInfo() {
        new Thread(() -> {
            try {
                ApiResponse<HashMap<String, Object>> apiResponse = userRepository.getUserInfo().get();
                if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                    // 将HashMap转换为User对象
                    HashMap<String, Object> data = apiResponse.getData();
                    User user = new User();
                    user.setId((String) data.get("id"));
                    user.setPhone((String) data.get("phone"));
                    user.setBalance((Double) data.get("balance"));

                    userLiveData.postValue(user);
                }
            } catch (InterruptedException | ExecutionException e) {
                // 处理异常
                e.printStackTrace();
            }
        }).start();
    }


    // 修正提现方法：返回正确类型
    public LiveData<ApiResponse<HashMap<String, Object>>> withdraw(double amount) {
        // 直接返回仓库的结果，无需赋值
        return accountRepository.withdraw(amount);
    }
}