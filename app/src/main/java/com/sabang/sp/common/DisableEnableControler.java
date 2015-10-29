package com.sabang.sp.common;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
/**
 * Odered by KangSeokHyun on 2015-10-29.
 */



public class DisableEnableControler {
    //activity false -> true    getView
    public static void call(boolean enable, @NonNull View view) {
        view.setEnabled(enable);
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup)view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                call(enable, vg.getChildAt(i));
            }
        }
    }

    //false -> true     getWindow
    public static void call(boolean enable, @NonNull Window window) {
        View rootView = window.getDecorView().getRootView();
        call(enable, rootView);
    }
}
