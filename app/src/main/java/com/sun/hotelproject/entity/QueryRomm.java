package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by a'su's on 2018/4/13.
 * 查询可入房
 */

public class QueryRomm implements Serializable{
    private String rtpmsno;
    private List<GuestRoom.Bean> datas;

    public String getRtpmsno() {
        return rtpmsno;
    }

    public void setRtpmsno(String rtpmsno) {
        this.rtpmsno = rtpmsno;
    }

    public List<GuestRoom.Bean> getDatas() {
        return datas;
    }

    public void setDatas(List<GuestRoom.Bean> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "QueryRomm{" +
                "rtpmsno='" + rtpmsno + '\'' +
                ", datas=" + datas +
                '}';
    }
}
