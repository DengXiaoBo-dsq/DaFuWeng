package com.dsq.DaFuWeng.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.repository.AccountRepository;
import java.util.HashMap;

public class RechargeViewModel extends ViewModel {
    private AccountRepository accountRepository;
    // 修正类型：与AccountRepository的返回类型保持一致
    private MutableLiveData<ApiResponse<HashMap<String, Object>>> rechargeResponse;

    public RechargeViewModel() {
        accountRepository = AccountRepository.getInstance();
        rechargeResponse = new MutableLiveData<>();
    }

    // 修正返回类型：返回LiveData<ApiResponse<HashMap<String, Object>>>
    public LiveData<ApiResponse<HashMap<String, Object>>> recharge(double amount) {
        // 直接返回Repository的结果（无需赋值，避免类型转换）
        return accountRepository.recharge(amount);
    }

    // 如需获取提现结果，新增提现方法（与充值逻辑一致）
    public LiveData<ApiResponse<HashMap<String, Object>>> withdraw(double amount) {
        return accountRepository.withdraw(amount);
    }
}