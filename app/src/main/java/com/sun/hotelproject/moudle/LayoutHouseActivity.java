package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.base.adapter.CommonAdapter;
import com.sun.hotelproject.base.adapter.ViewHolder;
import com.sun.hotelproject.dao.DaoSimple;
import com.sun.hotelproject.entity.FloorTable;
import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.HouseTable;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.entity.RoomTable;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.DividerItemDecoration;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.view.CustomPopWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author  sun
 * 时间 2017/11/24
 * TODO:选择房型界面
 */
@Route(path = "/hotel/layouthouse")
public class LayoutHouseActivity extends BaseActivity {
    @BindView(R.id.recycler) RecyclerView recycler;
    @BindView(R.id.beginTime) TextView begin;
    @BindView(R.id.endTime) TextView end;
    @BindView(R.id.content) TextView content;
    @BindView(R.id.imgdowm) ImageView imgdown;
    @BindView(R.id.speed_of_progress)ImageView speed_of_progress;
    CommonAdapter adapter;
    CustomPopWindow popWindow;
    List<HouseTable.Bean> list;
    private static final String TAG = "LayoutHouseActivity";
    private DaoSimple daoSimple;
    String date="";
    private int mYear,mMonth,mDay;
    String month="";//英文
    String startTime="";
    String finshTime="";
    String startTime1="";
    String finshTime1="";
    String month2="";
    String lastDay="";
    @SuppressLint("HandlerLeak")
    @Override
    protected int layoutID() {
        return R.layout.activity_layout_house;
    }

