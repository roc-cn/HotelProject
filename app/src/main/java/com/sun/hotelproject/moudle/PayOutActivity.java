package com.sun.hotelproject.moudle;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author  sun
 * 时间 2018-1-9
 *退房卡activity
 */

public class PayOutActivity extends BaseActivity {
    @BindView(R.id.check_out)Button check_out;
    @BindView(R.id.toolBarBack)ImageView toolBarBack;
    String url;
    private static byte MacAddr = 0;
    @Override
    protected int layoutID() {
        return R.layout.activity_pay_out;
    }

    @Override
    protected void initData() {

    }
    @OnClick(R.id.check_out)
    void OnClick(View v){
        Connect();
    }

    @OnClick(R.id.toolBarBack)
    void Click(View v){
       finish();
    }
    /**
     * 连接
     */
    private void Connect(){
        // dialog= CustomProgressDialog.createLoadingDialog(getApplicationContext(),"正在出卡请稍候....");
        //dialog.show();
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
                Tip.show(getApplicationContext(),"设备连接失败",false);
                // ShowMessage("设备连接失败，错误代码为："+K720_Serial.ErrorCode(re, 0));
                K720_Serial.K720_CommClose();
            }
            else
            {
                reTrieve();
            }
        }
        else{


            //  ShowMessage("串口打开错误，错误代码为："+K720_Serial.ErrorCode(re, 0));
//					Com4052.Com4052Close(handle);
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
     * 断开连接
     */
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
            Router.jumpL("/hotel/main");
            finish();

        }
        else{
            Tip.show(getApplicationContext(),"设备断开失败",false);
        }
        //  ShowMessage("设备断开失败，错误代码为："+K720_Serial.ErrorCode(nRet, 0));
    }

}
