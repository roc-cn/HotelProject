package com.sun.hotelproject.moudle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Route;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;

import com.sun.hotelproject.entity.Draw;
import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.entity.QueryRoomBill;
import com.sun.hotelproject.moudle.id_card.IDCardInfo;
import com.sun.hotelproject.moudle.id_card.IDCardReaderCallBack;
import com.sun.hotelproject.moudle.id_card.IDCarderReader;

import com.sun.hotelproject.moudle.id_card.ReadIDThread;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;
import com.szxb.smart.pos.jni_interface.Card_Sender;


import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author  sun
 * 时间 2017/11/22
 * 身份证界面
 */
@Route(path = "/hotel/identification")
public class IdentificationActivity extends BaseActivity {
    @BindView(R.id.piv_tv)
    TextView piv_tv;
    @BindView(R.id.sp_tv4)TextView sp_tv4;
    @BindView(R.id.linear_sp1)LinearLayout linear_sp1;
    @BindView(R.id.linear_sp2)LinearLayout linear_sp2;
    @BindView(R.id.sp2_content3)TextView sp2_content3;
    @BindView(R.id.sp2_tv3)TextView sp2_tv3;
    @BindView(R.id.sp_img4)ImageView sp_img4;
    @BindView(R.id.sp2_img3)ImageView sp2_img3;
    private static final String TAG = "SecondActivity";
    private IDCardReaderCallBack readerCallBack;
    private IDCarderReader idCarderReader;
    byte[] bs;
    boolean key;
    private GuestRoom.Bean gBean;
    private String locksign;
    private String k;
    private String querytype ;
    private Double price = 0.00;
    int count = 0;
    private String mchid;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //count = 6;
                if (msg.what == 1) {
                    IDCardInfo idCardInfo = (IDCardInfo) msg.obj;

                    if (idCardInfo != null) {
                        Log.e(TAG, "handleMessage: "+idCardInfo.toString() );
                        piv_tv.setText("读取成功!");
                        handler.removeCallbacks(task);

                        String name = idCardInfo.getStrName();
                        String id_cardNo = idCardInfo.getStrIdCode();
                        String birth = idCardInfo.getStrBirth();
                        if (k.equals("1")){
                            Intent intent = new Intent(IdentificationActivity.this,FaceRecognitionActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("id_CardNo", id_cardNo);
                            intent.putExtra("birth", birth);
                            intent.putExtra("bean",gBean);
                            intent.putExtra("locksign",locksign);
                            intent.putExtra("k",k);
                            startActivity(intent);
                            finish();
                        }else if (k.equals("2")){
                            Intent intent = new Intent(IdentificationActivity.this,RenwalActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("id_CardNo", id_cardNo);
                            intent.putExtra("birth", birth);
                            intent.putExtra("k",k);
                            intent.putExtra("querytype",querytype);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        piv_tv.setText("读取失败！");
                    }
                }
        }
    };

    @Override
    protected int layoutID() {
        return R.layout.activity_identification;
    }

    @Override
    protected void initData() {
        ActivityManager.getInstance().addActivity(this);
        mchid = (String) CommonSharedPreferences.get("mchid","");

        count = 0;
        k=getIntent().getStringExtra("k");
        if (k.equals("1")){
            linear_sp2.setVisibility(View.GONE);
            sp_img4.setVisibility(View.VISIBLE);
            sp_tv4.setBackgroundResource(R.drawable.oval_shape);
            sp_tv4.setTextColor(getResources().getColor(R.color.Swrite));
            gBean= (GuestRoom.Bean) getIntent().getSerializableExtra("bean");
            locksign=getIntent().getStringExtra("locksign");
        }else if (k.equals("2")){
            linear_sp1.setVisibility(View.GONE);
            sp2_img3.setVisibility(View.VISIBLE);
            sp2_tv3.setTextColor(getResources().getColor(R.color.Swrite));
            sp2_tv3.setBackgroundResource(R.drawable.oval_shape);
            sp2_content3.setText("身份证");
            querytype =getIntent().getStringExtra("querytype");
        }

        Card_Sender my_Card_Sender = new Card_Sender();
        int[] nStatus = new int[1];
        boolean zt = my_Card_Sender.TY_GetStatus(nStatus);
        Log.e("发卡机状态:", "发卡机状态:" + zt);

        handler.postDelayed(task,1*1000);
    }
    Runnable task=new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,5000);
            readCard_No();
        }
    };

