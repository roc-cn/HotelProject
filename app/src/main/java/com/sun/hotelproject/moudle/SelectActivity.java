package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.adapter.CommonAdapter;

import com.sun.hotelproject.base.adapter.ViewHolder;
import com.sun.hotelproject.dao.DaoSimple;
import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.HouseTable;
import com.sun.hotelproject.entity.LockRoom;
import com.sun.hotelproject.entity.QueryRomm;
import com.sun.hotelproject.entity.RoomTable;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.DividerItemDecoration;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;
import com.sun.hotelproject.view.RecyclerViewForEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 选择房间界面
 * @author  sun 2018/3/9
 */
public class SelectActivity extends Activity  {
    private DaoSimple daoSimple;
    Unbinder unbinder;
    private String mchid;
    @BindView(R.id.recycler)RecyclerView recycler;
    @BindView(R.id.relative1)RelativeLayout relativeLayout;
    @BindView(R.id.cancel)Button cancel;
    @BindView(R.id.anim_layout) RelativeLayout anim_lauout;
    @BindView(R.id.anim_img)ImageView anim_img;
    @BindView(R.id.anim_tv)TextView anim_tv;
    private String[] from ={"title","img"};
    private int[] to={R.id.title,R.id.image};
    CommonAdapter adapter;
    private String rtpmsno="";//房型码
    private static final String TAG = "SelectActivity";
    private List<GuestRoom.Bean> list;
    private String beginTime = "";
    private String endTime = "";
    private String content = "";
//    List<Map<String,String>> datas;
//    Map<String,String> map;
    private QueryRomm queryRomm;
    Animation operatingAnim;
    private String name;
    String price ="";
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //得到窗口
        Window window=getWindow();
        //隐藏返回键等虚拟按键
        WindowManager.LayoutParams params=window.getAttributes();
        params.systemUiVisibility= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
        //设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //保持屏幕 常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_select);
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        daoSimple =new DaoSimple(this);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.load_animation);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
      //  rtpmsno = getIntent().getStringExtra("rtpmsno");
        queryRomm = (QueryRomm) getIntent().getSerializableExtra("queryRomm");
        name = getIntent().getStringExtra("name");
        mchid =getIntent().getStringExtra("mchid");
        beginTime = (String) CommonSharedPreferences.get("beginTime","");
        endTime = (String) CommonSharedPreferences.get("endTime","");
        content = (String) CommonSharedPreferences.get("content","");
        Log.e(TAG, "onCreate: "+beginTime + " " +endTime +"  " +content );
      //  getPost();
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        ActivityManager.getInstance().addActivity(this);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CommonAdapter<GuestRoom.Bean>(SelectActivity.this, R.layout.grid_item,queryRomm.getDatas()) {
           @Override
           protected void convert(ViewHolder holder, final GuestRoom.Bean bean, int position) {
                final RoomTable.Bean bean1=daoSimple.selRoomNoByRpmno(bean.getRpmsno());

               Log.e(TAG, "convert: "+ daoSimple.selRoomNoByRpmno(bean.getRpmsno()));
               Log.e(TAG, "convert: "+bean.getRpmsno() );
               // price = String.valueOf();
               holder.setText(R.id.house_type,name+"\u3000无早\u3000不可取消");
               holder.setText(R.id.house_num,"房间号 "+bean1.getRoomno());
               holder.setText2(R.id.house_price,DataTime.updTextSize2(getApplicationContext(),"￥"+bean.getDealprice(),1));
               holder.setOnClickListener(R.id.reserve, new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (Utils.isFastClick()) {
                           CommonSharedPreferences.put("roomNum", bean1.getRoomno());
                           lockRoom(bean);
                       }
                   }
               });
           }
       } ;
        recycler.setAdapter(adapter);

    }

    /**
     * 锁房
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void lockRoom(final GuestRoom.Bean bean){

        OkGo.<LockRoom>post(HttpUrl.LOCKROOM)
                .tag(this)
                .params("mchid", mchid)
                .params("bstype","2")
                .params("constraint","1")
                .params("operation", "1")
                .params("bpmsno","")
                .params("rpmsno",bean.getRpmsno())
                .params("userno","")
                .params("opmsno","")

                .execute(new JsonCallBack<LockRoom>(LockRoom.class) {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(Response<LockRoom> response) {
                        super.onSuccess(response);
                        anim_lauout.setVisibility(View.VISIBLE);
                        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim);
                        anim_img.setBackground(animationDrawable);
                        anim_tv.setText("正在加载中......");
                        if (animationDrawable != null && !animationDrawable.isRunning()){
                            animationDrawable.start();
                        }
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("0000")) {
                            if (response.body().getDatalist().get(0).getLockres().equals("2")){
                                Tip.show(getApplicationContext(),"锁房失败！",false);
                                animationDrawable.stop();
                                anim_lauout.setVisibility(View.GONE);
                                Log.e(TAG, "onSuccess: "+response.body().getDatalist().get(0).getLockres() );
                            }else {
                                CommonSharedPreferences.put("house_type",name);
                                Tip.show(getApplicationContext(),"锁房成功！",true);
                                Intent intent =new Intent();
                                intent.putExtra("bean",bean);
                                intent.putExtra("locksign",response.body().getDatalist().get(0).getLocksign());
                                setResult(Activity.RESULT_OK,intent);
                                animationDrawable.stop();
                                anim_lauout.setVisibility(View.GONE);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LockRoom> response) {
                        super.onError(response);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                    }
                });
    }


//    /**
//     * 查询可住房
//     */
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    void  getPost(){
//        anim_lauout.setVisibility(View.VISIBLE);
//
//        // animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.load_animation);
//        anim_img.setAnimation(operatingAnim);
//        anim_img.startAnimation(operatingAnim);
//        anim_tv.setText("正在加载中......");
////        if (animationDrawable != null && !animationDrawable.isRunning()){
////            animationDrawable.start();
////        }
//        OkGo.<GuestRoom>post(HttpUrl.QUERYROOMINFO2)
//                .tag(this)
//                .params("mchid",mchid)
//                .params("indate",beginTime)
//                .params("outdate", endTime)
//                .params("rtpmsno",rtpmsno)
//                .execute(new JsonCallBack<GuestRoom>(GuestRoom.class) {
//                    @Override
//                    public void onSuccess(Response<GuestRoom> response) {
//                        super.onSuccess(response);
//
//                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
//                        if (response.body().getRescode().equals("0000")){
//                            list=response.body().getDatalist();
//                            Log.e(TAG, "onSuccess: "+list.toString() );
//                            init(list);
//                            anim_img.clearAnimation();
//                            anim_lauout.setVisibility(View.GONE);
//                        }else {
//                            anim_img.clearAnimation();
//                            anim_lauout.setVisibility(View.GONE);
//                            Tip.show(getApplicationContext(),response.body().getResult(),false);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<GuestRoom> response) {
//                        super.onError(response);
//                        anim_img.clearAnimation();
//                        anim_lauout.setVisibility(View.GONE);
//                        Tip.show(getApplicationContext(),"服务器连接异常",false);
//                    }
//                });
//    }



    @OnClick({R.id.relative1,R.id.cancel})
    void OnClick(View v){
        Intent intent =new Intent();
        setResult(0,intent);
        switch (v.getId()){
            case R.id.relative1:
                Utils.isFastClick();
                finish();
                break;
            case R.id.cancel:
                Utils.isFastClick();
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }
}
