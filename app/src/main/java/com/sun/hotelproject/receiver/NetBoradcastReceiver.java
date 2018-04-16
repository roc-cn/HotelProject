package com.sun.hotelproject.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.utils.CheckNetWork;

/**
 * Created by a'su's on 2018/4/10.
 */

public class NetBoradcastReceiver extends BootBroadcastReceiver {
    private boolean access = false;
    private static final String TAG = "NetBoradcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()){
            if (!access){
                Log.e(TAG, "onReceive: "+"有网" );
                access = true;
            }
        }else{
            if (access){
                Toast.makeText(context,"网络不给力",Toast.LENGTH_SHORT).show();
                access = false;
            }
        }
    }
}
