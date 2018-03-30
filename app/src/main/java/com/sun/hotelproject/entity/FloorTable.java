package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sun on 2018/2/26.
 * 楼层表
 */

public class FloorTable implements Serializable {
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
        private String fpmsno;//楼层PMS编码
        private String fpmsseq;//楼层序号
        private String fpmsname;//楼层名称
        private String fpmsstatus;//楼层状态
        private String flag;//标记
      //  private String buildcode;//所属楼宇PMS编号


        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getFpmsno() {
            return fpmsno;
        }

        public void setFpmsno(String fpmsno) {
            this.fpmsno = fpmsno;
        }

        public String getFpmsseq() {
            return fpmsseq;
        }

        public void setFpmsseq(String fpmsseq) {
            this.fpmsseq = fpmsseq;
        }

        public String getFpmsname() {
            return fpmsname;
        }

        public void setFpmsname(String fpmsname) {
            this.fpmsname = fpmsname;
        }

        public String getFpmsstatus() {
            return fpmsstatus;
        }

        public void setFpmsstatus(String fpmsstatus) {
            this.fpmsstatus = fpmsstatus;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "fpmsno='" + fpmsno + '\'' +
                    ", fpmsseq='" + fpmsseq + '\'' +
                    ", fpmsname='" + fpmsname + '\'' +
                    ", fpmsstatus='" + fpmsstatus + '\'' +
                    ", flag='" + flag + '\'' +
                    '}';
        }
    }
}
