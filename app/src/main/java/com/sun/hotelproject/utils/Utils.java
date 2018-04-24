package com.sun.hotelproject.utils;

import android.content.Context;
import android.util.Log;

import java.util.regex.Pattern;

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


    public final static String PHONE_PATTERN="^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17([0,1,6,7,]))|(18[0-2,5-9]))\\d{8}$";

    /**
     * 判断是否是手机号
     * @param s 判断的数字
     * @return 真或假
     */
    public static boolean isPhone(String s) {
        boolean isPhone = Pattern.compile(PHONE_PATTERN).matcher(s).matches();
        return isPhone;
    }
}
