package szxb.com.poslibrary;

import android.app.Application;

import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;

import szxb.com.poslibrary.manager.MyActivityLifecycleCallbacks;

/**
 * 作者: Tangren on 2017/7/26
 * 包名：szxb.com.poslibrary
 * 邮箱：996489865@qq.com
 * 一句话描述
 */

public class LibApp extends Application {
    @Override
    public void onCreate() {
        NoHttp.initialize(InitializationConfig.newBuilder(this)
                .networkExecutor(new OkHttpNetworkExecutor())
                .retry(3)
                .build());
        Logger.setDebug(true);
        Logger.setTag("信息记录");
        this.registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
        super.onCreate();

    }
}
