package com.sun.hotelproject.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.sun.hotelproject.R;
import com.sun.hotelproject.utils.Tip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 日期的管理类
 * Created by xiaozhu on 2016/8/7.
 */
public class DayManager {
    /**
     * 记录当前的时间
     */
    private static String currentTime;

    /**
     * 当前的日期
     */
    private static int current = -1;
    /**
     * 储存当前的日期
     */
    private static int tempcurrent=-1;
    /**
     *
     */
    private static String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
    private static String[] dayArray = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    /**
     * 设置当前的时间
     * @param currentTime
     */
    static void setCurrentTime(String currentTime) {
        DayManager.currentTime = currentTime;
    }

    /**
     * 获取当前的时间
     * @return
     */
    static String getCurrentTime() {
        return currentTime;
    }


    /**
     * 储存正常天数
     */
    private static Set<Integer> normalDays = new HashSet<>();

    /**
     * 添加正常天数
     *
     * @param i
     */
    public static void addNomalDays(int i) {
        normalDays.add(i);
    }

    /**
     * 移除正常的天数
     *
     * @param i
     */
    public static void removeNomalDays(int i) {
        if (normalDays.contains(i)) {
            normalDays.remove(i);
        }
    }

    /**
     * 储存异常天数---------->两个日期之间的天数
     */
    private static Set<Integer> abnormalDays = new HashSet<>();

    /**
     * 添加异常天数
     *
     * @param i
     */
    public static void addAbnormalDays(int i) {
        abnormalDays.add(i);
    }

    /**
     * 移除异常的天数
     *
     * @param i
     */
    public static void removeAbnormalDays(int i) {
        if (abnormalDays.contains(i)) {
            abnormalDays.remove(i);
        }
    }

    /**
     * 储存外出天数------------>选择之后保留的天数
     */
    private static Set<Integer> outDays = new HashSet<>();

    /**
     * 添加外出天数天数
     *
     * @param i
     */
    public static void addOutDays(int i) {
        outDays.add(i);
    }

    /**
     * 移除外出天数的天数
     *
     * @param i
     */
    public static void removeOutDays(int i) {
        if (outDays.contains(i)) {
            outDays.remove(i);
        }
    }

    /**
     * 储存休息天数
     */
    private static Set<Integer> restDays = new HashSet<>();


    static void setTempcurrent(int tempcurrent) {
        DayManager.tempcurrent = tempcurrent;
    }

    static int getTempcurrent() {
        return tempcurrent;
    }


    static void setCurrent(int current) {
        DayManager.current = current;
    }


    private static int select = -1;

    static void setSelect(int select) {
        DayManager.select = select;
    }

    private static int selectYear = -1;

    static void setSelectYear(int selectYear) {
        DayManager.selectYear = selectYear;
    }

    public static int getSelect() {
        return select;
    }

    private static int selectMonth = -1;

    public static int getSelectYear() {
        return selectYear;
    }

    static void setSelectMonth(int selectMonth) {
        DayManager.selectMonth = selectMonth;
    }

    public static int getSelectMonth() {
        return selectMonth;
    }

    /**
     * 根据日历对象创建日期集合
     *
     * @param calendar 日历
     * @param width 控件的宽度
     * @param heigh 控件的高度
     * @return 返回的天数的集合
     */
    static List<Day> createDayByCalendar(Calendar calendar, int width, int heigh, Context context) {
        //初始化休息的天数
        //initRestDays(calendar);
        //模拟数据
        imitateData(context);

        List<Day> days = new ArrayList<>();


        Day day;


        int dayWidth = width / 7;
        int dayHeight = heigh / (calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)+1);
       // Log.e("TAG", "createDayByCalendar: Width---->"+width+"\n Height---->"+heigh );

       // Log.e("TAG", "createDayByCalendar: dayWidth---->"+dayWidth+"\n dayHeight---->"+dayHeight );

        //添加星期标识，
        for (int i = 0; i < 7; i++) {
            day = new Day(dayWidth, dayHeight);
            //为星期设置位置，为第0行，
            day.location_x = i;
            day.location_y = 0;
            day.text = weeks[i];
            //设置日期颜色
            day.textClor = 0xFF699CF0;
            days.add(day);

        }
        int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstWeekCount = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        //生成每一天的对象，其中第i次创建的是第i+1天
        for (int i = 0; i < count; i++) {
            day = new Day(dayWidth, dayHeight);
            day.text = dayArray[i];

            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            //设置每个天数的位置
            day.location_y = calendar.get(Calendar.WEEK_OF_MONTH);
            day.location_x = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            //设置日期选择状态
            if (i == current - 1) {
                day.backgroundStyle = 3;
                day.textClor = R.color.colorAccent;   //当时日期文字 颜色

            } else if (i == select - 1) {
                day.backgroundStyle = 2;   //所选日期文字颜色
                day.textClor = Color.WHITE;

            } else {
                day.backgroundStyle = 1; //其它情况文字颜色
                day.textClor = R.color.colorGray;
            }
            //设置工作状态
            if (restDays.contains(1 + i)) {
                day.workState = 0;
            } else if (abnormalDays.contains(i + 1)) {

                day.workState = 2;
            } else if (outDays.contains(i + 1)) {
                day.workState = 3;
            } else {
                day.workState = 1;
            }
            days.add(day);
        }

        return days;
    }

    /**
     * 模拟数据
     */
    private static void imitateData(Context context) {
        abnormalDays.clear();
        outDays.clear();
        if (select-tempcurrent <= 0){
            return;
        }
        for (int i = tempcurrent+1; i <select ; i++) {
            abnormalDays.add(i);
        }
        //Log.e("TAG", "imitateData: ----------->abnormalDays\n" +abnormalDays );

        //    outDays.add(select+2);
        //Log.e("TAG", "imitateData: ----------->outDays\n" +outDays );

//        abnormalDays.add(2);
//        abnormalDays.add(11);
//        abnormalDays.add(16);
//        abnormalDays.add(17);
//        abnormalDays.add(23);
//
//        outDays.add(8);
//        outDays.add(9);
//        outDays.add(18);
//        outDays.add(22);

    }

    /**
     * 初始化休息的天数  计算休息的天数
     *
     * @param calendar
     */
    private static void initRestDays(Calendar calendar) {
        int total = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < total; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
                restDays.add(i + 1);
            }
        }
    }

}
