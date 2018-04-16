package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;

import com.sun.hotelproject.moudle.camera.CameraFragment;
import com.sun.hotelproject.moudle.camera.control.SetParametersException;
import com.sun.hotelproject.moudle.camera.tools.MyMath;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.Animutils;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;
import com.sun.hotelproject.view.BannerBean;
import com.sun.hotelproject.view.BannerView;
import com.sun.hotelproject.view.MyVideoView;

import java.util.ArrayList;
import java.util.List;
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
    @BindView(R.id.myVideo)MyVideoView myVideo;
    @BindView(R.id.img_xufang)ImageView xufang;
   // @BindView(R.id.anim_iv)ImageView iv;
    float ivX,ivY;
    private int mCurrentTimer = 5;
    private boolean flag ;
    public static byte MacAddr = 0;
    private static final String TAG = "MainActivity";
    String url= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/123.flv";
    private int ids[] = new int[]{R.drawable.beijing,R.drawable.beijing1,R.drawable.beijing2};

    @BindView(R.id.banner)BannerView bannerView;
    private List<Integer> list = new ArrayList<>();

    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        List<BannerBean> mList = new ArrayList<BannerBean>();
        for(int i = 0 ;i<ids.length;i++){
            BannerBean bean = new BannerBean();
            bean.setType(0);
            bean.setDrawableforint(ids[i]);
            mList.add(bean);
        }
        bannerView.setData(mList);
        bannerView.setItemClickListener(new BannerView.ItemClickListener() {
            @Override
            public void click(View view, BannerBean bean,int position) {
                if(bean.getType()==0){
                    Toast.makeText(MainActivity.this,bean.getDrawableforint()+"  "+position,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,bean.getDrawableforurl()+"   "+position,Toast.LENGTH_SHORT).show();
                }
            }
        });

//        list.add(R.drawable.beijing);
//        list.add(R.drawable.beijing);
//        list.add(R.drawable.beijing);
//        list.add(R.drawable.beijing);
//        PagerAdapter adapter =new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return list.size();
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return view == object;
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                ImageView iv = new ImageView(MainActivity.this);
//                iv.setImageResource(list.get(position));
//                container.addView(iv);
//                return iv;
//            }
//        };
//      //  viewPager.setPageMargin(80);//相邻页面之间的像素距离
//        viewPager.setOffscreenPageLimit(3); //distahow许多页面将保持屏幕处于闲置状态。
//        viewPager.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        isRuning = false;
        toolBarTime.setVisibility(View.VISIBLE);
        toolbarBack.setVisibility(View.GONE);
        handler.postDelayed(runnable,1000);
        ActivityManager.getInstance().addActivity(this);
        Connect();
        /* 获取MediaController对象，控制媒体播放 */
        MediaController mc = new MediaController(this);

        myVideo.setMediaController(mc);
        /*  请求获取焦点 */
        myVideo.requestFocus();
        myVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,1000);
            toolBarTime.setText(DataTime.curenTime());
        }
    };
