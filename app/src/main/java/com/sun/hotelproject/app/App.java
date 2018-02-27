package com.sun.hotelproject.app;


import com.alibaba.android.arouter.launcher.ARouter;

import szxb.com.poslibrary.LibApp;

/**
 *@author sun
 *时间：2017/11/22
 *TODO:app
 */

public class App extends LibApp {

    private static App instance = null;

    public static App getInstance() {
        synchronized (App.class) {
            if (instance == null) {
                instance = new App();
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(this);
    }
}
