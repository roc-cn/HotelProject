package com.sun.hotelproject.entity;

import java.io.Serializable;

/**
 * Created by a'su's on 2018/4/4.
 *
 */

public class Login implements Serializable{
    private String result;
    private String rescode;
    private String username;
    private String mchid;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    @Override
    public String toString() {
        return "Login{" +
                "result='" + result + '\'' +
                ", rescode='" + rescode + '\'' +
                ", username='" + username + '\'' +
                ", mchid='" + mchid + '\'' +
                '}';
    }
}
