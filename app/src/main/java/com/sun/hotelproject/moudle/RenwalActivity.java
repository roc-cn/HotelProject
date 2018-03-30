package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.QueryCheckin;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

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
    private String k;
    private static final String TAG = "RenwalActivity";
    private int mYear,mMonth,mDay;
    private String month,month2;
    private String startTime,finshTime,startTime1,finshTime1;
    private String name,idCard_No;
    private ArrayList<QueryCheckin> list;
    private QueryCheckin.Bean b;
    private String lastData;
    @Override
    protected int layoutID() {
        return R.layout.activity_renwal;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        k=getIntent().getStringExtra("k");
        name=getIntent().getStringExtra("name");
        idCard_No=getIntent().getStringExtra("id_CardNo");
        Calendar c = Calendar.getInstance();//
        c.setTime(new Date());
        mYear = c.get(Calendar.YEAR); // 获取当前年份
        mMonth = c.get(Calendar.MONTH)+1;// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
//        month = DataTime.returnToEnglish(mMonth);
//        Log.e(TAG, "initData: "+mYear+" "+mMonth+" "+mDay );
//
//        begin.setText(DataTime.updTextSize(getApplicationContext(),mDay+" / "+mMonth+" "+month,2), TextView.BufferType.SPANNABLE);
//        end.setText(DataTime.updTextSize(getApplicationContext(),(mDay+1)+" / "+mMonth+" "+month,2), TextView.BufferType.SPANNABLE);

        preson.setText(name.trim());
        queryCheckin("2",idCard_No.trim());
        //lastData =DataTime.getLastOfMonth();
    }
    @OnClick({R.id.imgdowm,R.id.relative_confirm})
    public void onClick(View v){
        Intent intent =new Intent();
        switch (v.getId()){
            case R.id.imgdowm:
                intent.setClass(this,DatePickActivity.class);
                intent.putExtra("k","2");
                startActivityForResult(intent,1);
                 break;
            case R.id.relative_confirm:
                CommonSharedPreferences.put("beginTime",startTime);
                CommonSharedPreferences.put("endTime",finshTime);
                CommonSharedPreferences.put("beginTime1",startTime1);
                CommonSharedPreferences.put("endTime1",finshTime1);
                CommonSharedPreferences.put("content",content.getText().toString());
                intent.setClass(RenwalActivity.this,FaceRecognitionActivity.class);
                intent.putExtra("bean", b);
                intent.putExtra("k",k);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 查询入住单
     * @param querytype 查询值类型 2-入住人身份证号,3-房号,4-入住单PMS编码
     * @param querydata  具体证件号码
     */
    public void queryCheckin(String querytype,String querydata){
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
                            b=response.body().getDatalist().get(0);
                          //  Log.e(TAG, "onSuccess: "+b.toString());
                            startTime =DataTime.getMyDate2(b.getOuttime());
                          //  Log.e(TAG, "onSuccess: "+startTime );
                            lastData = DataTime.getLastOfMonth(startTime);
                            String []lastdatas= lastData.split("-");
                            String lastDay = lastdatas[2];
                          //  Log.e(TAG, "onSuccess: "+lastData);
                            String[] outTime =startTime.split("-");
                            String year = outTime[0];
                            String month1 = outTime[1];
                            String day = outTime[2];
                            mDay = Integer.parseInt(day);
                            mYear = Integer.parseInt(year);
                            String month2=month1.substring(month1.length()-1,month1.length());
                            mMonth = Integer.parseInt(month2);
                            month = DataTime.returnToEnglish(mMonth);
                            //Log.e(TAG, "onSuccess: "+month3);
                            if ((mDay+1) > Integer.parseInt(lastDay)){
                                if (mMonth<10) {
                                    begin.setText(DataTime.updTextSize(getApplicationContext(), mDay + " / 0" + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);
                                    month = DataTime.returnToEnglish(mMonth + 1);
                                    end.setText(DataTime.updTextSize(getApplicationContext(), "01" + "  / 0" + (mMonth + 1) + " " + month, 2), TextView.BufferType.SPANNABLE);
                                    startTime1 = "0"+mMonth + "/"+mDay;
                                    finshTime = mYear + "-" + "0"+(mMonth+1) + "-" + "01";
                                    finshTime1 = "0"+(mMonth+1) + "/" + "01";
                                }else {
                                    begin.setText(DataTime.updTextSize(getApplicationContext(), mDay + " / " + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);
                                    month = DataTime.returnToEnglish(mMonth + 1);
                                    end.setText(DataTime.updTextSize(getApplicationContext(), "01" + " / " + (mMonth + 1) + " " + month, 2), TextView.BufferType.SPANNABLE);
                                    startTime1 = mMonth + "/"+mDay;
                                    finshTime = mYear + "-" +(mMonth+1) + "-" + "01";
                                    finshTime1 = (mMonth+1) + "/" + "01";
                                }

                            }else {
                                if (mDay <10 && mMonth<10 &&(mDay+1)!=10) {
                                    begin.setText(DataTime.updTextSize(getApplicationContext(),"0"+mDay+" / 0"+mMonth+" "+month,2), TextView.BufferType.SPANNABLE);
                                    end.setText(DataTime.updTextSize(getApplicationContext(),"0"+(mDay+1)+" / 0"+mMonth+" "+month,2), TextView.BufferType.SPANNABLE);

                                    startTime1 = "0"+mMonth + "/" + "0"+mDay;
                                    finshTime = mYear + "-" + "0"+mMonth + "-0" + (mDay + 1);
                                    finshTime1 = "0"+mMonth + "/0" + (mDay + 1);
                                }else {
                                    startTime1 = mMonth + "/" + mDay;
                                    finshTime = mYear + "-" + mMonth + "-" + (mDay + 1);
                                    finshTime1 = mMonth + "/" + (mDay + 1);
                                }
                            }
                            content.setText(DataTime.updTextSize(getApplicationContext(),DataTime.phase(startTime,finshTime)+"晚 / (night)",3), TextView.BufferType.SPANNABLE);
                            time.setText(startTime1+"——"+finshTime1+"  "+content.getText().toString());

                            Log.e(TAG, "onSuccess: "+finshTime );


                        }else {
                            Tip.show(getApplicationContext(),response.body().getResult(),false);
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==1 && resultCode== Activity.RESULT_OK){
            if (isRuning){
                handler.postDelayed(timeRunnable,1000);
            }
            int selectYear = Integer.parseInt(data.getStringExtra("selectYear"));
            int selectMonth = Integer.parseInt(data.getStringExtra("selectMonth"));
            int selectDay = Integer.parseInt(data.getStringExtra("selectDay"));
            Log.e(TAG, "onActivityResult: "+selectYear );

            month = DataTime.returnToEnglish(selectMonth);
            end.setText(DataTime.updTextSize(getApplicationContext(),selectDay+" / "+selectMonth+" "+month,2), TextView.BufferType.SPANNABLE);
            if (selectMonth<10){
                finshTime =selectYear+"-"+"0"+selectMonth+"-"+selectDay;
                finshTime1 = "0"+selectMonth+"/"+selectDay;
            }else {
                finshTime =selectYear+"-"+selectMonth+"-"+selectDay;
                finshTime1 = selectMonth+"/"+selectDay;
            }
            // content.setText(data.getStringExtra("weeked"));
            this.content.setText(DataTime.updTextSize(getApplicationContext(),DataTime.phase(startTime,finshTime)+"晚 / (night)",3));
            time.setText(startTime1+"——"+finshTime1+"  "+content.getText().toString());
        }
    }
}
