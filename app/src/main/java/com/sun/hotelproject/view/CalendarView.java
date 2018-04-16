package com.sun.hotelproject.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.sun.hotelproject.moudle.LayoutHouseActivity;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.Tip;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 自定义的日历控件
 * Created by xiaozhu on 2016/8/1.
 */
public class CalendarView extends View {
    private int selectYear,selectMonth,selectDay;
    /**
     * 点击的时间
     */
    public  String time="";

    private Context context;
    /**
     * 画笔
     */
    private Paint paint;
    /***
     * 当前的时间
     */
    private Calendar calendar;
    private OnSelectChangeListener listener;
    private OnItemClickListener clickListener;

    /**
     * 改变日期，并更改当前状态，由于绘图是在calendar基础上进行绘制的，所以改变calendar就可以改变图片
     *
     * @param calendar
     */
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        if ((calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.YEAR)).equals(DayManager.getCurrentTime())) {
            DayManager.setCurrent(DayManager.getTempcurrent());
        } else {
            DayManager.setCurrent(-1);
        }
        invalidate();
    }

    public CalendarView(Context context) {
        super(context);
        this.context = context;
        //初始化控件
        initView();
    }


    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //初始化控件
        initView();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //初始化控件
        initView();
    }

    public String getTime(){
        return time;
    }
    /***
     * 初始化控件
     */
    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        calendar = Calendar.getInstance();
        DayManager.setCurrent(calendar.get((Calendar.DAY_OF_MONTH)));
        DayManager.setTempcurrent(calendar.get(Calendar.DAY_OF_MONTH
        ));
        DayManager.setCurrentTime(calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.YEAR));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取day集合并绘制
        List<Day> days = DayManager.createDayByCalendar(calendar, getMeasuredWidth(), getMeasuredHeight(),context);

       // Log.e("TAG", "onDraw:  getMeasuredWidth()---> "+getMeasuredWidth()+"getMeasuredHeight()---->"+getMeasuredHeight() );


        for (Day day : days) {
            day.drawDays(canvas, context, paint);
        }

    }

    @Override
    public boolean callOnClick() {
        return super.callOnClick();
    }


    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }
        @SuppressLint("ClickableViewAccessibility")
        @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            //判断点击的是哪个日期
            float x = event.getX();
            float y = event.getY();
            //计算点击的是哪个日期
            int locationX = (int) (x * 7 / getMeasuredWidth());
            int locationY = (int) ((calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + 1) * y / getMeasuredHeight());
            if (locationY == 0) {
                return super.onTouchEvent(event);
            } else if (locationY == 1) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                System.out.println("xiaozhu" + calendar.get(Calendar.DAY_OF_WEEK) + ":" + locationX);
                if (locationX < calendar.get(Calendar.DAY_OF_WEEK) - 1) {
                    return super.onTouchEvent(event);
                }
            } else if (locationY == calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                if (locationX > calendar.get(Calendar.DAY_OF_WEEK) + 1) {
                    return super.onTouchEvent(event);
                }
            }
            calendar.set(Calendar.WEEK_OF_MONTH, locationY);
            calendar.set(Calendar.DAY_OF_WEEK, locationX + 1);
            DayManager.setSelect(calendar.get(Calendar.DAY_OF_MONTH));
            DayManager.setSelectMonth((calendar.get(Calendar.MONTH)+1));
            DayManager.setSelectYear(calendar.get(Calendar.YEAR));

            time=calendar.get(Calendar.YEAR)+"年"+
                    calendar.get(Calendar.MONTH)+"月"+
                    calendar.get(Calendar.DAY_OF_MONTH)+"日";

            if (listener!=null){
                listener.selectChange(this,calendar.getTime());
            }
            invalidate();
        }else if (event.getAction() ==MotionEvent.ACTION_UP) {
            if (clickListener !=null){
                clickListener.selectClick(this);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 日期选择监听的接口
     */
    public interface OnItemClickListener {
        void selectClick(View view);
    }

    /**
     * 设置日期选择改变监听
     * @param listener
     */
    public void setOnItemClickListener (OnItemClickListener listener){

        this.clickListener = listener;
    }


    /**
     * 设置日期选择改变监听
     * @param listener
     */
    public void setOnSelectChangeListener (OnSelectChangeListener listener){

        this.listener = listener;
    }

    /**
     * 日期选择改变监听的接口
     */
    public interface OnSelectChangeListener {
        void selectChange(CalendarView calendarView, Date date);
    }


}
