package szxb.com.poslibrary.manager;

import android.app.Activity;

/**
 * 作者: Tangren on 2017/7/25
 * 包名：com.wxx.activityManager
 * 邮箱：996489865@qq.com
 * 一句话描述
 */

public interface ActivityState {

    //当前的Activity
    Activity currentActivity();

    //任务栈Activity总数
    int count();


    //判断应用是否可见,是否在运行
    boolean isVisable();


}
