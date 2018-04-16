package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.Draw;
import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.LockRoom;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sun 2018/3/9
 * 订单详情页
 */
@Route(path = "/hotel/orderdetails")
public class OrderDetailsActivity extends BaseActivity {
    @BindView(R.id.confirm_pay)
    Button confirm_pay;
    @BindView(R.id.check_inTime)
    TextView check_inTime;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.tv20)
    TextView tv20;

    @BindView(R.id.house_name)
    TextView house_name;
    @BindView(R.id.sp_tv3) TextView sp_tv3;
    @BindView(R.id.sp_img3)ImageView sp_img3;

    private List<Map<String,String>> datas;
    //private String sum;
    private static final String TAG = "OrderDetailsActivity";
    private String beginTime = "";
    private String endTime = "";
    private String content = "";
    private Set<String> set;
    private Double b;
    private String sum;
    private String select_house;
    private String locksign;
    private GuestRoom.Bean gBean;
    private String name;
    private List<LockRoom.Bean> lockRooms;
    private String k;
    @Override
    protected int layoutID() {
        return R.layout.activity_order_details;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {

        sp_tv3.setBackgroundResource(R.drawable.oval_shape);
        sp_tv3.setTextColor(getResources().getColor(R.color.Swrite));
        sp_img3.setVisibility(View.VISIBLE);
        ActivityManager.getInstance().addActivity(this);
        k=getIntent().getStringExtra("k");
        isRuning = true;
        name = (String) CommonSharedPreferences.get("house_type","");

        beginTime = (String) CommonSharedPreferences.get("beginTime1","");
        endTime = (String) CommonSharedPreferences.get("endTime1","");
        content = (String) CommonSharedPreferences.get("content","");
        check_inTime .setText( beginTime +"——"+ endTime+"   "+ content);
        locksign= getIntent().getStringExtra("locksign");
        Log.e(TAG, "initData: "+beginTime +" " +endTime );
//        sum=getIntent().getStringExtra("sum");
//        select_house=getIntent().getStringExtra("selecthouse");
        gBean= (GuestRoom.Bean) getIntent().getSerializableExtra("bean");
        house_name.setText(name);
        Log.e(TAG, "initData: "+gBean.toString() );
       // datas = (List<Map<String, String>>) getIntent().getSerializableExtra("datas");
      //  sum=getIntent().getStringExtra("sum");
       // Log.e(TAG, "initData: "+datas.toString()+ "\n"+sum );

//        b=0.00;
//        for (Map<String,String> map: datas) {
//                set=map.keySet();
//                for (String key :map.keySet()){
//                    b+=Double.valueOf(map.get(key));
//                }
//            }
//        Log.e(TAG, "initData: "+set.toString());
        tv20.setText(gBean.getRpmsno());
        price.setText(DataTime.updTextSize2(getApplicationContext(),"￥"+gBean.getDealprice(),1),TextView.BufferType.SPANNABLE);
    }
    @OnClick({R.id.confirm_pay})
    void OnClick(View v){
        switch (v.getId()){
            case R.id.confirm_pay:
                if (Utils.isFastClick()) {
                    Intent intent = new Intent(OrderDetailsActivity.this, IdentificationActivity.class);
                    intent.putExtra("bean", gBean);
                    intent.putExtra("locksign", locksign);
                    intent.putExtra("k", k);
                    startActivity(intent);
                }
                break;
        }
    }


}
