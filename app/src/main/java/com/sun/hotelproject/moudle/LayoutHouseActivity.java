package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.base.adapter.CommonAdapter;
import com.sun.hotelproject.base.adapter.MultiItemTypeAdapter;
import com.sun.hotelproject.base.adapter.ViewHolder;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.DatePickUtils;
import com.sun.hotelproject.utils.DividerItemDecoration;
import com.sun.hotelproject.utils.Tip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author  sun
 * 时间 2017/11/24
 * TODO:选择房型界面
 */
@Route(path = "/hotel/layouthouse")
public class LayoutHouseActivity extends BaseActivity {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.begin)
    TextView begin;
    @BindView(R.id.end)
    TextView end;
    @BindView(R.id.line_ll)
    LinearLayout line_ll;
    @BindView(R.id.content1)
    TextView content1;
    @BindView(R.id.content2)
    TextView content2;
    @BindView(R.id.data)
    TextView data;
    CommonAdapter adapter;
    List<LayoutHouse> list;
    @BindView(R.id.toolBarBack)
    ImageView toolBarBack;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;
    private static final String TAG = "LayoutHouseActivity";
    String date="";
    @Override
    protected int layoutID() {
        return R.layout.activity_layout_house;
    }

    @Override
    protected void initView() {
        super.initView();
        list=getDatas();
        //Log.e(TAG, "initData: "+list.toString() );
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(manager);
        recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));
        adapter=new CommonAdapter<LayoutHouse>(LayoutHouseActivity.this,R.layout.recycle_item,list) {
            @Override
            protected void convert(ViewHolder holder, LayoutHouse layoutHouse, int position) {
                holder.setText(R.id.type, layoutHouse.getType());
                holder.setText(R.id.content,layoutHouse.getAcreage()+"|"+layoutHouse.getBed_type()+"|"
                        +layoutHouse.getIswindow()+"|"+layoutHouse.getIsbreakfast());
                holder.setText(R.id.iscancel,layoutHouse.getIscancel());
                holder.setText(R.id.price,layoutHouse.getPrice());
                switch (position){
                    case 0:
                        holder.setImageDrawable(R.id.img,getResources().getDrawable(R.drawable.house1));
                        break;
                    case 1:
                        holder.setImageDrawable(R.id.img,getResources().getDrawable(R.drawable.house2));
                        break;
                    case 2:
                        holder.setImageDrawable(R.id.img,getResources().getDrawable(R.drawable.house3));
                        break;
                }
                final LayoutHouse house=list.get(position);
                holder.setOnClickListener(R.id.check, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (end.getText().toString().equals("结束时间")){
                            Tip.show(LayoutHouseActivity.this, "请选择入住天数", false);
                            return;
                        }
                        if (DataTime.phase(DataTime.curenData(),end.getText().toString())<=0){
                            Tip.show(LayoutHouseActivity.this, "选择时间不合理！",false);
                            return;
                        }

                        CommonSharedPreferences.put("beginTime",begin.getText().toString());
                        CommonSharedPreferences.put("endTime",end.getText().toString());
                        CommonSharedPreferences.put("content",data.getText().toString());
                        Intent intent=new Intent(LayoutHouseActivity.this,IdentificationActivity.class);
                        intent.putExtra("house",house);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        };
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        calendar=new GregorianCalendar();
        begin.setText(DataTime.curenData());
        end.setText(DataTime.Tomorrow());
        content1.setText("今天");
        content1.setTextColor(getResources().getColor(R.color.colorSsgray));
        content2.setTextColor(getResources().getColor(R.color.colorSsgray));
        data.setText(DataTime.phase(begin.getText().toString(),end.getText().toString())+"晚");
        content2.setText(DataTime.dayForWeek(end.getText().toString()));
       /* beginTime=getIntent().getStringExtra("begin");
        endTime=getIntent().getStringExtra("end");
        content=getIntent().getStringExtra("content");
        data2=getIntent().getStringExtra("data");

        begin.setText(beginTime);
        end.setText(endTime);
        content2.setText(content);
        data.setText(data2);*/

     /*   String str1="入住\u3000"+beginTime+"\u3000今天";
        //为TextView设置不同的字体大小和颜色
        *//*setMovementMethod

        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)

        Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)

        Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)

        Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)*//*

        SpannableString styledText = new SpannableString(str1);

        styledText.setSpan(new TextAppearanceSpan(this, R.style.textstyle1), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        styledText.setSpan(new TextAppearanceSpan(this, R.style.textstyle0), 2,beginTime.length()+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        styledText.setSpan(new TextAppearanceSpan(this, R.style.textstyle1), beginTime.length()+3, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        begin.setText(styledText, TextView.BufferType.SPANNABLE);

        end.setText(endTime);
        content1.setText(content);*/
    }
    @OnClick({R.id.line_ll,R.id.toolBarBack})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.line_ll:
            //  getdate(end);
                Intent intent =new Intent(LayoutHouseActivity.this,DatePickActivity.class);
                startActivityForResult(intent,1);
               // content2.setText(DataTime.dayForWeek(end.getText().toString()));

              break;
            case R.id.toolBarBack:
                finish();
                break;
        }

    }

    public List<LayoutHouse> getDatas(){
        List<LayoutHouse> list=new ArrayList<LayoutHouse>();
        LayoutHouse house=new LayoutHouse("大床房","18-30㎡","大床","无窗","不含早","不可取消","￥150");
        LayoutHouse house2=new LayoutHouse("标间","15-20㎡","双人床","无窗","不含早","不可取消","￥128");
        LayoutHouse house3=new LayoutHouse("单人间","10-18㎡","单人床","无窗","不含早","不可取消","￥110");
        list.add(house);
        list.add(house2);
        list.add(house3);

        return list;
    }

  /*  public void getdate(final TextView t) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                t.setText(new StringBuilder().append(mYear).append("-")
                        .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                        .append((0 + mDay < 10) ? "0" + mDay : mDay));
                if (DataTime.phase(DataTime.curenData(),t.getText().toString())<=0){
                    // Toast.makeText(IdentificationActivity.this, DataTime.phase(DataTime.curenData(),t.getText().toString())+"天", Toast.LENGTH_SHORT).show();
                    Toast.makeText(LayoutHouseActivity.this, "时间选择不合理,请重新选择时间", Toast.LENGTH_SHORT).show();
                    data.setText("入住时间小于1天");
                    data.setTextColor(Color.RED);
                    return;
                }
               *//* if (end.getText().toString().equals(DataTime.Tomorrow())){
                    content1.setTextColor(Color.GRAY);
                    content1.setText("明晚离店"+"\u3000共"+(DataTime.phase(beginTime,end.getText().toString()))+"晚");
                }else {*//*
                    data.setTextColor(getResources().getColor(R.color.colorSsgray));
                    data.setText(DataTime.phase(begin.getText().toString(),end.getText().toString())+"晚");
                    content2.setText(DataTime.dayForWeek(end.getText().toString()));
              //  }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1 && resultCode == Activity.RESULT_OK){
            this.date=data.getStringExtra("date");
            end.setText(date);
            content2.setText(data.getStringExtra("weeked"));

            this.data.setText(DataTime.phase(begin.getText().toString(),end.getText().toString())+"晚");
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
