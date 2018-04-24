package com.sun.hotelproject.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun.hotelproject.R;
import com.sun.hotelproject.moudle.LoginActivity;
import com.sun.hotelproject.moudle.MainActivity;
import com.sun.hotelproject.receiver.NetBoradcastReceiver;
import com.sun.hotelproject.utils.CheckNetWork;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.Utils;


import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import butterknife.Unbinder;


/**
 * @author sun
 * 时间：2017/11/27
 * TODO:基类Activity
 */
public abstract class BaseActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private NetBoradcastReceiver receiver;
    private static final String TAG = "BaseActivity";
    @BindView(R.id.toolBar_logo)
    ImageView toolBar_logo;
    @BindView(R.id.toolbarBack)
    Button toolbarBack;
    public   int time = 90;
    public Window window;
    public  int isTime = 1;

    protected abstract int layoutID();


    protected abstract void initData();
    private Unbinder unbinder;
    public int flag = 1;
    public boolean isRuning = true; //是否启动线程
    @SuppressLint("HandlerLeak")
    public Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //得到窗口
        window=getWindow();
        //隐藏返回键等虚拟按键
        WindowManager.LayoutParams params=window.getAttributes();
        params.systemUiVisibility= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
        //设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //保持屏幕 常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(layoutID());
        unbinder = ButterKnife.bind(this);

        toolbarBack.setText("返回("+time+"s)");
        initView();
        initData();
        if (isRuning){
            if (isTime ==1){
                time = 90;
            }else {
                time = 30;
            }
            handler.post(timeRunnable);
        }else {

        }
    }
    public Runnable timeRunnable =new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            if (time>0 ){
                time--;
                handler.postDelayed(this,1000);
                toolbarBack.setText("返回("+time+"s)");
            }else {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        }
    };

    protected void initView() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        receiver = new NetBoradcastReceiver();
        registerReceiver(receiver,intentFilter);
//        window.setCallback(new SimpleWinCallback(window.getCallback()){
//            @Override
//            public boolean dispatchTouchEvent(MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    handler.removeCallbacks(timeRunnable);
//                    time = 90;
//                    toolbarBack.setText("返回("+time+"s)");
//                }if (event.getAction() == MotionEvent.ACTION_UP){
//                    if (isRuning){
//                        handler.postDelayed(timeRunnable,1000);
//                    }
//                }
//
//                return super.dispatchTouchEvent(event);
//            }
//        });
    }
    @OnClick({R.id.toolbarBack,R.id.toolBar_logo})
    void OnClick(View v) {
        switch (v.getId()){
            case R.id.toolbarBack:
                Utils.isFastClick();
                if (flag == 1){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else {
                    finish();
                }
                break;
            case R.id.toolBar_logo:
                Utils.isFastClick();
                if (isRuning) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    CommonSharedPreferences.put("data","管理");
                }
                break;
        }

    }


    @OnTouch({R.id.toolBar_logo})
    boolean OnTouch(View v,MotionEvent event) {
        switch (v.getId()){
            case R.id.toolBar_logo:
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    toolBar_logo.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    toolBar_logo.getBackground().setAlpha(255);
                }
                break;
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isRuning){
            if (isTime ==1){
                time = 90;
            }else {
                time = 30;
            }
            handler.post(timeRunnable);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (isRuning){
            if (isTime ==1){
                time = 90;
            }else {
                time = 30;
            }
        }
        handler.removeCallbacks(timeRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unbinder.unbind();
        handler.removeCallbacks(timeRunnable);
    }

}
