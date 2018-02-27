package szxb.com.poslibrary.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者: Tangren on 2017/7/25
 * 包名：com.wxx.activityManager
 * 邮箱：996489865@qq.com
 * 统一管理Activity
 */

public class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks, ActivityState {

    //存储Activity
    private static List<Activity> activityList = Collections.synchronizedList(new LinkedList<Activity>());

    //存储可见的Activity
    private static List<Activity> resumeActivity = Collections.synchronizedList(new LinkedList<Activity>());


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        System.out.println("activity = [" + activity.getClass().getSimpleName() + "]:被添加到了List列表中了");
        activityList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        System.out.println("activity = [" + activity.getClass().getSimpleName() + "]:被添加到可见的List列表中了");
        if (!resumeActivity.contains(activity)) {
            resumeActivity.add(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        System.out.println("activity = [" + activity.getClass().getSimpleName() + "]:被移除了可见的List列表中了");
        if (resumeActivity == null && resumeActivity.isEmpty()) return;
        if (resumeActivity.contains(activity))
            resumeActivity.remove(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        System.out.println("activity = [" + activity.getClass().getSimpleName() + "]:被移除了List列表中了");
        if (activityList == null && activityList.isEmpty()) return;
        if (activityList.contains(activity))
            activityList.remove(activity);

    }

    @Override
    public Activity currentActivity() {
        return activityList.size() > 0 ? activityList.get(0) : null;
    }

    @Override
    public int count() {
        return activityList.size();
    }

    @Override
    public boolean isVisable() {
        return resumeActivity.size() > 0;
    }

    /**
     * 退出app
     */
    public static void appExit() {
        System.out.println("退出应用程序!");
        finishAll();
    }

    /**
     * 结束所有Activity
     */
    public static void finishAll() {
        if (activityList == null) return;
        for (Activity activity : activityList) {
            System.out.println("activity.getClass().getSimpleName()finishAll = " + activity.getClass().getSimpleName());
            activity.finish();
        }
        activityList.clear();
    }

    /**
     * 结束指定类名的Activity
     *
     * @param clazz Activity类名
     */
    public static void finishActivityClass(Class<?> clazz) {
        System.out.println("结束指定类名的clazz.getSimpleName() = " + clazz.getSimpleName());
        if (activityList == null || activityList.isEmpty()) {
            return;
        }
        //为了防止并发修改异常(ConcurrentModificationException)所以选择迭代器移除需要移除的Activity
        Iterator<Activity> iterator = activityList.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass().equals(clazz)) {
                iterator.remove();
                activity.finish();
                activity = null;

            }
        }
    }

    /**
     * 结束多个Activity
     *
     * @param clazz 多个activity
     */
    public static void finishActivityClass(Class<?>... clazz) {
        Class<?>[] clazzs = clazz;
        if (activityList == null || activityList.isEmpty()) {
            return;
        }
        //为了防止并发修改异常(ConcurrentModificationException)所以选择迭代器移除需要移除的Activity
        if (clazzs != null && clazzs.length > 0) {
            for (int i = 0; i < clazzs.length; i++) {
                Iterator<Activity> iterator = activityList.iterator();
                while (iterator.hasNext()) {
                    Activity activity = iterator.next();
                    if (activity.getClass().equals(clazzs[i])) {
                        iterator.remove();
                        activity.finish();
                        activity = null;

                    }
                }
            }
        }


    }


    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    public static void finishActivity(Activity activity) {
        System.out.println("结束指定的activity.getClass().getSimpleName() = " + activity.getClass().getSimpleName());
        if (activityList == null || activityList.isEmpty()) {
            return;
        }
        if (activity != null) {
            activityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 销毁当前的activity
     */
    public static void finishCurrentActivity() {
        if (activityList == null || activityList.isEmpty()) {
            return;
        }
        Activity activity = activityList.get(activityList.size() - 1);
        finishActivity(activity);
    }


    /**
     * 获取栈顶的Activity名字
     *
     * @return string
     */
    public String getTopActivityName() {
        Activity mBaseActivity = null;
        synchronized (activityList) {
            final int size = activityList.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = activityList.get(size);
        }
        return mBaseActivity.getClass().getName();
    }
    /**
     * 获取栈顶Activity的实例
     *
     * @return Activity
     */
    public Activity getTopActivity() {
        Activity mBaseActivity = null;
        synchronized (activityList) {
            final int size = activityList.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = activityList.get(size);
        }
        return mBaseActivity;

    }

    /**
     * 通过类名找到Activity
     *
     * @param cls 类名
     * @return 通过类名找到Activity
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (activityList != null) {
            for (Activity activity : activityList) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }


    public static List getAllActivity() {
        return activityList;
    }
}
