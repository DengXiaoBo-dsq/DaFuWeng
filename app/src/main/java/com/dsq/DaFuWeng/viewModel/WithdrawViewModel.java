package com.dsq.DaFuWeng.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.repository.AccountRepository;
import com.dsq.DaFuWeng.repository.UserRepository;

public class WithdrawViewModel extends ViewModel {
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private MutableLiveData<ApiResponse<Double>> withdrawResponse;
    private MutableLiveData<User> userLiveData;

    public WithdrawViewModel() {
        accountRepository = AccountRepository.getInstance();
        userRepository = UserRepository.getInstance();
        withdrawResponse = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();
        loadUserInfo(); // 初始化时加载用户信息
    }

    // 添加获取用户信息的方法
    public LiveData<User> getUserInfo() {
        return userLiveData;
    }

    // 加载用户信息
    private void loadUserInfo() {
        userRepository.getUserInfo().thenAccept(apiResponse -> {
            if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                userLiveData.postValue(apiResponse.getData());
            }
        });
    }

    public LiveData<ApiResponse<Double>> withdraw(double amount) {
        withdrawResponse = accountRepository.withdraw(amount);
        return withdrawResponse;
    }
}