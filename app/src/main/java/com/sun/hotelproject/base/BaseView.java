package com.sun.hotelproject.base;

/**
 * @author sun
 * 时间：2017/11/22
 * TODO:基类接口
 */
public interface BaseView {

    void onSuccess(String str);

    void onFail(String str);

    void onPaySuccess();
}
