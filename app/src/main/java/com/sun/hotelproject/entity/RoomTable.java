package com.sun.hotelproject.entity;

import java.io.Serializable;

/**
 * Created by sun on 2018/2/26.
 * 房间表
 */

public class RoomTable implements Serializable {
    private String roomcode;//房间PMS编码
    private String roomnum;//房号
    private String housecode;//所属房型PMS编码
    private String buildcode;//所属楼宇PMS编码
    private String floorcode;//所属楼层PMS编码
    private String serial_numlock;//门锁串号
    private String proomnum;//公安系统房号
    private String lockofbuild;//门锁设备楼栋
    private String lockof_floor;//门锁设备楼层
    private String serialnum;//门锁设备流水号
    private String openlocknum;//门锁设备微信开门锁号
    private String featurenum;//房间特征编号

    public RoomTable() {
    }

    public RoomTable(String roomcode, String roomnum, String housecode, String buildcode, String floorcode, String serial_numlock, String proomnum, String lockofbuild, String lockof_floor, String serialnum, String openlocknum, String featurenum) {
        this.roomcode = roomcode;
        this.roomnum = roomnum;
        this.housecode = housecode;
        this.buildcode = buildcode;
        this.floorcode = floorcode;
        this.serial_numlock = serial_numlock;
        this.proomnum = proomnum;
        this.lockofbuild = lockofbuild;
        this.lockof_floor = lockof_floor;
        this.serialnum = serialnum;
        this.openlocknum = openlocknum;
        this.featurenum = featurenum;
    }

    public String getRoomcode() {
        return roomcode;
    }

    public void setRoomcode(String roomcode) {
        this.roomcode = roomcode;
    }

    public String getRoomnum() {
        return roomnum;
    }

    public void setRoomnum(String roomnum) {
        this.roomnum = roomnum;
    }

    public String getHousecode() {
        return housecode;
    }

    public void setHousecode(String housecode) {
        this.housecode = housecode;
    }

    public String getBuildcode() {
        return buildcode;
    }

    public void setBuildcode(String buildcode) {
        this.buildcode = buildcode;
    }

    public String getFloorcode() {
        return floorcode;
    }

    public void setFloorcode(String floorcode) {
        this.floorcode = floorcode;
    }

    public String getSerial_numlock() {
        return serial_numlock;
    }

    public void setSerial_numlock(String serial_numlock) {
        this.serial_numlock = serial_numlock;
    }

    public String getProomnum() {
        return proomnum;
    }

    public void setProomnum(String proomnum) {
        this.proomnum = proomnum;
    }

    public String getLockofbuild() {
        return lockofbuild;
    }

    public void setLockofbuild(String lockofbuild) {
        this.lockofbuild = lockofbuild;
    }

    public String getLockof_floor() {
        return lockof_floor;
    }

    public void setLockof_floor(String lockof_floor) {
        this.lockof_floor = lockof_floor;
    }

    public String getSerialnum() {
        return serialnum;
    }

    public void setSerialnum(String serialnum) {
        this.serialnum = serialnum;
    }

    public String getOpenlocknum() {
        return openlocknum;
    }

    public void setOpenlocknum(String openlocknum) {
        this.openlocknum = openlocknum;
    }

    public String getFeaturenum() {
        return featurenum;
    }

    public void setFeaturenum(String featurenum) {
        this.featurenum = featurenum;
    }

    @Override
    public String toString() {
        return "RoomTable{" +
                "roomcode='" + roomcode + '\'' +
                ", roomnum='" + roomnum + '\'' +
                ", housecode='" + housecode + '\'' +
                ", buildcode='" + buildcode + '\'' +
                ", floorcode='" + floorcode + '\'' +
                ", serial_numlock='" + serial_numlock + '\'' +
                ", proomnum='" + proomnum + '\'' +
                ", lockofbuild='" + lockofbuild + '\'' +
                ", lockof_floor='" + lockof_floor + '\'' +
                ", serialnum='" + serialnum + '\'' +
                ", openlocknum='" + openlocknum + '\'' +
                ", featurenum='" + featurenum + '\'' +
                '}';
    }
}
