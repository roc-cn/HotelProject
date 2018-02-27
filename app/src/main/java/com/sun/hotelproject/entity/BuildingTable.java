package com.sun.hotelproject.entity;

import java.io.Serializable;

/**
 * Created by aun on 2018/2/26.
 * 楼宇表
 */

public class BuildingTable implements Serializable {
    private String buildcode;//楼宇PMS编码
    private String buildname;//楼宇名

    public BuildingTable(String buildcode, String buildname) {
        this.buildcode = buildcode;
        this.buildname = buildname;
    }

    public BuildingTable() {
    }

    public String getBuildcode() {
        return buildcode;
    }

    public void setBuildcode(String buildcode) {
        this.buildcode = buildcode;
    }

    public String getBuildname() {
        return buildname;
    }

    public void setBuildname(String buildname) {
        this.buildname = buildname;
    }

    @Override
    public String toString() {
        return "BuildingTable{" +
                "buildcode='" + buildcode + '\'' +
                ", buildname='" + buildname + '\'' +
                '}';
    }
}
