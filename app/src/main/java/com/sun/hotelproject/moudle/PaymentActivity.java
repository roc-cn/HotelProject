package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.sun.hotelproject.entity.LockRoom;
import com.sun.hotelproject.entity.Order;
import com.sun.hotelproject.entity.QueryCheckin;
import com.sun.hotelproject.entity.SeqNo;
import com.sun.hotelproject.utils.ActivityManager;
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

    @BindView(R.id.linear_1) LinearLayout linearLayout;
    @BindView(R.id.relative)RelativeLayout relativeLayout;
    @BindView(R.id.wechat_pay)LinearLayout wechat_pay;
    @BindView(R.id.zhifub_pay)LinearLayout zhifub_pay;
    @BindView(R.id.house_type) TextView house_type;
    @BindView(R.id.parson_name) TextView parson_name;
    @BindView(R.id.relative_3)RelativeLayout relative_3;
    @BindView(R.id.result)TextView result;
    @BindView(R.id.confirm)Button confirm;
    @BindView(R.id.retry)Button retry;
    @BindView(R.id.anim_layout) RelativeLayout anim_lauout;
    @BindView(R.id.anim_img)ImageView anim_img;
    @BindView(R.id.anim_tv)TextView anim_tv;
    @BindView(R.id.sp_tv6)TextView sp_tv6;
    @BindView(R.id.sp2_tv6)TextView sp2_tv6;
    @BindView(R.id.linear_sp1)LinearLayout linear_sp1;
    @BindView(R.id.linear_sp2)LinearLayout linear_sp2;
    @BindView(R.id.linear_sp3)LinearLayout linear_sp3;
    @BindView(R.id.sp3_tv3)TextView sp3_tv3;
    @BindView(R.id.sp_img6)ImageView sp_img6;
    @BindView(R.id.sp2_img6)ImageView sp2_img6;
    @BindView(R.id.sp3_img3)ImageView sp3_img3;
    private AnimationDrawable animationDrawable;
    private String url;
    private String paytype;
    private LayoutHouse house;
    private String beginTime;
    private String endTime;
    private String stratTime;
    private String finsihTime;
    private String content;
    private String house_name;
    private String orderId;
    private String id_CardNo;
    private String birth;
    private String path;
    private String locksign;
    private GuestRoom.Bean gBean;
    private QueryCheckin.Bean qBean;
    private String k;
    private String payMoney;
    private int inDay;
    private List<Map<String,String>> list;
    private static final String TAG = "PaymentActivity";
    private String inorderpmsno;
    private String querytype;
    private String name;
    private String mchid;
    private Affirmstay.Bean ab;
    Animation operatingAnim;
    @Override
    protected int layoutID() {
        return R.layout.payment2;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        super.initView();
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.load_animation);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        ActivityManager.getInstance().addActivity(this);
        linearLayout.setVisibility(View.GONE);
        relative_3.setVisibility(View.GONE);
        mchid = (String) CommonSharedPreferences.get("mchid","");

        stratTime = (String) CommonSharedPreferences.get("beginTime1", "");
        finsihTime = (String) CommonSharedPreferences.get("endTime1", "");
        beginTime = (String) CommonSharedPreferences.get("beginTime", "");
        endTime = (String) CommonSharedPreferences.get("endTime", "");
        content = (String) CommonSharedPreferences.get("content", "");
        String day = (String) CommonSharedPreferences.get("inDay","");
        Log.e(TAG, "initView: "+stratTime +" "+ finsihTime+" " +beginTime +" "+endTime );
        inDay = Integer.parseInt(day);
       // Log.e(TAG, "initView: "+beginTime +endTime );
        check_inTime.setText(stratTime + "——" + finsihTime + "   " + content);
        k=getIntent().getStringExtra("k");//k=1入住，2续住，3退房
        if (k.equals("1")) {
            linear_sp2.setVisibility(View.GONE);
            linear_sp3.setVisibility(View.GONE);
            sp_img6.setVisibility(View.VISIBLE);
            sp_tv6.setBackgroundResource(R.drawable.oval_shape);
            sp_tv6.setTextColor(getResources().getColor(R.color.Swrite));

         //   shibie.setText("正在识别中，请稍后......");
          //  shibie.setTextColor(Color.WHITE);
            house_name = (String) CommonSharedPreferences.get("house_type","");
            path = getIntent().getStringExtra("path");
            name = getIntent().getStringExtra("name");
            birth = getIntent().getStringExtra("birth");
            gBean = (GuestRoom.Bean) getIntent().getSerializableExtra("bean");
            id_CardNo = getIntent().getStringExtra("id_CardNo");
            locksign = getIntent().getStringExtra("locksign");
            check_inTime.setText(stratTime + "——" + finsihTime + "   " + content);
            tv20.setText(gBean.getRpmsno());
            house_type.setText(house_name);
            parson_name.setText(name);
            price.setText(DataTime.updTextSize2(getApplicationContext(), "￥" + gBean.getDealprice(), 1), TextView.BufferType.SPANNABLE);
          get();
         // Scanpay("12");
        }else if (k.equals("2")){
            linear_sp3.setVisibility(View.GONE);
            linear_sp1.setVisibility(View.GONE);
            sp2_img6.setVisibility(View.VISIBLE);
            sp2_tv6.setBackgroundResource(R.drawable.oval_shape);
            sp2_tv6.setTextColor(getResources().getColor(R.color.Swrite));
            //shibie.setText("正在识别中，请稍后......");
            //shibie.setTextColor(Color.WHITE);
            qBean = (QueryCheckin.Bean) getIntent().getSerializableExtra("bean");
            path = getIntent().getStringExtra("path");
            name = qBean.getGuestname();
            id_CardNo =qBean.getDocno();
            querytype = getIntent().getStringExtra("querytype");
            ab = (Affirmstay.Bean) getIntent().getSerializableExtra("bean2");
            Log.e(TAG, "initView: "+ab.toString() );
            birth = qBean.getBirth();
            parson_name.setText(name);
            tv20.setText(qBean.getRoomno());
            price.setText(DataTime.updTextSize2(getApplicationContext(), "￥" + ab.getSureprice(), 1), TextView.BufferType.SPANNABLE);

         //  get();

          Scanpay(ab.getSureprice(),"12");
        }else {
            linear_sp2.setVisibility(View.GONE);
            linear_sp3.setVisibility(View.GONE);
            sp3_img3.setVisibility(View.VISIBLE);
            sp3_tv3.setTextColor(getResources().getColor(R.color.Swrite));
            sp3_tv3.setBackgroundResource(R.drawable.oval_shape);
            inorderpmsno =getIntent().getStringExtra("inorderpmsno");
            payMoney = getIntent().getStringExtra("price");
            name = getIntent().getStringExtra("name");
            Log.e(TAG, "initView: "+payMoney );
            list = (List<Map<String, String>>) getIntent().getSerializableExtra("list");
            Log.e(TAG, "initView: "+payMoney +"\n"+list.toString() );
            price.setText(DataTime.updTextSize2(getApplicationContext(), "￥" + payMoney, 1), TextView.BufferType.SPANNABLE);
            parson_name.setText(name);
            Scanpay(payMoney,"12",inorderpmsno);
        }
    }

    @OnClick({R.id.img,R.id.zhifub_pay,R.id.wechat_pay,R.id.retry,R.id.confirm})
    void OnClick(View v){
        Intent intent =new Intent();
        switch (v.getId()){
            case R.id.img:
//                intent.setClass(PaymentActivity.this , PaySussecsActivity.class);
//                startActivity(intent);
//                finish();
                break;
            case R.id.wechat_pay:
//                switch (k) {
//                    case "1":
//                        Scanpay("141");
//                        break;
//                    case "2":
//                        Scanpay(ab.getSureprice(),"12");
//                        break;
//                    default:
//                        Scanpay(payMoney, "141", inorderpmsno);
//                        break;
//                }
                break;
            case R.id.zhifub_pay:
//                switch (k) {
//                    case "1":
//                        Scanpay("12");
//                        break;
//                    case "2":
//                        Scanpay(ab.getSureprice(),"12");
//                        break;
//                    default:
//                        Scanpay(payMoney, "12", inorderpmsno);
//                        break;
//                }
                break;
            case R.id.confirm:
                relative_3.setVisibility(View.GONE);
                startActivity(new Intent(PaymentActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.retry:
                intent =  new Intent(PaymentActivity.this,FaceRecognitionActivity.class);
                intent.putExtra("k","4");
                startActivityForResult(intent,1);
                relative_3.setVisibility(View.GONE);
               // finish();
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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void get(){
        anim_lauout.setVisibility(View.VISIBLE);

        // animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.load_animation);
        anim_img.setAnimation(operatingAnim);
        anim_img.startAnimation(operatingAnim);
        anim_tv.setText("正在加载中......");
//        if (animationDrawable != null && !animationDrawable.isRunning()){
//            animationDrawable.start();
//        }
        OkGo.<SeqNo>get(HttpUrl.SEQNO)
                .tag(this)
                .execute(new JsonCallBack<SeqNo>(SeqNo.class) {
                    @Override
                    public void onSuccess(Response<SeqNo> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("00") && response.body().getRetcode().equals("0")){
                            Post(response.body().getSeq_no(),response.body().getAccount());
                        }else {
                           anim_img.clearAnimation();
                           anim_lauout.setVisibility(View.GONE);
                            Tip.show(getApplicationContext(),response.body().getRetmsg(),false);
                        }
                    }

                    @Override
                    public void onError(Response<SeqNo> response) {
                        anim_img.clearAnimation();
                        anim_lauout.setVisibility(View.GONE);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                        super.onError(response);
                    }
                });
    }
    /**
     * 解锁
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void lockRoom(final GuestRoom.Bean bean){

        OkGo.<LockRoom>post(HttpUrl.LOCKROOM)
                .tag(this)
                .params("mchid", mchid)
                .params("bstype","2")
                .params("constraint","1")
                .params("operation", "1")
                .params("bpmsno","")
                .params("rpmsno",bean.getRpmsno())
                .params("userno","")
                .params("opmsno","")

                .execute(new JsonCallBack<LockRoom>(LockRoom.class) {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(Response<LockRoom> response) {
                        super.onSuccess(response);

                    //    Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("0000")) {
                            if (response.body().getDatalist().get(0).getLockres().equals("2")){
                                Tip.show(getApplicationContext(),"解锁失败！",false);
                            }else {
                                Tip.show(getApplicationContext(),"解锁成功！",true);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LockRoom> response) {
                        super.onError(response);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
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
                               relative_3.setVisibility(View.GONE);
                               Tip.show(getApplicationContext(), "比对成功 得分：" + response.body().getScore(), true);
                               if (k.equals("1")){
                                    Scanpay("12");
                               }else if(k.equals("2")) {
                                   Scanpay(ab.getSureprice(),"12");
                               }
                           }else {
                             anim_img.clearAnimation();
                             anim_lauout.setVisibility(View.GONE);
                           relative_3.setVisibility(View.VISIBLE);

                           }
                        }else {
                            relative_3.setVisibility(View.VISIBLE);
                          anim_img.clearAnimation();
                        anim_lauout.setVisibility(View.GONE);
                    }
                    }

                    @Override
                    public void onError(Response<FaceRecognition> response) {
                        anim_img.clearAnimation();
                        anim_lauout.setVisibility(View.GONE);
                        Tip.show(getApplicationContext(), "服务器连接异常", false);
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == Activity.RESULT_OK){
            path = data.getStringExtra("path");
            Log.e(TAG, "onActivityResult: "+path );
            get();
        }
    }



    /**
     * 退房扫码支付
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void Scanpay(String price, String payway, String inorderpmsno) {
        anim_lauout.setVisibility(View.VISIBLE);

        // animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.load_animation);
        anim_img.setAnimation(operatingAnim);
        anim_img.startAnimation(operatingAnim);
        anim_tv.setText("正在加载中......");
//        if (animationDrawable != null && !animationDrawable.isRunning()){
//            animationDrawable.start();
//        }
        orderId= DataTime.orderId();
        StringBuffer sb=new StringBuffer();
        sb.append("3").append("#").append(payway)
                .append("#").append(price)
                .append("##").append(DataTime.currentTime())
                .append("####").append("0");
        OkGo.<Draw>post(HttpUrl.SCANPAY)
                .tag(this)
                .params("orderid", orderId)
                .params("mchid", mchid)
                .params("type","03")
                .params("inorderpmsno",inorderpmsno)
                .params("dutypmsno","1")
                .params("arid", "")
                .params("userno","")
                .params("payinfo", String.valueOf(sb))
                .params("payway",payway)
                .params("devno","")
                .params("breaknum","1000")
                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onSuccess(Response<Draw> response) {
                        super.onSuccess(response);
                        if (response.body().getRescode().equals("0000")) {
                            anim_img.clearAnimation();
                            anim_lauout.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                            relativeLayout.setVisibility(View.GONE);
                            Picasso.with(PaymentActivity.this).load(response.body().getCodeimgurl()).into(img);
                            orderId = response.body().getOrderid();
                            handler.postDelayed(task,5*1000);
                        }else {
                            Tip.show(getApplicationContext(),response.body().getResult(),false);
                           anim_img.clearAnimation();
                            anim_lauout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Response<Draw> response) {
                        super.onError(response);
                        anim_img.clearAnimation();
                        anim_lauout.setVisibility(View.GONE);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                    }
                });
    }

    /**
     * 续住扫码支付
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void Scanpay(String price, String payway) {
        anim_lauout.setVisibility(View.VISIBLE);

        // animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.load_animation);
        anim_img.setAnimation(operatingAnim);
        anim_img.startAnimation(operatingAnim);
        anim_tv.setText("正在加载中......");
//        if (animationDrawable != null && !animationDrawable.isRunning()){
//            animationDrawable.start();
//        }
        orderId= DataTime.orderId();
        StringBuffer sb=new StringBuffer();
        sb.append("3").append("#").append(payway)
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
                .params("payway",payway)
                .params("dealprice",price)
                .params("morncode","")

                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<Draw> response) {
                        super.onSuccess(response);
                        if (response.body().getRescode().equals("0000")) {
                            anim_img.clearAnimation();
                            anim_lauout.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                            Picasso.with(PaymentActivity.this).load(response.body().getCodeimgurl()).into(img);
                            orderId = response.body().getOrderid();
                            Log.e(TAG, "onSuccess: "+orderId);
                            handler.postDelayed(task,5*1000);
                        }else {
                            anim_img.clearAnimation();
                            anim_lauout.setVisibility(View.GONE);
                            Tip.show(getApplicationContext(),response.body().getResult(),false);
                        }
                    }

                    @Override
                    public void onError(Response<Draw> response) {
                        super.onError(response);
                        anim_img.clearAnimation();
                        anim_lauout.setVisibility(View.GONE);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                    }
                });
    }
    /**
     * 入住扫码支付
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void Scanpay(String payway) {
        anim_lauout.setVisibility(View.VISIBLE);

        // animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.load_animation);
        anim_img.setAnimation(operatingAnim);
        anim_img.startAnimation(operatingAnim);
        anim_tv.setText("正在加载中......");
        if (animationDrawable != null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
        orderId= DataTime.orderId();
        String arprice = String.valueOf(Double.valueOf(gBean.getDealprice()) * inDay);
        StringBuffer sb=new StringBuffer();
        sb.append(name).append("#").append("1")
                .append("#").append(id_CardNo)
                .append("####").append(DataTime.getMyDate(birth)).append("#");
        StringBuffer sb1=new StringBuffer();
        sb1.append("3").append("#").append(payway)
                .append("#").append(gBean.getDealprice())
                .append("##").append(DataTime.currentTime())
                .append("####").append("0");
        OkGo.<Draw>post(HttpUrl.SCANPAY)
                .tag(this)
                .params("orderid", orderId)
                .params("payway",payway)
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
                .params("arprice",arprice)
                .params("payinfo", String.valueOf(sb1))
                .params("breaknum","1000")
                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<Draw> response) {
                        super.onSuccess(response);
                        if (response.body().getRescode().equals("0000")) {
                           anim_img.clearAnimation();
                            anim_lauout.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                            Picasso.with(PaymentActivity.this).load(response.body().getCodeimgurl()).into(img);
                            orderId = response.body().getOrderid();
                            handler.postDelayed(task,5*1000);
                        }else {
                            anim_img.clearAnimation();
                            anim_lauout.setVisibility(View.GONE);
                            Tip.show(getApplicationContext(),response.body().getResult(),false);
                        }
                    }

                    @Override
                    public void onError(Response<Draw> response) {
                        super.onError(response);
                        anim_img.clearAnimation();
                        anim_lauout.setVisibility(View.GONE);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);

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
                            if (k.equals("2")){
                                intent.putExtra("querytype",querytype);
                            }else if (k.equals("3")){
                                intent.putExtra("flag","1");
                            }
                            startActivity(intent);
                            handler.removeCallbacks(task);
                            finish();
                        }else {
                            Log.e(TAG, "onSuccess: "+response.body().getResult());
                        }
                    }

                    @Override
                    public void onError(Response<Draw> response) {
                        super.onError(response);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);

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

    @Override
    protected void onPause() {
        handler.removeCallbacks(task);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (k.equals("1")){
            lockRoom(gBean);
        }
       handler.removeCallbacks(task);
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
