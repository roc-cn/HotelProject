package com.sun.hotelproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by a'su's on 2018/4/25.
 */

@SuppressLint("AppCompatCustomView")
public class ExplosionView extends ImageView {
    public ExplosionView(Context context) {
        super(context);
    }
    public ExplosionView(Context context , AttributeSet attributes){
        super(context,attributes);

    }
    //handle the location of the explosion
    public void setLocation(int top,int left){
        this.setFrame(left, top, left+40, top+40);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
