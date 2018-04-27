package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.szxb.smart.pos.jni_interface.printer;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

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
    @BindView(R.id.linear_sp4)LinearLayout linear_sp4;
    @BindView(R.id.sp4_img8)ImageView sp4_img8;
    @BindView(R.id.sp4_tv8)TextView sp4_tv8;
    @BindView(R.id.sp3_layout3)LinearLayout sp3_layout3;
    @BindView(R.id.sp2_tv7)TextView sp2_tv7;
    @BindView(R.id.sp3_tv4)TextView sp3_tv4;
    @BindView(R.id.sp3_content3)TextView sp3_content3;
    @BindView(R.id.sp_img7)ImageView sp_img7;
    @BindView(R.id.sp2_img7)ImageView sp2_img7;
    @BindView(R.id.sp3_img4)ImageView sp3_img4;
    @BindView(R.id.sp3_img_layout)LinearLayout sp3_img_layout;
    @BindView(R.id.fial) Button fial;
    @BindView(R.id.toolbarBack)Button toolbarBack;
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
        isTime = 0;
        ActivityManager.getInstance().addActivity(this);
        relative2.setVisibility(View.GONE);
        daoSimple = new DaoSimple(this);
        k=getIntent().getStringExtra("k");
        switch (k) {
            case "1":
                linear_sp1.setVisibility(View.VISIBLE);
                sp_img7.setVisibility(View.VISIBLE);
                sp_tv7.setTextColor(getResources().getColor(R.color.Swrite));
                sp_tv7.setBackgroundResource(R.drawable.oval_shape);
                relative1.setVisibility(View.VISIBLE);
                tv1.setText("");
                tv2.setText("");
                roomNum = (String) CommonSharedPreferences.get("roomNum", "");
                moveCard();
                break;
            case "2":
                linear_sp2.setVisibility(View.VISIBLE);
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
            case "3":
                relative1.setVisibility(View.GONE);
                relative2.setVisibility(View.VISIBLE);
                linear_sp3.setVisibility(View.VISIBLE);
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
            case "4":
                tv1.setText("正在办理入住....");
                tv2.setText("");
                linear_sp4.setVisibility(View.VISIBLE);
                sp4_img8.setVisibility(View.VISIBLE);
                sp4_tv8.setTextColor(getResources().getColor(R.color.Swrite));
                sp4_tv8.setBackgroundResource(R.drawable.oval_shape);
                relative1.setVisibility(View.VISIBLE);
                tv1.setText("");
                tv2.setText("");
                roomNum = (String) CommonSharedPreferences.get("roomNum", "");
                moveCard();
                break;
            default:
                break;
        }
    }
    @OnClick({R.id.fial,R.id.toolbarBack})
    void OnClick(View v){
        switch (v.getId()) {
            case R.id.fial:
                reTrieve();
                DisConnect();
                Connect();
                 moveCard();
                fial.setVisibility(View.GONE);
            break;
            case R.id.toolbarBack:
                startActivity(new Intent(PaySussecsActivity.this,MainActivity.class));
                finish();
                break;
                default:
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
       // handler.removeCallbacks(task);
    }

    /**
     * 写卡
     */
    void wridtCard(){
        String cardNumber="100100100001"+roomNum;
        byte [] wrbuf=cardNumber.getBytes();
        int nRet;
        String[] RecordInfo=new String[2];
        nRet=K720_Serial.K720_S50WriteBlock(MacAddr,(byte)0x02,(byte)0x02,wrbuf,RecordInfo);
        if(nRet == 0){
            String cardNum= DataTime.bytesToHexString(wrbuf);
            String card_No=DataTime.hexStr2Str(cardNum);
//            String cardNum=bytesToHexString(wrbuf);
//            card_No=hexStr2Str(cardNum);
            roomNo =daoSimple.roomNoSel(roomNum);
            if (roomNo!=null){
                daoSimple.roomNoUpd(roomNo.getData(),DataTime.currentTime());
            }else {
                daoSimple.roomNoAdd(new RoomNo(roomNum,DataTime.currentTime()));
            }
            getCard();

           // Tip.show(getApplicationContext(),"S50卡读卡命令执行成功"+nRet+"写入的卡号为"+card_No,true);
            Log.e("TAG", "wridtCard: "+card_No );
        }
        else{
            Log.e(TAG, "wridtCard: 写卡命令执行失败");
            Tip.show(getApplicationContext(),"写卡命令执行失败",false);
        }
    }

    /**
     * 卡密钥验证
     */
    void checkCard(){
        tv1.setText("密钥验证......");
        int nRet;
        byte[] key = { (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff};
        String[] RecordInfo=new String[2];
        nRet=K720_Serial.K720_S50LoadSecKey(MacAddr,(byte)0x02,(byte)0x30,key,RecordInfo);
        if(nRet == 0)
        {
            wridtCard();
//            ShowMessage("S50卡检验命令执行成功"+nRet);
        }
        else{
            fial.setVisibility(View.VISIBLE);
            tv1.setText("密钥验证失败......");
        }
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
          //  handler.postDelayed(task,1000);
            checkCard();
        }
        else {
            tv1.setText("卡片移动到读卡位置失败");
            Log.e(TAG, "moveCard: 卡片移动到读卡位置失败");
        }
    }






//    /**
//     * 写卡
//     */
//    public void writeCard(){
//        int nRet;
//       // String cardNumber="2001";
//       String cardNumber="100100100001"+roomNum ;
//        byte [] wrbuf=cardNumber.getBytes();
//        byte[] SendBuf=new byte[3];
//        byte[] key = { (byte) 0xff, (byte) 0xff,
//                (byte) 0xff, (byte) 0xff, (byte) 0xff,
//                (byte) 0xff};
//        String[] RecordInfo=new String[2];
//        SendBuf[0] = 0x46;
//        SendBuf[1] = 0x43;
//        SendBuf[2] = 0x38;
////        nRet = K720_Serial.K720_S50DetectCard(MacAddr, RecordInfo);//寻卡
////        if (nRet == 0){
////            Log.e(TAG, "readCard: "+"S50卡寻卡命令执行成功" );
//            nRet=K720_Serial.K720_S50LoadSecKey(MacAddr,(byte)0x02,(byte)0x30,key,RecordInfo);//密码检验
//            if(nRet == 0)
//            {
//                Log.e(TAG, "readCard: "+"S50卡检验命令执行成功" );
//                nRet=K720_Serial.K720_S50WriteBlock(MacAddr,(byte)0x02,(byte)0x02,wrbuf,RecordInfo);
//                if(nRet == 0){
//                    String cardNum= DataTime.bytesToHexString(wrbuf);
//                    String card_No=DataTime.hexStr2Str(cardNum);
//                   // handler.removeCallbacks(task);
//                    Log.e(TAG, "readCard: "+"写入的卡号为"+card_No);
//                    roomNo =daoSimple.roomNoSel(roomNum);
//                    if (roomNo!=null){
//                        daoSimple.roomNoUpd(roomNo.getData(),DataTime.currentTime());
//                    }else {
//                        daoSimple.roomNoAdd(new RoomNo(roomNum,DataTime.currentTime()));
//                    }
//                    getCard();
//                }else{
//                    fial.setVisibility(View.VISIBLE);
//                    //reTrieve();
//                    Tip.show(this,"S50卡写卡失败,正在重新写卡...",false);
//                }
//            } else{
//                fial.setVisibility(View.VISIBLE);
//               // reTrieve();
//                Tip.show(this,"S50卡密钥错误,正在重新验证密钥...",false);
//            }
////        } else {
////            Tip.show(this,"没有找到房间卡",false);
////        }
//    }


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
            tv1.setText("出卡中请稍候...");
            tv2.setText("出卡成功,请取走您的卡片和身份证");
        }
        else{
            tv1.setText("出卡失败");
            tv2.setText("");
        }
    }

    private void print(printer p) {
        // BitmapDrawable drawable = (BitmapDrawable) this.getResources()
        // .getDrawable(R.drawable.jjj);
        // Bitmap bitmap = drawable.getBitmap();
        // p.printBitmap(bitmap, 0, 0);
        p.PrinterOpen();
        byte[] b = new byte[] { (byte) 0x1c, (byte) 0x70, (byte) 0x01,
                (byte) 0x00 };
        int data = p.PrinterWrite(b, 4);
        byte[] one = HexString2Bytes(sb("CITIGO"));
        byte[] fir = HexString2Bytes(sb("房间号  Room No"));
        byte[] sec = HexString2Bytes(sb("308"));
        byte[] thr = HexString2Bytes(sb("入住日期  Arrival Date"));
        byte[] fou = HexString2Bytes(sb(""));
        byte[] fiv = HexString2Bytes(sb("离店日期  Departure Date"));
        byte[] sev = HexString2Bytes(sb(""));
        byte[] six = HexString2Bytes(sb("_______________________________"));
        byte[] th = HexString2Bytes(sb("打印时间  Print Time "+ DataTime.currentTime()));
        p.PrinterWrite(printer.getCmdEscEN(1), 3);// 加粗
        p.PrinterWrite(printer.getCmdEscAN(1), 3);// 居中
        byte[] a = { (byte) 1, (byte) 2 };
        p.PrinterWrite(a, 2);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdGs_N(1), printer.getCmdGs_N(1).length);
        p.PrinterWrite(printer.getCmdEscSo(), printer.getCmdEscSo().length);
        p.PrinterWrite(one, one.length);
        p.PrinterWrite(printer.getCmdGs_N(0), printer.getCmdGs_N(0).length);
        p.PrinterWrite(printer.getCmdEscDc4(), printer.getCmdEscDc4().length);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        // 两倍宽 getCmdEscSo() getCmdFf()换行
        p.PrinterWrite(fir, fir.length);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdEscSo(), printer.getCmdEscSo().length);
        p.PrinterWrite(sec, sec.length);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdEscDc4(), 2);// 加宽还原
        p.PrinterWrite(thr, thr.length);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(fou, fou.length);

        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(fiv, fiv.length);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(sev, sev.length);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdEsc___N(0), 3);
        p.PrinterWrite(six, six.length);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(th, th.length);
        p.PrinterWrite(printer.getCmdLf(), 1);
        // p.PrinterWrite(re, re.length);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);
        p.PrinterWrite(printer.getCmdLf(), 1);

        byte[] b1 = new byte[] { (byte) 0x1b, (byte) 0x69 };// 切纸

        int lock = p.PrinterWrite(b1, 2);
        System.out.println(lock + "打印完成？0：其他");

        p.PrinterClose();
    }


    public static byte[] HexString2Bytes(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        return (byte) (_b0 ^ _b1);
    }

    public static String sb(String content) {
        String str = content;

        String hexString = "0123456789ABCDEF";
        byte[] bytes;
        try {
            bytes = str.getBytes("GBK");// 如果此处不加编码转化，得到的结果就不是理想的结果，中文转码
            StringBuilder sb = new StringBuilder(bytes.length * 2);

            for (byte aByte : bytes) {
                sb.append(hexString.charAt((aByte & 0xf0) >> 4));
                sb.append(hexString.charAt((aByte & 0x0f)));
                // sb.append("");
            }
            str = sb.toString();

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
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
}