//    /**
//     * 查询客房账单
//     */
//    private void queryRoomBill(final String querytype, final String querydata){
//        OkGo.<QueryRoomBill>post(HttpUrl.QUERYROOMBILL)
//                .tag(this)
//                .params("mchid",mchid)
//                .params("querytype",querytype)
//                .params("querydata",querydata)
//                .execute(new JsonCallBack<QueryRoomBill>(QueryRoomBill.class) {
//                    @Override
//                    public void onSuccess(Response<QueryRoomBill> response) {
//                        super.onSuccess(response);
//                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
//                        if (response.body().getRescode().equals("0000")){
//                            String ss =response.body().getDatalist().get(0).getAccountprice();
//                         //判断是否有消费
//                                if(ss.contains("-")){
//                                    ss=ss.replace("-","");
//                                }else{
//                                    ss="-"+ss;
//                                }
//                                if (Double.valueOf(ss)>price){
//                                Intent intent =new Intent(IdentificationActivity.this,PaymentActivity.class);
//                                intent.putExtra("price",response.body().getDatalist().get(0).getAddprice());
//                                intent.putExtra("k","3");
//                                intent.putExtra("querytype",querytype);
//                                intent.putExtra("inorderpmsno",response.body().getDatalist().get(0).getInorderpmsno());
//                                intent.putExtra("list", (Serializable) response.body().getDatalist().get(0).getBills());
//                                startActivity(intent);
//                                finish();
//                            }else { //无消费
//                                checkOutRoom(response.body().getDatalist().get(0).getInorderpmsno(),"12",ss);
//
//                            }
//
//                        }else {
//                            Tip.show(getApplicationContext(),response.body().getResult(),false);
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 无消费，直接退房
//     */
//    private void checkOutRoom(String inorderpmsno,String payway,String ss){
//        StringBuffer sb=new StringBuffer();
//        sb.append("0").append("#").append(payway)
//                .append("#").append(ss)
//                .append("##").append(DataTime.currentTime())
//                .append("####").append("0");
//
//        OkGo.<Draw>post(HttpUrl.CHECKOUTROOM)
//                .tag(this)
//                .params("mchid",mchid)
//                .params("inorderpmsno",inorderpmsno)
//                .params("dutypmsno","1")
//                .params("arid","")
//                .params("userno","")
//                .params("payinfo", String.valueOf(sb))
//                .params("payway",payway)
//                .params("devno","")
//                .execute(new JsonCallBack<Draw>(Draw.class) {
//                    @Override
//                    public void onSuccess(Response<Draw> response) {
//                        super.onSuccess(response);
//                       // Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
//                        if (response.body().getRescode().equals("0000")){
//                            Intent intent =new Intent(IdentificationActivity.this,PaySussecsActivity.class);
//                            intent.putExtra("k","3");
//                            startActivity(intent);
//                            finish();
//                        }else {
//                            Tip.show(getApplicationContext(),response.body().getResult(),false);
//                        }
//                    }
//                });
//    }
//


    private void  readCard_No(){
        //读身份证卡号
        Thread f = new Thread(new Runnable() {
            public void run() {
                while (true) {

                    try {
                        ReadIDThread rt = new ReadIDThread();
                        bs = rt.ReadCardUID();

                        break;

                    } catch (Exception ignored) {

                    }

                }

            }
        });
        f.start();
        try {
            f.join();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try{
            Thread.sleep(500);
        }catch(Exception ignored){

        }

        System.out.println(11111);
        key = true;

        IDCardReaderCallBack rc = new IDCardReaderCallBack() {
            @Override
            public void onReadIdComplete(int iMode, IDCardInfo idCardInfo) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = idCardInfo;

                handler.sendMessage(msg);
            }
        };

        IDCarderReader iR = new IDCarderReader();
        iR.startReaderIDCard(rc, 1);
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        piv_tv.setText("请将身份证放置在下方感应区");
//        handler.post(task);
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(task);
    }
}
