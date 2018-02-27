package com.sun.hotelproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sun.hotelproject.entity.BuildingTable;
import com.sun.hotelproject.entity.FloorTable;
import com.sun.hotelproject.entity.HouseTable;
import com.sun.hotelproject.entity.RoomTable;

import java.util.ArrayList;

/**
 * Created by a'su's on 2018/2/26.
 */

public class DaoSimple implements Dao {
    private String TABLE_NAME1="Building_Table";//楼宇表
    private String TABLE_NAME2="Floor_Table";//楼层表
    private String TABLE_NAME3="House_Table";//房型表
    private String TABLE_NAME4="Room_Table";//房间表
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
    public void buildAdd(BuildingTable buildingTable) {
        db=helper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("buildcode",buildingTable.getBuildcode());
        values.put("buildname",buildingTable.getBuildname());
        db.insert(TABLE_NAME1,null,values);
        db.close();

    }

    @Override
    public ArrayList<BuildingTable> buildSelAll() {
        db=helper.getWritableDatabase();
        ArrayList<BuildingTable> list=new ArrayList<BuildingTable>();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME1,null);
            for (int i = 0; i <c.getCount() ; i++) {
                c.moveToNext();
                BuildingTable buildingTable=new BuildingTable();
                buildingTable.setBuildcode(c.getString(c.getColumnIndex("buildcode")));
                buildingTable.setBuildname(c.getString(c.getColumnIndex("buildname")));
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
    public BuildingTable buildSel(String buildcode) {
        db=helper.getWritableDatabase();

        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME1+" where buildcode=?",new String[]{buildcode});
            if (c.moveToNext()){
                BuildingTable buildingTable=new BuildingTable();
                buildingTable.setBuildcode(c.getString(c.getColumnIndex("buildcode")));
                buildingTable.setBuildname(c.getString(c.getColumnIndex("buildname")));
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
    //楼层表
    @Override
    public void floorAdd(FloorTable floorTable) {
        db=helper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("floorcode",floorTable.getFloorcode());
        values.put("floornum",floorTable.getFloornum());
        values.put("floorname",floorTable.getFloorname());
        values.put("floorstate",floorTable.getFloorstate());
        values.put("buildcode",floorTable.getBuildcode());
        db.insert(TABLE_NAME2,null,values);
        db.close();
    }

    @Override
    public ArrayList<FloorTable> floorSelAll() {
        db=helper.getWritableDatabase();
        ArrayList<FloorTable> list=new ArrayList<FloorTable>();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME2,null);
            for (int i = 0; i <c.getCount() ; i++) {
                c.moveToNext();
                FloorTable floorTable=new FloorTable();
                floorTable.setFloorcode(c.getString(c.getColumnIndex("floorcode")));
                floorTable.setFloornum(c.getString(c.getColumnIndex("floornum")));
                floorTable.setFloorname(c.getString(c.getColumnIndex("floorname")));
                floorTable.setFloorstate(c.getString(c.getColumnIndex("floorstate")));
                floorTable.setBuildcode(c.getString(c.getColumnIndex("buildcode")));
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
    public FloorTable floorSel(String floorcode) {
        db=helper.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME2+" where floorcode=?",new String[]{floorcode});
            if (c.moveToNext()){
                FloorTable floorTable=new FloorTable();
                floorTable.setFloorcode(c.getString(c.getColumnIndex("floorcode")));
                floorTable.setFloornum(c.getString(c.getColumnIndex("floornum")));
                floorTable.setFloorname(c.getString(c.getColumnIndex("floorname")));
                floorTable.setFloorstate(c.getString(c.getColumnIndex("floorstate")));
                floorTable.setBuildcode(c.getString(c.getColumnIndex("buildcode")));
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
    //房型表
    @Override
    public void houseAdd(HouseTable houseTable) {
        db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("housecode",houseTable.getHousecode());
        values.put("housename",houseTable.getHousename());
        db.insert(TABLE_NAME3,null,values);
        db.close();
    }

    @Override
    public ArrayList<HouseTable> houseSelAll() {
        db=helper.getWritableDatabase();
        ArrayList<HouseTable> list=new ArrayList<HouseTable>();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME3,null);
            for (int i = 0; i <c.getCount() ; i++) {
                c.moveToNext();
                HouseTable houseTable=new HouseTable();
                houseTable.setHousecode(c.getString(c.getColumnIndex("housecode")));
                houseTable.setHousename(c.getString(c.getColumnIndex("housename")));
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
    public HouseTable houseSel(String housecode) {
        db=helper.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME3+" where housecode=?",new String[]{housecode});
            if (c.moveToNext()){
                HouseTable houseTable=new HouseTable();
                houseTable.setHousecode(c.getString(c.getColumnIndex("housecode")));
                houseTable.setHousename(c.getString(c.getColumnIndex("housename")));
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
    //房间表
    @Override
    public void roomAdd(RoomTable roomTable) {
        db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("roomcode",roomTable.getRoomcode());
        values.put("roomnum",roomTable.getRoomnum());
        values.put("housecode",roomTable.getHousecode());
        values.put("buildcode",roomTable.getBuildcode());
        values.put("floorcode",roomTable.getFloorcode());
        values.put("serial_numlock",roomTable.getSerial_numlock());
        values.put("proomnum",roomTable.getProomnum());
        values.put("lockofbuild",roomTable.getLockofbuild());
        values.put("lockof_floor",roomTable.getLockof_floor());
        values.put("serialnum",roomTable.getSerialnum());
        values.put("openlocknum",roomTable.getOpenlocknum());
        values.put("featurenum",roomTable.getFeaturenum());
        db.insert(TABLE_NAME4,null,values);
        db.close();
    }

    @Override
    public ArrayList<RoomTable> roomSelAll() {
        db=helper.getWritableDatabase();
        ArrayList<RoomTable> list=new ArrayList<RoomTable>();
        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME4,null);
            for (int i = 0; i <c.getCount() ; i++) {
                c.moveToNext();
                RoomTable roomTable=new RoomTable();
                roomTable.setRoomcode(c.getString(c.getColumnIndex("roomcode")));
                roomTable.setRoomnum(c.getString(c.getColumnIndex("roomnum")));
                roomTable.setHousecode(c.getString(c.getColumnIndex("housecode")));
                roomTable.setBuildcode(c.getString(c.getColumnIndex("buildcode")));
                roomTable.setFloorcode(c.getString(c.getColumnIndex("floorcode")));
                roomTable.setSerial_numlock(c.getString(c.getColumnIndex("serial_numlock")));
                roomTable.setProomnum(c.getString(c.getColumnIndex("proomnum")));
                roomTable.setLockofbuild(c.getString(c.getColumnIndex("lockofbuild")));
                roomTable.setLockof_floor(c.getString(c.getColumnIndex("lockof_floor")));
                roomTable.setSerialnum(c.getString(c.getColumnIndex("serialnum")));
                roomTable.setOpenlocknum(c.getString(c.getColumnIndex("openlocknum")));
                roomTable.setFeaturenum(c.getString(c.getColumnIndex("featurenum")));
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

    @Override
    public RoomTable roomSel(String roomcode) {
        db=helper.getWritableDatabase();

        Cursor c=null;
        try{
            c=db.rawQuery("select * from "+ TABLE_NAME4+" where roomcode=?",new String[]{roomcode});
            if (c.moveToNext()){
                RoomTable roomTable=new RoomTable();
                roomTable.setRoomcode(c.getString(c.getColumnIndex("roomcode")));
                roomTable.setRoomnum(c.getString(c.getColumnIndex("roomnum")));
                roomTable.setHousecode(c.getString(c.getColumnIndex("housecode")));
                roomTable.setBuildcode(c.getString(c.getColumnIndex("buildcode")));
                roomTable.setFloorcode(c.getString(c.getColumnIndex("floorcode")));
                roomTable.setSerial_numlock(c.getString(c.getColumnIndex("serial_numlock")));
                roomTable.setProomnum(c.getString(c.getColumnIndex("proomnum")));
                roomTable.setLockofbuild(c.getString(c.getColumnIndex("lockofbuild")));
                roomTable.setLockof_floor(c.getString(c.getColumnIndex("lockof_floor")));
                roomTable.setSerialnum(c.getString(c.getColumnIndex("serialnum")));
                roomTable.setOpenlocknum(c.getString(c.getColumnIndex("openlocknum")));
                roomTable.setFeaturenum(c.getString(c.getColumnIndex("featurenum")));
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
}
