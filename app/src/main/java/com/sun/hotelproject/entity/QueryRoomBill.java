package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by a'su's on 2018/3/30.
 * 查询客房账单
 */

public class QueryRoomBill implements Serializable{
    private String result;//处理结果
    private String rescode;//响应码
    private List<Bean> datalist;

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

    public class Bean{
        private String name;//姓名
        private String dcono;//身份证号码
        private String intime;//入住时间
        private String outtime;//离店时间
        private String rtpmsno;//房型pms编码
        private String roomno;//房号
        private String addprice;//应加收房费
        private String accountprice;//账户余额
        private String inorderpmsno;//入住pms单号
        private List<Map<String,String>> bills;//账单明细

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDcono() {
            return dcono;
        }

        public void setDcono(String dcono) {
            this.dcono = dcono;
        }

        public String getIntime() {
            return intime;
        }

        public void setIntime(String intime) {
            this.intime = intime;
        }

        public String getOuttime() {
            return outtime;
        }

        public void setOuttime(String outtime) {
            this.outtime = outtime;
        }

        public String getRtpmsno() {
            return rtpmsno;
        }

        public void setRtpmsno(String rtpmsno) {
            this.rtpmsno = rtpmsno;
        }

        public String getRoomno() {
            return roomno;
        }

        public void setRoomno(String roomno) {
            this.roomno = roomno;
        }

        public String getAddprice() {
            return addprice;
        }

        public void setAddprice(String addprice) {
            this.addprice = addprice;
        }

        public String getAccountprice() {
            return accountprice;
        }

        public void setAccountprice(String accountprice) {
            this.accountprice = accountprice;
        }

        public String getInorderpmsno() {
            return inorderpmsno;
        }

        public void setInorderpmsno(String inorderpmsno) {
            this.inorderpmsno = inorderpmsno;
        }

        public List<Map<String, String>> getBills() {
            return bills;
        }

        public void setBills(List<Map<String, String>> bills) {
            this.bills = bills;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "name='" + name + '\'' +
                    ", dcono='" + dcono + '\'' +
                    ", intime='" + intime + '\'' +
                    ", outtime='" + outtime + '\'' +
                    ", rtpmsno='" + rtpmsno + '\'' +
                    ", roomno='" + roomno + '\'' +
                    ", addprice='" + addprice + '\'' +
                    ", accountprice='" + accountprice + '\'' +
                    ", inorderpmsno='" + inorderpmsno + '\'' +
                    ", bills='" + bills + '\'' +
                    '}';
        }
    }
}
