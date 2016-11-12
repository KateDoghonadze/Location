package com.example.mac05.myapplication;

import android.os.Build;

/**
 * Created by mac05 on 11/12/16.
 */
public class Tools {
    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean atLeastMarshmallow() {
        return getAndroidVersion() >= Build.VERSION_CODES.M;
    }

    public static boolean atLeastKitKat() {
        return getAndroidVersion() >= Build.VERSION_CODES.M;
    }

    public static boolean atLeastJellyBeanMR1() {
        return getAndroidVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

}

