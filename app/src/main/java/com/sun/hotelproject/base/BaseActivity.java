package com.sun.hotelproject.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun.hotelproject.R;


import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * @author sun
 * 时间：2017/11/27
 * TODO:基类Activity
 */
public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.toolBar_logo)
    ImageView toolBar_logo;
    @BindView(R.id.toolbarBack)
    Button toolbarBack;
    public   int time = 30;
    Window window;
    public String mchid ="100100100101";
    protected abstract int layoutID();

    protected abstract void initData();
    private Unbinder unbinder;
    public boolean isRuning = true; //是否启动线程
    @SuppressLint("HandlerLeak")
    public Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
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
           // handler.postDelayed(timeRunnable,1000);
        }
    }
    public Runnable timeRunnable =new Runnable() {
        @Override
        public void run() {
            if (time>0 ){
                time--;
                handler.postDelayed(this,1000);
                toolbarBack.setText("返回("+time+"s)");
            }else {
                finish();
            }
        }
    };

    protected void initView() {
        window.setCallback(new SimpleWinCallback(window.getCallback()){
            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    handler.removeCallbacks(timeRunnable);
                    time = 30;
                    toolbarBack.setText("返回("+time+"s)");
                }if (event.getAction() == MotionEvent.ACTION_UP){
                    if (isRuning){
                        handler.postDelayed(timeRunnable,1000);
                    }
                }

                return super.dispatchTouchEvent(event);
            }
        });
    }

    @OnClick({R.id.toolBar_logo,R.id.toolbarBack})
    void OnClick(View v){
        switch (v.getId()){
            case R.id.toolBar_logo:
                finish();
                break;
            case R.id.toolbarBack:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
//        if (isRuning){
//            handler.postDelayed(timeRunnable,1000);
//        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(timeRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        handler.removeCallbacks(timeRunnable);
    }

}