//    Runnable task =new Runnable() {
//        @Override
//        public void run() {
//            if (mCurrentTimer > 0) {
//                //time.setText(mCurrentTimer + "");
//
//                mCurrentTimer--;
//                handler.postDelayed(task, 1000);
//            } else {
//               if (flag){
//                   img_wuka.setVisibility(View.GONE);
//                   handler.removeCallbacks(task);
//               }else {
//                   wangshang.setVisibility(View.GONE);
//                   handler.removeCallbacks(task);
//               }
//                mCurrentTimer = 5;
//            }
//        }
//    };


    @OnClick({R.id.check_in,R.id.check_out,R.id.invoice,R.id.renwal,R.id.reserve,R.id.play})
    void OnClick(View v){
        Intent intent =new Intent();
        switch (v.getId()){
            case R.id.check_in://入住 首先判断卡箱是否有卡
                if (Utils.isFastClick()) {
                    int nRet;
                    byte[] StateInfo = new byte[4];
                    String[] RecordInfo = new String[2];
                    nRet = K720_Serial.K720_SensorQuery(MacAddr, StateInfo, RecordInfo);
                    if (nRet == 0) {
                        if (Integer.toHexString(StateInfo[3] & 0xFF).toUpperCase().equals("30")) {

                            intent.setClass(MainActivity.this, LayoutHouseActivity.class);
                            intent.putExtra("k", "1");
                            startActivity(intent);

                        } else {
                            //flag =true;
                           img_wuka.setVisibility(View.VISIBLE);
                            Animutils.alphaAnimation(img_wuka);
                           //handler.post(task);
                        }
                    }
                    // Tip.show(getApplicationContext(),"传感器状态查询成功，其值分别为："+Integer.toHexString(StateInfo[0] & 0xFF).toUpperCase()+" "+Integer.toHexString(StateInfo[1] & 0xFF).toUpperCase()+" "+Integer.toHexString(StateInfo[2] & 0xFF).toUpperCase()+" "+Integer.toHexString(StateInfo[3] & 0xFF).toUpperCase(),false);
                    else
                        Tip.show(getApplicationContext(), "状态查询失败", false);
                }
                break;
            case R.id.check_out: //退房
                if (Utils.isFastClick()) {
                    intent.setClass(MainActivity.this, CheckOutActivity.class);
                    intent.putExtra("k", "3");
                    startActivity(intent);
                }
                break;
            case R.id.invoice: //打印发票
                if (Utils.isFastClick()) {
//             getCard();
                //    Router.jumpL("/hotel/orderdetails");
                }
                break;
            case R.id.renwal: //续住
                if (Utils.isFastClick()) {
                    xufang.setVisibility(View.VISIBLE);
                    Animutils.alphaAnimation(xufang);
//                    intent.setClass(MainActivity.this, ChoiceActivity.class);
//                    intent.putExtra("k", "2");
//                    startActivity(intent);
                }
                break;
            case R.id.reserve://预定
                if (Utils.isFastClick()){
                    wangshang.setVisibility(View.VISIBLE);
                    Animutils.alphaAnimation(wangshang);
                }
               // getCard();
                break;
            case R.id.play://播放视频
                if (Utils.isFastClick()){
//                myVideo.setVisibility(View.VISIBLE);
//                    myVideo.setVideoPath(url);
//                    myVideo.start();
                }
                break;
                default:
                    break;
        }
    }
    @OnTouch({R.id.check_in,R.id.check_out,R.id.invoice,R.id.renwal,R.id.reserve,R.id.play})
    boolean OnTouch(View v, MotionEvent event){
//        ivX =iv.getX();
//        ivY =iv.getY();
        switch (v.getId()){
            case R.id.check_in://入住 首先判断卡箱是否有卡
              if (event.getAction() == MotionEvent.ACTION_DOWN){
                check_in.getBackground().setAlpha(128);
//                iv.setVisibility(View.VISIBLE);
//                  float lastX = event.getRawX();
//                  float lastY = event.getRawY();
//                  Animutils.translateAnimation(iv,ivX,lastX,ivY,lastY);
              }else if (event.getAction() == MotionEvent.ACTION_UP){
                  check_in.getBackground().setAlpha(255);
              }
                break;
            case R.id.check_out: //退房
                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    iv.setVisibility(View.VISIBLE);
//                    float lastX = event.getRawX();
//                    float lastY = event.getRawY();
//                    Animutils.translateAnimation(iv,ivX,lastX,ivY,lastY);
                    check_out.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    check_out.getBackground().setAlpha(255);
                }
                break;
            case R.id.invoice: //打印发票
                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    iv.setVisibility(View.VISIBLE);
//                    float lastX = event.getRawX();
//                    float lastY = event.getRawY();
//                    Animutils.translateAnimation(iv,ivX,lastX,ivY,lastY);
                    invoice.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    invoice.getBackground().setAlpha(255);
                }
                break;
            case R.id.renwal: //续住
                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    iv.setVisibility(View.VISIBLE);
//                    float lastX = event.getRawX();
//                    float lastY = event.getRawY();
//                    Animutils.translateAnimation(iv,ivX,lastX,ivY,lastY);
                    renwal.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    renwal.getBackground().setAlpha(255);
                }
                break;
            case R.id.reserve://预定
                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    iv.setVisibility(View.VISIBLE);
//                    float lastX = event.getRawX();
//                    float lastY = event.getRawY();
//                    Animutils.translateAnimation(iv,ivX,lastX,ivY,lastY);
                    reserve.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    reserve.getBackground().setAlpha(255);
                }
                break;
            case R.id.play://播放视频
                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    iv.setVisibility(View.VISIBLE);
//                    float lastX = event.getRawX();
//                    float lastY = event.getRawY();
//                    Animutils.translateAnimation(iv,ivX,lastX,ivY,lastY);
                    play.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    play.getBackground().setAlpha(255);
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 前端进卡
     */
    private void reTrieve(){
        int nRet;
        byte[] SendBuf=new byte[3];
        String[] RecordInfo=new String[2];
        SendBuf[0] = 0x46;
        SendBuf[1] = 0x43;
        SendBuf[2] = 0x38;
        nRet = K720_Serial.K720_SendCmd(MacAddr, SendBuf, 3, RecordInfo);
        if(nRet == 0)
            Tip.show(this,"前端进卡命令执行成功",true);
        else
           Tip.show(this,"前端进卡命令执行失败",false);
    }
    /**
     *查询卡箱
     */
    private void  getStates(){

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
               // Tip.show(this,"设备连接失败",false);
                // ShowMessage("设备连接失败，错误代码为："+K720_Serial.ErrorCode(re, 0));
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

    /**
     *
     * 取卡
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

            Tip.show(this,"出卡成功",true);
        }
        else{


            Tip.show(this,"出卡失败",false);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postAtTime(runnable,1000);
        isRuning = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisConnect();
       handler.removeCallbacks(runnable);
    }


    }
