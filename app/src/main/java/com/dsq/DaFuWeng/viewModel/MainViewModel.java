package com.dsq.DaFuWeng.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.UserParticipation;
import com.dsq.DaFuWeng.repository.ParticipationRepository;

import java.util.List;

public class MainViewModel extends ViewModel {
    private ParticipationRepository participationRepository;
    private MutableLiveData<ApiResponse<List<UserParticipation>>> participationListLiveData;

    public MainViewModel() {
        participationRepository = ParticipationRepository.getInstance();
        participationListLiveData = new MutableLiveData<>();
        loadParticipationList();
    }

    public LiveData<ApiResponse<List<UserParticipation>>> getParticipationList() {
        return participationListLiveData;
    }

    private void loadParticipationList() {
        participationRepository.getParticipationList().observeForever(apiResponse -> {
            participationListLiveData.setValue(apiResponse);
        });
    }
}