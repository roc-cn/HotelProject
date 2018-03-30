package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.Button;
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
import com.sun.hotelproject.entity.Affirmstay;
import com.sun.hotelproject.entity.Draw;
import com.sun.hotelproject.entity.FaceRecognition;
import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.entity.Order;
import com.sun.hotelproject.entity.QueryCheckin;
import com.sun.hotelproject.entity.SeqNo;
import com.sun.hotelproject.utils.CommonSharedPreferences;

import com.sun.hotelproject.utils.CustomProgressDialog;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;
import com.szxb.smart.pos.jni_interface.printer;

import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

import static com.sun.hotelproject.moudle.MainActivity.MacAddr;

/**
 * @author  sun
 * 时间：2017/11/27
 * TODO:支付界面
 */
@Route(path = "/hotel/payment")
public class PaymentActivity extends BaseActivity {
    @BindView(R.id.img)ImageView img;
    @BindView(R.id.check_inTime) TextView check_inTime;
    @BindView(R.id.tv20) TextView tv20;
    @BindView(R.id.price)TextView price;
    @BindView(R.id.toolbarBack)Button toolbarBack;
    @BindView(R.id.speed_of_progress) ImageView speed_of_progress;
    @BindView(R.id.linear_1) LinearLayout linearLayout;
    @BindView(R.id.shibie)
    TextView shibie;
    @BindView(R.id.relative)RelativeLayout relativeLayout;
    private String url;
    private String paytype;
    private LayoutHouse house;
    private String beginTime;
    private String endTime;
    private String stratTime;
    private String finsihTime;
    private String content;
    private String name;
    private String orderId;
    private String id_CardNo;
    private String birth;
    private String path;
    private String locksign;
    private GuestRoom.Bean gBean;
    private QueryCheckin.Bean qBean;
    private String k;
    private String payMoney;
    private List<Map<String,String>> list;
    private static final String TAG = "PaymentActivity";
    private String inorderpmsno;
    @Override
    protected int layoutID() {
        return R.layout.payment2;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        super.initView();
        linearLayout.setVisibility(View.GONE);
        speed_of_progress.setImageResource(R.drawable.home_six);
        stratTime = (String) CommonSharedPreferences.get("beginTime1", "");
        finsihTime = (String) CommonSharedPreferences.get("endTime1", "");
        beginTime = (String) CommonSharedPreferences.get("beginTime", "");
        endTime = (String) CommonSharedPreferences.get("endTime", "");
        content = (String) CommonSharedPreferences.get("content", "");

        Log.e(TAG, "initView: "+beginTime +endTime );
        check_inTime.setText(stratTime + "——" + finsihTime + "   " + content);
        k=getIntent().getStringExtra("k");//k=1入住，2续住，3退房
        if (k.equals("1")) {
            shibie.setText("正在识别中，请稍后......");
            shibie.setTextColor(Color.WHITE);
            path = getIntent().getStringExtra("path");
            name = getIntent().getStringExtra("name");
            birth = getIntent().getStringExtra("birth");
            gBean = (GuestRoom.Bean) getIntent().getSerializableExtra("bean");
            id_CardNo = getIntent().getStringExtra("id_CardNo");
            locksign = getIntent().getStringExtra("locksign");
            check_inTime.setText(stratTime + "——" + finsihTime + "   " + content);
            tv20.setText(gBean.getRpmsno());
            price.setText(DataTime.updTextSize2(getApplicationContext(), "￥" + gBean.getDealprice(), 1), TextView.BufferType.SPANNABLE);
            get();
        }else if (k.equals("2")){
            shibie.setText("正在识别中，请稍后......");
            shibie.setTextColor(Color.WHITE);
            qBean = (QueryCheckin.Bean) getIntent().getSerializableExtra("bean");
            path = getIntent().getStringExtra("path");
            name = qBean.getGuestname();
            id_CardNo =qBean.getDocno();
            birth = qBean.getBirth();
            tv20.setText(qBean.getRoomno());
            get();
        }else {
            inorderpmsno =getIntent().getStringExtra("inorderpmsno");
            payMoney = getIntent().getStringExtra("price");
            list = (List<Map<String, String>>) getIntent().getSerializableExtra("list");
            Log.e(TAG, "initView: "+payMoney +"\n"+list.toString() );
            Scanpay(payMoney,"12");
        }
    }

