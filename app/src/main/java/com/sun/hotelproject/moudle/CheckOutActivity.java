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
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.Draw;


import com.sun.hotelproject.entity.QueryRoomBill;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

import static com.sun.hotelproject.moudle.MainActivity.MacAddr;

/**
 * @author sun
 * 时间:2017/12/27
 * TODO:退房 -- 读房卡
 */
@Route(path = "/hotel/checkout")
public class CheckOutActivity extends BaseActivity{

//    @BindView(R.id.readCard)Button readCard;
//    @BindView(R.id.card_tv)TextView card_tv;
    @BindView(R.id.toolbarBack)Button toolBarBack;
    @BindView(R.id.tv1)TextView tv1;
    String orderId;
    private static final String TAG = "CheckOutActivity";
    private String msgs;
    private String roomNo;
    private Double price =0.00;
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            if (msg.what ==1){
//              Intent intent1=new Intent(CheckOutActivity.this,DialogmsgActivity.class);
//              intent1.putExtra("msgs",msgs);
//              startActivityForResult(intent1,1);
//            }
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
            readCard();

        }
    };
    @OnClick(R.id.toolbarBack)
    void OnClick(){
        finish();
    }


    public void readCard(){
        tv1.setText("数据读取中......");
        int nRet;
        byte[] SendBuf=new byte[3];
        byte[] rebuf=new byte[16];
        byte[] key = { (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff};
        String[] RecordInfo=new String[2];
        SendBuf[0] = 0x46;
        SendBuf[1] = 0x43;
        SendBuf[2] = 0x38;
        nRet = K720_Serial.K720_S50DetectCard(MacAddr, RecordInfo);
        if (nRet == 0){
            Log.e(TAG, "readCard: "+"S50卡寻卡执行成功" );
                nRet=K720_Serial.K720_S50LoadSecKey(MacAddr,(byte)0x02,(byte)0x30,key,RecordInfo);//密码检验
                if(nRet == 0)
                {
                    Log.e(TAG, "readCard: "+"S50卡检验命令执行成功" );
                    nRet=K720_Serial.K720_S50ReadBlock(MacAddr,(byte)0x02,(byte)0x02,rebuf,RecordInfo);
                    if(nRet == 0){
                        String cardNum=DataTime.bytesToHexString(rebuf);
                        String card_No=DataTime.hexStr2Str(cardNum);
                        Log.e(TAG, "readCard: "+"读出的卡号为"+card_No);
                        roomNo = card_No.substring(card_No.length()-4,card_No.length());
                        tv1.setText("卡号为:"+roomNo);
                        handler.removeCallbacks(task);
                        queryRoomBill("3",roomNo);
//                        Intent intent =new Intent(CheckOutActivity.this,PaymentActivity.class);
//                        intent.putExtra("card_No",card_No);
//                        startActivity(intent);
//                        finish();
                    }else Tip.show(this,"S50卡读卡失败"+nRet,false);
                }else Tip.show(this,"S50卡密钥错误"+nRet,false);
        } else Tip.show(this,"没有找到房间卡，请插入房卡",false);
    }

    /**
     * 查询客房账单
     */
    private void queryRoomBill(String querytype,String querydata){
        OkGo.<QueryRoomBill>post(HttpUrl.QUERYROOMBILL)
                .tag(this)
                .params("mchid",mchid)
                .params("querytype",querytype)
                .params("querydata",querydata)
                .execute(new JsonCallBack<QueryRoomBill>(QueryRoomBill.class) {
                    @Override
                    public void onSuccess(Response<QueryRoomBill> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
                        if (response.body().getRescode().equals("0000")){
                            if (Double.valueOf(response.body().getDatalist().get(0).getAddprice())>price){

                            Intent intent =new Intent(CheckOutActivity.this,PaymentActivity.class);
                                intent.putExtra("price",response.body().getDatalist().get(0).getAddprice());
                                intent.putExtra("k","3");
                                intent.putExtra("inorderpmsno",response.body().getDatalist().get(0).getInorderpmsno());
                                intent.putExtra("list", (Serializable) response.body().getDatalist().get(0).getBills());
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent =new Intent(CheckOutActivity.this,PaySussecsActivity.class);
                                intent.putExtra("k","3");
                                startActivity(intent);
                                finish();
                            }

                        }else {
                            Tip.show(getApplicationContext(),response.body().getResult(),false);
                        }
                    }
                });
    }


  /*  public void readCard(){
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
*//*
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					p = new printer();
					print(p);

				}
			}).start();
*//*                String str = byte2HexStr(bysCardUid);
        byte[] result = mi.readM1Card(bysKey, (byte) 0, (byte) 1);// 读卡
        boolean results = mi.WriteM1Card(bysKey, (byte) 0, (byte) 1,
                bysWrite);// 写卡
        msgs=str;
        tv1.setText("读取成功");
        handler.sendEmptyMessage(1);
        mi.close();

    }*/


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==1 && resultCode== Activity.RESULT_OK){
//            orderId = DataTime.orderId();
//            String msg =data.getStringExtra("msg");
//            if (msg.equals("Ok")){
//                Intent intent =new Intent(CheckOutActivity.this,VerificationResultActivity.class);
//                intent.putExtra("k","2");
//                intent.putExtra("orderid",orderId);
//                intent.putExtra("msgs",msgs);
//                startActivity(intent);
//                finish();
//            }
//        }
//
//    }

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



}
