package com.sun.hotelproject.entity;

import java.io.Serializable;

/**
 * Created by sun on 2017/12/5.
 * 人脸识别实体
 */

public class FaceRecognition implements Serializable {
    private String result;//请求后台返回结果
    private String rescode;//请求后台返回结果状态吗
    private String retcode;//请求腾讯接口返回状态码
    private String retmsg;//请求腾讯接口返回说明

    private String status;//验证结果
    private String score;//比对得分
    private String seq_no;//流水号
    private String account;//用户账号
    private String name;
    private String creid_no;//证件ID
    private String timestamp;//时间戳

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

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(String seq_no) {
        this.seq_no = seq_no;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreid_no() {
        return creid_no;
    }

    public void setCreid_no(String creid_no) {
        this.creid_no = creid_no;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "FaceRecognition{" +
                "result='" + result + '\'' +
                ", rescode='" + rescode + '\'' +
                ", retcode='" + retcode + '\'' +
                ", retmsg='" + retmsg + '\'' +
                ", status='" + status + '\'' +
                ", score='" + score + '\'' +
                ", seq_no='" + seq_no + '\'' +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", creid_no='" + creid_no + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
