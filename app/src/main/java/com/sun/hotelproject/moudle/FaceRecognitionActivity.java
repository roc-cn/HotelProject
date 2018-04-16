package com.sun.hotelproject.moudle;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.Affirmstay;
import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.entity.QueryCheckin;
import com.sun.hotelproject.moudle.camera.CameraFragment;
import com.sun.hotelproject.utils.ActivityManager;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sun
 * 时间：2017年12月19
 * TODO:人脸识别页面
 */
@Route(path = "/hotel/facerecognition")
public class FaceRecognitionActivity extends BaseActivity {

    @BindView(R.id.sp_tv5)TextView sp_tv5;
    @BindView(R.id.sp2_tv5)TextView sp2_tv5;
    @BindView(R.id.linear_sp2)LinearLayout linear_sp2;
    @BindView(R.id.linear_sp1)LinearLayout linear_sp1;
    @BindView(R.id.sp_img5)ImageView sp_img5;
    @BindView(R.id.sp2_img5)ImageView sp2_img5;
    CameraFragment fragment;
    private String name;
    private String id_CardNo;
    private String birth;
    private GuestRoom.Bean gBean;
    private LayoutHouse house;
    private String locksign;
    private QueryCheckin.Bean b;
    private String k;
    private static final String TAG = "FaceRecognitionActivity";
    private String querytype;
    private Affirmstay.Bean ab;
    @Override
    protected int layoutID() {
        Window window;
        //得到窗口
        window=getWindow();
        //设置全屏
        //   window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //保持屏幕 常亮
       window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return R.layout.activity_face_recognition;
    }

    @Override
    protected void initData() {
        ActivityManager.getInstance().addActivity(this);
        Bundle bundle=new Bundle();
        k=getIntent().getStringExtra("k");
        if (k.equals("1")){
            linear_sp2.setVisibility(View.GONE);
            sp_img5.setVisibility(View.VISIBLE);
            sp_tv5.setBackgroundResource(R.drawable.oval_shape);
            sp_tv5.setTextColor(getResources().getColor(R.color.Swrite));
            name=getIntent().getStringExtra("name");
            gBean= (GuestRoom.Bean) getIntent().getSerializableExtra("bean");
            id_CardNo=getIntent().getStringExtra("id_CardNo");
            locksign=getIntent().getStringExtra("locksign");
            birth = getIntent().getStringExtra("birth");
            bundle.putString("name",name);
            bundle.putString("k",k);
            bundle.putString("id_CardNo",id_CardNo);
            bundle.putSerializable("bean",gBean);
            bundle.putString("locksign",locksign);
        }else if (k.equals("2")){
            sp2_img5.setVisibility(View.VISIBLE);
            linear_sp1.setVisibility(View.GONE);
            sp2_tv5.setBackgroundResource(R.drawable.oval_shape);
            sp2_tv5.setTextColor(getResources().getColor(R.color.Swrite));
            querytype =getIntent().getStringExtra("querytype");
            b= (QueryCheckin.Bean) getIntent().getSerializableExtra("bean");
            ab = (Affirmstay.Bean) getIntent().getSerializableExtra("bean2");
            bundle.putSerializable("bean",b);
            bundle.putString("k",k);
            bundle.putSerializable("bean2",ab);
        }else {
            bundle.putString("k",k);
        }
        fragment=new CameraFragment();
        fragment.setArguments(bundle);
        ChangeFragment(fragment);
        setScreenBrightness(200);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 切换
     *
     * @param fragment 需要切换到的fragment
     */
    private void ChangeFragment(Fragment fragment) {
        fragment.setArguments(getIntent().getExtras());
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }

    private void setScreenBrightness(int i) {
        Window localWindow=getWindow();
        WindowManager.LayoutParams params=localWindow.getAttributes();
        float f=i/255.0F;
        params.screenBrightness=f;
        localWindow.setAttributes(params);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
