package com.sun.hotelproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sun.hotelproject.entity.BuildingTable;
import com.sun.hotelproject.entity.FloorTable;
import com.sun.hotelproject.entity.HouseTable;
import com.sun.hotelproject.entity.Order;
import com.sun.hotelproject.entity.RoomNo;
import com.sun.hotelproject.entity.RoomTable;

import java.util.ArrayList;

/**
 * Created by a'su's on 2018/2/26.
 * 增删改查实现类
 */

public class DaoSimple implements Dao {
    private String TABLE_NAME1="Building_Table";//楼宇表
    private String TABLE_NAME2="Floor_Table";//楼层表
    private String TABLE_NAME3="House_Table";//房型表
    private String TABLE_NAME4="Room_Table";//房间表
    private String TABLE_NAME5="Room_No";//房间号表
    private String DB_NAME="House_db";//酒店信息数据库
    private MyHelper helper;
    private SQLiteDatabase db;
    private Context c;
    public DaoSimple(Context c){
        this.c=c;
        helper=new MyHelper(c,DB_NAME,null,1);
    }
    //楼宇表
    @Override
    public void buildAdd(BuildingTable.Bean buildingTable) {
        db=helper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("buildcode",buildingTable.getBpmsno());
        values.put("buildname",buildingTable.getBpmsnname());
        values.put("flag",buildingTable.getFlag());
        db.insert(TABLE_NAME1,null,values);
        db.close();

    }

    @Override
    public ArrayList<BuildingTable.Bean> buildSelAll() {
        db=helper.getWritableDatabase();
        ArrayList<BuildingTable.Bean> list= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME1,null);
            for (int i = 0; i <c.getCount() ; i++) {
                c.moveToNext();
                BuildingTable.Bean buildingTable=new BuildingTable().new Bean();
                buildingTable.setBpmsno(c.getString(c.getColumnIndex("buildcode")));
                buildingTable.setBpmsnname(c.getString(c.getColumnIndex("buildname")));
                buildingTable.setFlag(c.getString(c.getColumnIndex("flag")));
                list.add(buildingTable);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return list;
    }

    @Override
    public BuildingTable.Bean buildSel(String buildcode) {
        db=helper.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME1+" where buildcode=?",new String[]{buildcode});
            if (c.moveToNext()){
                BuildingTable.Bean buildingTable=new BuildingTable().new Bean();
                buildingTable.setBpmsno(c.getString(c.getColumnIndex("buildcode")));
                buildingTable.setBpmsnname(c.getString(c.getColumnIndex("buildname")));
                buildingTable.setFlag(c.getString(c.getColumnIndex("flag")));
                return buildingTable;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return null;
    }

