package com.sun.hotelproject.dao;

import com.sun.hotelproject.entity.BuildingTable;
import com.sun.hotelproject.entity.FloorTable;
import com.sun.hotelproject.entity.HouseTable;
import com.sun.hotelproject.entity.RoomTable;

import java.util.ArrayList;

/**
 * Created by a'su's on 2018/2/26.
 */

public interface Dao {
    //楼宇表
    void buildAdd(BuildingTable buildingTable);
    ArrayList<BuildingTable> buildSelAll();
    BuildingTable buildSel(String buildcode);
    //楼层表
    void floorAdd(FloorTable floorTable);
    ArrayList<FloorTable> floorSelAll();
    FloorTable floorSel(String floorcode);
    //房型表
    void houseAdd(HouseTable houseTable);
    ArrayList<HouseTable> houseSelAll();
    HouseTable houseSel(String housecode);

    //房间表
    void roomAdd(RoomTable roomTable);
    ArrayList<RoomTable> roomSelAll();
    RoomTable roomSel(String roomcode);
}
