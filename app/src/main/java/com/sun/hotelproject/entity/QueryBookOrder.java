package com.sun.hotelproject.entity;

import com.sun.hotelproject.moudle.SelectActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by a'su's on 2018/4/20.
 * 查询预定单
 */

public class QueryBookOrder implements Serializable {
    private String result;//处理结果
    private String rescode;//响应码
    private List<Bean>datalist;

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
        private String opmsno;//预定单PMS编码
        private String bookname;//预定人名称
        private String booktel;//预定人手机号
        private String booktime;//预定时间
        private String reachtime;//抵达日期
        private String outtime;//离店日期
        private String assuretype;//担保类型
        private String payprice;//已支付金额
        private String rtpmsno;//房型PMS编码
        private String roomnum;//房间数量
        private String roomno;//房号
        private String indate;//入住日期
        private String dealprice;//成交房价
        private String oldprice;//原始房价
        private String breaknum;//早餐份数
        private String membercode;//会员卡号
        private String memberpmsno;//会员级别PMS编码
        private String unit;//协议单位名称

        private String channelmer;//渠道商名称
        private String orderno;//订单号
        private String rmk;//备注
        private String intype;//入住类型
        private String code;//代码
        private String relevanceroom;//联房标记格式
        private String ismainorder;//是否是主订单

        private String pcode;//PMS房价码
        private String issecurity;//房价是否保密
        private String adorderno;//预订单ID
        private String type;//状态
        private String id;//纯预留ID

        public String getAssuretype() {
            return assuretype;
        }

        public void setAssuretype(String assuretype) {
            this.assuretype = assuretype;
        }

        public String getPayprice() {
            return payprice;
        }

        public void setPayprice(String payprice) {
            this.payprice = payprice;
        }

        public String getRtpmsno() {
            return rtpmsno;
        }

        public void setRtpmsno(String rtpmsno) {
            this.rtpmsno = rtpmsno;
        }

        public String getRoomnum() {
            return roomnum;
        }

        public void setRoomnum(String roomnum) {
            this.roomnum = roomnum;
        }

        public String getRoomno() {
            return roomno;
        }

        public void setRoomno(String roomno) {
            this.roomno = roomno;
        }

        public String getIndate() {
            return indate;
        }

        public void setIndate(String indate) {
            this.indate = indate;
        }

        public String getDealprice() {
            return dealprice;
        }

        public void setDealprice(String dealprice) {
            this.dealprice = dealprice;
        }

        public String getOldprice() {
            return oldprice;
        }

        public void setOldprice(String oldprice) {
            this.oldprice = oldprice;
        }

        public String getBreaknum() {
            return breaknum;
        }

        public void setBreaknum(String breaknum) {
            this.breaknum = breaknum;
        }

        public String getMembercode() {
            return membercode;
        }

        public void setMembercode(String membercode) {
            this.membercode = membercode;
        }

        public String getMemberpmsno() {
            return memberpmsno;
        }

        public void setMemberpmsno(String memberpmsno) {
            this.memberpmsno = memberpmsno;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getChannelmer() {
            return channelmer;
        }

        public void setChannelmer(String channelmer) {
            this.channelmer = channelmer;
        }

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public String getRmk() {
            return rmk;
        }

        public void setRmk(String rmk) {
            this.rmk = rmk;
        }

        public String getIntype() {
            return intype;
        }

        public void setIntype(String intype) {
            this.intype = intype;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getRelevanceroom() {
            return relevanceroom;
        }

        public void setRelevanceroom(String relevanceroom) {
            this.relevanceroom = relevanceroom;
        }

        public String getIsmainorder() {
            return ismainorder;
        }

        public void setIsmainorder(String ismainorder) {
            this.ismainorder = ismainorder;
        }

        public String getPcode() {
            return pcode;
        }

        public void setPcode(String pcode) {
            this.pcode = pcode;
        }

        public String getIssecurity() {
            return issecurity;
        }

        public void setIssecurity(String issecurity) {
            this.issecurity = issecurity;
        }

        public String getAdorderno() {
            return adorderno;
        }

        public void setAdorderno(String adorderno) {
            this.adorderno = adorderno;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOpmsno() {
            return opmsno;
        }

        public void setOpmsno(String opmsno) {
            this.opmsno = opmsno;
        }

        public String getBookname() {
            return bookname;
        }

        public void setBookname(String bookname) {
            this.bookname = bookname;
        }

        public String getBooktel() {
            return booktel;
        }

        public void setBooktel(String booktel) {
            this.booktel = booktel;
        }

        public String getBooktime() {
            return booktime;
        }

        public void setBooktime(String booktime) {
            this.booktime = booktime;
        }

        public String getReachtime() {
            return reachtime;
        }

        public void setReachtime(String reachtime) {
            this.reachtime = reachtime;
        }

        public String getOuttime() {
            return outtime;
        }

        public void setOuttime(String outtime) {
            this.outtime = outtime;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "opmsno='" + opmsno + '\'' +
                    ", bookname='" + bookname + '\'' +
                    ", booktel='" + booktel + '\'' +
                    ", booktime='" + booktime + '\'' +
                    ", reachtime='" + reachtime + '\'' +
                    ", outtime='" + outtime + '\'' +
                    ", assuretype='" + assuretype + '\'' +
                    ", payprice='" + payprice + '\'' +
                    ", rtpmsno='" + rtpmsno + '\'' +
                    ", roomnum='" + roomnum + '\'' +
                    ", roomno='" + roomno + '\'' +
                    ", indate='" + indate + '\'' +
                    ", dealprice='" + dealprice + '\'' +
                    ", oldprice='" + oldprice + '\'' +
                    ", breaknum='" + breaknum + '\'' +
                    ", membercode='" + membercode + '\'' +
                    ", memberpmsno='" + memberpmsno + '\'' +
                    ", unit='" + unit + '\'' +
                    ", channelmer='" + channelmer + '\'' +
                    ", orderno='" + orderno + '\'' +
                    ", rmk='" + rmk + '\'' +
                    ", intype='" + intype + '\'' +
                    ", code='" + code + '\'' +
                    ", relevanceroom='" + relevanceroom + '\'' +
                    ", ismainorder='" + ismainorder + '\'' +
                    ", pcode='" + pcode + '\'' +
                    ", issecurity='" + issecurity + '\'' +
                    ", adorderno='" + adorderno + '\'' +
                    ", type='" + type + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }

}
