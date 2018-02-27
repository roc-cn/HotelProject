package com.sun.hotelproject.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.hotelproject.moudle.Not_scheduledActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by win7 on 2017/11/27.
 */

public class DatePickUtils {
    private static Calendar calendar=new GregorianCalendar();
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    /**
     * 时间选择器
     * @param t 控件
     */
    public static void getdate(Context c, final TextView t, final TextView content, final String beginDate, final String endDate ) {
        new DatePickerDialog(c, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                t.setText(new StringBuilder().append(mYear).append("-")
                        .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                        .append((0 + mDay < 10) ? "0" + mDay : mDay));
                if (DataTime.phase(DataTime.curenData(),endDate)<=0){
                    // Toast.makeText(IdentificationActivity.this, DataTime.phase(DataTime.curenData(),t.getText().toString())+"天", Toast.LENGTH_SHORT).show();
                   // Toast.makeText(Not_scheduledActivity.this, "时间选择不合理,请重新选择时间", Toast.LENGTH_SHORT).show();
                    content.setText("入住时间小于1天");
                    content.setTextColor(Color.RED);
                    return;
                }
                if (endDate.equals(DataTime.Tomorrow())){
                    content.setTextColor(Color.GRAY);
                    content.setText("明晚离店"+"\u3000共"+(DataTime.phase(beginDate,endDate))+"晚");
                }else {
                    content.setTextColor(Color.GRAY);
                    content.setText(DataTime.dayForWeek(endDate)+"离店"+"\u3000共"+(DataTime.phase(beginDate,endDate))+"晚");
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
