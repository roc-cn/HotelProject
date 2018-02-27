package com.sun.hotelproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.entity.Order;
import com.sun.hotelproject.moudle.MainActivity;
import com.sun.hotelproject.moudle.PaymentActivity;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;

/**
 * Created by sun on 2018/2/26.
 * 开机广播
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    private String orderId;
    private static final String TAG = "BootBroadcastReceiver";
    /**
     * 可以实现开机自动打开软件并运行app
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("XRGPS", "BootReceiver.onReceive: " + intent.getAction());
        System.out.println("自启动程序即将执行");
        //MainActivity就是开机显示的界面
        if (intent.getAction().equals(ACTION)){
            Intent intent1=new Intent(context, MainActivity.class);
            //下面这句话必须加上才能开机自动运行app的界面
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        getPost(context);
    }

    public void getPost(final Context context){
        orderId= DataTime.orderId();
        OkGo.<Order>post(HttpUrl.URL+HttpUrl.QUERYORDER)
                .tag(this)
                .params("MCHID","100100100101")
                .params("ORDERID",orderId)
                .execute(new JsonCallBack<Order>(Order.class) {
                    @Override
                    public void onSuccess(Response<Order> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("00")){
                            if (response.body().getVarList().size() !=0){
                                if (response.body().getVarList().get(0).getPAYSTS().equals("1"))
                                    Tip.show(context,"支付成功",true);

                            }else {
                               Tip.show(context,"没有查到数据",false);
                            }
                        }

                    }
                });
    }
}
