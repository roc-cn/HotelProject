package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.Affirmstay;
import com.sun.hotelproject.entity.QueryCheckin;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

import static com.sun.hotelproject.moudle.MainActivity.MacAddr;

/**
 * 续住
 */

public class RenwalActivity extends BaseActivity {

    @BindView(R.id.imgdowm)
    ImageView imgdowm;
    @BindView(R.id.beginTime)
    TextView begin;
    @BindView(R.id.endTime)
    TextView end;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.relative_time)
    TextView time;
    @BindView(R.id.relative_preson)
    TextView preson;
    @BindView(R.id.relative_confirm)
    Button confirm;
    @BindView(R.id.price)TextView price;
    @BindView(R.id.sp2_img4)ImageView sp2_img4;
    private String k;
    private int inDay;
    private static final String TAG = "RenwalActivity";
    private String mYear,mMonth,mDay;
    private String month,month2;
    private String startTime,finshTime,startTime1,finshTime1;
    private String name,idCard_No;
    private ArrayList<QueryCheckin> list;
    private QueryCheckin.Bean b;
    private String lastData;
    private String querytype;
    private String roomNum;
    private String mchid;
    Animation operatingAnim;
    @BindView(R.id.anim_layout)
    RelativeLayout anim_lauout;
    @BindView(R.id.anim_img)ImageView anim_img;
    @BindView(R.id.anim_tv)TextView anim_tv;
    @BindView(R.id.show_data)LinearLayout show_data;
    @BindView(R.id.sp2_tv4)TextView sp2_tv4;
    private AnimationDrawable animationDrawable;
    Affirmstay.Bean ab;

    @Override
    protected int layoutID() {
        return R.layout.activity_renwal;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        ActivityManager.getInstance().addActivity(this);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.load_animation);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        mchid = (String) CommonSharedPreferences.get("mchid","");
        querytype =getIntent().getStringExtra("querytype");
        price.setText(DataTime.updTextSize2(getApplicationContext(), "￥" + "0.00", 1), TextView.BufferType.SPANNABLE);
        sp2_tv4.setBackgroundResource(R.drawable.oval_shape);
        sp2_tv4.setTextColor(getResources().getColor(R.color.Swrite));
        sp2_img4.setVisibility(View.VISIBLE);
        k = getIntent().getStringExtra("k");
        if (querytype.equals("2")) {

            name = getIntent().getStringExtra("name");
            idCard_No = getIntent().getStringExtra("id_CardNo");
            queryCheckin(querytype,idCard_No.trim());
        }else {
            roomNum =getIntent().getStringExtra("roomNum");
            queryCheckin(querytype,roomNum);
        }