    @Override
    protected void initView() {
        super.initView();
        isRuning = true;
        speed_of_progress.setImageResource(R.drawable.home_two);
        daoSimple=new DaoSimple(this);
        list=daoSimple.houseSelAll();
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(manager);
        recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));
        adapter=new CommonAdapter<HouseTable.Bean>(LayoutHouseActivity.this,R.layout.recycle_item,list) {
            @Override
            protected void convert(ViewHolder holder, final HouseTable.Bean bean, int position) {
                RoomTable.Bean bean1= daoSimple.selFloorByRtpmno(bean.getRtpmsno());
                FloorTable.Bean floor=daoSimple.floorSel(bean1.getFpmsno());
                holder.setText(R.id.type, bean.getRtpmsnname());

                holder.setText(R.id.floor_num,floor.getFpmsname());
//                Log.e(TAG, "convert: "+daoSimple.selFloorByRtpmno(bean.getRtpmsno()));
//                Log.e(TAG, "convert: "+daoSimple.floorSel(bean1.getFpmsno()));
                holder.setOnClickListener(R.id.check, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (end.getText().toString().equals("结束时间")){
//                            Tip.show(LayoutHouseActivity.this, "请选择入住天数", false);
//                            return;
//                        }
                        if (DataTime.phase(startTime,finshTime)<=0){
                            Tip.show(LayoutHouseActivity.this, "选择时间不合理！",false);
                            return;
                        }
                        CommonSharedPreferences.put("beginTime",startTime);
                        CommonSharedPreferences.put("endTime",finshTime);
                        CommonSharedPreferences.put("beginTime1",startTime1);
                        CommonSharedPreferences.put("endTime1",finshTime1);
                        CommonSharedPreferences.put("content",content.getText().toString());
                        Intent intent=new Intent(LayoutHouseActivity.this,SelectActivity.class);
                        intent.putExtra("rtpmsno",bean.getRtpmsno());//房型码
                        intent.putExtra("name",bean.getRtpmsnname());
                        intent.putExtra("mchid",mchid);
                        startActivityForResult(intent,2);

                    }
                });
            }
        };
        recycler.setAdapter(adapter);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        Calendar c = Calendar.getInstance();//
        c.setTime(new Date());
        mYear = c.get(Calendar.YEAR); // 获取当前年份
        mMonth = c.get(Calendar.MONTH)+1;// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        month = DataTime.returnToEnglish(mMonth);
        lastDay = DataTime.getLastOfMonth(DataTime.curenData());
        Log.e(TAG, "initData: "+mYear+" "+mMonth+" "+mDay );
        if ((mDay+1)>Integer.parseInt(lastDay)) {
                if (mMonth<10) {
                    begin.setText(DataTime.updTextSize(getApplicationContext(), mDay + " / 0" + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);
                    month = DataTime.returnToEnglish(mMonth + 1);
                    end.setText(DataTime.updTextSize(getApplicationContext(), "01" + " / 0" + (mMonth + 1) + " " + month, 2), TextView.BufferType.SPANNABLE);
                    startTime = mYear + "-0" +mMonth + "-" + mDay;
                    startTime1 = "0"+mMonth + "/" + mDay;
                    finshTime = mYear + "-0" + mMonth + "-" + "01";
                    finshTime1 = "0"+mMonth + "/" + "01";
                }else {
                    begin.setText(DataTime.updTextSize(getApplicationContext(), mDay + " / " + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);
                    month = DataTime.returnToEnglish(mMonth + 1);
                    end.setText(DataTime.updTextSize(getApplicationContext(), "01" + " / " + (mMonth + 1) + " " + month, 2), TextView.BufferType.SPANNABLE);
                    startTime = mYear + "-" +mMonth + "-" + mDay;
                    startTime1 = ""+mMonth + "/" + mDay;
                    finshTime = mYear + "-" + mMonth + "-" + "01";
                    finshTime1 = ""+mMonth + "/" + "01";

                }
        }else {
            if (mDay<10 && mMonth<10 &&(mDay+1)!=10){
            begin.setText(DataTime.updTextSize(getApplicationContext(), "0"+mDay + " / 0" + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);
            end.setText(DataTime.updTextSize(getApplicationContext(), "0"+(mDay + 1) + " / 0" + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);

            startTime = mYear + "-" +"0"+mMonth + "-0" + mDay;
            startTime1 = "0"+mMonth + "/" + mDay;
            finshTime = mYear + "-0" + mMonth + "-0" + (mDay + 1);
            finshTime1 = "0"+mMonth + "/" + "0"+(mDay + 1);
        }else {
            begin.setText(DataTime.updTextSize(getApplicationContext(), mDay + " / " + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);
            end.setText(DataTime.updTextSize(getApplicationContext(), (mDay + 1) + " / " + mMonth + " " + month, 2), TextView.BufferType.SPANNABLE);
            startTime = mYear + "-" +mMonth + "-" + mDay;
            startTime1 = mMonth + "/" + mDay;
            finshTime = mYear + "-" + mMonth + "-" + (mDay + 1);
            finshTime1 = mMonth + "/" + (mDay + 1);
        }
        }
        content.setText(DataTime.updTextSize(getApplicationContext(),DataTime.phase(startTime,finshTime)+"晚 / (night)",3), TextView.BufferType.SPANNABLE);

        Log.e(TAG, "initData: "+startTime+"\n"+finshTime );

    }
    @OnClick({R.id.imgdowm})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.imgdowm:
               Intent intent =new Intent(this,DatePickActivity.class);
                    intent.putExtra("k","1");
                    startActivityForResult(intent,1);
                break;
        }
    }



    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1 && resultCode == Activity.RESULT_OK){
            //this.date=data.getStringExtra("date");
            if (isRuning){
            handler.postDelayed(timeRunnable,1000);
        }
            int selectYear = Integer.parseInt(data.getStringExtra("selectYear"));
            int selectMonth = Integer.parseInt(data.getStringExtra("selectMonth"));
            int selectDay = Integer.parseInt(data.getStringExtra("selectDay"));
            Log.e(TAG, "onActivityResult: "+selectYear );

            month = DataTime.returnToEnglish(selectMonth);
            end.setText(DataTime.updTextSize(getApplicationContext(),selectDay+" / "+selectMonth+" "+month,2), TextView.BufferType.SPANNABLE);
           if (selectMonth<10 &&selectDay <10){
            finshTime =selectYear+"-"+"0"+selectMonth+"-0"+selectDay;
            finshTime1 = "0"+selectMonth+"/0"+selectDay;
           }else {
               finshTime =selectYear+"-"+selectMonth+"-"+selectDay;
               finshTime1 = selectMonth+"/"+selectDay;
           }
            // content.setText(data.getStringExtra("weeked"));
            this.content.setText(DataTime.updTextSize(getApplicationContext(),DataTime.phase(startTime,finshTime)+"晚 / (night)",3));
          //  getPost();
        }
        if (requestCode == 2 && resultCode ==0){
            if (isRuning){
                handler.postDelayed(timeRunnable,1000);
            }
        }
        if (requestCode==2 && resultCode == Activity.RESULT_OK){
            String locksign =data.getStringExtra("locksign");
            GuestRoom.Bean gBean= (GuestRoom.Bean) data.getSerializableExtra("bean");
                Intent intent=new Intent(LayoutHouseActivity.this,OrderDetailsActivity.class);
                intent.putExtra("bean",gBean);
                intent.putExtra("locksign",locksign);
                startActivity(intent);
                finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
