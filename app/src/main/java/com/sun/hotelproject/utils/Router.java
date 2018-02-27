package com.sun.hotelproject.utils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sun.hotelproject.R;

/**
 * Created by win7 on 2017/8/21.
 */

public class Router {

    //直接跳转,可能有拦截
    public static void jump(String url) {
        ARouter.getInstance()
                .build(url)
                .withTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain)
                .navigation();
    }

    //绿色跳转无拦截
    public static void jumpL(String url) {
        ARouter.getInstance()
                .build(url)
                .withTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain)
                .greenChannel()
                .navigation();
    }
}
