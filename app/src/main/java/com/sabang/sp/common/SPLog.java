package com.sabang.sp.common;
import android.util.Log;

/**
 * Created by mgsmurf on 2015. 8. 17..
 */
public class SPLog {
    private static final String TAG = "SP";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void v(String msg) {
        Log.v(TAG, msg);
    }

    public static void wtf(String msg) {
        Log.wtf(TAG, msg);
    }
}