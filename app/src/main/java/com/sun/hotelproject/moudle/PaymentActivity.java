package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.squareup.picasso.Picasso;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.entity.Order;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.CustomProgressDialog;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;
import com.szxb.smart.pos.jni_interface.printer;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author  sun
 * 时间：2017/11/27
 * TODO:支付界面
 */
@Route(path = "/hotel/payment")
public class PaymentActivity extends BaseActivity {
    //@BindView(R.id.web)WebView webView;
    @BindView(R.id.type)TextView type;
    @BindView(R.id.check_in)TextView check_in;
    @BindView(R.id.check_out)TextView check_out;
    @BindView(R.id.content1)TextView content1;
    @BindView(R.id.content2)TextView content2;
    @BindView(R.id.tishi)TextView tishi;
    @BindView(R.id.houseNum)TextView houseNum;
    @BindView(R.id.preson)TextView preson;
    @BindView(R.id.phone)TextView phone;
    @BindView(R.id.money)TextView money;
    @BindView(R.id.shaoma)TextView shaoma;
    //@BindView(R.id.shaoma2)TextView shaoma2;
    @BindView(R.id.toolBarBack)ImageView toolBarBack;
    @BindView(R.id.linear_ll)LinearLayout linear_ll;
    @BindView(R.id.linear_3)LinearLayout linear_3;
    @BindView(R.id.msg)TextView msg;
    @BindView(R.id.houseType)ImageView houseType;
    @BindView(R.id.relative)RelativeLayout relative;

