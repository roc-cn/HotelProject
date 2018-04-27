package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by a'su's on 2018/4/26.
 * 查询可住房型
 */

public class QueryRoomType  implements Serializable{
    private String result;
    private String rescode;
    private List<String> datalist;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRescode() {
        return rescode;
    }

    public void setRescode(String rescode) {
        this.rescode = rescode;
    }

    public List<String> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<String> datalist) {
        this.datalist = datalist;
    }
}
