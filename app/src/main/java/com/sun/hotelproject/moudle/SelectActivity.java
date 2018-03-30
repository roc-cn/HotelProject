package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.sun.hotelproject.entity.RoomTable;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.DividerItemDecoration;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;

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
    private String name;
    String price ="";

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

        rtpmsno = getIntent().getStringExtra("rtpmsno");
        name = getIntent().getStringExtra("name");
        mchid =getIntent().getStringExtra("mchid");
        beginTime = (String) CommonSharedPreferences.get("beginTime","");
        endTime = (String) CommonSharedPreferences.get("endTime","");
        content = (String) CommonSharedPreferences.get("content","");
        Log.e(TAG, "onCreate: "+beginTime + " " +endTime +"  " +content );
        getPost();
    }

    @SuppressLint("SetTextI18n")
    private void init(List<GuestRoom.Bean> list) {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CommonAdapter<GuestRoom.Bean>(SelectActivity.this, R.layout.grid_item,list) {
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
                       CommonSharedPreferences.put("roomNum",bean1.getRoomno());
                       lockRoom(bean);
                   }
               });
           }
       } ;

        recycler.setAdapter(adapter);

    }

    /**
     * 锁房
     */
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
                    @Override
                    public void onSuccess(Response<LockRoom> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("0000")) {
                            if (response.body().getDatalist().get(0).getLockres().equals("2")){
                                Tip.show(getApplicationContext(),"锁房失败！",false);
                                Log.e(TAG, "onSuccess: "+response.body().getDatalist().get(0).getLockres() );
                            }else {
                                Tip.show(getApplicationContext(),"锁房成功！",true);
                                Intent intent =new Intent();
                                intent.putExtra("bean",bean);
                                intent.putExtra("locksign",response.body().getDatalist().get(0).getLocksign());
                                setResult(Activity.RESULT_OK,intent);
                                finish();
                            }
                        }
                    }
                });
    }


    /**
     * 查询可住房
     */
    void  getPost(){
        OkGo.<GuestRoom>post(HttpUrl.QUERYROOMINFO2)
                .tag(this)
                .params("mchid",mchid)
                .params("indate",beginTime)
                .params("outdate", endTime)
                .params("rtpmsno",rtpmsno)
                .execute(new JsonCallBack<GuestRoom>(GuestRoom.class) {
                    @Override
                    public void onSuccess(Response<GuestRoom> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().getDatalist().toString() + "]");
                        if (response.body().getRescode().equals("0000")){
                            list=response.body().getDatalist();
                            Log.e(TAG, "onSuccess: "+list.toString() );
                            init(list);
                        }
                    }
                });
    }



    @OnClick({R.id.relative1,R.id.cancel})
    void OnClick(View v){
        Intent intent =new Intent();
        setResult(0,intent);
        switch (v.getId()){
            case R.id.relative1:
                finish();
                break;
            case R.id.cancel:
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