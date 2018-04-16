package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.dao.DaoSimple;
import com.sun.hotelproject.entity.RoomNo;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.Tip;

import org.w3c.dom.Text;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

import static com.sun.hotelproject.moudle.MainActivity.MacAddr;

/**
 * @author  sun 2018/3/12
 * 支付成功界面
 */
public class PaySussecsActivity extends BaseActivity {

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
    @BindView(R.id.sp_tv7)
    TextView sp_tv7;
    @BindView(R.id.linear_sp1)LinearLayout linear_sp1;
    @BindView(R.id.linear_sp2)LinearLayout linear_sp2;
    @BindView(R.id.linear_sp3)LinearLayout linear_sp3;
    @BindView(R.id.sp3_layout3)LinearLayout sp3_layout3;
    @BindView(R.id.sp2_tv7)TextView sp2_tv7;
    @BindView(R.id.sp3_tv4)TextView sp3_tv4;
    @BindView(R.id.sp3_content3)TextView sp3_content3;
    @BindView(R.id.sp_img7)ImageView sp_img7;
    @BindView(R.id.sp2_img7)ImageView sp2_img7;
    @BindView(R.id.sp3_img4)ImageView sp3_img4;
    @BindView(R.id.sp3_img_layout)LinearLayout sp3_img_layout;
    private static final String TAG = "PaySussecsActivity";
    private String roomNum;
    private String k;
    private DaoSimple daoSimple;
    private RoomNo roomNo;
    private String  querytype;
    private String flags;//是否有消费的标志
    @Override
    protected int layoutID() {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void initData() {
        ActivityManager.getInstance().addActivity(this);
        relative2.setVisibility(View.GONE);

        k=getIntent().getStringExtra("k");
        switch (k) {
            case "1":
                sp_img7.setVisibility(View.VISIBLE);
                linear_sp2.setVisibility(View.GONE);
                linear_sp3.setVisibility(View.GONE);
                sp_tv7.setTextColor(getResources().getColor(R.color.Swrite));
                sp_tv7.setBackgroundResource(R.drawable.oval_shape);

                daoSimple = new DaoSimple(this);
                relative1.setVisibility(View.VISIBLE);
                tv1.setText("");
                tv2.setText("");
                roomNum = (String) CommonSharedPreferences.get("roomNum", "");
                moveCard();
                break;
            case "2":
                linear_sp1.setVisibility(View.GONE);
                linear_sp3.setVisibility(View.GONE);
                sp_img7.setVisibility(View.VISIBLE);
                sp_tv7.setTextColor(getResources().getColor(R.color.Swrite));
                sp_tv7.setBackgroundResource(R.drawable.oval_shape);

                querytype = getIntent().getStringExtra("querytype");
                if (querytype.equals("3")) {
                    relative1.setVisibility(View.VISIBLE);
                    getCard();
                } else {
                    relative1.setVisibility(View.VISIBLE);
                }

                break;
            default:
                relative1.setVisibility(View.GONE);
                relative2.setVisibility(View.VISIBLE);
                linear_sp1.setVisibility(View.GONE);
                linear_sp2.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                flags =getIntent().getStringExtra("flag");
                if (flags.equals("0")){
                    sp3_img4.setVisibility(View.VISIBLE);
                    sp3_layout3.setVisibility(View.GONE);
                    sp3_img_layout.setVisibility(View.GONE);
                    sp3_tv4.setText("3");
                    sp3_content3.setVisibility(View.GONE);
                    sp3_tv4.setBackgroundResource(R.drawable.oval_shape);
                    sp3_tv4.setTextColor(getResources().getColor(R.color.Swrite));
                }else {
                    sp3_img4.setVisibility(View.VISIBLE);
                    sp3_tv4.setBackgroundResource(R.drawable.oval_shape);
                    sp3_tv4.setTextColor(getResources().getColor(R.color.Swrite));
                }
                success.setText("退卡成功");
                reTrieve();
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
        // ShowMessage("回收到卡箱命令执行失败");
    }

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
       String cardNumber="100100100001"+roomNum ;
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
            if (k.equals("1")){
                roomNo =daoSimple.roomNoSel(roomNum);
                if (roomNo!=null){
                    daoSimple.roomNoUpd(roomNo.getData(),DataTime.currentTime());
                }else {
                    daoSimple.roomNoAdd(new RoomNo(roomNum,DataTime.currentTime()));
                }
              tv2.setText("出卡成功,请取走您的卡片和身份证");
            }else  if (k.equals("2")){

            }
        }
        else{
           // tv2.setText("出卡失败");
        }
    }
}
