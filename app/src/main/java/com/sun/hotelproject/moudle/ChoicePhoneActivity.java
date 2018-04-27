package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;

import com.sun.hotelproject.entity.Draw;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;
import com.sun.hotelproject.view.ExplosionView;

import butterknife.BindView;
import butterknife.OnClick;

public class ChoicePhoneActivity extends BaseActivity {
    @BindView(R.id.num_0)Button num_0;
    @BindView(R.id.num_1)Button num_1;
    @BindView(R.id.num_2)Button num_2;
    @BindView(R.id.num_3)Button num_3;
    @BindView(R.id.num_4)Button num_4;
    @BindView(R.id.num_5)Button num_5;
    @BindView(R.id.num_6)Button num_6;
    @BindView(R.id.num_7)Button num_7;
    @BindView(R.id.num_8)Button num_8;
    @BindView(R.id.num_9)Button num_9;
    @BindView(R.id.num_clean)Button num_clean;
    @BindView(R.id.num_confirm)Button num_cofirm;
    @BindView(R.id.phone_tv)TextView phone_tv;
    @BindView(R.id.sp4_img3)ImageView sp4_img3;
    @BindView(R.id.sp4_tv3)TextView sp4_tv3;
    @BindView(R.id.sp4_content3)TextView sp4_content3;
    @BindView(R.id.code_tv)TextView code_tv;
    @BindView(R.id.code_content)TextView code_content;
    @BindView(R.id.code_linear)LinearLayout code_linear;
    @BindView(R.id.line)View line;
    @BindView(R.id.title2)TextView title2;
    @BindView(R.id.code_bt)Button code_bt;
    @BindView(R.id.img) ExplosionView img;
    @BindView(R.id.fragme)FrameLayout fragme;
    private AnimationDrawable animationDrawable;
    private String k;
    private String querytype;
    private  int state = 1;
    private  int inTime = 60;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){};
    @Override
    protected int layoutID() {
        return R.layout.activity_choice_phone;
    }

    @Override
    protected void initView() {
        super.initView();
        img.setVisibility(View.INVISIBLE);
        img.setBackgroundResource(R.drawable.explosion);
        animationDrawable = (AnimationDrawable) img.getBackground();
        fragme.setOnTouchListener(new LayoutListener());
    }

    @Override
    protected void initData() {
        k =getIntent().getStringExtra("k");
        querytype =getIntent().getStringExtra("querytype");
        sp4_img3.setVisibility(View.VISIBLE);
        sp4_tv3.setTextColor(getResources().getColor(R.color.Swrite));
        sp4_tv3.setBackgroundResource(R.drawable.oval_shape);
        if (querytype.equals("1")) {
            code_content.setText("");
            line.setVisibility(View.VISIBLE);
            code_linear.setVisibility(View.GONE);
            sp4_content3.setText("订单号");
            title2.setText("请输入订单号");
        }else if (querytype.equals("3")){
            sp4_content3.setText("手机号");
        }
    }

    @OnClick({R.id.num_clean,R.id.num_confirm,R.id.num_0,R.id.num_1,R.id.num_2,R.id.num_3, R.id.num_4,
            R.id.num_5,R.id.num_6,R.id.num_7,R.id.num_8,R.id.num_9,R.id.code_tv,R.id.phone_tv,R.id.code_bt})
    void OnClick(View v){
        switch (v.getId()){
            case R.id.num_0:
                input("0");
                break;
            case R.id.num_1:
                input("1");
                break;
            case R.id.num_2:
                input("2");
                break;
            case R.id.num_3:
                input("3");
                break;
            case R.id.num_4:
                input("4");
                break;
            case R.id.num_5:
                input("5");
                break;
            case R.id.num_6:
                input("6");
                break;
            case R.id.num_7:
                input("7");
                break;
            case R.id.num_8:
                input("8");
                break;
            case R.id.num_9:
                input("9");
                break;
            case R.id.num_clean:
                if (state ==1) {
                    phone_tv.setText("");
                }else {
                    code_tv.setText("");
                }
                break;
            case R.id.num_confirm:
//                if (phone_tv.getText().toString().equals("")){
//                    Tip.show(getApplicationContext(),"手机号不能为空",false);
//                    return;
//                }
//                if (code_tv.getText().toString().equals("")){
//                    Tip.show(getApplicationContext(),"验证码不能为空",false);
//                    return;
//                }

                if (Utils.isPhone(phone_tv.getText().toString())){
                    Intent intent = new Intent(ChoicePhoneActivity.this,OrderDetailsActivity.class);
                    intent.putExtra("phone_No",phone_tv.getText().toString());
                    intent.putExtra("k",k);
                    intent.putExtra("querytype",querytype);
                    startActivity(intent);
                    finish();
                  // smsCheck(phone_tv.getText().toString(),code_tv.getText().toString());
                }else {
                    if (phone_tv.getText().equals("")){
                        return;
                    }
                    if (phone_tv.getText().length()==10){
                        Intent intent = new Intent(ChoicePhoneActivity.this,OrderDetailsActivity.class);
                        intent.putExtra("phone_No",phone_tv.getText().toString().trim());
                        intent.putExtra("k",k);
                        intent.putExtra("querytype",querytype);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(this, "输入信息有误", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.code_tv:
                state = 2;
                code_tv.setBackgroundResource(R.drawable.backsharp2);
                phone_tv.setBackgroundResource(R.drawable.backsharp);
                break;
            case R.id.phone_tv:
                state = 1;
                code_tv.setBackgroundResource(R.drawable.backsharp);
                phone_tv.setBackgroundResource(R.drawable.backsharp2);
                break;
            case R.id.code_bt:
                if (phone_tv.getText().equals("")){
                    return;
                }
                if (Utils.isPhone(phone_tv.getText().toString())){
                    //handler.post(task);
                    Tip.show(getApplicationContext(),"短信验证功能已关闭",false);
                    // String phoneNumber = phone_tv.getText().toString().substring(0, 3) + "****" + phone_tv.getText().toString().substring(7, phone_tv.getText().length());
                  //  smsSend(phone_tv.getText().toString());
                }
                break;
                default:
                    break;
        }
    }

    private void input(String s){
        if (state == 1) {
            String result = phone_tv.getText().toString().trim() + s;
            if (result.length() > 11) {
                return;
            }
            phone_tv.setText(result);
        }else {
            String result =code_tv.getText().toString().trim()+s;
            if (result.length()>6){
                return;
            }
            code_tv.setText(result);
        }
    }
    public Runnable task =new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            if (inTime>0 ){
                inTime--;
                code_bt.setEnabled(false);
                code_bt.setBackgroundResource(R.drawable.btn_bg4);
                handler.postDelayed(this,1000);
                code_bt.setText(inTime+"s后重发");

            }else {
                inTime = 60;
                code_bt.setText("获取验证码");
               code_bt.setEnabled(true);
               code_bt.setBackgroundResource(R.drawable.btn_bg3);
            }
        }
    };
    /**
     * 获取验证码
     */
    void smsSend(final String phone){
        OkGo.<Draw>post(HttpUrl.SMSSEND)
                .tag(this)
                .params("phone",phone)
                .params("appcode","")
                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onError(Response<Draw> response) {
                        super.onError(response);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                    }
                    @Override
                    public void onSuccess(Response<Draw> response) {
                        super.onSuccess(response);
                        if (mResponse.getRescode().equals("0000")){
                            String phoneNumber = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
                            code_content.setText("已将验证码发送到"+phoneNumber);
                            code_bt.setEnabled(false);
                            code_bt.setBackgroundResource(R.drawable.btn_bg4);
                            handler.post(task);
                        }
                    }
                });
    }
    /**
     * 验证短信码
     */
    void smsCheck(final String phone,String dxcode){
        OkGo.<Draw>post(HttpUrl.SMSCHECK)
                .tag(this)
                .params("phone",phone)
                .params("dxcode",dxcode)
                .execute(new JsonCallBack<Draw>(Draw.class) {
                    @Override
                    public void onError(Response<Draw> response) {
                        super.onError(response);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                    }
                    @Override
                    public void onSuccess(Response<Draw> response) {
                        super.onSuccess(response);
                        if (mResponse.getRescode().equals("0000")){
                            Intent intent = new Intent(ChoicePhoneActivity.this,OrderDetailsActivity.class);
                            intent.putExtra("phone_No",phone);
                            intent.putExtra("k",k);
                            intent.putExtra("querytype",querytype);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
    @Override
    protected void onDestroy() {
        handler.removeCallbacks(task);
        super.onDestroy();
    }
    class LayoutListener implements View.OnTouchListener {

        public boolean onTouch(View v, MotionEvent event) {
            //first u have to stop the animation,or if the animation
            //is starting ,u can start it again!
            img.setVisibility(View.INVISIBLE);
            animationDrawable.stop();
            float x = event.getX();
            float y = event.getY();
            img.setLocation((int)y-20, (int)x-20);
            img.setVisibility(View.VISIBLE);
            animationDrawable.start();
            return false;
        }

    }
}
