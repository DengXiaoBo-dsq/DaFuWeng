package com.dsq.DaFuWeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;

public class LauncherActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 1500; // 启动页显示时长（毫秒）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher); // 使用启动页布局

        // 模拟加载动画，实际项目中可替换为真实动画
        new Handler().postDelayed(() -> {
            checkLoginStatus();
        }, SPLASH_DELAY);
    }

    private void checkLoginStatus() {
        boolean isLoggedIn = SharedPrefsUtil.getInstance(this).isLoggedIn();

        Intent intent;
        if (isLoggedIn) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        // 清除启动页栈，避免返回时回到启动页
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
