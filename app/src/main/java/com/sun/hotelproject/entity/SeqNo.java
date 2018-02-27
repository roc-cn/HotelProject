package com.sun.hotelproject.entity;

import java.io.Serializable;

/**
 * Created by sun on 2017/12/5.
 * 流水号实体
 */

public class SeqNo implements Serializable {
    private String result;
    private String rescode;
    private String retcode;
    private String retmsg;
    private String seq_no;
    private String account;

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

    @Override
    public String toString() {
        return "SeqNo{" +
                "result='" + result + '\'' +
                ", rescode='" + rescode + '\'' +
                ", retcode='" + retcode + '\'' +
                ", retmsg='" + retmsg + '\'' +
                ", seq_no='" + seq_no + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
