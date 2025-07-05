//package com.dsq.DaFuWeng.network;
//
//import android.app.Application;
//import android.content.Context;
//
//import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
//import java.util.concurrent.TimeUnit;
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class RetrofitClient {
//    private static RetrofitClient instance;
//    private Retrofit retrofit;
//    private ApiService apiService;
//    private final String BASE_URL="your_url";
//    private static Application application;
//
//    private RetrofitClient() {
//        // 初始化OkHttpClient
//        OkHttpClient client = createOkHttpClient();
//
//        // 初始化Retrofit
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BuildConfig.BASE_URL)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        // 创建API服务实例
//        apiService = retrofit.create(ApiService.class);
//    }
//
//    // 初始化Application
//    public static void init(Application app) {
//        application = app;
//    }
//
//    // 获取Application实例
//    public static Application getApplication() {
//        if (application == null) {
//            throw new IllegalStateException("RetrofitClient未初始化，请先调用init(Application)");
//        }
//        return application;
//    }
//
//    // 获取RetrofitClient实例
//    public static RetrofitClient getInstance() {
//        if (instance == null) {
//            instance = new RetrofitClient();
//        }
//        return instance;
//    }
//
//    // 获取ApiService实例
//    public ApiService getApiService() {
//        return apiService;
//    }
//
//    // 创建OkHttpClient
//    private OkHttpClient createOkHttpClient() {
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        if (BuildConfig.DEBUG) {
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        } else {
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//        }
//
//        return new OkHttpClient.Builder()
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .readTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
//                .addInterceptor(loggingInterceptor)
//                .addInterceptor(chain -> {
//                    // 添加Token到请求头
//                    String token = SharedPrefsUtil.getInstance(application).getToken();
//                    okhttp3.Request originalRequest = chain.request();
//                    okhttp3.Request newRequest = originalRequest.newBuilder()
//                            .header("Authorization", "Bearer " + token)
//                            .build();
//                    return chain.proceed(newRequest);
//                })
//                .build();
//    }
//}
package com.dsq.DaFuWeng.network;

import static com.dsq.DaFuWeng.utils.AppConstants.BASE_URL;
import static com.dsq.DaFuWeng.utils.AppConstants.DEBUG;

import android.app.Application;
import android.content.Context;

import com.dsq.DaFuWeng.utils.SharedPrefsUtil;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance;
    private Retrofit retrofit;
    private ApiService apiService;
    private static Application application;

    private RetrofitClient() {
        // 初始化OkHttpClient
        OkHttpClient client = createOkHttpClient();

        // 初始化Retrofit，使用BuildConfig中的BASE_URL
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建API服务实例
        apiService = retrofit.create(ApiService.class);
    }

    // 初始化Application
    public static void init(Application app) {
        application = app;
    }

    // 获取Application实例
    public static Application getApplication() {
        if (application == null) {
            throw new IllegalStateException("RetrofitClient未初始化，请先调用init(Application)");
        }
        return application;
    }

    // 获取RetrofitClient实例
    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    // 获取ApiService实例
    public ApiService getApiService() {
        return apiService;
    }

    // 创建OkHttpClient
    private OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // 使用BuildConfig.DEBUG控制日志级别
        if (DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    // 添加Token到请求头
                    String token = SharedPrefsUtil.getInstance(application).getToken();
                    okhttp3.Request originalRequest = chain.request();
                    okhttp3.Request newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();
    }
}