package com.sun.hotelproject.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun.hotelproject.R;


/**
 * @author  sun
 * TODO 自定义弹出框进度条
 * Created by win7 on 2017/9/25.
 */

public class CustomProgressDialog {
    public static Dialog createLoadingDialog(Context c, String msg){
        LayoutInflater inflater= LayoutInflater.from(c);
        View v=inflater.inflate(R.layout.loadingdialog,null);
        LinearLayout layout= (LinearLayout) v.findViewById(R.id.loading_ll);
        ImageView imageView= (ImageView) v.findViewById(R.id.loadingImg);
        TextView textView= (TextView) v.findViewById(R.id.loadingTv);
        //加载动画
        Animation animation= AnimationUtils.loadAnimation(c,R.anim.load_animation);
        //使用imageview显示动画
        imageView.startAnimation(animation);
        textView.setText(msg);
        //创建自定义dialog样式
        Dialog dialog=new Dialog(c,R.style.loading_dialog);
        dialog.setCancelable(true);//可以返回键取消dialog
        dialog.setContentView(layout,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));//设置布局
        return dialog;

    }


}
