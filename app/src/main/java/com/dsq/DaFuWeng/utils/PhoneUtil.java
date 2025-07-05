package com.dsq.DaFuWeng.utils;

import java.util.regex.Pattern;

public class PhoneUtil {
    // 手机号码格式验证（简化版）
    private static final String PHONE_PATTERN = "^1[3-9]\\d{9}$";
    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    public static boolean isPhoneValid(String phone) {
        if (phone == null) return false;
        return pattern.matcher(phone).matches();
    }

    // 获取手机号码归属地（示例，实际需调用API）
    public static String getPhoneLocation(String phone) {
        if (!isPhoneValid(phone)) return "未知";
        // 实际项目中应通过运营商API获取归属地
        return "未知地区";
    }
}