//        Calendar c = Calendar.getInstance();//
//        c.setTime(new Date());
//        mYear = c.get(Calendar.YEAR); // 获取当前年份
//        mMonth = c.get(Calendar.MONTH)+1;// 获取当前月份
//        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
//        month = DataTime.returnToEnglish(mMonth);
//        Log.e(TAG, "initData: "+mYear+" "+mMonth+" "+mDay );
//
//        begin.setText(DataTime.updTextSize(getApplicationContext(),mDay+" / "+mMonth+" "+month,2), TextView.BufferType.SPANNABLE);
//        end.setText(DataTime.updTextSize(getApplicationContext(),(mDay+1)+" / "+mMonth+" "+month,2), TextView.BufferType.SPANNABLE);



        //lastData =DataTime.getLastOfMonth();
    }
    @OnClick({R.id.show_data,R.id.relative_confirm})
    public void onClick(View v){
        Intent intent =new Intent();
        switch (v.getId()){
            case R.id.show_data:
                if (Utils.isFastClick()) {
                    intent.setClass(this, DatePickActivity.class);
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("k", "2");
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.relative_confirm:

                if (Utils.isFastClick()) {
                    if (end.getText().toString().equals("续住时间")){
                        Tip.show(RenwalActivity.this, "请选择入住天数", false);
                        return;
                    }
                    if (price.getText().toString().trim().equals("￥0.00")){
                        return;
                    }
                    CommonSharedPreferences.put("beginTime", startTime);
                    CommonSharedPreferences.put("endTime", finshTime);
                    CommonSharedPreferences.put("beginTime1", startTime1);
                    CommonSharedPreferences.put("endTime1", finshTime1);
                    CommonSharedPreferences.put("content", content.getText().toString());
                    CommonSharedPreferences.put("inDay", inDay + "");
                    intent.setClass(RenwalActivity.this, FaceRecognitionActivity.class);
                    intent.putExtra("bean", b);
                    intent.putExtra("k", k);
                    intent.putExtra("bean2",ab);
                    intent.putExtra("querytype", querytype);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     *确认续住
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void Affirmstay(){
        anim_lauout.setVisibility(View.VISIBLE);

        // animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.load_animation);
        anim_img.setAnimation(operatingAnim);
        anim_img.startAnimation(operatingAnim);
        anim_tv.setText("正在加载中......");
//        if (animationDrawable != null && !animationDrawable.isRunning()){
//            animationDrawable.start();
//        }
        OkGo.<Affirmstay>post(HttpUrl.AFFIRMSTAY)
                .tag(this)
                .params("mchid",mchid)
                .params("pcodetype","R")
                .params("indate",startTime)
                .params("outdate",finshTime)
                .params("rtpmsno",b.getRtpmsno())
                .execute(new JsonCallBack<Affirmstay>(Affirmstay.class) {
                    @Override
                    public void onSuccess(Response<Affirmstay> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
                        if (response.body().getRescode().equals("0000")) {
                            anim_img.clearAnimation();
                            anim_lauout.setVisibility(View.GONE);
                            ab =response.body().getDatalist().get(response.body().getDatalist().size()-1);

                            Log.e(TAG, "onSuccess: "+ab.toString() );
                            price.setText(DataTime.updTextSize2(getApplicationContext(), "￥" + ab.getSureprice(), 1), TextView.BufferType.SPANNABLE);
                        }else {
                            Tip.show(getApplicationContext(),response.body().getResult(),false);
                            anim_img.clearAnimation();
                            anim_lauout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Response<Affirmstay> response) {
                        super.onError(response);
                        anim_img.clearAnimation();
                        anim_lauout.setVisibility(View.GONE);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                    }
                });

    }

    /**
     * 查询入住单
     * @param querytype 查询值类型 2-入住人身份证号,3-房号,4-入住单PMS编码
     * @param querydata  具体证件号码
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void queryCheckin(final String querytype, String querydata){
        anim_lauout.setVisibility(View.VISIBLE);

        // animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.load_animation);
        anim_img.setAnimation(operatingAnim);
        anim_img.startAnimation(operatingAnim);
        anim_tv.setText("正在加载中......");
//        if (animationDrawable != null && !animationDrawable.isRunning()){
//            animationDrawable.start();
//        }
        OkGo.<QueryCheckin>post(HttpUrl.QUERYCHECKIN)
                .tag(this)
                .params("mchid",mchid)
                .params("querytype",querytype)
                .params("querydata",querydata)
                .execute(new JsonCallBack<QueryCheckin>(QueryCheckin.class) {
                    @Override
                    public void onSuccess(Response<QueryCheckin> response) {
                        super.onSuccess(response);
                       // Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
                        if (response.body().getRescode().equals("0000")){
                            b=response.body().getDatalist().get(response.body().getDatalist().size()-1);
                          //  Log.e(TAG, "onSuccess: "+b.toString());
                            startTime =DataTime.getMyDate2(b.getOuttime());
                            String [] time = startTime.split("-");
                            mMonth =time[1];
                            mDay =time[2];
                            startTime1 = mMonth +"/"+mDay;
                            month =DataTime.returnToEnglish(Integer.parseInt(mMonth));
                            begin.setText(DataTime.updTextSize(getApplicationContext(), mDay + " / " + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);
                            end.setText("续住时间");
                            content.setText("共计时间");
                            anim_lauout.setVisibility(View.GONE);
                           anim_img.clearAnimation();
                            Log.e(TAG, "onSuccess: "+startTime );
                          //  Log.e(TAG, "onSuccess: "+startTime );
//                            lastData = DataTime.getLastOfMonth();
//                            String []lastdatas= lastData.split("-");
//                            String lastDay = lastdatas[2];
//                          //  Log.e(TAG, "onSuccess: "+lastData);
//                            String[] outTime =startTime.split("-");
//                            String year = outTime[0];
//                            String month1 = outTime[1];
//                            String day = outTime[2];
//                            mDay = Integer.parseInt(day);
//                            mYear = Integer.parseInt(year);
//                            String month2=month1.substring(month1.length()-1,month1.length());
//                            mMonth = Integer.parseInt(month2);
//                            month = DataTime.returnToEnglish(mMonth);
//                            //Log.e(TAG, "onSuccess: "+month3);
//                            if ((mDay+1) > Integer.parseInt(lastDay)){
//                                if (mMonth<10) {
//                                    begin.setText(DataTime.updTextSize(getApplicationContext(), mDay + " / 0" + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);
//                                    month = DataTime.returnToEnglish(mMonth + 1);
//                                    end.setText(DataTime.updTextSize(getApplicationContext(), "01" + "  / 0" + (mMonth + 1) + " " + month, 2), TextView.BufferType.SPANNABLE);
//                                    startTime1 = "0"+mMonth + "/"+mDay;
//                                    finshTime = mYear + "-" + "0"+(mMonth+1) + "-" + "01";
//                                    finshTime1 = "0"+(mMonth+1) + "/" + "01";
//                                }else {
//                                    begin.setText(DataTime.updTextSize(getApplicationContext(), mDay + " / " + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);
//                                    month = DataTime.returnToEnglish(mMonth + 1);
//                                    end.setText(DataTime.updTextSize(getApplicationContext(), "01" + " / " + (mMonth + 1) + " " + month, 2), TextView.BufferType.SPANNABLE);
//                                    startTime1 = mMonth + "/"+mDay;
//                                    finshTime = mYear + "-" +(mMonth+1) + "-" + "01";
//                                    finshTime1 = (mMonth+1) + "/" + "01";
//                                }
//
//                            }else {
//                                if (mDay <10 && mMonth<10 &&(mDay+1)!=10) {
//                                    begin.setText(DataTime.updTextSize(getApplicationContext(),"0"+mDay+" / 0"+mMonth+" "+month,2), TextView.BufferType.SPANNABLE);
//                                    end.setText(DataTime.updTextSize(getApplicationContext(),"0"+(mDay+1)+" / 0"+mMonth+" "+month,2), TextView.BufferType.SPANNABLE);
//
//                                    startTime1 = "0"+mMonth + "/" + "0"+mDay;
//                                    finshTime = mYear + "-" + "0"+mMonth + "-0" + (mDay + 1);
//                                    finshTime1 = "0"+mMonth + "/0" + (mDay + 1);
//                                }else {
//                                    startTime1 = mMonth + "/" + mDay;
//                                    finshTime = mYear + "-" + mMonth + "-" + (mDay + 1);
//                                    finshTime1 = mMonth + "/" + (mDay + 1);
//                                }
//                            }
//
//                            content.setText(DataTime.updTextSize(getApplicationContext(),DataTime.phase(startTime,finshTime)+"晚 / (night)",3), TextView.BufferType.SPANNABLE);
//                            time.setText(startTime1+"——"+finshTime1+"  "+content.getText().toString());
//                            inDay = DataTime.phase(startTime,finshTime);
//                            Log.e(TAG, "onSuccess: "+finshTime );

                            preson.setText(b.getGuestname());
                        }else {
                            anim_lauout.setVisibility(View.GONE);
                            anim_img.clearAnimation();
                            if (querytype.equals("3")){
                                getCard();
                            }
                            Tip.show(getApplicationContext(),response.body().getResult(),false);
                        }
                    }

                    @Override
                    public void onError(Response<QueryCheckin> response) {
                        anim_lauout.setVisibility(View.GONE);
                        anim_img.clearAnimation();
                        if (querytype.equals("3")){
                            getCard();
                        }
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                        super.onError(response);
                    }
                });

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

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==1 && resultCode== Activity.RESULT_OK){
            if (isRuning){
                handler.postDelayed(timeRunnable,1000);
            }

            String selectTime = data.getStringExtra("selectTime");
            String[] select =selectTime.split("-");
            String selectYear = select[0];
            String selectMonth = select[1];
            String selectDay = select[2];
            finshTime =selectTime;
            finshTime1 =selectMonth+"/"+selectDay;
            Log.e(TAG, "onActivityResult: "+selectYear +selectMonth +selectDay );
            //  Log.e(TAG, "onActivityResult: "+selectYear );

            month = DataTime.returnToEnglish(Integer.parseInt(selectMonth));

            end.setText(DataTime.updTextSize(getApplicationContext(),selectDay+" / "+selectMonth+" "+month,2), TextView.BufferType.SPANNABLE);


            inDay = DataTime.phase(startTime,finshTime);
            if (inDay<10) {

                this.content.setText(DataTime.updTextSize(getApplicationContext(), "0"+inDay + "/晚(night)", 2));
            }else {

                this.content.setText(DataTime.updTextSize(getApplicationContext(), inDay + "/晚(night)", 2));

            }
            time.setText(startTime1+"——"+finshTime1+"  "+content.getText().toString());
            //price.setText("￥"+(int) (Double.valueOf(b.getTodayprice())*inDay));
            Affirmstay();
        }
    }
}
