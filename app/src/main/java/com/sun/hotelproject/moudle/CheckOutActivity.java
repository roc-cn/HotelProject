package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.OkGo;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.Draw;
import com.sun.hotelproject.moudle.id_card.MiCardReader;
import com.sun.hotelproject.utils.CustomProgressDialog;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sun
 * 时间:2017/12/27
 * TODO:退房 -- 读房卡
 */
@Route(path = "/hotel/checkout")
public class CheckOutActivity extends BaseActivity{
    @BindView(R.id.toolBarBack)ImageView toolBarBack;
    @BindView(R.id.readCard)Button readCard;
    @BindView(R.id.card_tv)TextView card_tv;
    @BindView(R.id.tv1)TextView tv1;
    String orderId;
    private static final String TAG = "CheckOutActivity";
    private PopupWindow pop;
    private Dialog dialog;
    byte[] bysCardUid;
    MiCardReader mi;
    private String msgs;
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1){
              Intent intent1=new Intent(CheckOutActivity.this,DialogmsgActivity.class);
              intent1.putExtra("msgs",msgs);
              startActivityForResult(intent1,1);
            }

            super.handleMessage(msg);
        }
    };
    @Override
    protected int layoutID() {
        return R.layout.activity_chenk_out;
    }
    @Override
    protected void initData() {
           // Connect();
        handler.postDelayed(task,5000);
    }

    private Runnable task =new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,5*1000);//延迟5秒调用
            tv1.setText("数据读取中......");
            readCard();

        }
    };

    public void readCard(){


        byte[] bysKey = { (byte) 0x19, (byte) 0x94, (byte) 0x04,
                (byte) 0x28, (byte) 0x19, (byte) 0x70 };// 秘钥
        byte[] bysWrite = { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x0, 0x00, 0x39 };// 写入数据
        mi = new MiCardReader();
        mi.open();// 打开M1设备

        selectM1 sl = new selectM1();
        sl.start();
        try {
            sl.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bysCardUid = mi.selectM1Card();// 寻卡
/*
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					p = new printer();
					print(p);

				}
			}).start();
*/                String str = byte2HexStr(bysCardUid);
        byte[] result = mi.readM1Card(bysKey, (byte) 0, (byte) 1);// 读卡
        boolean results = mi.WriteM1Card(bysKey, (byte) 0, (byte) 1,
                bysWrite);// 写卡
        msgs=str;
        tv1.setText("读取成功");
        handler.sendEmptyMessage(1);
        mi.close();

    }

    @OnClick({R.id.toolBarBack,R.id.readCard})
    void OnClick(View view){
        switch (view.getId()){

            case R.id.toolBarBack:
                finish();
                break;
            case R.id.readCard:

                break;
                default:
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode== Activity.RESULT_OK){
            orderId = DataTime.orderId();
            String msg =data.getStringExtra("msg");
            if (msg.equals("Ok")){
                Intent intent =new Intent(CheckOutActivity.this,VerificationResultActivity.class);
                intent.putExtra("k","2");
                intent.putExtra("orderid",orderId);
                intent.putExtra("msgs",msgs);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(task);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(task);
        super.onDestroy();
    }

    class selectM1 extends Thread {
        @Override
        public void run() {
            bysCardUid = mi.selectM1Card();// 寻卡
            super.run();
        }
    }

    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;

        }
        return hs.toUpperCase();
    }
}
