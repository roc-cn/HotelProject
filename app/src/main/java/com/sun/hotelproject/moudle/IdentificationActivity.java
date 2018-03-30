package com.sun.hotelproject.moudle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Route;

import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;

import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.moudle.id_card.IDCardInfo;
import com.sun.hotelproject.moudle.id_card.IDCardReaderCallBack;
import com.sun.hotelproject.moudle.id_card.IDCarderReader;

import com.sun.hotelproject.moudle.id_card.ReadIDThread;
import com.szxb.smart.pos.jni_interface.Card_Sender;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author  sun
 * 时间 2017/11/22
 * 身份证界面
 */
@Route(path = "/hotel/identification")
public class IdentificationActivity extends BaseActivity {
    @BindView(R.id.piv_tv)
    TextView piv_tv;
    @BindView(R.id.speed_of_progress)
    ImageView speed_of_progress;
    private String name;  //姓名
    private String id_cardNo; //身份证号
    private String birth;//出生日期
    private static final String TAG = "SecondActivity";
    private Thread thread;
    private IDCardReaderCallBack readerCallBack;
    private IDCarderReader idCarderReader;
    LayoutHouse house;
    byte[] bs;
    boolean key;
    private GuestRoom.Bean gBean;
    private String locksign;
    private String k;


    int count = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //count = 6;
                if (msg.what == 1) {
                    IDCardInfo idCardInfo = (IDCardInfo) msg.obj;


                    if (idCardInfo != null) {
                        Log.e(TAG, "handleMessage: "+idCardInfo.toString() );
                        piv_tv.setText("读取成功!");
                        handler.removeCallbacks(task);

                        name = idCardInfo.getStrName();
                        id_cardNo = idCardInfo.getStrIdCode();
                        birth = idCardInfo.getStrBirth();
                        if (k.equals("1")){
                            Intent intent = new Intent(IdentificationActivity.this,FaceRecognitionActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("id_CardNo",id_cardNo);
                            intent.putExtra("birth",birth);
                            intent.putExtra("bean",gBean);
                            intent.putExtra("locksign",locksign);
                            intent.putExtra("k",k);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(IdentificationActivity.this,RenwalActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("id_CardNo",id_cardNo);
                            intent.putExtra("birth",birth);
                            intent.putExtra("k",k);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        piv_tv.setText("读取失败！");
                    }
                }
        }
    };

    @Override
    protected int layoutID() {
        return R.layout.activity_identification;
    }

    @Override
    protected void initData() {
        speed_of_progress.setImageResource(R.drawable.home_four);
        count = 0;
        k=getIntent().getStringExtra("k");
        if (k.equals("1")){
            gBean= (GuestRoom.Bean) getIntent().getSerializableExtra("bean");
            locksign=getIntent().getStringExtra("locksign");
        }

        Card_Sender my_Card_Sender = new Card_Sender();
        int[] nStatus = new int[1];
        boolean zt = my_Card_Sender.TY_GetStatus(nStatus);
//        house= (LayoutHouse) getIntent().getSerializableExtra("house");
        Log.e("发卡机状态:", "发卡机状态:" + zt);

        handler.postDelayed(task,5000);
    }
    Runnable task=new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,5000);
            readCard_No();
        }
    };


    private void  readCard_No(){
        //读身份证卡号
        Thread f = new Thread(new Runnable() {
            public void run() {
                while (true) {

                    try {
                        ReadIDThread rt = new ReadIDThread();
                        bs = rt.ReadCardUID();

                        break;

                    } catch (Exception e) {

                    }

                }

            }
        });
        f.start();
        try {
            f.join();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try{
            Thread.sleep(500);
        }catch(Exception E){

        }

        System.out.println(11111);
        key = true;

        IDCardReaderCallBack rc = new IDCardReaderCallBack() {
            @Override
            public void onReadIdComplete(int iMode, IDCardInfo idCardInfo) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = idCardInfo;

                handler.sendMessage(msg);
            }
        };

        IDCarderReader iR = new IDCarderReader();
        iR.startReaderIDCard(rc, 1);
    }


    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(task);
    }
}
