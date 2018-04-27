package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;

import com.sun.hotelproject.entity.BannerModel;
import com.sun.hotelproject.entity.RoomNo;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.Animutils;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;
import com.sun.hotelproject.view.MyVideoView;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * @author  sun
 * 时间：2017/11/22
 * TODO:主界面
 */
@Route(path = "/hotel/main")
public class MainActivity extends BaseActivity {
    @BindView(R.id.invoice)Button invoice; //发票
    @BindView(R.id.check_in)Button check_in;//入住
    @BindView(R.id.check_out)Button check_out;//退房
    @BindView(R.id.renwal)Button renwal;//续住
    @BindView(R.id.reserve)Button reserve;//打印发票
    @BindView(R.id.play)ImageView play;//播放
    @BindView(R.id.toolBarTime)TextView toolBarTime;
    @BindView(R.id.toolBar_logo) ImageView toolBar_logo;
    @BindView(R.id.toolbarBack)Button toolbarBack;
    @BindView(R.id.title2) TextView title2;
    @BindView(R.id.img_wuka)ImageView img_wuka;
    @BindView(R.id.img_wangshang)ImageView wangshang;
    //@BindView(R.id.myVideo)MyVideoView myVideo;
    @BindView(R.id.img_xufang)ImageView xufang;

    @BindView(R.id.imgchange) ImageView imgchange;
    @BindView(R.id.videoView) MyVideoView videoView;
    private Timer timer = new Timer();
    private TimerTask task;
    private int flag  = 0;
    //定义切换的图片的数组id
    int imgids[] = new int[]{R.drawable.beijing, R.drawable.beijing1,
            R.drawable.beijing2};
    int imgstart = 0;
    boolean isTrue = false;

    public static byte MacAddr = 0;
    private static final String TAG = "MainActivity";
    String url= Environment.getExternalStorageDirectory().getAbsolutePath()+"/123.mp4";

    //定时轮播图片，需要在主线程里面修改 UI
    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.d("数据", String.valueOf(imgstart));
                imgchange.setImageResource(imgids[imgstart++]);
            }else if(msg.what == 1){
                imgstart = 0;
                flag = 0;
//                imgchange.setVisibility(View.VISIBLE);
//                videoView.setVisibility(View.GONE);
//                flag = 2;//首先要将这个标签换掉 不然会出现因为定时器的原因导致视频播放不全的问题。
//                Log.d("测试", String.valueOf(flag));
//                imgchange.setVisibility(View.GONE);
//                videoView.setVisibility(View.VISIBLE);
//                initData();//播放视频的方法

            }else{
                Log.d(TAG, "啥我也不干  空定时器"  );
            }
        }
    };


    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        start();
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        isRuning = false;
        toolBarTime.setVisibility(View.VISIBLE);
        toolbarBack.setVisibility(View.GONE);
        handler.postDelayed(runnable,1000);
        ActivityManager.getInstance().addActivity(this);
        Connect();

    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,1000);
            toolBarTime.setText(DataTime.curenTime());
        }
    };

    /**选择播放图片还是播放视频*/
    public void start() {
        task = new TimerTask() {
            @Override
            public void run() {
                if (imgstart < imgids.length) {
                    Log.d(TAG, "length" +imgids.length );
                    Message msg = Message.obtain();
                    msg.what = 0;
                    myHandler.sendEmptyMessage(flag);
                    Log.d(TAG, "flag" +flag );
                } else {
                    if(flag == 2){
                        myHandler.sendEmptyMessage(flag);
                        //啥也不干
                    }else {
                        flag = 1;
                        Log.d("测试", String.valueOf(flag));
                        myHandler.sendEmptyMessage(flag);
                        //播放视频
                    }
                }
            }
        };
        //定时器开始执行
        timer.schedule(task,0,3000);

    }

    //播放视频
    public void initVideo() {

        //String uri = "android.resource://" + getPackageName() + "/" + R.raw.b;
        videoView.setVideoURI(Uri.parse(url));
        //开始播放
        videoView.start();

        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //防止出现视频播放错误的问题
        videoView.setOnErrorListener(videoErrorListener);

    }
    //防出现无法播放此视频窗口
    public MediaPlayer.OnErrorListener videoErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            return true;
        }
    };

    //回调方法
    private class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            /**再次启动图片的轮播,设置了imgstart为初始值*/
            /**多个视频可以在这进行切换，进行一次判断加入还有视频就播放，没有就走下面这一段*/
