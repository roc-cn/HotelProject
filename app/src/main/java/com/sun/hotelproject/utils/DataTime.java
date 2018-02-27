package com.sun.hotelproject.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author sun
 * Created by win7 on 2017/11/22.
 * TODO：获取时间
 */

public class DataTime {
    /**
     * 生成订单号
     * @return 生成订单号
     */
    public static  String orderId(){
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        java.util.Date curDate = new java.util.Date(System.currentTimeMillis());
        String strRand="" ;
        for(int i=0;i<4;i++){
            strRand += String.valueOf((int)(Math.random() * 10)) ;
        }
        return "bip"+formatter.format(curDate)+strRand;
    }

    /**
     *
     * @return 当前时间
     */
    public static String currentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy/MM/dd/HH:mm:ss");
        java.util.Date curDate = new java.util.Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 获取当前日期
     * @return 当前日期
     */
    public static String curenData() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date curDate=new Date(System.currentTimeMillis());
        return format.format(curDate);
    }

    /**
     *
     * @return 明天的时间
     */
    public static String   Tomorrow(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date curDate=new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        return format.format(calendar.getTime());
    }

    /**
     * 判断 当前日期是周几
     *
     * @param time 要判断的时间
     * @return
     * @Exception  发生异常
     */

    public static String dayForWeek(String time){
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
       Calendar c=Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayforWeek=0;
       if (c.get(Calendar.DAY_OF_WEEK)==1){
           dayforWeek=0;
       }else {
           dayforWeek=c.get(Calendar.DAY_OF_WEEK)-1;
       }
        return  weekDays[dayforWeek];
    }


    /**
     * 获取当前时间
     * @return 当前 时分
     */
    public static String curenTime(){
        SimpleDateFormat format=new SimpleDateFormat("HH:mm");
        Date curDate=new Date(System.currentTimeMillis());
        return format.format(curDate);
    }
    /**
     * 获取当前日期是星期几<br>
     *
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date dt=new Date();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

   public  static  int phase(String startDate,String endDate){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        int a; long date = 0;
       try {
           date=format.parse(endDate).getTime()-format.parse(startDate).getTime();
       } catch (ParseException e) {
           e.printStackTrace();
       }
       a= Integer.parseInt(String.valueOf(date/1000/60/60/24));
       return a;
   }


    /**
     * @param time
     * 时间戳转换成字符窜
     */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

}
