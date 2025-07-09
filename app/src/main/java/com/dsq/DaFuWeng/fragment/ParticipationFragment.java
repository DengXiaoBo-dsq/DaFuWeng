package com.dsq.DaFuWeng.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.activity.MainActivity;
import com.dsq.DaFuWeng.adapter.ParticipantAdapter;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.model.UserParticipation;
import com.dsq.DaFuWeng.repository.UserRepository;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import java.util.List;

public class ParticipationFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private ParticipantAdapter adapter;
    private UserRepository userRepository;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participation, container, false);

        // 初始化视图
        recyclerView = view.findViewById(R.id.recycler_view);
        tvEmpty = view.findViewById(R.id.tv_empty);

        // 初始化数据
        initData();

        // 加载参与者数据
        loadParticipants();

        return view;
    }

    private void initData() {
        userRepository = UserRepository.getInstance();
        User currentUser = SharedPrefsUtil.getInstance(getContext()).getUser();
        if (currentUser != null) {
            currentUserId = currentUser.getId();
        }

        // 初始化适配器
        adapter = new ParticipantAdapter(getContext(), currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * 加载所有参与者数据
     */
    private void loadParticipants() {
        // 这里应该从服务器获取所有参与者信息
        // 为了示例，我们先获取当前用户的参与记录
        if (getContext() instanceof MainActivity) {
            Long userId = ((MainActivity) getContext()).getCurrentUserId();
            if (userId != null) {
                userRepository.getUserParticipations(userId)
                        .thenAccept(response -> {
                            if (response != null && response.isSuccess() && response.getData() != null) {
                                List<UserParticipation> participations = (List<UserParticipation>) response.getData();
                                updateParticipantList(participations);
                            } else {
                                showEmptyState();
                            }
                        })
                        .exceptionally(e -> {
                            showEmptyState();
                            return null;
                        });
            }
        }
    }

    /**
     * 更新参与者列表
     */
    private void updateParticipantList(List<UserParticipation> participations) {
        if (participations != null && participations.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            adapter.setParticipations(participations);
        } else {
            showEmptyState();
        }
    }

    /**
     * 显示空状态
     */
    private void showEmptyState() {
        if (isAdded()) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText("暂无参与人员，欢迎参与！");
        }
    }
}