    @Override
    public void buildUpd(String s1, String s2) {
        db=helper.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME1+" set flag=? where flag=?",new String[]{s1,s2});
        db.close();
    }

    //楼层表
    @Override
    public void floorAdd(FloorTable.Bean floorTable) {
        db=helper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("floorcode",floorTable.getFpmsno());
        values.put("floornum",floorTable.getFpmsseq());
        values.put("floorname",floorTable.getFpmsname());
        values.put("floorstate",floorTable.getFpmsstatus());
        values.put("flag",floorTable.getFlag());
        db.insert(TABLE_NAME2,null,values);
        db.close();
    }

    @Override
    public ArrayList<FloorTable.Bean> floorSelAll() {
        db=helper.getWritableDatabase();
        ArrayList<FloorTable.Bean> list= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME2,null);
            for (int i = 0; i <c.getCount() ; i++) {
                c.moveToNext();
                FloorTable.Bean floorTable=new FloorTable().new Bean();
                floorTable.setFpmsno(c.getString(c.getColumnIndex("floorcode")));
                floorTable.setFpmsseq(c.getString(c.getColumnIndex("floornum")));
                floorTable.setFpmsname(c.getString(c.getColumnIndex("floorname")));
                floorTable.setFpmsstatus(c.getString(c.getColumnIndex("floorstate")));
                floorTable.setFlag(c.getString(c.getColumnIndex("flag")));
                list.add(floorTable);
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return list;
    }

    @Override
    public FloorTable.Bean floorSel(String floorcode) {
        db=helper.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME2+" where floorcode=?",new String[]{floorcode});
            if (c.moveToNext()){
                FloorTable.Bean floorTable=new FloorTable().new Bean();
                floorTable.setFpmsno(c.getString(c.getColumnIndex("floorcode")));
                floorTable.setFpmsseq(c.getString(c.getColumnIndex("floornum")));
                floorTable.setFpmsname(c.getString(c.getColumnIndex("floorname")));
                floorTable.setFpmsstatus(c.getString(c.getColumnIndex("floorstate")));
                floorTable.setFlag(c.getString(c.getColumnIndex("flag")));
                return floorTable;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return null;
    }

    @Override
    public void floorUpd(String s1, String s2) {
        db=helper.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME2+" set flag=? where flag=?",new String[]{s1,s2});
        db.close();
    }

    //房型表
    @Override
    public void houseAdd(HouseTable.Bean houseTable) {
        db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("housecode",houseTable.getRtpmsno());
        values.put("housename",houseTable.getRtpmsnname());
        values.put("flag",houseTable.getFlag());
        db.insert(TABLE_NAME3,null,values);
        db.close();
    }

    @Override
    public ArrayList<HouseTable.Bean> houseSelAll() {
        db=helper.getWritableDatabase();
        ArrayList<HouseTable.Bean> list= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME3,null);
            for (int i = 0; i <c.getCount() ; i++) {
                c.moveToNext();
                HouseTable.Bean houseTable=new HouseTable().new Bean();
                houseTable.setRtpmsno(c.getString(c.getColumnIndex("housecode")));
                houseTable.setRtpmsnname(c.getString(c.getColumnIndex("housename")));
                houseTable.setFlag(c.getString(c.getColumnIndex("flag")));
                list.add(houseTable);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return list;
    }

    @Override
    public HouseTable.Bean houseSel(String housecode) {
        db=helper.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME3+" where housecode=?",new String[]{housecode});
            if (c.moveToNext()){
                HouseTable.Bean houseTable=new HouseTable().new Bean();
               // houseTable.setRtpmsno(c.getString(c.getColumnIndex("housecode")));
                houseTable.setRtpmsnname(c.getString(c.getColumnIndex("housename")));
                houseTable.setFlag(c.getString(c.getColumnIndex("flag")));
                return houseTable;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return null;
    }


    @Override
    public void houseUpd(String s1, String s2) {
        db=helper.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME3+" set flag=? where flag=?",new String[]{s1,s2});
        db.close();
    }

    //房间表
    @Override
    public void roomAdd(RoomTable.Bean roomTable) {
        db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("roomcode",roomTable.getRpmsno());
        values.put("roomnum",roomTable.getRoomno());
        values.put("housecode",roomTable.getRtpmsno());
        values.put("buildcode",roomTable.getBpmsno());
        values.put("floorcode",roomTable.getFpmsno());
        values.put("serial_numlock",roomTable.getLockno());
        values.put("proomnum",roomTable.getPpolicesystemno());
        values.put("lockofbuild",roomTable.getLockdevicebpms());
        values.put("lockof_floor",roomTable.getLockdevicefpms());
        values.put("serialnum",roomTable.getLockdeviceno());
        values.put("openlocknum",roomTable.getLockdevicewxopenno());
        values.put("featurenum",roomTable.getRoomfeatureno());
        values.put("flag",roomTable.getFlag());
        db.insert(TABLE_NAME4,null,values);
        db.close();
    }

    @Override
    public ArrayList<RoomTable.Bean> roomSelAll() {
        db=helper.getWritableDatabase();
        ArrayList<RoomTable.Bean> list= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME4,null);
            for (int i = 0; i <c.getCount() ; i++) {
                c.moveToNext();
                RoomTable.Bean roomTable=new RoomTable().new Bean();
                roomTable.setRpmsno(c.getString(c.getColumnIndex("roomcode")));
                roomTable.setRoomno(c.getString(c.getColumnIndex("roomnum")));
                roomTable.setRtpmsno(c.getString(c.getColumnIndex("housecode")));
                roomTable.setBpmsno(c.getString(c.getColumnIndex("buildcode")));
                roomTable.setFpmsno(c.getString(c.getColumnIndex("floorcode")));
                roomTable.setLockno(c.getString(c.getColumnIndex("serial_numlock")));
                roomTable.setPpolicesystemno(c.getString(c.getColumnIndex("proomnum")));
                roomTable.setLockdevicebpms(c.getString(c.getColumnIndex("lockofbuild")));
                roomTable.setLockdevicefpms(c.getString(c.getColumnIndex("lockof_floor")));
                roomTable.setLockdeviceno(c.getString(c.getColumnIndex("serialnum")));
                roomTable.setLockdevicewxopenno(c.getString(c.getColumnIndex("openlocknum")));
                roomTable.setRoomfeatureno(c.getString(c.getColumnIndex("featurenum")));
                roomTable.setFlag(c.getString(c.getColumnIndex("flag")));
                list.add(roomTable);
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return list;
    }

    @Override//房型编码
    public RoomTable.Bean selFloorByRtpmno(String rtpmsno) {
        db=helper.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME4+" where housecode=?",new String[]{rtpmsno});
            if (c.moveToNext()){
                RoomTable.Bean roomTable=new RoomTable().new Bean();
                roomTable.setFpmsno(c.getString(c.getColumnIndex("floorcode")));
                return roomTable;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return null;
    }

    @Override//房间编码
    public RoomTable.Bean selRoomNoByRpmno(String rpmsno) {
        db=helper.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME4+" where roomcode=?",new String[]{rpmsno});
            if (c.moveToNext()){
                RoomTable.Bean roomTable=new RoomTable().new Bean();
                roomTable.setFpmsno(c.getString(c.getColumnIndex("floorcode")));
                roomTable.setRoomno(c.getString(c.getColumnIndex("roomnum")));
                return roomTable;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return null;
    }

    @Override
    public RoomTable.Bean selRpmnoNoByRoom(String roomnum) {
        db=helper.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME4+" where roomnum=?",new String[]{roomnum});
            if (c.moveToNext()){
                RoomTable.Bean roomTable=new RoomTable().new Bean();
                roomTable.setFpmsno(c.getString(c.getColumnIndex("floorcode")));
                roomTable.setRpmsno(c.getString(c.getColumnIndex("roomcode")));
                return roomTable;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return null;
    }


    @Override
    public void roomUpd(String s1, String s2) {
        db=helper.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME4+" set flag=? where flag=?",new String[]{s1,s2});
        db.close();
    }

    @Override
    public void delete(String s1) {
        db=helper.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME1+" where flag=?",new String[]{s1});
        db.execSQL("delete from "+TABLE_NAME2+" where flag=?",new String[]{s1});
        db.execSQL("delete from "+TABLE_NAME3+" where flag=?",new String[]{s1});
        db.execSQL("delete from "+TABLE_NAME4+" where flag=?",new String[]{s1});
        db.close();
    }

    @Override
    public void roomNoAdd(RoomNo roomNo) {
        db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("roomno",roomNo.getRoommo());
        values.put("data",roomNo.getData());
        db.insert(TABLE_NAME5,null,values);
        db.close();
    }

    @Override
    public void roomNoUpd(String data1, String data2) {
        db=helper.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME5+" set data=? where data=?",new String[]{data1,data2});
        db.close();
    }

    @Override
    public RoomNo roomNoSel(String roomno) {
        db=helper.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME5+" where roomno=?",new String[]{roomno});
            if (c.moveToNext()){
                RoomNo roomNo =new RoomNo();
                roomNo.setRoommo(c.getString(c.getColumnIndex("roomno")));
                roomNo.setData(c.getString(c.getColumnIndex("data")));
                return roomNo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return null;
    }

    @Override
    public ArrayList<RoomNo> roomNoSelAll() {
        db=helper.getWritableDatabase();
        ArrayList<RoomNo> list= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME5,null);
            for (int i = 0; i <c.getCount() ; i++) {
                c.moveToNext();
                RoomNo roomNo =new RoomNo();
                roomNo.setRoommo(c.getString(c.getColumnIndex("roomno")));
                roomNo.setData(c.getString(c.getColumnIndex("data")));
                list.add(roomNo);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }if (db!=null){
                db.close();
            }
        }
        return list;
    }
}
