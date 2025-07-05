package com.dsq.DaFuWeng.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.repository.AccountRepository;

public class RechargeViewModel extends ViewModel {
    private AccountRepository accountRepository;
    private MutableLiveData<ApiResponse<Double>> rechargeResponse;

    public RechargeViewModel() {
        accountRepository = AccountRepository.getInstance();
        rechargeResponse = new MutableLiveData<>();
    }

    public LiveData<ApiResponse<Double>> recharge(double amount) {
        rechargeResponse = accountRepository.recharge(amount);
        return rechargeResponse;
    }
}    