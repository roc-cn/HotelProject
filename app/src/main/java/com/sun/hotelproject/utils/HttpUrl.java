package com.sun.hotelproject.utils;

/**
 * @author sun
 * 时间：2017/11/22
 * TODO 网络地址
 */
public class HttpUrl {
    //private static final String URL = "http://139.199.158.253";
    private static final String URL = "http://blackskin.imwork.net:35540";//"http://139.199.158.253";//////"http://blackskin.imwork.net:35540";////"http://blackskin.imwork.net:35540";//"http://118.89.152.33:8080";
//    public static final String SEQNO = "/bipwlt/facerec/seqNo";
//    public static final String FACERECOQNITION = "/bipwlt/facerec/faceRecognition";
    public static final String SEQNO = URL + "/biphotel/facerec/seqNo";
    public static final String FACERECOQNITION = URL + "/biphotel/facerec/faceRecognition";
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
    public static final String QUERYBOOKORDER = URL + "/biphotel/booking/querybookorder";
    public static final String INROOMNOPAY = URL +"/biphotel/checkin/inroomnopay";
}
