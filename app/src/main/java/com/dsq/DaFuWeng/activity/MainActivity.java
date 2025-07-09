package com.dsq.DaFuWeng.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.fragment.AccountFragment;
import com.dsq.DaFuWeng.fragment.FamilyFragment;
import com.dsq.DaFuWeng.fragment.ParticipationFragment;
import com.dsq.DaFuWeng.fragment.RichFragment;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.repository.UserRepository;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView tvPhone, tvBalance, tvTotalParticipants;
    private User currentUser;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 获取当前用户信息
        currentUser = SharedPrefsUtil.getInstance(this).getUser();
        userRepository = UserRepository.getInstance();

        // 更新顶部用户信息
        updateTopUserInfo();

        // 加载总参与人数
        loadTotalParticipants();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // 设置默认选中项
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ParticipationFragment())
                .commit();
    }

    private void initViews() {
        tvPhone = findViewById(R.id.tv_phone);
        tvBalance = findViewById(R.id.tv_balance);
        tvTotalParticipants = findViewById(R.id.tv_total_participants);
    }

    /**
     * 更新顶部用户信息
     */
    private void updateTopUserInfo() {
        if (currentUser != null) {
            // 显示手机号码，中间用*替换
            String phone = currentUser.getPhone();
            if (phone != null && phone.length() == 11) {
                String maskedPhone = phone.substring(0, 3) + "****" + phone.substring(7);
                tvPhone.setText(maskedPhone);
            }

            // 显示余额
            tvBalance.setText(String.format("余额: ¥%.2f", currentUser.getBalance()));
        }
    }

    /**
     * 加载总参与人数
     */
    private void loadTotalParticipants() {
        // 这里应该从服务器获取总参与人数
        // 暂时模拟数据
        tvTotalParticipants.setText("总参与: 0人");

        // 实际项目中应该调用API获取
        /*
        userRepository.getTotalParticipants().enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    int total = response.body().getData();
                    tvTotalParticipants.setText(String.format("总参与: %d人", total));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                // 处理失败
            }
        });
        */
    }

    /**
     * 更新余额并刷新显示
     */
    public void updateBalance(double newBalance) {
        if (currentUser != null) {
            currentUser.setBalance(newBalance);
            SharedPrefsUtil.getInstance(this).saveUser(currentUser);
            tvBalance.setText(String.format("余额: ¥%.2f", newBalance));
        }
    }

    /**
     * 更新总参与人数
     */
    public void updateTotalParticipants(int total) {
        tvTotalParticipants.setText(String.format("总参与: %d人", total));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_participation) {
                        selectedFragment = new ParticipationFragment();
                    } else if (itemId == R.id.nav_family) {
                        selectedFragment = new FamilyFragment();
                    } else if (itemId == R.id.nav_rich) {
                        selectedFragment = new RichFragment();
                    } else if (itemId == R.id.nav_account) {
                        selectedFragment = new AccountFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    }

                    return true;
                }
            };

    // MainActivity.java 中修改
    public Long getCurrentUserId() {
        if (currentUser != null && currentUser.getId() != null) {
            try {
                // 处理带小数点的ID（如"21.0"）
                String idStr = currentUser.getId();
                if (idStr.contains(".")) {
                    // 先转为Double再取整
                    return (long) Double.parseDouble(idStr);
                } else {
                    // 正常转换
                    return Long.parseLong(idStr);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null; // 转换失败时返回null
            }
        }
        return null;
    }

    // 刷新数据
    @Override
    protected void onResume() {
        super.onResume();
        // 刷新用户信息
        currentUser = SharedPrefsUtil.getInstance(this).getUser();
        updateTopUserInfo();
    }
}