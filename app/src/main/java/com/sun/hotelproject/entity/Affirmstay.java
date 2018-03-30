package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by a'su's on 2018/3/29.
 * 确认续住信息
 */

public class Affirmstay implements Serializable {
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

    public class Bean implements Serializable{
        private String businessdate;//业务日期
        private String rtpmsno;//房型PMS编码
        private String normnightprice;//标准过夜房价
        private String normweeprice;//标准凌晨房价
        private String discountnightprice;//折后过夜房价
        private String discountweeprice;//折后凌晨房价
        private String pcode;//房价编码(房价码)
        private String pricetype;//价格类别
        private String sureprice;//成交价格
        private String pcodedetail;//房价编码描述
        private String bfcode;//早餐编码

        public String getBusinessdate() {
            return businessdate;
        }

        public void setBusinessdate(String businessdate) {
            this.businessdate = businessdate;
        }

        public String getRtpmsno() {
            return rtpmsno;
        }

        public void setRtpmsno(String rtpmsno) {
            this.rtpmsno = rtpmsno;
        }

        public String getNormnightprice() {
            return normnightprice;
        }

        public void setNormnightprice(String normnightprice) {
            this.normnightprice = normnightprice;
        }

        public String getNormweeprice() {
            return normweeprice;
        }

        public void setNormweeprice(String normweeprice) {
            this.normweeprice = normweeprice;
        }

        public String getDiscountnightprice() {
            return discountnightprice;
        }

        public void setDiscountnightprice(String discountnightprice) {
            this.discountnightprice = discountnightprice;
        }

        public String getDiscountweeprice() {
            return discountweeprice;
        }

        public void setDiscountweeprice(String discountweeprice) {
            this.discountweeprice = discountweeprice;
        }

        public String getPcode() {
            return pcode;
        }

        public void setPcode(String pcode) {
            this.pcode = pcode;
        }

        public String getPricetype() {
            return pricetype;
        }

        public void setPricetype(String pricetype) {
            this.pricetype = pricetype;
        }

        public String getSureprice() {
            return sureprice;
        }

        public void setSureprice(String sureprice) {
            this.sureprice = sureprice;
        }

        public String getPcodedetail() {
            return pcodedetail;
        }

        public void setPcodedetail(String pcodedetail) {
            this.pcodedetail = pcodedetail;
        }

        public String getBfcode() {
            return bfcode;
        }

        public void setBfcode(String bfcode) {
            this.bfcode = bfcode;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "businessdate='" + businessdate + '\'' +
                    ", rtpmsno='" + rtpmsno + '\'' +
                    ", normnightprice='" + normnightprice + '\'' +
                    ", normweeprice='" + normweeprice + '\'' +
                    ", discountnightprice='" + discountnightprice + '\'' +
                    ", discountweeprice='" + discountweeprice + '\'' +
                    ", pcode='" + pcode + '\'' +
                    ", pricetype='" + pricetype + '\'' +
                    ", sureprice='" + sureprice + '\'' +
                    ", pcodedetail='" + pcodedetail + '\'' +
                    ", bfcode='" + bfcode + '\'' +
                    '}';
        }
    }
}
