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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.squareup.picasso.Picasso;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.Draw;
import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.LockRoom;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;

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
    @BindView(R.id.speed_of_progress)
    ImageView speed_of_progress;

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
    private List<LockRoom.Bean> lockRooms;
    @Override
    protected int layoutID() {
        return R.layout.activity_order_details;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        isRuning = true;
        speed_of_progress.setImageResource(R.drawable.home_three);
        beginTime = (String) CommonSharedPreferences.get("beginTime1","");
        endTime = (String) CommonSharedPreferences.get("endTime1","");
        content = (String) CommonSharedPreferences.get("content","");
        check_inTime .setText( beginTime +"——"+ endTime+"   "+ content);
        locksign= getIntent().getStringExtra("locksign");
//        sum=getIntent().getStringExtra("sum");
//        select_house=getIntent().getStringExtra("selecthouse");
        gBean= (GuestRoom.Bean) getIntent().getSerializableExtra("bean");
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
                Intent intent =new Intent(OrderDetailsActivity.this,IdentificationActivity.class);
                        intent.putExtra("bean",gBean);
                        intent.putExtra("locksign",locksign);
                        intent.putExtra("k","1");
                        startActivity(intent);
                        finish();

                break;
        }
    }


}
