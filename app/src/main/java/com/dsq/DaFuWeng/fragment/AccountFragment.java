package com.dsq.DaFuWeng.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.activity.MainActivity;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.repository.UserRepository;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import java.math.BigDecimal;

public class AccountFragment extends Fragment {

    private TextView tvCurrentBalance;
    private EditText etAmount;
    private Button btnRecharge, btnWithdraw;
    private User currentUser;
    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // 初始化视图
        initViews(view);

        // 初始化数据
        initData();

        // 设置按钮点击事件
        setButtonListeners();

        return view;
    }

    private void initViews(View view) {
        tvCurrentBalance = view.findViewById(R.id.tv_current_balance);
        etAmount = view.findViewById(R.id.et_amount);
        btnRecharge = view.findViewById(R.id.btn_recharge);
        btnWithdraw = view.findViewById(R.id.btn_withdraw);
    }

    private void initData() {
        userRepository = UserRepository.getInstance();
        currentUser = SharedPrefsUtil.getInstance(getContext()).getUser();

        // 显示当前余额
        if (currentUser != null) {
            tvCurrentBalance.setText(String.format("当前余额: ¥%.2f", currentUser.getBalance()));
        }
    }

    private void setButtonListeners() {
        // 充值按钮
        btnRecharge.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();
            handleRecharge(amountStr);
        });

        // 提现按钮
        btnWithdraw.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();
            handleWithdraw(amountStr);
        });
    }

    /**
     * 处理充值
     */
    private void handleRecharge(String amountStr) {
        if (amountStr.isEmpty()) {
            Toast.makeText(getContext(), "请输入充值金额", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                Toast.makeText(getContext(), "请输入有效的金额", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentUser != null && getContext() instanceof MainActivity) {
                Long userId = ((MainActivity) getContext()).getCurrentUserId();
                if (userId != null) {
                    userRepository.recharge(userId, BigDecimal.valueOf(amount))
                            .thenAccept(response -> {
                                if (response != null && response.isSuccess()) {
                                    // 充值成功，更新余额
                                    double newBalance = currentUser.getBalance() + amount;
                                    currentUser.setBalance(newBalance);
                                    SharedPrefsUtil.getInstance(getContext()).saveUser(currentUser);

                                    // 更新界面
                                    getActivity().runOnUiThread(() -> {
                                        tvCurrentBalance.setText(String.format("当前余额: ¥%.2f", newBalance));
                                        etAmount.setText("");
                                        Toast.makeText(getContext(), "充值成功", Toast.LENGTH_SHORT).show();

                                        // 更新主界面余额
                                        ((MainActivity) getContext()).updateBalance(newBalance);
                                    });
                                } else {
                                    String message = response != null ? response.getMessage() : "充值失败";
                                    getActivity().runOnUiThread(() ->
                                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
                                    );
                                }
                            })
                            .exceptionally(e -> {
                                getActivity().runOnUiThread(() ->
                                        Toast.makeText(getContext(), "网络错误: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                                return null;
                            });
                }
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "请输入有效的数字", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理提现
     */
    private void handleWithdraw(String amountStr) {
        if (amountStr.isEmpty()) {
            Toast.makeText(getContext(), "请输入提现金额", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                Toast.makeText(getContext(), "请输入有效的金额", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentUser != null) {
                // 检查余额是否足够
                if (currentUser.getBalance() < amount) {
                    Toast.makeText(getContext(), "余额不足", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 这里应该调用提现API
                // 为了示例，我们直接更新余额
                double newBalance = currentUser.getBalance() - amount;
                currentUser.setBalance(newBalance);
                SharedPrefsUtil.getInstance(getContext()).saveUser(currentUser);

                // 更新界面
                tvCurrentBalance.setText(String.format("当前余额: ¥%.2f", newBalance));
                etAmount.setText("");
                Toast.makeText(getContext(), "提现申请已提交", Toast.LENGTH_SHORT).show();

                // 更新主界面余额
                if (getContext() instanceof MainActivity) {
                    ((MainActivity) getContext()).updateBalance(newBalance);
                }
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "请输入有效的数字", Toast.LENGTH_SHORT).show();
        }
    }
}