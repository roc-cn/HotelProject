package com.sun.hotelproject.moudle;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.Router;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author  sun
 * 时间：2017/11/24
 * TODO:是否预定
 */
@Route(path = "/hotel/schedule")
public class ScheduleActivity extends BaseActivity {
    @BindView(R.id.Not_scheduled)
    Button Not_scheduled; //未预定
    @BindView(R.id.Already_booked)
    Button Already_booked;//已预定
    LayoutHouse house;

    @Override
    protected int layoutID() {
        return R.layout.activity_schedule;
    }
    @Override
    protected void initData() {
        house=new LayoutHouse("大床房","18-30㎡","大床","无窗","不含早","不可取消","￥150");
    }
    @OnClick({R.id.Not_scheduled,R.id.Already_booked})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.Not_scheduled:
                Router.jumpL("/hotel/layouthouse"); //跳转到未预定界面
                finish();
                break;
            case R.id.Already_booked:
                CommonSharedPreferences.put("beginTime", "2018-01-08");
                CommonSharedPreferences.put("endTime","2018-01-10");
                Intent intent=new Intent(this,IdentificationActivity.class);
                intent.putExtra("house",house);
                startActivity(intent);

               // Router.jumpL("/hotel/identification");//跳转到已预定界面
                finish();
                break;



        }


    }
 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }*/

}
