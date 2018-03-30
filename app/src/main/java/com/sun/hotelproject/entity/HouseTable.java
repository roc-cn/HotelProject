package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sun on 2018/2/26.
 * 房型表
 */

public class HouseTable implements Serializable {
    private String result;//处理结果
    private String rescode;//响应码
    private List<Bean> datalist; //返回的结果

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

    public List<Bean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<Bean> datalist) {
        this.datalist = datalist;
    }

    public class Bean {
        private String rtpmsno;//房型PMS编码
        private String rtpmsnname;//房型名称
        private String flag;//标记

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getRtpmsno() {
            return rtpmsno;
        }

        public void setRtpmsno(String rtpmsno) {
            this.rtpmsno = rtpmsno;
        }

        public String getRtpmsnname() {
            return rtpmsnname;
        }

        public void setRtpmsnname(String rtpmsnname) {
            this.rtpmsnname = rtpmsnname;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "rtpmsno='" + rtpmsno + '\'' +
                    ", rtpmsnname='" + rtpmsnname + '\'' +
                    ", flag='" + flag + '\'' +
                    '}';
        }
    }
}
