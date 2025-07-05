package com.dsq.DaFuWeng.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.adapter.ParticipationAdapter;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.UserParticipation;
import com.dsq.DaFuWeng.viewModel.MainViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.List;

public class ParticipationFragment extends Fragment {

    private RecyclerView recyclerView;
    private ParticipationAdapter adapter;
    private List<UserParticipation> participationList = new ArrayList<>();
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participation, container, false);

        recyclerView = view.findViewById(R.id.rv_participations); // 修正ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ParticipationAdapter(participationList);
        recyclerView.setAdapter(adapter);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getParticipationList().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                participationList.clear();
                participationList.addAll(apiResponse.getData()); // 从ApiResponse中获取数据
                adapter.notifyDataSetChanged();
            } else {
                // 处理错误或空数据
                participationList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}