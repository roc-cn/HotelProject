package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.Draw;
import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.LockRoom;
import com.sun.hotelproject.entity.QueryBookOrder;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;

import java.io.Serializable;
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
    @BindView(R.id.toolbarBack) Button toolbarBack;
    @BindView(R.id.house_name)
    TextView house_name;
    @BindView(R.id.sp_tv3) TextView sp_tv3;
    @BindView(R.id.sp_img3)ImageView sp_img3;
    @BindView(R.id.linear_sp1)LinearLayout linear_sp1;
    @BindView(R.id.linear_sp4)LinearLayout linear_sp4;
    @BindView(R.id.sp4_tv4)TextView sp4_tv4;
    @BindView(R.id.sp4_img4)ImageView sp4_img4;
    private List<Map<String,String>> datas;
    @BindView(R.id.relative)RelativeLayout relative;
    @BindView(R.id.linear)LinearLayout linear;
    //private String sum;
    private static final String TAG = "OrderDetailsActivity";
    private String content = "";
    private Set<String> set;
    private Double b;
    private String sum;
    private String select_house;
    private GuestRoom.Bean gBean;
    private String name;
    private List<LockRoom.Bean> lockRooms;
    private String k;
    private String mchid;
    private String querytype;
    private String phone_No;
    private String houseName;
    private String beginTime,endTime,beginTime1,endTime1;
    QueryBookOrder.Bean qBean;
    String inDay;
    String locksign;
    @Override
    protected int layoutID() {
        return R.layout.activity_order_details;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        flag = 0;
        mchid = (String) CommonSharedPreferences.get("mchid","");
        ActivityManager.getInstance().addActivity(this);
        k=getIntent().getStringExtra("k");
        beginTime = (String) CommonSharedPreferences.get("beginTime1", "");
        endTime = (String) CommonSharedPreferences.get("endTime1", "");
        content = (String) CommonSharedPreferences.get("content", "");
        inDay = (String) CommonSharedPreferences.get("inDay","");
        if (k.equals("1")) {
            linear_sp1.setVisibility(View.VISIBLE);
            sp_tv3.setBackgroundResource(R.drawable.oval_shape);
            sp_tv3.setTextColor(getResources().getColor(R.color.Swrite));
            sp_img3.setVisibility(View.VISIBLE);
            name = (String) CommonSharedPreferences.get("house_type", "");

            check_inTime.setText(beginTime + "——" + endTime + "   " + content);
            locksign = getIntent().getStringExtra("locksign");
            gBean = (GuestRoom.Bean) getIntent().getSerializableExtra("bean");
            house_name.setText(name);
            tv20.setText(gBean.getRpmsno());
            price.setText(DataTime.updTextSize2(getApplicationContext(), "￥" + Double.valueOf(gBean.getDealprice())*Integer.parseInt(inDay), 1), TextView.BufferType.SPANNABLE);
        }else if (k.equals("4")){
            linear.setVisibility(View.GONE);
            linear_sp4.setVisibility(View.VISIBLE);
            sp4_tv4.setBackgroundResource(R.drawable.oval_shape);
            sp4_tv4.setTextColor(getResources().getColor(R.color.Swrite));
            sp4_img4.setVisibility(View.VISIBLE);
            querytype =getIntent().getStringExtra("querytype");

            phone_No =getIntent().getStringExtra("phone_No");
            queryBookOrder(querytype,phone_No);
        }
    }
    @OnClick({R.id.confirm_pay})
    void OnClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.confirm_pay:
                if (Utils.isFastClick()) {
                    if (k.equals("1")) {
                        intent.setClass(OrderDetailsActivity.this, IdentificationActivity.class);
                        intent.putExtra("bean", gBean);
                        intent.putExtra("locksign", locksign);
                        intent.putExtra("k", k);
                        startActivity(intent);
                        finish();
                    }else if (k.equals("4")){
                        intent.setClass(OrderDetailsActivity.this, IdentificationActivity.class);
                        intent.putExtra("bean", qBean);
                        intent.putExtra("k", k);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
        }
    }

    /**
     * 查询预定单
     */
    public void queryBookOrder(String querytype,String querydata){
        OkGo.<QueryBookOrder>post(HttpUrl.QUERYBOOKORDER)
                .tag(this)
                .params("mchid",mchid)
                .params("querytype",querytype)
                .params("querydata",querydata)
                .params("checkindate","")
                .execute(new JsonCallBack<QueryBookOrder>(QueryBookOrder.class) {
                    @Override
                    public void onError(Response<QueryBookOrder> response) {
                        super.onError(response);
                        Log.e(TAG, "onError: 服务器连接异常" );
                    }

                    @Override
                    public void onSuccess(Response<QueryBookOrder> response) {
                        super.onSuccess(response);
                        if (mResponse.getRescode().equals("0000")){
                            Intent intent = new Intent(getApplicationContext(),SelectActivity.class);
                                intent.putExtra("list", (Serializable) mResponse.getDatalist());
                                intent.putExtra("k",k);
                                intent.putExtra("mchid",mchid);
                                startActivityForResult(intent,2);
                            Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
                        }else {
                           // Tip.show(getApplicationContext(),mResponse.getResult(),false);
                            relative.setVisibility(View.VISIBLE);
                            linear.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode ==0){
            if (isRuning){
                if (isTime ==1){
                    time = 90;
                }else {
                    time = 30;
                }
                toolbarBack.setText("返回("+time+"s)");
                handler.post(timeRunnable);
            }else {

            }
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK){
            if (isRuning){
                if (isTime ==1){
                    time = 90;
                }else {
                    time = 30;
                }
                toolbarBack.setText("返回("+time+"s)");
                handler.post(timeRunnable);
            }else {

            }
           // locksign =data.getStringExtra("locksign");
            houseName = (String) CommonSharedPreferences.get("house_type", "");
            qBean= (QueryBookOrder.Bean) data.getSerializableExtra("bean");
            String startTime =qBean.getIndate();
            String finshTime = qBean.getOuttime();
            String inTime =startTime.substring(0,10);
            String outTime = finshTime.substring(0,10);
            String [] time1 =inTime.split("-");
            String month1=time1[1];
            String day1 =time1[2];
            String [] time2 =outTime.split("-");
            String month2=time2[1];
            String day2 =time2[2];
            beginTime = month1+"/"+day1;
            endTime =month2+"/"+day2;

            int inDay = DataTime.phase(inTime,outTime);

            CommonSharedPreferences.put("beginTime", inTime);
            CommonSharedPreferences.put("endTime", outTime);
            CommonSharedPreferences.put("beginTime1", beginTime);
            CommonSharedPreferences.put("endTime1", endTime);
            CommonSharedPreferences.put("inDay", inDay + "");
            if (inDay<10) {
                content = "0"+inDay +"/晚(night)";
            }else {
                content = inDay +"/晚(night)";
            }
            CommonSharedPreferences.put("content", content);
            check_inTime.setText(beginTime + "——" + endTime + "   " + content);
            Log.e(TAG, "onActivityResult: "+inTime + outTime );
            linear.setVisibility(View.VISIBLE);
            house_name.setText(houseName);
            tv20.setText(qBean.getRoomno());
            price.setText(DataTime.updTextSize2(getApplicationContext(), "￥" + qBean.getDealprice(), 1), TextView.BufferType.SPANNABLE);


        }
    }
}
