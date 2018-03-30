package com.sun.hotelproject.moudle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.Tip;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

import static com.sun.hotelproject.moudle.MainActivity.MacAddr;

/**
 * @author  sun 2018/3/12
 * 支付成功界面
 */
public class PaySussecsActivity extends BaseActivity {
    @BindView(R.id.speed_of_progress)ImageView speed_of_progress;
    @BindView(R.id.success)
    TextView success;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.relative1)
    RelativeLayout relative1;
    @BindView(R.id.relative2)
    RelativeLayout relative2;
    private static final String TAG = "PaySussecsActivity";
    private String roomNo;
    private String k;
    @Override
    protected int layoutID() {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void initData() {
        relative2.setVisibility(View.GONE);
        speed_of_progress.setImageResource(R.drawable.home_seven);
        k=getIntent().getStringExtra("k");
        if (k.equals("1")){
           tv1.setText("");
           tv2.setText("");
            roomNo = (String) CommonSharedPreferences.get("roomNum","");
            moveCard();
        }else if (k.equals("2")){

        }else {
            relative1.setVisibility(View.GONE);
            relative2.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);
            success.setText("退卡成功");
            reTrieve();
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


//    /**
//     * 连接
//     */
//    private void Connect(){
//        String strPort = "/dev/ttyS3";
//        int re = 0;
//        byte i;
//        String[] RecordInfo=new String[2];
//        int Baudate = 9600;
//        re = K720_Serial.K720_CommOpen(strPort);
//
//        if(re==0)
//        {
//            for(i = 0; i < 16; i++)
//            {
//                re = K720_Serial.K720_AutoTestMac(i, RecordInfo);
//                if(re == 0)
//                {
//                    MacAddr = i;
//                    break;
//                }
//            }
//            if(i == 16 && MacAddr == 0)
//            {
//                // Tip.show(this,"设备连接失败",false);
//                // ShowMessage("设备连接失败，错误代码为："+K720_Serial.ErrorCode(re, 0));
//                K720_Serial.K720_CommClose();
//            }
//        }
//    }
//
//    private void DisConnect(){
//        int nRet;
//        nRet = K720_Serial.K720_CommClose();
//        if(nRet == 0)
//        {
//            MacAddr = 0;
//            /**
//             * 连接断开后 休眠 1秒后跳到主界面
//             */
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//        else{
//            Tip.show(this,"设备断开失败",false);
//        }
//        Tip.show(this,"设备断开失败，错误代码为："+K720_Serial.ErrorCode(nRet, 0),false);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonSharedPreferences.put("beginTime","");
        CommonSharedPreferences.put("endTime","");
        CommonSharedPreferences.put("beginTime1","");
        CommonSharedPreferences.put("endTime1","");
        CommonSharedPreferences.put("content","");
       // DisConnect();
    }

    /**
     * 移动到读卡位置
     */
    private void moveCard(){
        tv1.setText("正在出卡中......");
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
        int nRet;
       // String cardNumber="2001";
       String cardNumber="100100100001"+roomNo ;
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
                    Log.e(TAG, "readCard: "+"写入的卡号为"+card_No);
                    getCard();
                }else Tip.show(this,"S50卡读卡失败"+nRet,false);
            }else Tip.show(this,"S50卡密钥错误"+nRet,false);
        } else Tip.show(this,"没有找到房间卡，请补充房卡",false);
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
          tv2.setText("出卡成功,请取走您的卡片和身份证");
        }
        else{
            tv2.setText("出卡失败");
        }
    }
}
