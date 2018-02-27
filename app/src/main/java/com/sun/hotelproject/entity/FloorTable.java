package com.sun.hotelproject.entity;

import java.io.Serializable;

/**
 * Created by sun on 2018/2/26.
 * 楼层表
 */

public class FloorTable implements Serializable {
    private String floorcode;//楼层PMS编码
    private String floornum;//楼层序号
    private String floorname;//楼层名称
    private String floorstate;//楼层状态
    private String buildcode;//所属楼宇PMS编号

    public FloorTable() {
    }

    public FloorTable(String floorcode, String floornum, String floorname, String floorstate, String buildcode) {
        this.floorcode = floorcode;
        this.floornum = floornum;
        this.floorname = floorname;
        this.floorstate = floorstate;
        this.buildcode = buildcode;
    }

    public String getFloorcode() {
        return floorcode;
    }

    public void setFloorcode(String floorcode) {
        this.floorcode = floorcode;
    }

    public String getFloornum() {
        return floornum;
    }

    public void setFloornum(String floornum) {
        this.floornum = floornum;
    }

    public String getFloorname() {
        return floorname;
    }

    public void setFloorname(String floorname) {
        this.floorname = floorname;
    }

    public String getFloorstate() {
        return floorstate;
    }

    public void setFloorstate(String floorstate) {
        this.floorstate = floorstate;
    }

    public String getBuildcode() {
        return buildcode;
    }

    public void setBuildcode(String buildcode) {
        this.buildcode = buildcode;
    }

    @Override
    public String toString() {
        return "FloorTable{" +
                "floorcode='" + floorcode + '\'' +
                ", floornum='" + floornum + '\'' +
                ", floorname='" + floorname + '\'' +
                ", floorstate='" + floorstate + '\'' +
                ", buildcode='" + buildcode + '\'' +
                '}';
    }
}