    @OnClick({R.id.img,R.id.toolbarBack})
    void OnClick(View v){
        switch (v.getId()){
            case R.id.img:
                Intent intent =new Intent(PaymentActivity.this , PaySussecsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.toolbarBack:
                finish();
                break;
        }
    }





    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {

    }

    /**
     * 生成流水号
     */
    public void get(){
        OkGo.<SeqNo>get(HttpUrl.SEQNO)
                .tag(this)
                .execute(new JsonCallBack<SeqNo>(SeqNo.class) {
                    @Override
                    public void onSuccess(Response<SeqNo> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("00") && response.body().getRetcode().equals("0")){
                            Post(response.body().getSeq_no(),response.body().getAccount());
                        }
                    }
                });
    }

    /**
     * 人脸识别
     * @param seq_no 流水号
     * @param account 账号
     */
    public void Post(String seq_no,String account){
        OkGo.<FaceRecognition>post(HttpUrl.FACERECOQNITION)
                .tag(this)
                .retryCount(3)//超时重连次数
                .cacheTime(3000)//缓存过期时间
                .params("name",name.trim())
                .params("creid_no",id_CardNo.trim())
                .params("account",account)
                .params("type",8)
                .params("seq_no",seq_no)
                .params("photo_check_live",0) //0防翻拍，1关闭防翻拍
                .isMultipart(true)//强制使用multipart/form-data 表单上传
                .params("image_fn", new File(path))
                .execute(new JsonCallBack<FaceRecognition>(FaceRecognition.class) {
                    @Override
                    public void onSuccess(Response<FaceRecognition> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body() + "]");

                        if (response.body().getRescode().equals("00") && response.body().getRetcode().equals("0")){
                           if (response.body().getStatus().equals("1")) {
                               Tip.show(getApplicationContext(), "比对成功 得分：" + response.body().getScore(), true);
                               if (k.equals("1")){
                                    Scanpay();
                               }else if(k.equals("2")) {
                                  Affirmstay();
                               }
                           }else {
                               Tip.show(getApplicationContext(),response.body().getRetmsg(),false);
                           }
                        }else {
                            Tip.show(getApplicationContext(),response.body().getRetmsg(),false);

                        }
                    }
                });
    }
    /**
     *确认续住
     */
    public void Affirmstay(){
        OkGo.<Affirmstay>post(HttpUrl.AFFIRMSTAY)
                .tag(this)
                .params("mchid",mchid)
                .params("pcodetype","R")
                .params("indate",beginTime)
                .params("outdate",endTime)
                .params("rtpmsno",qBean.getRtpmsno())
                .execute(new JsonCallBack<Affirmstay>(Affirmstay.class) {
                    @Override
                    public void onSuccess(Response<Affirmstay> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
                        price.setText(response.body().getDatalist().get(0).getSureprice());
                        Scanpay(response.body().getDatalist().get(0).getSureprice());
                    }
                });

    }

    /**
     * 退房扫码支付
     */
    public void Scanpay(String price,String payway) {
       // orderId= DataTime.orderId();
        StringBuffer sb=new StringBuffer();
        sb.append("0").append("#").append(payway)
                .append("#").append(price)
                .append("##").append(DataTime.currentTime())
                .append("####").append("0");
        OkGo.<Draw>post(HttpUrl.SCANPAY)
                .tag(this)
                .params("mchid", mchid)
                .params("type","03")
                .params("inorderpmsno",inorderpmsno)
                .params("dutypmsno","1")
                .params("arid", "")
                .params("userno","")
                .params("payinfo", String.valueOf(sb))
                .params("payway",payway)
                .params("devno","")
                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onSuccess(Response<Draw> response) {
                        super.onSuccess(response);
                        if (response.body().getRescode().equals("0000")) {
                            linearLayout.setVisibility(View.VISIBLE);
                            relativeLayout.setVisibility(View.GONE);
                            Picasso.with(PaymentActivity.this).load(response.body().getCodeimgurl()).into(img);
                            orderId = response.body().getOrderid();
                            handler.postDelayed(task,5*1000);
                        }
                    }
                });
    }

    /**
     * 续住扫码支付
     */
    public void Scanpay(String price) {
        orderId= DataTime.orderId();
        StringBuffer sb=new StringBuffer();
        sb.append("0").append("#").append("12")
                .append("#").append(price)
                .append("##").append(DataTime.currentTime())
                .append("####").append("0");
        OkGo.<Draw>post(HttpUrl.SCANPAY)
                .tag(this)
                .params("orderid", orderId)
                .params("type","02")
                .params("inorderpmsno",qBean.getInorderpmsno())
                .params("mchid",mchid)
                .params("indate", beginTime)
                .params("outdate",endTime)
                .params("devno",DataTime.getSerialNumber())
                .params("intype",qBean.getIntype())
                .params("dutypmsno","")
                .params("couponno","")
                .params("couponprice","")
                .params("userno","")
                .params("payinfo", String.valueOf(sb))
                .params("payway","12")
                .params("dealprice",price)
                .params("morncode","")
                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<Draw> response) {
                        super.onSuccess(response);
                        if (response.body().getRescode().equals("0000")) {
                            linearLayout.setVisibility(View.VISIBLE);
                            Picasso.with(PaymentActivity.this).load(response.body().getCodeimgurl()).into(img);
                            orderId = response.body().getOrderid();
                            Log.e(TAG, "onSuccess: "+orderId);
                            handler.postDelayed(task,5*1000);
                        }
                    }
                });
    }
    /**
     * 入住扫码支付
     */
    public void Scanpay() {
        orderId= DataTime.orderId();
        StringBuffer sb=new StringBuffer();
        sb.append(name).append("#").append("1")
                .append("#").append(id_CardNo)
                .append("####").append(DataTime.getMyDate(birth)).append("#");
        StringBuffer sb1=new StringBuffer();
        sb1.append("0").append("#").append("12")
                .append("#").append(gBean.getDealprice())
                .append("##").append(DataTime.currentTime())
                .append("####").append("0");
        OkGo.<Draw>post(HttpUrl.SCANPAY)
                .tag(this)
                .params("orderid", orderId)
                .params("payway","12")
                .params("type","01")
                .params("guestel","")
                .params("guestinfo", String.valueOf(sb))
                .params("rpmsno",gBean.getRpmsno())
                .params("rpriceno",gBean.getPmspcode())
                .params("intime",DataTime.currentTime())
                .params("outtime",endTime+" 12:00:00")
                .params("intype","1")
                .params("team","1")
               // .params("optime",DataTime.currentTime())
                .params("devno",DataTime.getSerialNumber())
                .params("mchid",mchid)
                .params("rtpmsno",gBean.getRtpmsno())
                .params("opmsno","")
                .params("indate",beginTime)
                .params("dealprice",gBean.getDealprice())
                .params("oldprice",gBean.getNormnightprice())
                .params("cardnum","1")
                .params("dutypmsno","")
                .params("ordertype","6")
                .params("rmk","")
                .params("locksign",locksign)
                .params("payinfo", String.valueOf(sb1))
                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<Draw> response) {
                        super.onSuccess(response);
                        if (response.body().getRescode().equals("0000")) {
                            linearLayout.setVisibility(View.VISIBLE);
                            Picasso.with(PaymentActivity.this).load(response.body().getCodeimgurl()).into(img);
                            orderId = response.body().getOrderid();
                            handler.postDelayed(task,5*1000);
                        }
                    }
                });
    }

    Runnable task =new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,5*1000);
            queryPayStatus(orderId);

        }
    };

    /**
     * 查询支付结果
     */
    public void queryPayStatus(String orderId){
        Log.e(TAG, "queryPayStatus: "+orderId );
        OkGo.<Draw>post(HttpUrl.QUERYPAYSTATUS)
                .tag(this)
                .params("mchid",mchid)
                .params("orderid",orderId)
                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onSuccess(Response<Draw> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("0000")){
                            Intent intent =new Intent(PaymentActivity.this,PaySussecsActivity.class);
                            intent.putExtra("k",k);
                            startActivity(intent);
                            handler.removeCallbacks(task);
                            finish();
                        }else {
                            Tip.show(getApplicationContext(),response.body().getResult(),false);
                        }
                    }
                });
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
      //  handler.removeCallbacks(task);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
      //  handler.removeCallbacks(task);
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        handler.removeCallbacks(task);
    }
}
