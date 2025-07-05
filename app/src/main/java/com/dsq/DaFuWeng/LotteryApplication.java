package com.dsq.DaFuWeng;

import android.app.Application;
import android.content.Context;

import com.dsq.DaFuWeng.network.RetrofitClient;

public class LotteryApplication extends Application {
    private static LotteryApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        RetrofitClient.init(this);
    }

    public static LotteryApplication getInstance() {
        return instance;
    }

    public Context getAppContext() {
        return getApplicationContext();
    }
}    