package com.sun.hotelproject.entity;

import java.io.Serializable;

/**
 * @author sun
 * Created by sun on 2017/11/24.
 * TODO:房型 实体
 */
public class LayoutHouse implements Serializable {
    private String type;//房型
    private String acreage;//面积
    private String bed_type;//床型
    private String iswindow;//是否有窗
    private String isbreakfast;//是否含早
    private String iscancel;//是否可取消;
    private String price;//价格

    public LayoutHouse() {
    }

    public LayoutHouse(String type, String acreage, String bed_type, String iswindow, String isbreakfast, String iscancel, String price) {
        this.type = type;
        this.acreage = acreage;
        this.bed_type = bed_type;
        this.iswindow = iswindow;
        this.isbreakfast = isbreakfast;
        this.iscancel = iscancel;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAcreage() {
        return acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public String getBed_type() {
        return bed_type;
    }

    public void setBed_type(String bed_type) {
        this.bed_type = bed_type;
    }

    public String getIswindow() {
        return iswindow;
    }

    public void setIswindow(String iswindow) {
        this.iswindow = iswindow;
    }

    public String getIsbreakfast() {
        return isbreakfast;
    }

    public void setIsbreakfast(String isbreakfast) {
        this.isbreakfast = isbreakfast;
    }

    public String getIscancel() {
        return iscancel;
    }

    public void setIscancel(String iscancel) {
        this.iscancel = iscancel;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "LayoutHouse{" +
                "type='" + type + '\'' +
                ", acreage='" + acreage + '\'' +
                ", bed_type='" + bed_type + '\'' +
                ", iswindow=" + iswindow +
                ", isbreakfast=" + isbreakfast +
                ", iscancel=" + iscancel +
                ", price='" + price + '\'' +
                '}';
    }
}
