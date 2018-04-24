package com.sun.hotelproject.moudle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;

import com.sun.hotelproject.utils.Utils;

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
    private String k;
    private String querytype;
    private  int state=1;
    @Override
    protected int layoutID() {
        return R.layout.activity_choice_phone;
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
                if (Utils.isPhone(phone_tv.getText().toString())){
                    Intent intent = new Intent(ChoicePhoneActivity.this,OrderDetailsActivity.class);
                        intent.putExtra("phone_No",phone_tv.getText().toString().trim());
                        intent.putExtra("k",k);
                        intent.putExtra("querytype",querytype);
                        startActivity(intent);
                        finish();
                 //   Tip.show(getApplicationContext(),"是手机号",true);
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
                    } //    Tip.show(getApplicationContext(),"不是手机号",false);
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
                    String phoneNumber = phone_tv.getText().toString().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
                   // String phoneNumber = phone_tv.getText().toString().substring(0, 3) + "****" + phone_tv.getText().toString().substring(7, phone_tv.getText().length());
                    code_content.setText("已将验证码发送到"+phoneNumber);
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

}
