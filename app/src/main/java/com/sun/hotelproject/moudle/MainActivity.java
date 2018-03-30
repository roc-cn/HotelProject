package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.dao.DaoSimple;
import com.sun.hotelproject.entity.BuildingTable;
import com.sun.hotelproject.entity.Draw;
import com.sun.hotelproject.entity.FloorTable;
import com.sun.hotelproject.entity.HouseTable;
import com.sun.hotelproject.entity.RoomTable;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;

import java.util.Timer;
import java.util.TimerTask;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author  sun
 * 时间：2017/11/22
 * TODO:主界面
 */
@Route(path = "/hotel/main")
public class MainActivity extends BaseActivity {
    @BindView(R.id.invoice)Button invoice; //发票
    @BindView(R.id.check_in)Button check_in;//入住
    @BindView(R.id.check_out)Button check_out;//退房
    @BindView(R.id.renwal)Button renwal;//续住
    @BindView(R.id.reserve)Button reserve;//打印发票
    @BindView(R.id.play)ImageView play;//播放
    @BindView(R.id.toolBarTime)TextView toolBarTime;
    @BindView(R.id.toolBar_logo) ImageView toolBar_logo;
    @BindView(R.id.toolbarBack)Button toolbarBack;
    @BindView(R.id.title2) TextView title2;
    public static byte MacAddr = 0;
    DaoSimple daoSimple;
    private static final String TAG = "MainActivity";
  //  private Timer timer=new Timer();
    @SuppressLint("HandlerLeak")

    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        isRuning = false;
        toolBarTime.setVisibility(View.VISIBLE);
        toolBar_logo.setClickable(false);
        toolbarBack.setVisibility(View.GONE);
        handler.postDelayed(runnable,1000);

       daoSimple = new DaoSimple(this);
        Connect();
        if (daoSimple.buildSelAll() != null && daoSimple.floorSelAll() != null
                && daoSimple.houseSelAll() != null && daoSimple.roomSelAll() != null){
            daoSimple.buildUpd("1","0");
            daoSimple.floorUpd("1","0");
            daoSimple.houseUpd("1","0");
            daoSimple.roomUpd("1","0");
            queryBuilding(this);
        }else {
            queryBuilding(this);
        }

    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,1000);
            toolBarTime.setText(DataTime.curenTime());
        }
    };

    @OnClick({R.id.check_in,R.id.check_out,R.id.invoice,R.id.renwal,R.id.reserve,R.id.play})
    void OnClick(View v){
        switch (v.getId()){
            case R.id.check_in://入住
                //startActivity(new Intent(getApplicationContext(),I.class));
                Router.jumpL("/hotel/layouthouse");
                break;
            case R.id.check_out: //退房
                Router.jumpL("/hotel/checkout");
                break;
            case R.id.invoice: //打印发票
//             getCard();
                Router.jumpL("/hotel/orderdetails");
                break;
            case R.id.renwal: //续住
                Intent intent =new Intent(MainActivity.this,IdentificationActivity.class);
                    intent.putExtra("k","2");
                    startActivity(intent);
                break;
            case R.id.reserve://预定
               // getCard();
                break;
            case R.id.play://播放视频
                break;
                default:
                    break;
        }
    }
    /**
     * 前端进卡
     */
    private void reTrieve(){
        int nRet;
        byte[] SendBuf=new byte[3];
        String[] RecordInfo=new String[2];
        SendBuf[0] = 0x46;
        SendBuf[1] = 0x43;
        SendBuf[2] = 0x38;
        nRet = K720_Serial.K720_SendCmd(MacAddr, SendBuf, 3, RecordInfo);
        if(nRet == 0)
            Tip.show(this,"前端进卡命令执行成功",true);
        else
           Tip.show(this,"前端进卡命令执行失败",false);
    }



    /**
     * 连接
     */
    private void Connect(){
        String strPort = "/dev/ttyS3";
        int re = 0;
        byte i;
        String[] RecordInfo=new String[2];
        int Baudate = 9600;
        re = K720_Serial.K720_CommOpen(strPort);

        if(re==0)
        {
            for(i = 0; i < 16; i++)
            {
                re = K720_Serial.K720_AutoTestMac(i, RecordInfo);
                if(re == 0)
                {
                    MacAddr = i;
                    break;
                }
            }
            if(i == 16 && MacAddr == 0)
            {
               // Tip.show(this,"设备连接失败",false);
                // ShowMessage("设备连接失败，错误代码为："+K720_Serial.ErrorCode(re, 0));
                K720_Serial.K720_CommClose();
            }
        }
    }

    private void DisConnect(){
        int nRet;
        nRet = K720_Serial.K720_CommClose();
        if(nRet == 0)
        {
            MacAddr = 0;
            /**
             * 连接断开后 休眠 1秒后跳到主界面
             */

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        else{
            Tip.show(this,"设备断开失败",false);
        }
        Tip.show(this,"设备断开失败，错误代码为："+K720_Serial.ErrorCode(nRet, 0),false);
    }

    /**
     *
     * 取卡
     */
    private void getCard(){
        int nRet;
        byte[] SendBuf=new byte[3];
        String[] RecordInfo=new String[2];
        SendBuf[0] = 0x46;
        SendBuf[1] = 0x43;
        SendBuf[2] = 0x34;
        nRet = K720_Serial.K720_SendCmd(MacAddr, SendBuf, 3, RecordInfo);
        if(nRet == 0){

            Tip.show(this,"出卡成功",true);
        }
        else{


            Tip.show(this,"出卡失败",false);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postAtTime(runnable,1000);
        isRuning = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisConnect();
       handler.removeCallbacks(runnable);
       OkGo.getInstance().cancelAll();
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
                            Tip.show(context,response.body().getResult(),false);
                        }
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
                            Tip.show(context,response.body().getResult(),false);
                        }
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
                            Tip.show(context,response.body().getResult(),false);
                        }
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
                            Log.e(TAG, "onSuccess: sel-->"+daoSimple.roomSelAll());
                        }else {
                            Tip.show(context,response.body().getResult(),false);
                        }
                    }
                });
    }
    }
