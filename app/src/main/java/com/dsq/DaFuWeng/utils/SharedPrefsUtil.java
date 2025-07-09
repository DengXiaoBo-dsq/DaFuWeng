package com.dsq.DaFuWeng.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.dsq.DaFuWeng.model.User;
import com.dsq.DaFuWeng.network.RetrofitClient;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SharedPrefsUtil {
    private static SharedPrefsUtil instance;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "DaFuWengPrefs";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private SharedPrefsUtil(Context context) {
        Context appContext = context.getApplicationContext();
        sharedPreferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefsUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsUtil(context);
        }
        return instance;
    }

    // 获取Token
    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    // 保存Token
    public void saveToken(String token) {
        sharedPreferences.edit().putString("token", token).apply();
    }

    // 保存用户ID（String类型）
    public void saveUserId(String userId) {
        sharedPreferences.edit().putString("user_id", userId).apply();
    }

    // 获取用户ID（String类型）
    public String getUserId() {
        return sharedPreferences.getString("user_id", "");
    }

    // 新增：获取用户ID（转换为Long类型）
    public Long getUserIdAsLong() {
        String userIdStr = getUserId();
        if (userIdStr.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            // 处理转换失败的情况（如ID格式错误）
            return null;
        }
    }


    // 保存用户信息
    public void onLoginSuccess(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", user.getId());
        editor.putString("user_phone", user.getPhone());
        editor.putFloat("user_balance", (float) user.getBalance());
        editor.apply();
    }

    // SharedPrefsUtil.java 中添加
    public User getUser() {
        User user = new User();
        user.setId(sharedPreferences.getString("user_id", ""));
        user.setPhone(sharedPreferences.getString("user_phone", ""));
        user.setBalance(sharedPreferences.getFloat("user_balance", 0f));
        return user;
    }

    // 保存用户完整信息
    public void saveUser(User user) {
        if (user == null) return;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", user.getId());
        editor.putString("user_phone", user.getPhone());
        editor.putString("user_password", user.getPassword());
        // 使用float替代double保存余额
        editor.putFloat("user_balance", (float) user.getBalance());


        if (user.getCreateTime() != null) {
            editor.putString("user_create_time", DATE_FORMAT.format(user.getCreateTime()));
        }

        editor.apply();
    }

    // 获取保存的用户信息
    public User getSavedUser() {
        if (!hasLoggedInUser()) return null;

        User user = new User();
        user.setId(sharedPreferences.getString("user_id", ""));
        user.setPhone(sharedPreferences.getString("user_phone", ""));
        user.setPassword(sharedPreferences.getString("user_password", ""));
        // 从float转换回double
        user.setBalance((double) sharedPreferences.getFloat("user_balance", 0.0f));


        String createTimeStr = sharedPreferences.getString("user_create_time", "");
        if (!createTimeStr.isEmpty()) {
            try {
                user.setCreateTime(DATE_FORMAT.parse(createTimeStr));
            } catch (Exception e) {
                user.setCreateTime(new Date());
            }
        }

        return user;
    }

    // 检查是否有已登录的用户
    public boolean hasLoggedInUser() {
        return !sharedPreferences.getString("user_id", "").isEmpty();
    }

    // 保存登录状态
    public void setLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply();
    }

    // 获取登录状态
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("is_logged_in", false);
    }



    // 退出登录时调用
    public void onLogout() {
        // 清除用户信息
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_id");
        editor.remove("user_phone");
        editor.remove("user_password");
        editor.remove("user_balance");
        editor.remove("user_level");
        editor.remove("user_create_time");
        editor.remove("user_is_vip");
        editor.remove("token");
        editor.apply();
        setLoggedIn(false);
    }
}
//
//package com.dsq.DaFuWeng.utils;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import com.dsq.DaFuWeng.model.User;
//import com.dsq.DaFuWeng.network.RetrofitClient;
//
//public class SharedPrefsUtil {
//    private static SharedPrefsUtil instance;
//    private SharedPreferences sharedPreferences;
//    private static final String PREF_NAME = "DaFuWengPrefs";
//
//    private SharedPrefsUtil(Context context) {
//        // 使用RetrofitClient中的Application Context
//        Context appContext = context.getApplicationContext();
//        sharedPreferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//    }
//
//    // 确保获取Context参数
//    public static synchronized SharedPrefsUtil getInstance(Context context) {
//        if (instance == null) {
//            instance = new SharedPrefsUtil(context);
//        }
//        return instance;
//    }
//
//    // 获取Token
//    public String getToken() {
//        return sharedPreferences.getString("token", "");
//    }
//
//    // 保存Token
//    public void saveToken(String token) {
//        sharedPreferences.edit().putString("token", token).apply();
//    }
//
//    // 保存用户ID
//    public void saveUserId(String userId) {
//        sharedPreferences.edit().putString("user_id", userId).apply();
//    }
//
//    // 获取用户ID
//    public String getUserId() {
//        return sharedPreferences.getString("user_id", "");
//    }
//    // 保存用户信息
//    public void saveUser(User user) {
//        if (user != null) {
//            saveToken(user.getToken()); // 假设User类有getToken方法
//            saveUserId(user.getId());   // 假设User类有getId方法
//        }
//    }
//}