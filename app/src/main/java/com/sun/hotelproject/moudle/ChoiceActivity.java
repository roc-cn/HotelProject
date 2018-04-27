package com.sun.hotelproject.moudle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择身份证或房卡
 */
@Route(path = "/hotel/choice")
public class ChoiceActivity extends BaseActivity {
//    @BindView(R.id.rb_linerar1)LinearLayout rb_linerar1;
//    @BindView(R.id.rb_linerar2)LinearLayout rb_linerar2;
//    @BindView(R.id.rb_img1)ImageView rb_img1;
//    @BindView(R.id.rb_img2)ImageView rb_img2;
//    @BindView(R.id.rb_tv1)ImageView rb_tv1;
//    @BindView(R.id.rb_tv2)ImageView rb_tv2;
    @BindView(R.id.orderNo)RadioButton orderNo;
    @BindView(R.id.rb_piv) RadioButton rb_piv;
    @BindView(R.id.rb_rmCard) RadioButton rb_rmCard;
    @BindView(R.id.confirm) Button confirm;
    @BindView(R.id.radiogroup) RadioGroup radioGroup;
    @BindView(R.id.sp2_tv2) TextView sp2_tv2;
    @BindView(R.id.sp2_img2)ImageView sp2_img2;
    @BindView(R.id.linear_sp2)LinearLayout linear_sp2;
    @BindView(R.id.linear_sp4)LinearLayout linear_sp4;
    @BindView(R.id.sp4_tv2) TextView sp4_tv2;
    @BindView(R.id.sp4_img2)ImageView sp4_img2;
    @BindView(R.id.title1)TextView title1;
    @BindView(R.id.title2)TextView title2;

    private String querytype = "5";
    private String k;
    private boolean isClicked = true;
    @Override
    protected int layoutID() {
        return R.layout.activity_choice;
    }

    @Override
    protected void initData() {
        ActivityManager.getInstance().addActivity(this);
        k=getIntent().getStringExtra("k");
        if (k.equals("2")){
            linear_sp2.setVisibility(View.VISIBLE);
            sp2_img2.setVisibility(View.VISIBLE);
            sp2_tv2.setBackgroundResource(R.drawable.oval_shape);
            sp2_tv2.setTextColor(getResources().getColor(R.color.Swrite));
        }else if (k.equals("4")){
            title1.setText("正在查询网上预定信息");
            title2.setText("请选择信息识别类型");
            rb_piv.setText("入住人身份证查询");
            orderNo.setText("订单号查询");
            rb_rmCard.setText("手机号查询");
            linear_sp4.setVisibility(View.VISIBLE);
            sp4_img2.setVisibility(View.VISIBLE);
            sp4_tv2.setBackgroundResource(R.drawable.oval_shape);
            sp4_tv2.setTextColor(getResources().getColor(R.color.Swrite));
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_piv:
                        querytype = "5";
                      //  Log.e("TAG", "onCheckedChanged: 1"+querytype );
                        break;
                    case R.id.orderNo:
                        querytype = "1";
                        break;
                    case R.id.rb_rmCard:
                        querytype = "3";
                      //  Log.e("TAG", "onCheckedChanged:2 "+querytype );
                        break;
                }
            }
        });
        Log.e("TAG", "initData: 0"+querytype );

    }
    @OnClick({R.id.confirm})
    void OnClick(View v){
        Intent intent =new Intent();
        switch (v.getId()) {
            case R.id.confirm://房卡
                if (Utils.isFastClick()) {
                    if (k.equals("2")) {
                        switch (querytype) {
                            case "2":
                                intent.setClass(ChoiceActivity.this, IdentificationActivity.class);
                                intent.putExtra("querytype", querytype);
                                intent.putExtra("k", k);
                                startActivity(intent);
                                finish();
                                Log.e("TAG", "onCheckedChanged: 3" + querytype);
                                break;
                            case "3":
                                intent.setClass(ChoiceActivity.this, CheckOutActivity.class);
                                intent.putExtra("querytype", querytype);
                                intent.putExtra("k", k);
                                startActivity(intent);
                                finish();
                                Log.e("TAG", "onCheckedChanged:4 " + querytype);
                                break;
                            default:
                                break;
                        }
                    } else if (k.equals("4")) {
                        switch (querytype) {
                            case "1":
                               // Tip.show(getApplicationContext(),"该功能暂时没有开放！",false);
                                intent.setClass(ChoiceActivity.this, ChoicePhoneActivity.class);
                                intent.putExtra("querytype", querytype);
                                intent.putExtra("k", k);
                                startActivity(intent);
                                finish();
//                                Log.e("TAG", "onCheckedChanged: 3" + querytype);
                                break;
                            case "5":
                                intent.setClass(ChoiceActivity.this, IdentificationActivity.class);
                                intent.putExtra("querytype", querytype);
                                intent.putExtra("k", k);
                                startActivity(intent);
                                finish();
                              //  Tip.show(getApplicationContext(),"该功能暂时没有开放！",false);
                                break;
                            case "3":
                                intent.setClass(ChoiceActivity.this, ChoicePhoneActivity.class);
                                intent.putExtra("querytype", querytype);
                                intent.putExtra("k", k);
                                startActivity(intent);
                                finish();
                                Log.e("TAG", "onCheckedChanged:4 " + querytype);
                                break;
                            default:
                                break;
                        }
                    }
                }
                break;
                default:
                    break;

        }

    }
}
