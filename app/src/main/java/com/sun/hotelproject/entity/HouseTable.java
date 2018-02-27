package com.sun.hotelproject.entity;

import java.io.Serializable;

/**
 * Created by sun on 2018/2/26.
 * 房型表
 */

public class HouseTable implements Serializable {
    private String housecode;//房型PMS编码
    private String housename;//房型名称

    public HouseTable() {
    }

    public HouseTable(String housecode, String housename) {
        this.housecode = housecode;
        this.housename = housename;
    }

    public String getHousecode() {
        return housecode;
    }

    public void setHousecode(String housecode) {
        this.housecode = housecode;
    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    @Override
    public String toString() {
        return "HouseTable{" +
                "housecode='" + housecode + '\'' +
                ", housename='" + housename + '\'' +
                '}';
    }
}
