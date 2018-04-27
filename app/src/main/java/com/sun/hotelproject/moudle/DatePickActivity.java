package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;
import com.sun.hotelproject.view.CalendarView;
import com.sun.hotelproject.view.DayManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author  sun
 * 时间 2018-1-9
 *时间选择 activity
 */
public class DatePickActivity extends Activity {
	@BindView(R.id.calendar)CalendarView calendar;
	@BindView(R.id.tv_pre)LinearLayout tv_pre;
	@BindView(R.id.tv_next) LinearLayout tv_next;
	@BindView(R.id.tv_month)TextView tv_month;
	@BindView(R.id.invoice_time)Button invoice_time;
	/**日历对象*/
	private Calendar cal;
	/**格式化工具*/
	private SimpleDateFormat formatter;
	/**日期*/
	private Date curDate;
	private static final String TAG = "DatePickActivity";
	private String str;
	private String weeked;
	Unbinder unbinder;
	private String selectTime="";
	private int selectYear,selectMonth,selectDay;
	String k;
	private String startTime;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		Window window;
		//得到窗口
		window=getWindow();
		//隐藏返回键等虚拟按键
		WindowManager.LayoutParams params=window.getAttributes();
		params.systemUiVisibility= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_IMMERSIVE;
		window.setAttributes(params);
		//设置全屏
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//保持屏幕 常亮
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		setContentView(R.layout.activity_date_pick);
		super.onCreate(savedInstanceState);
		unbinder =ButterKnife.bind(this);
		cal = Calendar.getInstance();
		k=getIntent().getStringExtra("k");
		if (k.equals("1")){
			startTime = DataTime.curenData();
			invoice_time.setText("入住酒店时间");
		}else {
			startTime = getIntent().getStringExtra("startTime");
			invoice_time.setText("续住酒店时间");
			Log.e(TAG, "onCreate: "+startTime );
		}
		init();
		ActivityManager.getInstance().addActivity(this);
		calendar.setOnSelectChangeListener(new CalendarView.OnSelectChangeListener() {
			@Override
			public void selectChange(CalendarView calendarView, Date date) {
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				selectTime =format.format(date);
				Log.e(TAG, "selectChange: "+selectTime );
				if (DataTime.phase(startTime, selectTime) <= 0) {
					if (k.equals("1")) {
						Tip.show(getApplicationContext(), "时间选择不合理", false);
					}else {
						Tip.show(getApplicationContext(), "续住时间必须大于退房时间", false);
					}
					return;
				}
				if (DataTime.phase(startTime, selectTime) > 30) {
					Tip.show(getApplicationContext(), "入住最多支持办理30天业务", false);
					return;
				}
				Intent intent = new Intent();
				intent.putExtra("selectTime", selectTime);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}


	@SuppressLint("SimpleDateFormat")
	protected void init() {
		formatter = new SimpleDateFormat("yyyy年MM月");
		//获取当前时间
		curDate = cal.getTime();
		String str = formatter.format(curDate);
		tv_month.setText(str);
//		String strPre=(cal.get(Calendar.MONTH))+"月";
//		if (strPre.equals("0月")){
//			strPre="12月";
//		}
//		tv_pre.setText(strPre);
//		String strNext=(cal.get(Calendar.MONTH)+2)+"月";
//		if(strNext.equals("13月")){
//			strNext="1月";
//		}
//		tv_next.setText(strNext);

	}


	@OnClick({R.id.tv_pre,R.id.tv_next,R.id.invoice_time})
	void OnClick(View v){
		switch (v.getId()){
			case R.id.tv_pre:
				if (Utils.isFastClick()){
					cal.add(Calendar.MONTH,-1);
					init();
					calendar.setCalendar(cal);
				}
				break;
			case R.id.tv_next:
				if (Utils.isFastClick()) {
					cal.add(Calendar.MONTH, +1);
					init();
					calendar.setCalendar(cal);
				}
				break;
			case R.id.invoice_time:
				Intent intent = new Intent();
				setResult(0, intent);
				finish();
				finish();
//				if (Utils.isFastClick()) {
//					selectYear = DayManager.getSelectYear();
//					selectMonth = DayManager.getSelectMonth();
//					selectDay = DayManager.getSelect();
//					selectTime = selectYear + "-" + selectMonth + "-" + selectDay;
//
//					if (DataTime.phase(startTime, selectTime) <= 0) {
//						Tip.show(getApplicationContext(), "时间选择不合理", false);
//						return;
//					}
//					if (DataTime.phase(startTime, selectTime) > 30) {
//						Tip.show(getApplicationContext(), "入住最多支持办理30天业务", false);
//						return;
//					}
//
//				//	Log.e(TAG, "OnClick: " + selectTime);
//					//weeked=DataTime.dayForWeek(selectTime);
//					Intent intent = new Intent();
//					intent.putExtra("selectYear", selectYear + "");
//					intent.putExtra("selectMonth", selectMonth + "");
//					intent.putExtra("selectDay", selectDay + "");
//					setResult(Activity.RESULT_OK, intent);
//					finish();
//				}
				break;
				default:
					break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}

}
