package com.example.memory_leak_in_message_system_demo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author zhangzhiyi
 * @version 1.0
 * @createTime 2016/3/8 15:35
 * @projectName AndroidDemo-MessageSystemDemo
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}