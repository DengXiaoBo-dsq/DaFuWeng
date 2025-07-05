package com.dsq.DaFuWeng.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.utils.PhoneUtil;
import com.dsq.DaFuWeng.viewModel.AuthViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etPhone, etVerifyCode, etNewPassword;
    private Button btnGetCode, btnResetPassword;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        initViews();
        initListeners();
    }

    private void initViews() {
        etPhone = findViewById(R.id.et_phone);
        etVerifyCode = findViewById(R.id.et_verify_code);
        etNewPassword = findViewById(R.id.et_new_password);
        btnGetCode = findViewById(R.id.btn_get_code);
        btnResetPassword = findViewById(R.id.btn_reset_password);
    }

    private void initListeners() {
        btnGetCode.setOnClickListener(v -> getVerifyCode());
        btnResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void getVerifyCode() {
        String phone = etPhone.getText().toString().trim();
        if (!PhoneUtil.isPhoneValid(phone)) {
            Toast.makeText(this, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 调用获取验证码API
        authViewModel.getVerifyCode(phone).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "验证码已发送至" + phone, Toast.LENGTH_SHORT).show();
                // 启动倒计时
                startCountdown();
            } else {
                Toast.makeText(this, "获取验证码失败：" +
                                (response != null ? response.getMessage() : "未知错误"),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPassword() {
        String phone = etPhone.getText().toString().trim();
        String verifyCode = etVerifyCode.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        if (!PhoneUtil.isPhoneValid(phone)) {
            Toast.makeText(this, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(verifyCode) || verifyCode.length() != 6) {
            Toast.makeText(this, "请输入6位验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(newPassword) || newPassword.length() < 8) {
            Toast.makeText(this, "密码长度不能小于8位", Toast.LENGTH_SHORT).show();
            return;
        }

        // 调用重置密码API
        authViewModel.resetPassword(phone, newPassword, verifyCode).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "密码重置成功，请使用新密码登录", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "密码重置失败：" +
                                (response != null ? response.getMessage() : "未知错误"),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCountdown() {
        // 实现倒计时逻辑，禁用按钮并显示剩余时间
        // 示例代码，需要根据实际情况实现
        // 建议使用CountDownTimer类
    }
}
