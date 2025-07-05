package com.dsq.DaFuWeng.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.model.ApiResponse;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.viewModel.WithdrawViewModel;

public class WithdrawActivity extends AppCompatActivity {

    private EditText etAmount;
    private TextView tvUsername, tvBalance;
    private WithdrawViewModel withdrawViewModel;
    private String userPhone; // 存储用户手机号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        withdrawViewModel = new ViewModelProvider(this).get(WithdrawViewModel.class);

        etAmount = findViewById(R.id.et_amount);
        tvUsername = findViewById(R.id.tv_username);
        tvBalance = findViewById(R.id.tv_balance);
        Button btnWithdraw = findViewById(R.id.btn_withdraw);

        // 获取用户信息
        withdrawViewModel.getUserInfo().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    userPhone = user.getPhone(); // 保存用户手机号
                    tvUsername.setText("用户：" + user.getPhone());
                    tvBalance.setText("余额：" + user.getBalance() + "元");
                }
            }
        });

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountStr = etAmount.getText().toString().trim();

                if (TextUtils.isEmpty(amountStr)) {
                    Toast.makeText(WithdrawActivity.this, "请填写提现金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    Toast.makeText(WithdrawActivity.this, "提现金额必须大于0", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 调用withdraw方法，只传递金额，手机号从已获取的用户信息中获取
                withdrawViewModel.withdraw(amount).observe(WithdrawActivity.this, response -> {
                    if (response != null && response.isSuccess()) {
                        Toast.makeText(WithdrawActivity.this, "提现申请已提交", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(WithdrawActivity.this, "提现失败：" +
                                (response != null ? response.getMessage() : "未知错误"), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}