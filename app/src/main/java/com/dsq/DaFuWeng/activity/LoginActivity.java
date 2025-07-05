
//
//package com.dsq.DaFuWeng.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.dsq.DaFuWeng.R;
//import com.dsq.DaFuWeng.model.User;
//import com.dsq.DaFuWeng.utils.PhoneUtil;
//import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
//import com.dsq.DaFuWeng.viewModel.AuthViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//public class LoginActivity extends AppCompatActivity {
//    private EditText etPhone, etPassword;
//    private Button btnLogin;
//    private AuthViewModel authViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
//        initViews();
//        initListeners();
//    }
//
//    private void initViews() {
//        etPhone = findViewById(R.id.et_phone);
//        etPassword = findViewById(R.id.et_password);
//        btnLogin = findViewById(R.id.btn_login);
//    }
//
//    // 登录成功处理
//    private void handleLoginSuccess(User user) {
//        // 保存用户信息和登录状态
//        SharedPrefsUtil.getInstance(this).onLoginSuccess(user);
//
//        // 导航到主界面
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }
//
//    private void initListeners() {
//        btnLogin.setOnClickListener(v -> login());
//    }
//
//    private void login() {
//        String phone = etPhone.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (!PhoneUtil.isPhoneValid(phone)) {
//            Toast.makeText(this, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        authViewModel.login(phone, password).observe(this, response -> {
//            if (response != null && response.isSuccess()) {
//                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
//                // 保存用户信息
//                // SharedPrefsUtil.saveUser(response.getData());
//                finish();
//            } else {
//                Toast.makeText(this, "登录失败：" + (response != null ? response.getMessage() : "账号或密码错误"),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
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

    // 登录成功处理
    private void handleLoginSuccess(User user) {
        // 保存用户信息和登录状态
        SharedPrefsUtil.getInstance(this).onLoginSuccess(user);

        // 导航到主界面
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void initListeners() {
        btnLogin.setOnClickListener(v -> login());

        // 忘记密码点击事件
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // 注册点击事件
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
                finish();
            } else {
                Toast.makeText(this, "登录失败：" + (response != null ? response.getMessage() : "账号或密码错误"),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}