package com.sun.hotelproject.moudle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.databinding.adapters.TextViewBindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.base.SimpleWinCallback;
import com.sun.hotelproject.dao.DaoSimple;
import com.sun.hotelproject.entity.BuildingTable;
import com.sun.hotelproject.entity.FloorTable;
import com.sun.hotelproject.entity.HouseTable;
import com.sun.hotelproject.entity.Login;
import com.sun.hotelproject.entity.RoomTable;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.Animutils;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;

import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

public class LoginActivity extends BaseActivity  {
    @BindView(R.id.linear) RelativeLayout linear;
    @BindView(R.id.login) Button login;
    @BindView(R.id.mchid) EditText mchid_et;
    @BindView(R.id.user) EditText user_et;
    @BindView(R.id.password) EditText password_et;
    @BindView(R.id.tishi) TextView tishi;
    @BindView(R.id.line) View line;
    @BindView(R.id.exit) Button exit;
    @BindView(R.id.anim_layout) RelativeLayout anim_lauout;
    @BindView(R.id.anim_img)ImageView anim_img;
    @BindView(R.id.anim_tv)TextView anim_tv;
    //@BindView(R.id.anim_iv)ImageView iv;
    @BindView(R.id.activity_login)RelativeLayout activity_login;
    private String mchid;
    private String user;
    private String password;
    Animation animation1, animation2;
    private AnimationDrawable animationDrawable;
    private String data = "";
    private static final String TAG = "LoginActivity";
    private DaoSimple daoSimple;
    TranslateAnimation translateAnimation;
    Animation operatingAnim;
    float ivX,ivY;
    boolean isText ;
//    TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService( Context.TELEPHONY_SERVICE);

    @Override
    protected int layoutID() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initView() {
        super.initView();
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.load_animation);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
//        final float ivX = iv.getX();
//        final float ivY = iv.getY();
//        window.setCallback(new SimpleWinCallback(window.getCallback()){
//            @Override
//            public boolean dispatchTouchEvent(MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        iv.setVisibility(View.VISIBLE);
//                        float lastX = event.getRawX();
//                        float lastY = event.getRawY();
//                        Log.e(TAG, "dispatchTouchEvent: "+lastX +" "+lastY+" "+ivX +" "+ivY );
//                        Animutils.translateAnimation(iv,ivX,lastX,ivY,lastY);
//                        break;
//                }
//                return super.dispatchTouchEvent(event);
//            }
//        });

//        activity_login.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        iv.setVisibility(View.VISIBLE);
//                        float lastX = event.getX();
//                        float lastY = event.getY();
//                        Animutils.translateAnimation(iv,ivX,lastX,ivY,lastY);
//                        return true;
//
//                }
//                return false;
//            }
//        });

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //不自动弹出键盘
        ActivityManager.getInstance().addActivity(this);

//        if (isText){
//            login.setText("登陆");
//        }else {
//            login.setText("管理");
//        }
        data = (String) CommonSharedPreferences.get("data","");
        if (!TextUtils.isEmpty(data)){
            login.setText(data);
        }

        daoSimple =new DaoSimple(this);
        if (TextUtils.isEmpty(mchid_et.getText()) || TextUtils.isEmpty(user_et.getText()) || TextUtils.isEmpty(password_et.getText())){
            login.setEnabled(false);
            login.getBackground().setAlpha(100);
        }else {
            login.setEnabled(true);
            login.getBackground().setAlpha(255);
        }

        isRuning = false;
        animation1 = AnimationUtils.loadAnimation(this, R.anim.in_from_left);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.out_to_left);
        linear.startAnimation(animation1);
        linear.setVisibility(View.VISIBLE);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linear.setX(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linear.setX(-762);
                Router.jumpL("/hotel/main");
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mchid_et.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(mchid_et.getText()) || TextUtils.isEmpty(user_et.getText()) || TextUtils.isEmpty(password_et.getText())){
                    login.setEnabled(false);
                    login.getBackground().setAlpha(100);
                }else {
                    login.setEnabled(true);
                    login.getBackground().setAlpha(255);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,int after) {

            }

            public void afterTextChanged(Editable s) {
//
            }
        });
        user_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(mchid_et.getText())|| TextUtils.isEmpty(user_et.getText()) || TextUtils.isEmpty(password_et.getText())){
                    login.setEnabled(false);
                    login.getBackground().setAlpha(100);
                }else {
                    login.setEnabled(true);
                    login.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("TAG", "beforeTextChanged: "+"123" );
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(mchid_et.getText())|| TextUtils.isEmpty(user_et.getText()) || TextUtils.isEmpty(password_et.getText())){
                    login.setEnabled(false);
                    login.getBackground().setAlpha(100);
                }else {
                    login.setEnabled(true);
                    login.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @OnTouch({R.id.login,R.id.exit})
    boolean onTouch(View v, MotionEvent event){
        switch (v.getId()){
            case R.id.login:
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    login.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    login.getBackground().setAlpha(255);
                    mchid = mchid_et.getText().toString().trim();
                    user = user_et.getText().toString().trim();
                    password = password_et.getText().toString().trim();
                    if (Utils.isFastClick()) {
                        login(mchid, user, password, login.getText().toString().trim());
                    }
                }
                break;
            case R.id.exit:
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    if (Utils.isFastClick()){
                        CommonSharedPreferences.put("data","");
                        login.setText("登陆");
                        ActivityManager.getInstance().exit();
                    }
                }
                break;
        }
        return false;
    }