//            imgstart = 0;
//            flag = 0;
//            imgchange.setVisibility(View.VISIBLE);
//            videoView.setVisibility(View.GONE);
            videoView.start();
        }
    }
    @OnClick({R.id.check_in,R.id.check_out,R.id.invoice,R.id.renwal,R.id.reserve,R.id.play})
    void OnClick(View v){
        Intent intent =new Intent();
        switch (v.getId()){
            case R.id.check_in://入住 首先判断卡箱是否有卡
                if (Utils.isFastClick()) {
                      clean();
                        if (getStates().equals("30")) {
                            intent.setClass(MainActivity.this, LayoutHouseActivity.class);
                            intent.putExtra("k", "1");
                            startActivity(intent);
                        } else {
                            img_wuka.setVisibility(View.VISIBLE);
                            Animutils.alphaAnimation(img_wuka);
                        }
                }
                break;
            case R.id.check_out: //退房
                if (Utils.isFastClick()) {
                    clean();
                   // play.setBackgroundResource(R.drawable.btn_play);
                    intent.setClass(MainActivity.this, CheckOutActivity.class);
                    intent.putExtra("k", "3");
                    startActivity(intent);
                }
                break;
            case R.id.invoice: //打印发票
                if (Utils.isFastClick()) {
                    clean();
                  moveCard();
                  //  play.setBackgroundResource(R.drawable.btn_play);
                }
                break;
            case R.id.renwal: //续住
                if (Utils.isFastClick()) {
//                    clean();
//                    xufang.setVisibility(View.VISIBLE);
//                    Animutils.alphaAnimation(xufang);
                    reTrieve();
                }
                break;
            case R.id.reserve://预定
                if (Utils.isFastClick()){
                    clean();

                    intent.setClass(MainActivity.this,ChoiceActivity.class);
                    intent.putExtra("k","4");
                    startActivity(intent);
                }
                break;
            case R.id.play://播放视频
                if (Utils.isFastClick()){
                    isTrue =!isTrue;
                    if (isTrue) {
                        play.setBackgroundResource(R.drawable.play2);
                        flag = 2;
                        Log.d("测试", String.valueOf(flag));
                        imgchange.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                        initVideo();//播放视频的方法
                    }else {
                      clean();
                    }
                }
                break;

                default:
                    break;
        }
    }
    public void  clean(){
        isTrue = false;
        play.setBackgroundResource(R.drawable.btn_play);
        imgstart = 0;
        flag = 0;
        imgchange.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
    }

    @OnTouch({R.id.check_in,R.id.check_out,R.id.invoice,R.id.renwal,R.id.reserve,R.id.play})
    boolean OnTouch(View v, MotionEvent event){

        switch (v.getId()){
            case R.id.check_in://入住 首先判断卡箱是否有卡
              if (event.getAction() == MotionEvent.ACTION_DOWN){
                check_in.getBackground().setAlpha(128);
              }else if (event.getAction() == MotionEvent.ACTION_UP){
                  check_in.getBackground().setAlpha(255);
              }
                break;
            case R.id.check_out: //退房
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    check_out.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    check_out.getBackground().setAlpha(255);
                }
                break;
            case R.id.invoice: //打印发票
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    invoice.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    invoice.getBackground().setAlpha(255);
                }
                break;
            case R.id.renwal: //续住
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    renwal.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    renwal.getBackground().setAlpha(255);
                }
                break;
            case R.id.reserve://预定
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    reserve.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    reserve.getBackground().setAlpha(255);
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     *
     * 出卡
     */
    private void getCard(){
        int nRet;
        byte[] SendBuf=new byte[3];
        String[] RecordInfo=new String[2];
        SendBuf[0] = 0x46;
        SendBuf[1] = 0x43;
        SendBuf[2] = 0x34;
        nRet = K720_Serial.K720_SendCmd(MacAddr, SendBuf, 3, RecordInfo);
        if(nRet == 0){

        }
        else{
            // tv2.setText("出卡失败");
        }
    }

    /**
     *查询卡箱
     */
    private String  getStates(){
        int nRet;
        String state = null;
        byte[] StateInfo = new byte[4];
        String[] RecordInfo = new String[2];
        nRet = K720_Serial.K720_SensorQuery(MacAddr, StateInfo, RecordInfo);
        if (nRet == 0) {
            state = Integer.toHexString(StateInfo[3] & 0xFF).toUpperCase();
        }else {
            Tip.show(getApplicationContext(),"状态值查询失败!",false);
        }
        return state;
    }

    /**
     * 连接
     */
    private void Connect(){
        String strPort = "/dev/ttyS3";
        int re;
        byte i;
        String[] RecordInfo=new String[2];
        int Baudate = 9600;
        re = K720_Serial.K720_CommOpen(strPort);

        if(re==0)
        {
            for(i = 0; i < 16; i++)
            {
                re = K720_Serial.K720_AutoTestMac(i, RecordInfo);
                if(re == 0)
                {
                    MacAddr = i;
                    break;
                }
            }
            if(i == 16 && MacAddr == 0)
            {
                K720_Serial.K720_CommClose();
            }
        }
    }
    private void DisConnect(){
        int nRet;
        nRet = K720_Serial.K720_CommClose();
        if(nRet == 0)
        {
            MacAddr = 0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            Tip.show(this,"设备断开失败",false);
        }
        Tip.show(this,"设备断开失败，错误代码为："+K720_Serial.ErrorCode(nRet, 0),false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if ( timer !=null){
            timer.cancel();
            timer =null;
            timer = new Timer();
            start();
        }
        timer =new Timer();
        start();
        handler.post(runnable);
        isRuning = false;


    }
    private  void  cancel(){
        if (task != null) {
        task.cancel();
        task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancel();
        handler.removeCallbacks(runnable);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisConnect();
        cancel();
       handler.removeCallbacks(runnable);
    }

    /**
     * 移动到读卡位置
     */
    private void moveCard(){
     //   tv1.setText("正在出卡中......");
        int nRet;
        byte[] SendBuf=new byte[3];
        String[] RecordInfo=new String[2];
        SendBuf[0] = 0x46;
        SendBuf[1] = 0x43;
        SendBuf[2] = 0x37;
        nRet = K720_Serial.K720_SendCmd(MacAddr, SendBuf, 3, RecordInfo);
        if(nRet == 0){
            writeCard();
        }
        else
            Tip.show(getApplicationContext(),"卡片移动到读卡位置失败",false);
    }

    /**
     * 写卡
     */
    public void writeCard(){
        StringBuilder strRand= new StringBuilder();
        for(int i=0;i<4;i++){
            strRand.append(String.valueOf((int) (Math.random() * 10)));
        }
        int nRet;
        // String cardNumber="2001";
        String cardNumber="100100100001"+strRand ;
        byte [] wrbuf=cardNumber.getBytes();
        byte[] SendBuf=new byte[3];
        byte[] key = { (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff};
        String[] RecordInfo=new String[2];
        SendBuf[0] = 0x46;
        SendBuf[1] = 0x43;
        SendBuf[2] = 0x38;
        nRet = K720_Serial.K720_S50DetectCard(MacAddr, RecordInfo);//寻卡
        if (nRet == 0){
            Log.e(TAG, "readCard: "+"S50卡寻卡命令执行成功" );
            nRet=K720_Serial.K720_S50LoadSecKey(MacAddr,(byte)0x02,(byte)0x30,key,RecordInfo);//密码检验
            if(nRet == 0)
            {
                Log.e(TAG, "readCard: "+"S50卡检验命令执行成功" );
                nRet=K720_Serial.K720_S50WriteBlock(MacAddr,(byte)0x02,(byte)0x02,wrbuf,RecordInfo);
                if(nRet == 0){
                    String cardNum= DataTime.bytesToHexString(wrbuf);
                    String card_No=DataTime.hexStr2Str(cardNum);
                   // handler.removeCallbacks(task);
                    Log.e(TAG, "readCard: "+"写入的卡号为"+card_No);
                    getCard();
                }else{
                    //reTrieve();
                    Tip.show(this,"S50卡写卡失败,正在重新写卡...",false);
                }
            } else{
                // reTrieve();
                Tip.show(this,"S50卡密钥错误,正在重新验证密钥...",false);
            }
        } else {
            Tip.show(this,"没有找到房间卡",false);
        }
    }
    /**
     * 回收到卡箱
     */
    private void reTrieve(){
        int nRet;
        byte[] SendBuf=new byte[3];
        String[] RecordInfo=new String[2];
        SendBuf[0] = 0x44;
        SendBuf[1] = 0x42;
        nRet = K720_Serial.K720_SendCmd(MacAddr, SendBuf, 2, RecordInfo);
        if(nRet == 0)
            Tip.show(getApplicationContext(),"卡片回收成功",true);
            //   ShowMessage("回收到卡箱命令执行成功");
        else
            Tip.show(getApplicationContext(),"回收到卡箱命令执行失败",false);
        // ShowMessage("回收到卡箱命令执行失败");
    }
}
