package com.sun.hotelproject.dao;

import com.sun.hotelproject.entity.BuildingTable;
import com.sun.hotelproject.entity.FloorTable;
import com.sun.hotelproject.entity.HouseTable;
import com.sun.hotelproject.entity.RoomNo;
import com.sun.hotelproject.entity.RoomTable;

import java.util.ArrayList;

/**
 * Created by a'su's on 2018/2/26.
 */

public interface Dao {
    //楼宇表
    void buildAdd(BuildingTable.Bean buildingTable);
    ArrayList<BuildingTable.Bean> buildSelAll();
    BuildingTable.Bean buildSel(String buildcode);
    void buildUpd(String s1,String s2);
    //楼层表
    void floorAdd(FloorTable.Bean floorTable);
    ArrayList<FloorTable.Bean> floorSelAll();
    FloorTable.Bean floorSel(String floorcode);
    void floorUpd(String s1,String s2);
    //房型表
    void houseAdd(HouseTable.Bean houseTable);
    ArrayList<HouseTable.Bean> houseSelAll();
    HouseTable.Bean houseSel(String housecode);
    void houseUpd(String s1,String s2);

    //房间表
    void roomAdd(RoomTable.Bean roomTable);
    ArrayList<RoomTable.Bean> roomSelAll();
    RoomTable.Bean selFloorByRtpmno(String rtpmsno);//房型查楼层
    RoomTable.Bean selRoomNoByRpmno(String rpmsno);//房型查房间号
    RoomTable.Bean selRpmnoNoByRoom(String roomnum);//房间查房型
    void roomUpd(String s1,String s2);

    void delete(String s1);

    //房间号列表
    void roomNoAdd(RoomNo roomNo);
    void roomNoUpd(String data1,String data2);
    RoomNo roomNoSel(String roomno);
    ArrayList<RoomNo> roomNoSelAll();
}
