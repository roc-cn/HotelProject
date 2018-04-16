package com.sun.hotelproject.utils;

import android.content.Context;
import android.util.Log;

/**
 * Created by a'su's on 2018/4/8.
 * 工具类
 */

public class Utils {
    private static final String TAG = "Utils";
    private static long lastClickTime;
    public static boolean isFastClick() {
        if (System.currentTimeMillis()-lastClickTime<800){
            Log.e(TAG, "isFastClick: 重复点击" );
            return false;
        }
        lastClickTime=System.currentTimeMillis();
        return true;
    }
}
