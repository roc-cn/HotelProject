package com.sun.hotelproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.dao.DaoSimple;
import com.sun.hotelproject.entity.BuildingTable;
import com.sun.hotelproject.entity.FloorTable;
import com.sun.hotelproject.entity.HouseTable;
import com.sun.hotelproject.entity.Order;
import com.sun.hotelproject.entity.RoomTable;
import com.sun.hotelproject.moudle.LoginActivity;
import com.sun.hotelproject.moudle.MainActivity;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Tip;


/**
 * Created by sun on 2018/2/26.
 * 开机广播
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    private String orderId;
    private static final String TAG = "BootBroadcastReceiver";
    DaoSimple daoSimple;
    private String mchid ="100100100101";
    /**
     * 可以实现开机自动打开软件并运行app
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("XRGPS", "BootReceiver.onReceive: " + intent.getAction());
        System.out.println("自启动程序即将执行");
        daoSimple = new DaoSimple(context);

        //MainActivity就是开机显示的界面
        if (intent.getAction().equals(ACTION)){
            Intent intent1=new Intent(context, LoginActivity.class);
            //下面这句话必须加上才能开机自动运行app的界面
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
//        if (daoSimple.buildSelAll() != null && daoSimple.floorSelAll() != null
//                && daoSimple.houseSelAll() != null && daoSimple.roomSelAll() != null){
//            daoSimple.buildUpd("1","0");
//            daoSimple.floorUpd("1","0");
//            daoSimple.houseUpd("1","0");
//            daoSimple.roomUpd("1","0");
//            queryBuilding(context);
//        }else {
//            queryBuilding(context);
//        }

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

                    @Override
                    public void onError(Response<BuildingTable> response) {
                        super.onError(response);
                        Log.e(TAG, "onError: 服务器异常" );
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

                    @Override
                    public void onError(Response<FloorTable> response) {
                        super.onError(response);
                        Log.e(TAG, "onError: 服务器异常" );
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

                    @Override
                    public void onError(Response<HouseTable> response) {
                        super.onError(response);
                        Log.e(TAG, "onError: 服务器异常" );
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

                    @Override
                    public void onError(Response<RoomTable> response) {
                        super.onError(response);
                        Log.e(TAG, "onError: 服务器异常" );
                    }
                });
    }

}