//    @OnClick({R.id.login,R.id.exit})
//    void OnClick(View v) {
//        switch (v.getId()) {
//            case R.id.login:
//                //Log.e(TAG, "dispatchTouchEvent: "+lastX +" "+lastY+" "+ivX +" "+ivY );
//                mchid = mchid_et.getText().toString().trim();
//                user = user_et.getText().toString().trim();
//                password = password_et.getText().toString().trim();
//                if (Utils.isFastClick()) {
//                    login(mchid, user, password, login.getText().toString().trim());
//                }
//                break;
//            case R.id.exit:
//                if (Utils.isFastClick()){
//                    CommonSharedPreferences.put("data","");
//                    login.setText("登陆");
//                    ActivityManager.getInstance().exit();
//                }
//                break;
//        }
//    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void login(final String mchid, String user, String password, final String s) {

        anim_lauout.setVisibility(View.VISIBLE);

        // animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.load_animation);
        anim_img.setAnimation(operatingAnim);
        anim_img.startAnimation(operatingAnim);
        anim_tv.setText("正在加载中......");
//        if (animationDrawable != null && !animationDrawable.isRunning()){
//            animationDrawable.start();
//        }
        OkGo.<Login>post(HttpUrl.LOGIN)
                .tag(this)
                .params("mchid", mchid)
                .params("username", user)
                .params("password", password)
                .cacheTime(6000)
                .execute(new JsonCallBack<Login>(Login.class) {
                    @Override
                    public void onSuccess(Response<Login> response) {
                        super.onSuccess(response);

                        if (response.body().getRescode().equals("0000")) {
                            CommonSharedPreferences.put("mchid", response.body().getMchid());
                            if (s.equals("登陆")){
//                                anim_tv.setText("登陆成功");
//
//                                anim_img.clearAnimation();
//                                anim_lauout.setVisibility(View.GONE);
//                                linear.startAnimation(animation2);
//                                if (daoSimple.buildSelAll() != null && daoSimple.floorSelAll() != null
//                                        && daoSimple.houseSelAll() != null && daoSimple.roomSelAll() != null){
//                                    daoSimple.buildUpd("1","0");
//                                    daoSimple.floorUpd("1","0");
//                                    daoSimple.houseUpd("1","0");
//                                    daoSimple.roomUpd("1","0");
//                                    queryBuilding(LoginActivity.this);
//                                }else {
//                                    queryBuilding(LoginActivity.this);
//                                }
                                new MyAsyncTask().execute();
                            }else {
                                anim_lauout.setVisibility(View.GONE);
                                anim_img.clearAnimation();
                                exit.setVisibility(View.VISIBLE);
                                line.setVisibility(View.VISIBLE);

                            }
                        } else {
                            anim_lauout.setVisibility(View.GONE);
                            anim_img.clearAnimation();
                           Tip.show(getApplicationContext(), response.body().getResult(), false);
                        }
                    }
                    @Override
                    public void onError(Response<Login> response) {
                        super.onError(response);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                        anim_img.clearAnimation();
                        anim_lauout.setVisibility(View.GONE);
                        // Log.e(TAG, "onError: 服务器连接失败" );
                    }
                });
        
    }

    //查询楼宇
    public void queryBuilding(final Context context){
        //orderId= DataTime.orderId();
        OkGo.<BuildingTable>post(HttpUrl.QUERYBUILDING)
                .tag(this)
                .params("mchid",mchid)
                .execute(new JsonCallBack<BuildingTable>(BuildingTable.class) {
                    @Override
                    public void onSuccess(Response<BuildingTable> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("0000")){
                            Log.e(TAG, "onSuccess: "+response.body().getDatalist().toString() );
                            for (BuildingTable.Bean bean : response.body().getDatalist()) {
                                bean.setFlag("0");
                                daoSimple.buildAdd(bean);
                            }
                            queryFloor(context);
                            Log.e(TAG, "onSuccess: sel-->"+daoSimple.buildSelAll());
                        }else {
                            anim_lauout.setVisibility(View.GONE);
                            anim_img.clearAnimation();
                            Tip.show(context,response.body().getResult(),false);
                        }
                    }

                    @Override
                    public void onError(Response<BuildingTable> response) {
                        super.onError(response);
                        anim_lauout.setVisibility(View.GONE);
                        anim_img.clearAnimation();
                        Tip.show(getApplicationContext(),"服务器连接异常",false);

                    }
                });
    }

    //查询楼层
    public void queryFloor(final Context context){
        OkGo.<FloorTable>post(HttpUrl.QUERYFLOOR)
                .tag(this)
                .params("mchid",mchid)
                .execute(new JsonCallBack<FloorTable>(FloorTable.class) {
                    @Override
                    public void onSuccess(Response<FloorTable> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("0000")){
                            Log.e(TAG, "onSuccess: "+response.body().getDatalist().toString() );
                            for (FloorTable.Bean bean : response.body().getDatalist()) {
                                bean.setFlag("0");
                                daoSimple.floorAdd(bean);
                            }
                            queryRoomType(context);
                            Log.e(TAG, "onSuccess: sel-->"+daoSimple.floorSelAll());
                        }else {
                            anim_lauout.setVisibility(View.GONE);
                            anim_img.clearAnimation();
                            Tip.show(context,response.body().getResult(),false);
                        }
                    }

                    @Override
                    public void onError(Response<FloorTable> response) {
                        super.onError(response);
                        anim_lauout.setVisibility(View.GONE);
                        anim_img.clearAnimation();
                        Tip.show(getApplicationContext(),"服务器连接异常",false);

                    }
                });
    }

    //查询房型
    public void queryRoomType(final Context context){
        OkGo.<HouseTable>post(HttpUrl.QUERYROOMTYPE)
                .tag(this)
                .params("mchid",mchid)
                .execute(new JsonCallBack<HouseTable>(HouseTable.class) {
                    @Override
                    public void onSuccess(Response<HouseTable> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("0000")){
                            Log.e(TAG, "onSuccess: "+response.body().getDatalist().toString() );
                            for (HouseTable.Bean bean : response.body().getDatalist()) {
                                bean.setFlag("0");
                                daoSimple.houseAdd(bean);
                            }
                            queryRoomInfo(context);
                            Log.e(TAG, "onSuccess: sel-->"+daoSimple.houseSelAll());
                        }else {
                            anim_lauout.setVisibility(View.GONE);
                            anim_img.clearAnimation();
                            Tip.show(context,response.body().getResult(),false);
                        }
                    }

                    @Override
                    public void onError(Response<HouseTable> response) {
                        super.onError(response);
                        anim_lauout.setVisibility(View.GONE);
                        anim_img.clearAnimation();
                        Tip.show(getApplicationContext(),"服务器连接异常",false);

                    }
                });
    }

    //查询房间
    public void queryRoomInfo(final Context context){
        OkGo.<RoomTable>post(HttpUrl.QUERYROOMINFO)
                .tag(this)
                .params("mchid",mchid)
                .execute(new JsonCallBack<RoomTable>(RoomTable.class) {
                    @Override
                    public void onSuccess(Response<RoomTable> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("0000")){
                            Log.e(TAG, "onSuccess: "+response.body().getDatalist().toString() );
                            for (RoomTable.Bean bean : response.body().getDatalist()) {
                                bean.setFlag("0");
                                daoSimple.roomAdd(bean);
                            }
                            daoSimple.delete("1");
                            anim_tv.setText("登陆成功");

                            anim_img.clearAnimation();
                            anim_lauout.setVisibility(View.GONE);
                            linear.startAnimation(animation2);
                            Log.e(TAG, "onSuccess: sel-->"+daoSimple.roomSelAll());
                        }else {
                            Tip.show(context,response.body().getResult(),false);
                            anim_lauout.setVisibility(View.GONE);
                            anim_img.clearAnimation();
                        }
                    }

                    @Override
                    public void onError(Response<RoomTable> response) {
                        super.onError(response);
                        anim_lauout.setVisibility(View.GONE);
                        anim_img.clearAnimation();
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                    }

                });
    }
    @Override
    protected void onDestroy() {
      //  isText = false;
       // ActivityManager.getInstance().exit();
        CommonSharedPreferences.put("data","");
        login.setText("登陆");
        anim_lauout.setVisibility(View.GONE);
        anim_img.clearAnimation();
        OkGo.getInstance().cancelTag(this);
        super.onDestroy();
    }
    public class MyAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("tag", "开始执行");
        }

        @Override
        protected String doInBackground(String... integers) {

            if (daoSimple.buildSelAll() != null && daoSimple.floorSelAll() != null
                    && daoSimple.houseSelAll() != null && daoSimple.roomSelAll() != null){
                daoSimple.buildUpd("1","0");
                daoSimple.floorUpd("1","0");
                daoSimple.houseUpd("1","0");
                daoSimple.roomUpd("1","0");
                queryBuilding(LoginActivity.this);
            }else {
                queryBuilding(LoginActivity.this);
            }
            return "成功";
        }
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            anim_tv.setText(""+integer);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            anim_tv.setText(""+values[0]);
        }
    }
}
