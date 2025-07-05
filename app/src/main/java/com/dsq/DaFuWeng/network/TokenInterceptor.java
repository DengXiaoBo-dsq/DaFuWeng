package com.dsq.DaFuWeng.network;

import android.content.Context;
import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private Context context;

    // 修改构造函数，接收Context参数
    public TokenInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 使用传入的context获取SharedPrefsUtil实例
        String token = SharedPrefsUtil.getInstance(context).getToken();

        Request originalRequest = chain.request();

        // 如果token不为空，添加到请求头
        if (token != null && !token.isEmpty()) {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(originalRequest);
    }
}