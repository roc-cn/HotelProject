package com.sun.hotelproject.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by a'su's on 2018/2/26.
 */

public class MyHelper extends SQLiteOpenHelper{
    private String TABLE_NAME1="Building_Table";//楼宇表
    private String TABLE_NAME2="Floor_Table";//楼层表
    private String TABLE_NAME3="House_Table";//房型表
    private String TABLE_NAME4="Room_Table";//房间表
    private Context c;
    public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.c=context;
    }
    /**初始化数据库的表结构*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+TABLE_NAME1+"(" +
                "buildcode varchar(50),"+//楼宇PMS编码
                "flag varchar(10),"+ //标记
                "buildname varchar(30))";//楼宇名
        db.execSQL(sql);
        sql="create table "+TABLE_NAME2+"(" +
                "floorcode varchar(50),"+//楼层PMS编码
                "floornum varchar(30),"+//楼层序号
                "floorname varchar(30),"+//楼层名称
                "floorstate varchar(10),"+//楼层状态
                "flag varchar(10),"+ //标记
                "buildcode varchar(50))";//所属楼宇PMS编号
        db.execSQL(sql);
        sql="create table "+TABLE_NAME3+"(" +
                "housecode varchar(50),"+//房型PMS编码
                "flag varchar(10),"+ //标记
                "housename varchar(30))";//房型名称
        db.execSQL(sql);
        sql="create table "+TABLE_NAME4+"(" +
                "roomcode varchar(50),"+//房间PMS编码
                "roomnum varchar(30),"+//房号
                "housecode varchar(50),"+//所属房型PMS编码
                "buildcode varchar(50),"+//所属楼宇PMS编码
                "floorcode varchar(50),"+//所属楼层PMS编码
                "serial_numlock varchar(30),"+//门锁串号
                "proomnum varchar(20),"+//公安系统房号
                "lockofbuild varchar(20),"+//门锁设备楼栋
                "lockof_floor varchar(20),"+//门锁设备楼层
                "serialnum varchar(50),"+//门锁设备流水号
                "openlocknum varchar(50),"+//门锁设备微信开门锁号
                "flag varchar(10),"+ //标记
                "featurenum varchar(50))";//房间特征编号
        db.execSQL(sql);
    }
    /**数据库版本升级时调用*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
