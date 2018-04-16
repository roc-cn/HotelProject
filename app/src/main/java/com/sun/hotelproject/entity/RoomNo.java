package com.sun.hotelproject.entity;

import java.io.Serializable;

/**
 * Created by a'su's on 2018/4/2.
 *
 */

public class RoomNo implements Serializable {
    private String roommo;//房间号
    private String data;//添加时间

    public RoomNo() {
    }

    public RoomNo(String roommo, String data) {
        this.roommo = roommo;
        this.data = data;
    }

    public String getRoommo() {
        return roommo;
    }

    public void setRoommo(String roommo) {
        this.roommo = roommo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RoomNo{" +
                "roommo='" + roommo + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
