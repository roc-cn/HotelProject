package com.sun.hotelproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * @author sun
 * 时间：2017/11/22
 * TODO:基类Activity
 */
public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    protected T mPresenter;

    protected T getChildPresenter() {
        return null;
    }

    protected Map<String, Object> getRequestParams() {
        return null;
    }

    protected Map<String, Object> getLoopRequestParams() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (null != getChildPresenter()) {
            mPresenter =  getChildPresenter();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSuccess(String str) {

    }

    @Override
    public void onPaySuccess() {

    }

    @Override
    public void onFail(String str) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter = null;
        }
    }
}
