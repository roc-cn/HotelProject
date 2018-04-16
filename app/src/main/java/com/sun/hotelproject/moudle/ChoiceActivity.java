package com.sun.hotelproject.moudle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.Utils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择身份证或房卡
 */
@Route(path = "/hotel/choice")
public class ChoiceActivity extends BaseActivity {
    private String k;
    @BindView(R.id.rb_piv)
    RadioButton rb_piv;
    @BindView(R.id.rb_rmCard)
    RadioButton rb_rmCard;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.radiogroup)
    RadioGroup radioGroup;
    @BindView(R.id.sp2_tv2)
    TextView sp2_tv2;
    private String querytype = "2";
    @BindView(R.id.sp2_img2)ImageView sp2_img2;
    @Override
    protected int layoutID() {
        return R.layout.activity_choice;
    }

    @Override
    protected void initData() {
        ActivityManager.getInstance().addActivity(this);
        k=getIntent().getStringExtra("k");
        if (k.equals("2")){
            sp2_img2.setVisibility(View.VISIBLE);
            sp2_tv2.setBackgroundResource(R.drawable.oval_shape);
            sp2_tv2.setTextColor(getResources().getColor(R.color.Swrite));
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_piv:
                        querytype = "2";
                        Log.e("TAG", "onCheckedChanged: 1"+querytype );
                        break;
                    case R.id.rb_rmCard:
                        querytype = "3";
                        Log.e("TAG", "onCheckedChanged:2 "+querytype );
                        break;
                }
            }
        });
        Log.e("TAG", "initData: 0"+querytype );

    }
    @OnClick({R.id.confirm})
    void OnClick(View v){
        Intent intent =new Intent();
        switch (v.getId()){
            case R.id.confirm://房卡
                if (Utils.isFastClick()){
                    switch (querytype){
                        case "2":
                            intent.setClass(ChoiceActivity.this,IdentificationActivity.class);
                            intent.putExtra("querytype",querytype);
                            intent.putExtra("k",k);
                            Log.e("TAG", "onCheckedChanged: 3"+querytype );
                            break;
                        case "3":
                            intent.setClass(ChoiceActivity.this,CheckOutActivity.class);
                            intent.putExtra("querytype",querytype);
                            intent.putExtra("k",k);
                            Log.e("TAG", "onCheckedChanged:4 "+querytype );
                            break;
                }
            }
                break;
        }
        startActivity(intent);
    }
}
