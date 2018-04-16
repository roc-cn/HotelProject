package com.sun.hotelproject.utils;

/**
 * @author sun
 * 时间：2017/11/22
 * TODO 网络地址
 */
public class HttpUrl {
    private static final String URL = "http://112.74.102.125";//"http://118.89.152.33:8080";
    public static final String SEQNO = URL+"/bipwlt/facerec/seqNo";

    public static final String FACERECOQNITION =URL+"/bipwlt/facerec/faceRecognition";

    public static final int LOOP_WHAT = 2;
   // http://112.74.102.125/biphotel/checkout/queryroombill
    //http://112.74.102.125/biphotel/checkout/checkoutroom

    public static final String CHECKOUTROOM = URL + "/biphotel/checkout/checkoutroom";
    public static final String QUERYROOMBILL = URL + "/biphotel/checkout/queryroombill";
    public static final String QUERYBUILDING = URL + "/biphotel/initialization/querybuilding";
    public static final String QUERYFLOOR = URL + "/biphotel/initialization/queryfloor";
    public static final String QUERYROOMTYPE = URL + "/biphotel/initialization/queryroomtype";
    public static final String QUERYROOMINFO = URL + "/biphotel/initialization/queryroominfo";
    public static final String QUERYROOMINFO2 = URL +"/biphotel/checkin/queryroominfo";
    public static final String SCANPAY = URL + "/biphotel/interaction/scanpay";
    public static final String LOCKROOM = URL + "/biphotel/checkin/lockroom";
    public static final String QUERYPAYSTATUS = URL + "/biphotel/checkin/querypaystatus";
    public static final String  QUERYCHECKIN = URL + "/biphotel/stay/querycheckin";
    public static final String AFFIRMSTAY = URL + "/biphotel/stay/affirmstay";
    public static final String LOGIN = URL + "/biphotel/initialization/login";





}
