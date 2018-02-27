package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.OkGo;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.Draw;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

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
    private static byte MacAddr = 0;
 /*   @BindView(R.id.date_main)TextView date_main;
    @BindView(R.id.time_main)TextView time_main;
    @BindView(R.id.weeked_main)TextView weeked_main;
    @BindView(R.id.date_name)TextView date_name;*/
   /* @BindView(R.id.shao_main)ImageView shao_main;
    @BindView(R.id.wx_main)ImageView wx_main;
    @BindView(R.id.zfb_main)ImageView zfb_main;
    @BindView(R.id.cz_main)Button cz_main;*/
  /*  String orderId;
    String paytype;*/
    private static final String TAG = "MainActivity";
    boolean flag =false;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

       /*     if (msg.what == 2){
                Draw draw= (Draw) msg.obj;
                    Intent intent =new Intent(MainActivity.this,PaymentActivity.class);
                    intent.putExtra("url",draw.getCodeimgurl());
                    intent.putExtra("paytype",paytype);
                    startActivity(intent);
            }*/
        }
    };

    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
      /*  date_main.setText(DataTime.curenData());
        time_main.setText(DataTime.curenTime());
        weeked_main.setText(DataTime.getWeekOfDate());
        date_name.setText("小兵酒店");*/
    }
    @OnClick({R.id.check_in,R.id.check_out,R.id.invoice,R.id.renwal})
    void OnClick(View v){
        switch (v.getId()){
            case R.id.check_in:
                Router.jumpL("/hotel/schedule"); //跳转到是否预定界面
                break;
            case R.id.check_out:
                Router.jumpL("/hotel/checkout"); //跳转到是否预定界面
                break;
            case R.id.invoice:
                flag=false;
                Connect();
              /*  paytype="2";
                getPost(paytype);*/
                break;
            case R.id.renwal:
                flag=true;
                Connect();
                break;


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
        DisConnect();
        // ShowMessage("回收到卡箱命令执行失败");
    }



    /**
     * 连接
     */
    private void Connect(){
        // dialog= CustomProgressDialog.createLoadingDialog(this,"正在出卡请稍候....");
        // dialog.show();
        //连接
        String strPort = "/dev/ttyS3";
        int re = 0;
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
                Tip.show(this,"设备连接失败",false);
                // ShowMessage("设备连接失败，错误代码为："+K720_Serial.ErrorCode(re, 0));
                K720_Serial.K720_CommClose();
            }
            else
            {
                if (flag)
                getCard();
                else
                reTrieve();
            }
        }
        else{


            //  ShowMessage("串口打开错误，错误代码为："+K720_Serial.ErrorCode(re, 0));
//					Com4052.Com4052Close(handle);
        }
    }

    private void DisConnect(){
        int nRet;
        nRet = K720_Serial.K720_CommClose();
        if(nRet == 0)
        {
            MacAddr = 0;
            /**
             * 连接断开后 休眠 1秒后跳到主界面
             */

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
         //   Router.jumpL("/hotel/main");
         //   finish();

        }
        else{
            Tip.show(this,"设备断开失败",false);
        }
        //  ShowMessage("设备断开失败，错误代码为："+K720_Serial.ErrorCode(nRet, 0));
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
            // dialog.dismiss();
            // ShowMessage("卡片移动到取卡位置成功");
            Tip.show(this,"出卡成功",true);
        }
        else{

            //  dialog.dismiss();
            Tip.show(this,"出卡失败",false);
            // ShowMessage("卡片移动到取卡位置失败");
        }
        DisConnect();
    }


   /* *//**
     * 扫码支付
     * @param paytype
     *//*
    public void getPost(String paytype) {
        orderId= DataTime.orderId();
        OkGo.<Draw>post(HttpUrl.URL+HttpUrl.SCANPAY)
                .tag(this)
                .params("mch_id", "100100100101")//"100100100101"
                .params("paytype",paytype)
                .params("orderid",orderId)
                .params("total_fee","0.01")
                .params("trantype","0")
                .params("couponid","")
                .params("campaignid","")
                .params("amount","")
                .params("camdamt","")
                .params("coudamt","")
                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<Draw> response) {
                        super.onSuccess(response);
                        if (response.body().getRescode().equals("00")){
                            Log.e(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                            Message msg= new Message();
                            msg.what =2;
                            msg.obj=response.body();
                            handler.sendMessage(msg);
                        }else {
                            Tip.show(MainActivity.this,response.body().getResult(),false);
                        }
                    }
                });
    }*/
    }
