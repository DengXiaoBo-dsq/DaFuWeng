package com.dsq.DaFuWeng.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.activity.MainActivity;
import com.dsq.DaFuWeng.adapter.RichParticipantAdapter;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.repository.UserRepository;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FamilyFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvTotalParticipants, tvEmpty;
    private Button btnLevel1, btnLevel2, btnLevel3, btnLevel4;
    private RichParticipantAdapter adapter;
    private UserRepository userRepository;
    private String currentUserId;
    private double[] levels = {20, 50, 200, 500}; // 四个档次的金额
    private int currentSelectedLevel = 1; // 记录用户当前选中的档次（默认1）

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family, container, false);
        initViews(view);
        initData();
        setButtonListeners();
        // 初始加载默认档次（1档）的数据
        loadFamilyParticipants();
        loadTotalFamilyParticipants();
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        tvTotalParticipants = view.findViewById(R.id.tv_total_participants);
        tvEmpty = view.findViewById(R.id.tv_empty);
        btnLevel1 = view.findViewById(R.id.btn_level_1);
        btnLevel2 = view.findViewById(R.id.btn_level_2);
        btnLevel3 = view.findViewById(R.id.btn_level_3);
        btnLevel4 = view.findViewById(R.id.btn_level_4);

        // 设置按钮文本
        btnLevel1.setText(String.format("参与 %d元", (int) levels[0]));
        btnLevel2.setText(String.format("参与 %d元", (int) levels[1]));
        btnLevel3.setText(String.format("参与 %d元", (int) levels[2]));
        btnLevel4.setText(String.format("参与 %d元", (int) levels[3]));
    }

    private void initData() {
        userRepository = UserRepository.getInstance();
        User currentUser = SharedPrefsUtil.getInstance(getContext()).getUser();
        if (currentUser != null) {
            currentUserId = currentUser.getId();
        }

        // 初始化适配器
        adapter = new RichParticipantAdapter(getContext(), currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setButtonListeners() {
        // 20元档次（1档）
        btnLevel1.setOnClickListener(v -> {
            currentSelectedLevel = 1;
            participateInLevel(1);
        });

        // 50元档次（2档）
        btnLevel2.setOnClickListener(v -> {
            currentSelectedLevel = 2;
            participateInLevel(2);
        });

        // 200元档次（3档）
        btnLevel3.setOnClickListener(v -> {
            currentSelectedLevel = 3;
            participateInLevel(3);
        });

        // 500元档次（4档）
        btnLevel4.setOnClickListener(v -> {
            currentSelectedLevel = 4;
            participateInLevel(4);
        });
    }

    // FamilyFragment.java
    private void participateInLevel(int level) {
        if (getContext() instanceof MainActivity && level >= 1 && level <= 4) {
            MainActivity mainActivity = (MainActivity) getContext();
            Long userId = mainActivity.getCurrentUserId();
            User currentUser = SharedPrefsUtil.getInstance(getContext()).getUser();

            if (userId != null && currentUser != null) {
                double requiredAmount = levels[level - 1];
                if (currentUser.getBalance() >= requiredAmount) {
                    // 参与活动（合家欢playType=2）
                    userRepository.participateByTypeAndLevel(userId, 2, level)
                            .thenAccept(response -> {
                                if (response != null && response.isSuccess()) {
                                    // 更新余额（已有逻辑保留）
                                    double newBalance = currentUser.getBalance() - requiredAmount;
                                    currentUser.setBalance(newBalance);
                                    SharedPrefsUtil.getInstance(getContext()).saveUser(currentUser);
                                    mainActivity.updateBalance(newBalance);

                                    // 强制刷新：无论之前状态如何，重新获取活动ID并刷新数据
                                    getActivityIdByTypeAndLevel(2, level) // 用当前参与的level，而非currentSelectedLevel
                                            .thenAccept(activityId -> {
                                                if (activityId != null) {
                                                    loadFamilyParticipants(); // 刷新列表
                                                    loadTotalFamilyParticipants(); // 刷新人数
                                                } else {
                                                    // 活动ID获取失败时，仍尝试刷新（可能服务端延迟）
                                                    getActivity().runOnUiThread(() ->
                                                            Toast.makeText(getContext(), "活动数据加载中...", Toast.LENGTH_SHORT).show()
                                                    );
                                                }
                                            });

                                    getActivity().runOnUiThread(() ->
                                            Toast.makeText(getContext(), "参与成功", Toast.LENGTH_SHORT).show()
                                    );
                                } else {
                                    // 参与失败处理（已有逻辑保留）
                                    String message = response != null ? response.getMessage() : "参与失败";
                                    getActivity().runOnUiThread(() ->
                                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
                                    );
                                }
                            })
                            .exceptionally(e -> {
                                // 网络错误处理（已有逻辑保留）
                                getActivity().runOnUiThread(() ->
                                        Toast.makeText(getContext(), "网络错误: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                                return null;
                            });
                } else {
                    Toast.makeText(getContext(), "余额不足，请充值", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 加载当前选中档次的合家欢参与者（所有用户）
     */
    private void loadFamilyParticipants() {
        if (getContext() instanceof MainActivity) {
            // 获取当前选中的档次（1-4）
            getActivityIdByTypeAndLevel(2, currentSelectedLevel) // 合家欢playType=2
                    .thenAccept(activityId -> {
                        if (activityId != null) {
                            // 调用接口获取该活动的所有参与者
                            userRepository.getActivityParticipants(activityId)
                                    .thenAccept(participants -> {
                                        updateParticipantList(participants);
                                    });
                        }
                    });
        }
    }
    // FamilyFragment.java
    private void loadTotalFamilyParticipants() {
        if (getContext() instanceof MainActivity) {
            getActivityIdByTypeAndLevel(2, currentSelectedLevel)
                    .thenAccept(activityId -> {
                        if (activityId != null) {
                            userRepository.getActivityParticipantCount(activityId)
                                    .thenAccept(count -> {
                                        getActivity().runOnUiThread(() -> {
                                            // 直接更新，避免被其他逻辑覆盖
                                            tvTotalParticipants.setText("合家欢总参与: " + count + "人");
                                            // 可添加日志验证是否执行
                                            Log.d("FamilyFragment", "总人数更新为: " + count);
                                        });
                                    });
                        }
                    });
        }
    }



    /**
     * 通过playType和level获取活动ID（合家欢专用）
     */
    // FamilyFragment.java
    private CompletableFuture<Long> getActivityIdByTypeAndLevel(int playType, int level) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        userRepository.getActivityByTypeAndLevel(playType, level)
                .thenAccept(activity -> {
                    if (activity != null && activity.get("id") != null) {
                        future.complete(((Number) activity.get("id")).longValue());
                    } else {
                        getActivity().runOnUiThread(() ->
                                // 明确提示活动ID获取失败，影响数据刷新
                                Toast.makeText(getContext(), "活动数据获取失败，无法刷新列表", Toast.LENGTH_SHORT).show()
                        );
                        future.complete(null);
                    }
                })
                .exceptionally(e -> {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "获取活动失败: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                    future.complete(null);
                    return null;
                });
        return future;
    }

    /**
     * 更新参与者列表UI（适配服务端返回的HashMap数据）
     */
    private void updateParticipantList(List<HashMap<String, Object>> participants) {
        if (participants != null && !participants.isEmpty()) {
            getActivity().runOnUiThread(() -> {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmpty.setVisibility(View.GONE);
                adapter.setParticipationsFromServer(participants); // 用服务端数据更新适配器
            });
        } else {
            showEmptyState();
        }
    }

    private void showEmptyState() {
        if (isAdded()) {
            getActivity().runOnUiThread(() -> {
                recyclerView.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("暂无参与人员，欢迎参与！");
            });
        }
    }
}