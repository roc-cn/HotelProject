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
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Route;

import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;

import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.moudle.id_card.IDCardInfo;
import com.sun.hotelproject.moudle.id_card.IDCardReaderCallBack;
import com.sun.hotelproject.moudle.id_card.IDCarderReader;

import com.szxb.smart.pos.jni_interface.Card_Sender;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author  sun
 * 时间 2017/11/22
 * TODO:已预定界面
 */
@Route(path = "/hotel/identification")
public class IdentificationActivity extends BaseActivity {

    @BindView(R.id.close)
    Button close;
    @BindView(R.id.piv_tv)
    TextView piv_tv;
    @BindView(R.id.toolBarBack)
    ImageView toolBarBack;
   /* @BindView(R.id.xiangji)
    Button xiangji;
    @BindView(R.id.xiangji2)
    Button xiangji2;*/

    private String name;  //姓名
    private String id_cardNo; //身份证号
    private static final String TAG = "SecondActivity";
    private Thread thread;
    private IDCardReaderCallBack readerCallBack;
    private IDCarderReader idCarderReader;
    LayoutHouse house;


    // String url="https://m.10010.com/queen/tencent/king-half-tab.html?u=k11otXDHTj5LfSzAAg4aRS67754Su7/KWPx19C1rGxk=";
    //  String url2="http://192.168.0.111/tercent/page.html";


    int count = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            count = 6;

            if (piv_tv != null) {
                if (msg.what == 1) {
                    IDCardInfo idCardInfo = (IDCardInfo) msg.obj;
                    if (idCardInfo != null) {
                        piv_tv.setText("读取成功!");
                        piv_tv.setTextColor(Color.WHITE);
                        name = idCardInfo.getStrName();
                        id_cardNo = idCardInfo.getStrIdCode();
                        close.setEnabled(true);
                        //  paizhao();
                        Intent intent = new Intent(IdentificationActivity.this, VerificationResultActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("id_CardNo", id_cardNo);
                        intent.putExtra("house",house);
                        intent.putExtra("k","1");
                        startActivity(intent);
                        finish();
                    } else {
                        //弹窗提示
                        piv_tv.setText("读取失败！");
                        piv_tv.setTextColor(Color.RED);
                        close.setEnabled(true);
                    }
                }
            }
         /*   if (msg.what ==2){
                IDCardInfo idCardInfo = (IDCardInfo) msg.obj;
                if (idCardInfo != null){
                    piv_tv.setText("读取成功!");
                    piv_tv.setTextColor(Color.WHITE);
                    name=idCardInfo.getStrName();
                    id_cardNo=idCardInfo.getStrIdCode();
                    xiangji.setEnabled(true);
                    Intent intent =new Intent(SecondActivity.this,ThirdActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("id_cardNo",id_cardNo);
                    startActivity(intent);
                    finish();

                }else {
                    piv_tv.setText("读取失败！");
                    piv_tv.setTextColor(Color.RED);
                    xiangji.setEnabled(true);
                }

            }*/

        }
    };

    @Override
    protected int layoutID() {
        return R.layout.activity_identification;
    }

    @Override
    protected void initData() {
        count = 0;
        Card_Sender my_Card_Sender = new Card_Sender();
        int[] nStatus = new int[1];
        boolean zt = my_Card_Sender.TY_GetStatus(nStatus);
        house= (LayoutHouse) getIntent().getSerializableExtra("house");
        //verification.setVisibility(View.GONE);
     /*   xiangji.setVisibility(View.GONE);
        xiangji2.setVisibility(View.GONE);*/
        verification_fun();

        Log.e("发卡机状态:", "发卡机状态:" + zt);
        // str=getIntent().getStringExtra("select");
        // select.setText(str);
        //  Log.e(TAG, "initData: "+str);
    }


    private void verification_fun() {
        piv_tv.setTextColor(Color.WHITE);
        piv_tv.setText("数据读取中请稍后......");

        readerCallBack = new IDCardReaderCallBack() {
            @Override
            public void onReadIdComplete(int iMode, IDCardInfo idCardInfo) {

                Message msg = new Message();
                msg.what = 1;
                msg.obj = idCardInfo;
                handler.sendMessage(msg);
            }
        };
        idCarderReader = new IDCarderReader();


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (count <= 5) {

                    idCarderReader.startReaderIDCard(readerCallBack, 1);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                    if (count > 5) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {


                                //Toast.makeText(SecondActivity.this, "失败识别", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        thread.start();
    }

    @OnClick({R.id.close,R.id.toolBarBack})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                count = 6;
                finish();
         /*   Intent intent =new Intent(this,FaceRecognitionActivity.class);
            startActivity(intent);
*/                break;
            case R.id.toolBarBack:
                count = 6;
                finish();
                break;
            default:
                break;
        }
    }

}