    @BindView(R.id.webUrl)ImageView webUrl;
    private static byte MacAddr = 0;
    private String url;
    private String paytype;
    private LayoutHouse house;
    private String beginTime;
    private String endTime;
    private String content;
    private String name;
    private String orderId;
    private Dialog dialog;
    private String k;
    private String msgs;
    private int flag =5;
    private static final String TAG = "PaymentActivity";
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1 ){
                Intent intent =new Intent(PaymentActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }if (msg.what == 3){
                Intent intent = new Intent(PaymentActivity.this,PayOutActivity.class);
                startActivity(intent);
                finish();

            }
          /*  if (msg.what == 0){
                Bitmap bmp=(Bitmap)msg.obj;
                img.setImageBitmap(bmp);
            }
*/
            super.handleMessage(msg);
        }
    };


    @Override
    protected int layoutID() {
        return R.layout.activity_payment;
    }

    @Override
    protected void initView() {
        super.initView();
        handler.postDelayed(task,5000);
        //thread.start();
    }
    private Runnable task =new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,5*1000);//延迟5秒调用
            flag--;
            if (flag<=0){
                handler.removeCallbacks(task);
                Tip.show(getApplicationContext(),"支付超时！",false);
                finish();
            }
            if (k.equals("1")){
                getPost();
            }else {
                getPost2();
            }

        }
    };



    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        Intent intent=getIntent();
        k=intent.getStringExtra("k");
        if (k.equals("1")) {
            url = intent.getStringExtra("url");
            paytype = intent.getStringExtra("paytype");
            house = (LayoutHouse) intent.getSerializableExtra("house");
            endTime = String.valueOf(CommonSharedPreferences.get("endTime", ""));
            beginTime = String.valueOf(CommonSharedPreferences.get("beginTime", ""));
            content = String.valueOf(CommonSharedPreferences.get("content", ""));
            name = getIntent().getStringExtra("name");
            orderId = getIntent().getStringExtra("orderId");

            Log.e(TAG, "initData: orderId--->" + orderId);

            // Bitmap bitmap =getURLimage(url);
            // img.setImageBitmap(bitmap);

            if (house.getType().equals("大床房")){
                houseType.setImageResource(R.drawable.house1);
            }
            if (house.getType().equals("标间")){
                houseType.setImageResource(R.drawable.house2);
            }
            if (house.getType().equals("单人间")){
                houseType.setImageResource(R.drawable.house3);
            }
            type.setText("房型："+house.getType());
            check_in.setText("入住时间："+beginTime);
            check_out.setText("离店时间："+endTime);
            content1.setText("共："+content);
            content2.setText(house.getAcreage() + "|" + house.getIsbreakfast() + "|" + house.getBed_type() + "|" + house.getIswindow());
            tishi.setText("订单确认后" + house.getIscancel() + "如未入住 ，酒店将扣除全额夜房费" + "\n" + "房间整晚保留,请及时入住");
            money.setText("支付金额："+house.getPrice());
            houseNum.setText("房间\u3000数："+"1间");
            preson.setText("入住\u3000人："+name);
            phone.setText("手机\u3000号:"+"无手机号码");

        }else {
            relative.setVisibility(View.GONE);
            linear_ll.setVisibility(View.GONE);
            linear_3.setVisibility(View.VISIBLE);
            msgs=getIntent().getStringExtra("msgs");
            url = intent.getStringExtra("url");
            orderId = getIntent().getStringExtra("orderId");
            paytype = intent.getStringExtra("paytype");

           // shaoma2.setText(paytype + "扫一扫");
           msg.setText("卡号"+msgs+"\n"+"消费："+"\n"+"方便面，数量1，单价 4元"+"\n"+"毛巾,数量2，单价 8元"+"\n"+"共计：20元");
        }
        Log.e(TAG, "initData: "+orderId );
        if (paytype.equals("1")) {
            paytype = "微信";
        } else {
            paytype = "支付宝";
        }
        shaoma.setText( paytype + "扫码");
       Picasso.with(this).load(url).into(webUrl);
       /* webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT>=21){
                    Uri uri=request.getUrl();
                    webView.loadUrl(uri.toString());
                }
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        //设置webview的属性
        //设置支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        //设置js可以打开窗口
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //设置允许缩放
        webView.getSettings().setSupportZoom(true);
        //设置允许展示缩放按钮
      //  webView.getSettings().setBuiltInZoomControls(true);
        //设置可以任意比例缩放
      //  webView.getSettings().setUseWideViewPort(true);
        //设置自适应屏幕大小
        webView.getSettings().setLoadWithOverviewMode(true);
        //WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
      //  webView.getSettings().setUseWideViewPort(true);

        //app启动时加载本地html网页
        webView.loadUrl(url);*/
    }

    @OnClick(R.id.toolBarBack)
    void OnClick(View v){
        finish();
    }
 /*   Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            Bitmap bmp = getURLimage(url);
            Message msg = new Message();
            msg.what = 0;
            msg.obj = bmp;
            //System.out.println("000");
            handler.sendMessage(msg);
        }
    });
*/
    private void getPost(){
        OkGo.<Order>post(HttpUrl.URL+HttpUrl.QUERYORDER)
                .tag(this)
                .params("MCHID","100100100101")
                .params("ORDERID",orderId)
                .execute(new JsonCallBack<Order>(Order.class) {
                    @Override
                    public void onSuccess(Response<Order> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("00")){
                            if (response.body().getVarList().size() !=0){
                               if (response.body().getVarList().get(0).getPAYSTS().equals("1"))
                                Tip.show(PaymentActivity.this,"支付成功",true);
                                Connect();

                            }else {
                               // Tip.show(PaymentActivity.this,"没有查到数据",false);
                            }
                        }

                    }
                });
    }
    private void getPost2(){
        OkGo.<Order>post(HttpUrl.URL+HttpUrl.QUERYORDER)
                .tag(this)
                .params("MCHID","100100100101")
                .params("ORDERID",orderId)
                .execute(new JsonCallBack<Order>(Order.class) {
                    @Override
                    public void onSuccess(Response<Order> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("00")){
                            if (response.body().getVarList().size() !=0){
                                if (response.body().getVarList().get(0).getPAYSTS().equals("1"))
                                    Tip.show(PaymentActivity.this,"支付成功",true);
                              //  Connect();
                                handler.sendEmptyMessage(3);

                            }else {
                                // Tip.show(PaymentActivity.this,"没有查到数据",false);
                            }
                        }

                    }
                });
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
                getCard();
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
            Router.jumpL("/hotel/main");
            finish();

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
        byte[] fou = HexString2Bytes(sb(beginTime));
        byte[] fiv = HexString2Bytes(sb("离店日期  Departure Date"));
        byte[] sev = HexString2Bytes(sb(endTime));
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
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static String sb(String content) {
        String str = content;

        String hexString = "0123456789ABCDEF";
        byte[] bytes;
        try {
            bytes = str.getBytes("GBK");// 如果此处不加编码转化，得到的结果就不是理想的结果，中文转码
            StringBuilder sb = new StringBuilder(bytes.length * 2);

            for (int i = 0; i < bytes.length; i++) {
                sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
                sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
                // sb.append("");
            }
            str = sb.toString();

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;

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
}
