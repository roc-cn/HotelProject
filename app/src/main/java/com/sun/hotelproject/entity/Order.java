package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sun on 2017/12/25.
 * 订单实体
 */

public class Order implements Serializable {
    private String result;
    private String rescode;
    private List<Bean> varList;

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

    public List<Bean> getVarList() {
        return varList;
    }

    public void setVarList(List<Bean> varList) {
        this.varList = varList;
    }

    public class Bean {
        private String ORDER_ID;
        private String ORDERID;
        private String DEVNO;
        private String MCHID;
        private String MERNAME;
        private String AMOUNT;
        private String TRANTIME;
        private String PAYSTS;
        private String RMK;

        public String getORDER_ID() {
            return ORDER_ID;
        }

        public void setORDER_ID(String ORDER_ID) {
            this.ORDER_ID = ORDER_ID;
        }

        public String getORDERID() {
            return ORDERID;
        }

        public void setORDERID(String ORDERID) {
            this.ORDERID = ORDERID;
        }

        public String getDEVNO() {
            return DEVNO;
        }

        public void setDEVNO(String DEVNO) {
            this.DEVNO = DEVNO;
        }

        public String getMCHID() {
            return MCHID;
        }

        public void setMCHID(String MCHID) {
            this.MCHID = MCHID;
        }

        public String getMERNAME() {
            return MERNAME;
        }

        public void setMERNAME(String MERNAME) {
            this.MERNAME = MERNAME;
        }

        public String getAMOUNT() {
            return AMOUNT;
        }

        public void setAMOUNT(String AMOUNT) {
            this.AMOUNT = AMOUNT;
        }

        public String getTRANTIME() {
            return TRANTIME;
        }

        public void setTRANTIME(String TRANTIME) {
            this.TRANTIME = TRANTIME;
        }

        public String getPAYSTS() {
            return PAYSTS;
        }

        public void setPAYSTS(String PAYSTS) {
            this.PAYSTS = PAYSTS;
        }

        public String getRMK() {
            return RMK;
        }

        public void setRMK(String RMK) {
            this.RMK = RMK;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "ORDER_ID='" + ORDER_ID + '\'' +
                    ", ORDERID='" + ORDERID + '\'' +
                    ", DEVNO='" + DEVNO + '\'' +
                    ", MCHID='" + MCHID + '\'' +
                    ", MERNAME='" + MERNAME + '\'' +
                    ", AMOUNT='" + AMOUNT + '\'' +
                    ", TRANTIME='" + TRANTIME + '\'' +
                    ", PAYSTS='" + PAYSTS + '\'' +
                    ", RMK='" + RMK + '\'' +
                    '}';
        }
    }
}
