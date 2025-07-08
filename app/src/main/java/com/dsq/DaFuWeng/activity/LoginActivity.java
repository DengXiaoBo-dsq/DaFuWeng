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
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.utils.PhoneUtil;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import com.dsq.DaFuWeng.viewModel.AuthViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText etPhone, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        initViews();
        initListeners();
    }

    private void initViews() {
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegister = findViewById(R.id.tv_register);
    }

    private void initListeners() {
        btnLogin.setOnClickListener(v -> login());
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (!PhoneUtil.isPhoneValid(phone)) {
            Toast.makeText(this, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        authViewModel.login(phone, password).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                // 将 HashMap 转换为 User 对象（删除level和isVip处理）
                User user = new User();
                HashMap<String, Object> data = response.getData();
                // 处理id（服务端id是Integer，转为String）
                user.setId(data.get("id") != null ? String.valueOf(data.get("id")) : "");
                user.setPhone((String) data.get("phone"));
                // 服务端返回中没有password，无需设置（避免null）
                // user.setPassword((String) data.get("password"));
                // 处理balance（Double类型）
                user.setBalance(data.get("balance") != null ? (Double) data.get("balance") : 0.0);
                // 移除level和isVip的设置代码

                SharedPrefsUtil.getInstance(this).onLoginSuccess(user);
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "登录失败：" + (response != null ? response.getMessage() : "账号或密码错误"),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}