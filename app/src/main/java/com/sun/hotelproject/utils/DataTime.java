package com.sun.hotelproject.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;

import com.sun.hotelproject.R;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author sun
 * Created by win7 on 2017/11/22.
 * TODO：大工具类
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
        return "PMS"+formatter.format(curDate)+strRand;
    }

    /**
     *
     * @return 当前时间
     */
    public static String currentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        java.util.Date curDate = new java.util.Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }
    /**
     *
     * @return 字符串格式化
     */
    @SuppressLint("SimpleDateFormat")
    public static String StringFormatting(String str)  {
        Date date= null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
       // return format.format(formatDate);
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
     * @return 当前 时分秒
     */
    public static String curenTime(){
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
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

    /**
     * byte数组转十六进制字符串
     * @param src 数组
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 十六进制字符串转字符串
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {

        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * 获得机器序列号
     * @return
     */
    public static String getSerialNumber(){

        String serial = null;

        try {

            Class<?> c =Class.forName("android.os.SystemProperties");

            Method get =c.getMethod("get", String.class);

            serial = (String)get.invoke(c, "ro.serialno");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return serial;

    }

    /**
     * 数字转英文
     * @param num
     */
    public static String returnToEnglish(int num){
        String month="";
        switch (num){
            case 1:
                 month="(Jan)";
                break;
            case 2:
                month= "(Feb)";
                break;
            case 3:
                month= "(Mar)";
                break;
            case 4:
                month= "(Apr)";
                break;
            case 5:
                month= "(May)";
                break;
            case 6:
                month= "(Jun)";
                break;
            case 7:
                month= "(Jul)";
                break;
            case 8:
                month= "(Aug)";
                break;
            case 9:
                month= "(Sep)";
                break;
            case 10:
                month= "(Oct)";
                break;
            case 11:
                month= "(Now)";
                break;
            case 12:
                month= "(Dec)";
                break;
                default:
                    month="不存在的月份";
                    break;
        }
        return month;

    }
    //字符串转指定格式时间
    public static String getMyDate2(String str) {
        return StringToDate(str, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
    }

    //字符串转指定格式时间
    public static String getMyDate(String str) {
        return StringToDate(str, "yyyyMMdd", "yyyy-MM-dd");
    }
    @SuppressLint("SimpleDateFormat")
    public static String StringToDate(String dateStr, String dateFormatStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try{
            date = sdf.parse(dateStr);
        } catch (ParseException e){
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }

    /**
     * 得到当月最后一天
     * @param data
     * @return
     */
    public static String getLastOfMonth(String data){
        // 获取当月的天数（需完善）
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 定义当前期间的1号的date对象
        Date date = null;
        try {
            date = dateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,1);//月增加1天
        calendar.add(Calendar.DAY_OF_MONTH,-1);//日期倒数一日,既得到本月最后一天
        Date voucherDate = calendar.getTime();
        return dateFormat.format(voucherDate);
    }


    /**
     *
     * @param c
     * @param str  字符串
     * @param position 下标
     * @return 不同字体 大小的文字
     */
    public static SpannableString updTextSize(Context c,String str, int position){
        SpannableString styledText = new SpannableString(str);
        styledText.setSpan(new TextAppearanceSpan(c, R.style.textstyle0), 0, position, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(c, R.style.textstyle1), position, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return styledText;
    }

    /**
     *
     * @param c
     * @param str  字符串
     * @param position 下标
     * @return 不同字体 大小的文字
     */
    public static SpannableString updTextSize2(Context c,String str, int position){
        SpannableString styledText = new SpannableString(str);
        styledText.setSpan(new TextAppearanceSpan(c, R.style.textstyle4), 0, position, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(c, R.style.textstyle3), position, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return styledText;
    }
}
