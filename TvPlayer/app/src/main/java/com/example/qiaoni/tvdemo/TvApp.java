package com.example.qiaoni.tvdemo;

import android.app.Application;

/**
 * Created by qiaoni on 16/11/21.
 */
public class TvApp extends Application {
    private static TvApp instance;
    public static TvApp getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }
}
