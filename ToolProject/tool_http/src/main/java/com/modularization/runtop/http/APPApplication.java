package com.modularization.runtop.http;

import android.app.Application;

/**
 * Created by runTop on 2018/3/16.
 */
public class APPApplication extends Application {
    public static APPApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
