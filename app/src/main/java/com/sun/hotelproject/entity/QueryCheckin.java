package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a'su's on 2018/3/29.
 * 查询入住单
 */

public class QueryCheckin implements Serializable{
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

    public class Bean implements Serializable{
        private String  inorderpmsno;//入住单PMS编码
        private String  guestname;//入住人姓名
        private String  docno;//证件号
        private String  nation;//民族
        private String  gender;//性别
        private String  address;//住址
        private String  birth;//出生
        private String  checkinno;//客人入住流水号
        private String  guestel;//手机号
        private String  intime;//入住时间
        private String  outtime;//离店时间
        private String  rtpmsno;//房型PMS编码
        private String  roomno;//房号
        private String  relevanceroom;//联房标记
        private String  membercode;//会员卡号
        private String  intype;//入住类型
        private String  memberpmsno;//会员级别PMS编码
        private String  iscontlive;//是否允许在自助设备上续住
        private String  obligate;//预留
        private String  balance;//账单余额
        private String todayprice;//每日房价
        private String  isysq;//是否用预授权入住
        private String  unitdes;//协议单位中文描述
        private String  ordercenterdes;//订房中心描述
        private String  traveldes;//旅行社描述
        private String opmsno;//预订单PMS编码
        private String  roomtype;//房间状态
        private String  checkpaypmsno;//登记单付款PMS代码
        private String  adpaypmsno;//预定单付款PMS代码
        private String  adtypedes;//预定类型描述

        public String getTodayprice() {
            return todayprice;
        }

        public void setTodayprice(String todayprice) {
            this.todayprice = todayprice;
        }

        public String getIsysq() {
            return isysq;
        }

        public void setIsysq(String isysq) {
            this.isysq = isysq;
        }

        public String getUnitdes() {
            return unitdes;
        }

        public void setUnitdes(String unitdes) {
            this.unitdes = unitdes;
        }

        public String getOrdercenterdes() {
            return ordercenterdes;
        }

        public void setOrdercenterdes(String ordercenterdes) {
            this.ordercenterdes = ordercenterdes;
        }

        public String getTraveldes() {
            return traveldes;
        }

        public void setTraveldes(String traveldes) {
            this.traveldes = traveldes;
        }

        public String getOpmsno() {
            return opmsno;
        }

        public void setOpmsno(String opmsno) {
            this.opmsno = opmsno;
        }

        public String getRoomtype() {
            return roomtype;
        }

        public void setRoomtype(String roomtype) {
            this.roomtype = roomtype;
        }

        public String getCheckpaypmsno() {
            return checkpaypmsno;
        }

        public void setCheckpaypmsno(String checkpaypmsno) {
            this.checkpaypmsno = checkpaypmsno;
        }

        public String getAdpaypmsno() {
            return adpaypmsno;
        }

        public void setAdpaypmsno(String adpaypmsno) {
            this.adpaypmsno = adpaypmsno;
        }

        public String getAdtypedes() {
            return adtypedes;
        }

        public void setAdtypedes(String adtypedes) {
            this.adtypedes = adtypedes;
        }

        public String getInorderpmsno() {
            return inorderpmsno;
        }

        public void setInorderpmsno(String inorderpmsno) {
            this.inorderpmsno = inorderpmsno;
        }

        public String getGuestname() {
            return guestname;
        }

        public void setGuestname(String guestname) {
            this.guestname = guestname;
        }

        public String getDocno() {
            return docno;
        }

        public void setDocno(String docno) {
            this.docno = docno;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getCheckinno() {
            return checkinno;
        }

        public void setCheckinno(String checkinno) {
            this.checkinno = checkinno;
        }

        public String getGuestel() {
            return guestel;
        }

        public void setGuestel(String guestel) {
            this.guestel = guestel;
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

        public String getRelevanceroom() {
            return relevanceroom;
        }

        public void setRelevanceroom(String relevanceroom) {
            this.relevanceroom = relevanceroom;
        }

        public String getMembercode() {
            return membercode;
        }

        public void setMembercode(String membercode) {
            this.membercode = membercode;
        }

        public String getIntype() {
            return intype;
        }

        public void setIntype(String intype) {
            this.intype = intype;
        }

        public String getMemberpmsno() {
            return memberpmsno;
        }

        public void setMemberpmsno(String memberpmsno) {
            this.memberpmsno = memberpmsno;
        }

        public String getIscontlive() {
            return iscontlive;
        }

        public void setIscontlive(String iscontlive) {
            this.iscontlive = iscontlive;
        }

        public String getObligate() {
            return obligate;
        }

        public void setObligate(String obligate) {
            this.obligate = obligate;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "inorderpmsno='" + inorderpmsno + '\'' +
                    ", guestname='" + guestname + '\'' +
                    ", docno='" + docno + '\'' +
                    ", nation='" + nation + '\'' +
                    ", gender='" + gender + '\'' +
                    ", address='" + address + '\'' +
                    ", birth='" + birth + '\'' +
                    ", checkinno='" + checkinno + '\'' +
                    ", guestel='" + guestel + '\'' +
                    ", intime='" + intime + '\'' +
                    ", outtime='" + outtime + '\'' +
                    ", rtpmsno='" + rtpmsno + '\'' +
                    ", roomno='" + roomno + '\'' +
                    ", relevanceroom='" + relevanceroom + '\'' +
                    ", membercode='" + membercode + '\'' +
                    ", intype='" + intype + '\'' +
                    ", memberpmsno='" + memberpmsno + '\'' +
                    ", iscontlive='" + iscontlive + '\'' +
                    ", obligate='" + obligate + '\'' +
                    ", balance='" + balance + '\'' +
                    ", todayprice='" + todayprice + '\'' +
                    ", isysq='" + isysq + '\'' +
                    ", unitdes='" + unitdes + '\'' +
                    ", ordercenterdes='" + ordercenterdes + '\'' +
                    ", traveldes='" + traveldes + '\'' +
                    ", opmsno='" + opmsno + '\'' +
                    ", roomtype='" + roomtype + '\'' +
                    ", checkpaypmsno='" + checkpaypmsno + '\'' +
                    ", adpaypmsno='" + adpaypmsno + '\'' +
                    ", adtypedes='" + adtypedes + '\'' +
                    '}';
        }
    }

}
