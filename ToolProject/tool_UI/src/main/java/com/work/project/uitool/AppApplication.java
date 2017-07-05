package com.work.project.uitool;

import android.app.Application;

/**
 *aa
 * Created by bazengliang on 2017/7/4.
 */

public class AppApplication extends Application {
    private static AppApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppApplication getInstance() {
        return instance;
    }
}
