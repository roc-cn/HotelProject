package com.sun.hotelproject.moudle;

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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;


import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.Tip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

	@BindView(R.id.date_picker)
	DatePicker datePicker;
	@BindView(R.id.ok)
	Button ok;
	@BindView(R.id.cancel)
	Button cancel;
	private static final String TAG = "DatePickActivity";
	private String str;
	private String weeked;
	Unbinder unbinder;

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
		initData();
	}


	protected void initData() {
		datePicker.setCalendarViewShown(false);
		str=getData();
		Log.e(TAG, "initData: str-->"+str );
	}
	@OnClick({R.id.date_picker,R.id.ok,R.id.cancel})
	void OnClick(View v){
		switch (v.getId()){
			case R.id.date_picker:
				break;
			case R.id.cancel:
				finish();
				break;
			case R.id.ok:
				str=getData();
				if (str.equals("")){
					return;
				}
				if (DataTime.phase(DataTime.curenData(),str)<=0){
					Tip.show(getApplicationContext(),"入住时间必须大于1天",false);
					return;
				}
				weeked=DataTime.dayForWeek(str);

				Intent intent =new Intent();
				intent.putExtra("date",str);
				intent.putExtra("weeked",weeked);
				setResult(Activity.RESULT_OK,intent);
				finish();
				break;
		}
	}


	private String getData() {
		StringBuilder str = new StringBuilder().append(datePicker.getYear()).append("-")
				.append((datePicker.getMonth() + 1) < 10 ? "0" + (datePicker.getMonth() + 1)
						: (datePicker.getMonth() + 1))
				.append("-")
				.append((datePicker.getDayOfMonth() < 10) ? "0" + datePicker.getDayOfMonth()
						: datePicker.getDayOfMonth());
		return str.toString();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}
}
