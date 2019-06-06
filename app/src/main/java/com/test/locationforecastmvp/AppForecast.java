package com.test.locationforecastmvp;

import android.app.Application;

public class AppForecast extends Application {
    private static final String LOG_TAG = "AppForecast";

    private static AppForecast instance;

    public static AppForecast getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
