package com.sabang.sp;

import android.app.Application;

public class SPApp extends Application {
    private static SPApp mInstance;

    public static SPApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }
}
