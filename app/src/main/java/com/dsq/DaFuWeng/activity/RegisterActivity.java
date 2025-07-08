package com.dsq.DaFuWeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.utils.PhoneUtil;
import com.dsq.DaFuWeng.viewModel.AuthViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText etPhone, etVerifyCode, etPassword, etConfirmPassword;
    private Button btnGetCode, btnRegister;
    private TextView tvGoLogin;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        initViews();
        initListeners();
    }

    private void initViews() {
        etPhone = findViewById(R.id.et_phone);
        etVerifyCode = findViewById(R.id.et_verify_code);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnGetCode = findViewById(R.id.btn_get_code);
        btnRegister = findViewById(R.id.btn_register);
        tvGoLogin = findViewById(R.id.tv_go_login);
    }

    private void initListeners() {
        btnGetCode.setOnClickListener(v -> getVerifyCode());
        btnRegister.setOnClickListener(v -> register());
        tvGoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getVerifyCode() {
        String phone = etPhone.getText().toString().trim();
        if (!PhoneUtil.isPhoneValid(phone)) {
            Toast.makeText(this, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        // 关键修改：调用正确的方法名 sendVerificationCode，并传递类型参数（1=注册）
        authViewModel.sendVerificationCode(phone, 1).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "验证码已发送至" + phone, Toast.LENGTH_SHORT).show();
                // 可在此处添加倒计时逻辑
            } else {
                Toast.makeText(this, "获取验证码失败：" +
                                (response != null ? response.getMessage() : "未知错误"),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register() {
        String phone = etPhone.getText().toString().trim();
        String verifyCode = etVerifyCode.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!PhoneUtil.isPhoneValid(phone)) {
            Toast.makeText(this, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(verifyCode) || verifyCode.length() != 6) {
            Toast.makeText(this, "请输入6位验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 8) {
            Toast.makeText(this, "密码长度不能小于8位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        authViewModel.register(phone, password, verifyCode).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "注册失败：" + (response != null ? response.getMessage() : "未知错误"),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}