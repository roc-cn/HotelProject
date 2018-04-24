package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.dao.DaoSimple;
import com.sun.hotelproject.entity.Draw;


import com.sun.hotelproject.entity.QueryRoomBill;
import com.sun.hotelproject.entity.RoomNo;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

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

    @BindView(R.id.anim_layout)
    RelativeLayout anim_lauout;
    @BindView(R.id.anim_img)ImageView anim_img;
    @BindView(R.id.anim_tv)TextView anim_tv;
    private AnimationDrawable animationDrawable;
    @BindView(R.id.toolbarBack)Button toolBarBack;
    @BindView(R.id.tv1)TextView tv1;
    @BindView(R.id.linear_sp2)LinearLayout linear_sp2;
    @BindView(R.id.linear_sp3)LinearLayout linear_sp3;
    @BindView(R.id.sp2_tv3)TextView sp2_tv3;
    @BindView(R.id.sp2_content3)TextView sp2_content3;
    @BindView(R.id.sp3_tv2)TextView sp3_tv2;
    @BindView(R.id.sp3_img2)ImageView sp3_img2;
    @BindView(R.id.sp2_img3)ImageView sp2_img3;
    @BindView(R.id.piv_tv)TextView piv_tv;

    String orderId;
    private static final String TAG = "CheckOutActivity";
    private String msgs;
    private String roomNum;
    private Double price =0.00;
    private DaoSimple daoSimple;
    private String  querytype = "3";
    private String k;
    private String mchid;
    private QueryRoomBill.Bean qb;
    Animation operatingAnim;
    private String mMonth,mDay;
    private String startTime,finshTime,startTime1,finshTime1;
    @Override
    protected int layoutID() {
        return R.layout.activity_chenk_out;
    }
    @Override
    protected void initData() {
           // Connect();
        ActivityManager.getInstance().addActivity(this);
        daoSimple = new DaoSimple(this);
        handler.postDelayed(task,1*1000);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.load_animation);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    @Override
    protected void initView() {
        super.initView();
        k=getIntent().getStringExtra("k");
        mchid = (String) CommonSharedPreferences.get("mchid","");
        if (k.equals("2")){
            linear_sp2.setVisibility(View.VISIBLE);
            linear_sp3.setVisibility(View.GONE);
            tv1.setText("正在办理续住手续");
            sp2_img3.setVisibility(View.VISIBLE);
            querytype =getIntent().getStringExtra("querytype");
            sp2_tv3.setTextColor(getResources().getColor(R.color.Swrite));
            sp2_tv3.setBackgroundResource(R.drawable.oval_shape);
            sp2_content3.setText("房卡");
        }else {
            tv1.setText("正在办理退房手续");
            linear_sp2.setVisibility(View.GONE);
            sp3_img2.setVisibility(View.VISIBLE);
            sp3_tv2.setBackgroundResource(R.drawable.oval_shape);
            sp3_tv2.setTextColor(getResources().getColor(R.color.Swrite));
        }
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


    @SuppressLint("SetTextI18n")
    public void readCard(){
       // tv1.setText("数据读取中......");
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
                        roomNum = card_No.substring(card_No.length()-4,card_No.length());
                        tv1.setText("卡号为:"+roomNum);
                        handler.removeCallbacks(task);
                        if (daoSimple.roomNoSel(roomNum)==null){
                            getCard("不是本机办理的房卡或不是房卡类型卡片");
                        }else {
                            if (k.equals("2")){
                                Intent intent =new Intent(CheckOutActivity.this,RenwalActivity.class);
                                        intent.putExtra("querytype",querytype);
                                        intent.putExtra("k",k);
                                        intent.putExtra("roomNum",roomNum);
                                        startActivity(intent);
                            }else if (k.equals("3")){
                                queryRoomBill(querytype, roomNum);
                            }
                        }
                    }else tv1.setText("S50卡读卡失败" );
                }else tv1.setText("S50卡密钥错误" );
        } else tv1.setText("没有找到房间卡，请插入房卡" );
    }

    /**
     * 查询客房账单
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void queryRoomBill(String querytype, String querydata){
        anim_lauout.setVisibility(View.VISIBLE);

        // animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.load_animation);
        anim_img.setAnimation(operatingAnim);
        anim_img.startAnimation(operatingAnim);
        anim_tv.setText("正在加载中......");
//        if (animationDrawable != null && !animationDrawable.isRunning()){
//            animationDrawable.start();
//        }
        OkGo.<QueryRoomBill>post(HttpUrl.QUERYROOMBILL)
                .tag(this)
                .params("mchid", mchid)
                .params("querytype",querytype)
                .params("querydata",querydata)
                .execute(new JsonCallBack<QueryRoomBill>(QueryRoomBill.class) {
                    @Override
                    public void onSuccess(Response<QueryRoomBill> response) {
                        super.onSuccess(response);
                       // Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
                        if (response.body().getRescode().equals("0000")){
                            qb =response.body().getDatalist().get(response.body().getDatalist().size()-1);
                           // Double  accountprice =Double.valueOf(response.body().getDatalist().get(0).getAccountprice());
                            startTime =qb.getIntime();
                            finshTime =qb.getOuttime();
                            String []time =startTime.split("-");
                            mMonth = time[1];
                            mDay = time[2];
                            startTime1 = mMonth +"/"+mDay;
                            Log.e(TAG, "onSuccess: "+startTime1 );
                            String []time1 =finshTime.split("-");
                            mMonth = time1[1];
                            mDay = time1[2];
                            finshTime1 = mMonth +"/"+mDay;
                            Log.e(TAG, "onSuccess: "+finshTime1);
                            CommonSharedPreferences.put("beginTime", startTime);
                            CommonSharedPreferences.put("endTime", finshTime);
                            CommonSharedPreferences.put("beginTime1", startTime1);
                            CommonSharedPreferences.put("endTime1", finshTime1);

                            String accountprice=qb.getAccountprice();
                            anim_lauout.setVisibility(View.GONE);
                            anim_img.clearAnimation();
                            if (Double.valueOf(accountprice)>0){
                                getCard("请到前台办理退款手续");
                               Tip.show(getApplicationContext(),"请到前台办理退款手续",true);
                            }else if (Double.valueOf(accountprice)==0){
                                checkOutRoom(qb.getInorderpmsno(),"141",accountprice,qb.getAddprice());
                            }else {
                                if (accountprice.contains("-")) {
                                    accountprice = accountprice.replace("-", "");
                                }
                                Intent intent =new Intent(CheckOutActivity.this,PaymentActivity.class);
                                intent.putExtra("price",accountprice);
                                intent.putExtra("name",qb.getName());
                                intent.putExtra("k","3");
                                intent.putExtra("addprice",qb.getAddprice());
                                intent.putExtra("inorderpmsno",qb.getInorderpmsno());
                                intent.putExtra("list", (Serializable) qb.getBills());
                                startActivity(intent);
                                finish();
                            }

                        }else {
                            anim_lauout.setVisibility(View.GONE);
                            anim_img.clearAnimation();
                          //  Tip.show(getApplicationContext(),response.body().getResult(),false);
                            getCard(response.body().getResult());
                        }
                    }

                    @Override
                    public void onError(Response<QueryRoomBill> response) {
                        super.onError(response);
                        anim_lauout.setVisibility(View.GONE);
                        anim_img.clearAnimation();
                       // Log.e(TAG, "onError: 服务器连接异常" );
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                    }
                });
    }

    /**
     * 无消费，直接退房
     */
    private void checkOutRoom(String inorderpmsno,String payway,String ss,String addprice){
        StringBuffer sb=new StringBuffer();
        sb.append("3").append("#").append(payway)
                .append("#").append(ss)
                .append("##").append(DataTime.currentTime())
                .append("####").append("0");

        OkGo.<Draw>post(HttpUrl.CHECKOUTROOM)
                .tag(this)
                .params("mchid",mchid)
                .params("inorderpmsno",inorderpmsno)
                .params("dutypmsno","1")
                .params("arid","")
                .params("userno","")
                .params("payinfo", String.valueOf(sb))
                .params("payway",payway)
                .params("addprice",addprice)
                .params("accountrmk","")
                .params("breaknum","0002")
                .params("devno","")
                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onSuccess(Response<Draw> response) {
                        super.onSuccess(response);
                        anim_lauout.setVisibility(View.GONE);
                        anim_img.clearAnimation();
                       // Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
                        if (response.body().getRescode().equals("0000")){
                            Intent intent =new Intent(CheckOutActivity.this,PaySussecsActivity.class);
                            intent.putExtra("k","3");
                            intent.putExtra("flag","0");
                            startActivity(intent);
                            finish();
                        }else {
                            anim_lauout.setVisibility(View.GONE);
                           anim_img.clearAnimation();
                          //  Tip.show(getApplicationContext(),response.body().getResult(),false);
                            getCard(response.body().getResult());
                        }
                    }

                    @Override
                    public void onError(Response<Draw> response) {
                        super.onError(response);
                        anim_lauout.setVisibility(View.GONE);
                        anim_img.clearAnimation();
                        getCard("错误");
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                    }
                });
    }

    /**
     *
     * 出卡
     */
    private void getCard(String s){
        int nRet;
        byte[] SendBuf=new byte[3];
        String[] RecordInfo=new String[2];
        SendBuf[0] = 0x46;
        SendBuf[1] = 0x43;
        SendBuf[2] = 0x34;
        nRet = K720_Serial.K720_SendCmd(MacAddr, SendBuf, 3, RecordInfo);
        if(nRet == 0){
            tv1.setText(s);
            handler.postDelayed(task,10*1000);
        }

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
//
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
    protected void onResume() {
        super.onResume();
    //    handler.postDelayed(task,5000);
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
        OkGo.getInstance().cancelTag(this);
    }



}
