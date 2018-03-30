package com.sun.hotelproject.moudle;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.utils.DataTime;

import com.sun.hotelproject.utils.Router;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author  sun
 * 时间：2017/11/24
 * TODO:未预定界面
 * 无效界面
 */
@Route(path = "/hotel/not_schedule")
public class Not_scheduledActivity extends BaseActivity {
    //@BindView(R.id.line_ll)LinearLayout line_ll;
   // @BindView(R.id.line_ll2)LinearLayout line_ll2;
    @BindView(R.id.dataselect)Button dataselect;
    @BindView(R.id.begin)TextView begin;
    @BindView(R.id.end)TextView end;
    @BindView(R.id.content1)TextView content1;
    @BindView(R.id.content2)TextView content2;
    @BindView(R.id.houseselect)Button houseselect;
    @BindView(R.id.data)TextView data;


    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;

    @Override
    protected int layoutID() {
        return R.layout.activity_not_scheduled;
    }

    @Override
    protected void initData() {
        calendar=new GregorianCalendar();
        begin.setText(DataTime.curenData());
        content1.setText("今天");
        content1.setTextColor(getResources().getColor(R.color.colorSsgray));
        content2.setTextColor(getResources().getColor(R.color.colorSsgray));
      //  showDialog();
    }
    @OnClick({R.id.dataselect,R.id.houseselect})
    void Click(View v){
        switch (v.getId()){
            case R.id.dataselect:
              getdate(end);
                break;
            case R.id.houseselect:
                if (end.getText().toString().equals("结束时间")){
                    Toast.makeText(this, "请选择入住天数", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DataTime.phase(DataTime.curenData(),end.getText().toString())<=0){
                    Toast.makeText(this, "选择时间不合理！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(this, LayoutHouseActivity.class);
                intent.putExtra("begin",begin.getText().toString());
                intent.putExtra("end",end.getText().toString());
                intent.putExtra("content",content2.getText().toString());
                intent.putExtra("data",data.getText().toString());
                startActivity(intent);
                finish();
                break;
        }

    }

    /**
     * 时间选择器
     * @param t 控件
     */
    public void getdate(final TextView t) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                t.setText(new StringBuilder().append(mYear).append("-")
                        .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                        .append((0 + mDay < 10) ? "0" + mDay : mDay));
                if (DataTime.phase(DataTime.curenData(),t.getText().toString())<=0){
                    // Toast.makeText(IdentificationActivity.this, DataTime.phase(DataTime.curenData(),t.getText().toString())+"天", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Not_scheduledActivity.this, "时间选择不合理,请重新选择时间", Toast.LENGTH_SHORT).show();
                   data.setText("时间选择不合理");
                  //  content2.setText("入住时间小于1天");
                    data.setTextColor(Color.RED);
                    return;
                }
              /*  if (end.getText().toString().equals(DataTime.Tomorrow())){
                    content2.setTextColor(getResources().getColor(R.color.colorSsgray));
                    content2.setText("明晚");
                    data.setText(DataTime.phase(begin.getText().toString(),end.getText().toString())+"晚");
                  //  content2.setText("明晚离店"+"\u3000共"+(DataTime.phase(begin.getText().toString(),end.getText().toString()))+"晚");
                }else {*/
                    content2.setText(DataTime.dayForWeek(end.getText().toString()));
                    data.setText(DataTime.phase(begin.getText().toString(),end.getText().toString())+"晚");
                    data.setTextColor(getResources().getColor(R.color.colorSsgray));
                   // content2.setText(DataTime.dayForWeek(end.getText().toString())+"离店"+"\u3000共"+()+"晚");
               // }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
