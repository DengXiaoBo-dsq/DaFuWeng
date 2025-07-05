package com.dsq.DaFuWeng.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.viewModel.RechargeViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RechargeActivity extends AppCompatActivity {

    private EditText etAmount;
    private RechargeViewModel rechargeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        rechargeViewModel = new ViewModelProvider(this).get(RechargeViewModel.class);

        etAmount = findViewById(R.id.et_amount);
        Button btnRecharge = findViewById(R.id.btn_recharge);

        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountStr = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amountStr)) {
                    Toast.makeText(RechargeActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    Toast.makeText(RechargeActivity.this, "充值金额必须大于0", Toast.LENGTH_SHORT).show();
                    return;
                }

                rechargeViewModel.recharge(amount).observe(RechargeActivity.this, response -> {
                    if (response != null && response.isSuccess()) {
                        Toast.makeText(RechargeActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RechargeActivity.this, "充值失败：" + (response != null ? response.getMessage() : "未知错误"), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}    