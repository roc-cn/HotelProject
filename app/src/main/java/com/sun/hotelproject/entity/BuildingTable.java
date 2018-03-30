package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aun on 2018/2/26.
 * 楼宇表
 */

public class BuildingTable implements Serializable {
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

        private String bpmsno;//楼宇PMS编码
        private String bpmsnname;//楼宇名
        private String flag;//标记

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getBpmsno() {
            return bpmsno;
        }

        public void setBpmsno(String bpmsno) {
            this.bpmsno = bpmsno;
        }

        public String getBpmsnname() {
            return bpmsnname;
        }

        public void setBpmsnname(String bpmsnname) {
            this.bpmsnname = bpmsnname;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "bpmsno='" + bpmsno + '\'' +
                    ", bpmsnname='" + bpmsnname + '\'' +
                    ", flag='" + flag + '\'' +
                    '}';
        }
    }
}
