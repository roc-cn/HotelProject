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
import com.sun.hotelproject.entity.QueryBookOrder;
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

import java.io.Serializable;
import java.util.List;


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
    private List<QueryBookOrder.Bean> datas;
    private List<GuestRoom.Bean> gList;
    private GuestRoom.Bean gBean;
    Animation operatingAnim;
    private String name;
    String price ="";
    AnimationDrawable animationDrawable;
    private String k;
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
        ActivityManager.getInstance().addActivity(this);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        k =getIntent().getStringExtra("k");
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.load_animation);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        daoSimple = new DaoSimple(this);
        if (k.equals("1")) {
            gList =(List<GuestRoom.Bean>)getIntent().getSerializableExtra("gList");
            name = getIntent().getStringExtra("name");
            mchid = getIntent().getStringExtra("mchid");
            beginTime = (String) CommonSharedPreferences.get("beginTime", "");
            endTime = (String) CommonSharedPreferences.get("endTime", "");
            content = (String) CommonSharedPreferences.get("content", "");
            init();
        }else if (k.equals("4")){
            mchid = getIntent().getStringExtra("mchid");
            datas = (List<QueryBookOrder.Bean>) getIntent().getSerializableExtra("list");
            init2(datas);
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CommonAdapter<GuestRoom.Bean>(SelectActivity.this, R.layout.grid_item,gList) {
           @Override
           protected void convert(ViewHolder holder, final GuestRoom.Bean bean, int position) {
                final RoomTable.Bean bean1=daoSimple.selRoomNoByRpmno(bean.getRpmsno());

               holder.setText(R.id.house_type,name+"\u3000无早\u3000不可取消");
               holder.setText(R.id.house_num,"房间号 "+bean1.getRoomno());
               holder.setText2(R.id.house_price,DataTime.updTextSize2(getApplicationContext(),"￥"+bean.getDealprice(),1));
               holder.setText(R.id.reserve,"确定");
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

    @SuppressLint("SetTextI18n")
    private void init2(List<QueryBookOrder.Bean> list) {
        recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CommonAdapter<QueryBookOrder.Bean>(SelectActivity.this, R.layout.grid_item,list) {
            @Override
            protected void convert(ViewHolder holder, final QueryBookOrder.Bean bean, int position) {
                final HouseTable.Bean houseSel =daoSimple.houseSel(bean.getRtpmsno());
                holder.setText(R.id.house_type,houseSel.getRtpmsnname()+"\u3000无早\u3000不可取消");
                holder.setText(R.id.house_num,"房间号 "+bean.getRoomno());
                holder.setText2(R.id.house_price,DataTime.updTextSize2(getApplicationContext(),"￥"+bean.getOldprice(),1));
                holder.setText(R.id.reserve,"确定");
                holder.setOnClickListener(R.id.reserve, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utils.isFastClick()) {
                           RoomTable.Bean roomSel=daoSimple.selRpmnoNoByRoom(bean.getRoomno()) ;
                                CommonSharedPreferences.put("house_type",houseSel.getRtpmsnname());
                                CommonSharedPreferences.put("roomNum", bean.getRoomno());
                                Intent intent =new Intent();
                                intent.putExtra("bean", bean);
                                setResult(Activity.RESULT_OK,intent);
                                finish();
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
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("0000")) {
                            if (response.body().getDatalist().get(0).getLockres().equals("2")){
                                Tip.show(getApplicationContext(),"锁房失败！",false);
                                Log.e(TAG, "onSuccess: "+response.body().getDatalist().get(0).getLockres() );
                            }else {
                                CommonSharedPreferences.put("house_type",name);
                                Tip.show(getApplicationContext(),"锁房成功！",true);
                                Intent intent =new Intent();
                                intent.putExtra("bean",bean);
                                intent.putExtra("locksign",response.body().getDatalist().get(0).getLocksign());
                                setResult(Activity.RESULT_OK,intent);
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


    @OnClick({R.id.relative1,R.id.cancel})
    void OnClick(View v){
        switch (v.getId()){
            case R.id.relative1:
                if (Utils.isFastClick()){
                    Intent intent =new Intent();
                    setResult(0,intent);
                    finish();
                }
                break;
            case R.id.cancel:
                if (Utils.isFastClick()){
                    Intent intent =new Intent();
                    setResult(0,intent);
                    finish();
                }
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
