package com.sun.hotelproject.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by win7 on 2017/12/5.
 */

public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout v= (RelativeLayout) inflater.inflate(layoutID(),null);
        unbinder= ButterKnife.bind(this,v);
        initView();
        initData();
        return v;
    }
    protected abstract int layoutID();


    protected void initView() {
    }


    protected abstract void initData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
