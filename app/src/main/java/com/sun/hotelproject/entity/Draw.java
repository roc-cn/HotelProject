package com.sun.hotelproject.entity;

import java.io.Serializable;

/**
 * TODO 多用实体 时间 2017/6/30
 * */
public class Draw implements Serializable {
    private String result;//处理结果
    private String rescode;//响应码
    private String codeimgurl;



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




    public String getCodeimgurl() {
        return codeimgurl;
    }

    public void setCodeimgurl(String codeimgurl) {
        this.codeimgurl = codeimgurl;
    }

    @Override
    public String toString() {
        return "Draw{" +
                "result='" + result + '\'' +
                ", rescode='" + rescode + '\'' +
                ", codeimgurl='" + codeimgurl + '\'' +
                '}';
    }
}