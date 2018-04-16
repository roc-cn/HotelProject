package com.sun.hotelproject.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by a'su's on 2018/4/12.
 */

public class Animutils {
    public static void alphaAnimation(View v) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(3000);
        //实践证明：重复3次的意思是：这个动画首次出现完全后，再重复3次，所以我们会看到有4次
        alphaAnimation.setRepeatCount(0);
        alphaAnimation.setRepeatMode(0);
        alphaAnimation.setFillAfter(true);
        //设置速率
//        alphaAnimation.setInterpolator();
        //设置开始时时间的偏移量，用在动画集合上面比较多
//        alphaAnimation.setStartOffset();
        v.setAnimation(alphaAnimation);
        v.startAnimation(alphaAnimation);
      //  alphaAnimation.start();
    }
    public static void translateAnimation(final View v,float fromX,float toX,float fromY,float toY) {
        /**
         * 此方法默认的都是使用绝对布局Animation.ABSOLUTE的
         */
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);

        /**
         * 下面构造方法是可以指定开始位置和结束位置依赖于哪一个布局参数来测量移动的
         * 一下是各个参数的解释：
         *Animation.ABSOLUTE：绝对位置，即距离屏幕远点（左上角）的位置
         *Animation.RELATIVE_TO_SELF：相对于自身的距离
         *Animation.RELATIVE_TO_PARENT：现对于控件父布局的距离
         */
        TranslateAnimation translateAnimation1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_PARENT, 300,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_PARENT, 300);

        translateAnimation.setDuration(500); //动画持续时间
        translateAnimation.setFillAfter(false); //动画之后，停留在最后面
        translateAnimation.setRepeatMode(0);
        v.setAnimation(translateAnimation);
        //设置动画的监听
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                alphaAnimation(v);//动画完成之后，再调用另外一个方法，继续透明度的动画
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(translateAnimation);
       //translateAnimation.start();

    }
}
