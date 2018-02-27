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

import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.moudle.camera.CameraFragment;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sun
 * 时间：2017年12月19
 * TODO:人脸识别页面
 */
public class FaceRecognitionActivity extends BaseActivity {
    @BindView(R.id.toolBarBack)
    ImageView toolBarBack;
    CameraFragment fragment;
    private String name;
    private String id_CardNo;
    private LayoutHouse house;
    private static final String TAG = "FaceRecognitionActivity";
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
        name=getIntent().getStringExtra("name");
        house= (LayoutHouse) getIntent().getSerializableExtra("house");
        id_CardNo=getIntent().getStringExtra("id_CardNo");
        fragment=new CameraFragment();
        Bundle bundle=new Bundle();
        bundle.putString("name",name);
        bundle.putSerializable("house",house);
        bundle.putString("id_CardNo",id_CardNo);
        fragment.setArguments(bundle);
        ChangeFragment(fragment);
        setScreenBrightness(200);
        Log.e(TAG, "initData: "+name+"\n"+id_CardNo );
        Log.e(TAG, "initData: house"+house );

    }
    @OnClick(R.id.toolBarBack)
    void Onclick(View view){
            finish();
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


}
