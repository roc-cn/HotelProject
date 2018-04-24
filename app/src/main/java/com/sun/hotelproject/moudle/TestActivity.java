package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.dao.DaoSimple;
import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.HouseTable;
import com.sun.hotelproject.entity.QueryRomm;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.view.MyVideoView;


import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {
    private List<GuestRoom.Bean> gblist ;
    private DaoSimple daoSimple;
    List<HouseTable.Bean> list;
    String startTime,finshTime;
    private QueryRomm qr =new QueryRomm();
    private List<QueryRomm> datas =new ArrayList<>();
    private static final String TAG = "TestActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        startTime = DataTime.curenData();
        finshTime =DataTime.Tomorrow();
        daoSimple=new DaoSimple(this);
        list=daoSimple.houseSelAll();
        gblist =new ArrayList<>();
        for (HouseTable.Bean bean:list) {
            getPost(bean.getRtpmsno(),startTime,finshTime);

        }
        Log.e(TAG, "onCreate: datas---->"+datas.toString());
    }

    /**
     * 查询可住房
     */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void   getPost(final String rtpmsno, String beginTime, String endTime){
//        anim_lauout.setVisibility(View.VISIBLE);
//        anim_img.setAnimation(operatingAnim);
//        anim_img.startAnimation(operatingAnim);
//        anim_tv.setText("正在加载中......");

        OkGo.<GuestRoom>post(HttpUrl.QUERYROOMINFO2)
                .tag(this)
                .params("mchid","100100100101")
                .params("indate",beginTime)
                .params("outdate", endTime)
                .params("rtpmsno",rtpmsno)
                .execute(new JsonCallBack<GuestRoom>(GuestRoom.class) {
                    @Override
                    public void onSuccess(Response<GuestRoom> response) {
                        super.onSuccess(response);
                        if (response.body().getRescode().equals("0000")) {
                            gblist = response.body().getDatalist();
                            Log.e(TAG, "onSuccess: gblist"+gblist.toString() );
                            qr = new QueryRomm();
                            qr.setDatas(gblist);
                            qr.setRtpmsno(rtpmsno);
                            datas.add(qr);
                            Log.e(TAG, "onSuccess: qr"+qr.toString() );
                            Log.e(TAG, "onSuccess: data--->"+datas.toString() );
                        }else {
                            Tip.show(getApplicationContext(),mResponse.getResult(),false);
                        }
                    }
                    @Override
                    public void onError(Response<GuestRoom> response) {
                        super.onError(response);
                        Tip.show(getApplicationContext(),"服务器连接异常",false);
                    }
                });
    }

}
