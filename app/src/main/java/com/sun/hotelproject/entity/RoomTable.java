package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sun on 2018/2/26.
 * 房间表
 */

public class RoomTable implements Serializable {
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
        private String rpmsno;//房间PMS编码
        private String roomno;//房号
        private String rtpmsno;//所属房型PMS编码
        private String bpmsno;//所属楼宇PMS编码
        private String fpmsno;//所属楼层PMS编码
        private String lockno;//门锁串号
        private String ppolicesystemno;//公安系统房号
        private String lockdevicebpms;//门锁设备楼栋
        private String lockdevicefpms;//门锁设备楼层
        private String lockdeviceno;//门锁设备流水号
        private String lockdevicewxopenno;//门锁设备微信开门锁号
        private String roomfeatureno;//房间特征编号
        private String flag;//标记

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getRpmsno() {
            return rpmsno;
        }

        public void setRpmsno(String rpmsno) {
            this.rpmsno = rpmsno;
        }

        public String getRoomno() {
            return roomno;
        }

        public void setRoomno(String roomno) {
            this.roomno = roomno;
        }

        public String getRtpmsno() {
            return rtpmsno;
        }

        public void setRtpmsno(String rtpmsno) {
            this.rtpmsno = rtpmsno;
        }

        public String getBpmsno() {
            return bpmsno;
        }

        public void setBpmsno(String bpmsno) {
            this.bpmsno = bpmsno;
        }

        public String getFpmsno() {
            return fpmsno;
        }

        public void setFpmsno(String fpmsno) {
            this.fpmsno = fpmsno;
        }

        public String getLockno() {
            return lockno;
        }

        public void setLockno(String lockno) {
            this.lockno = lockno;
        }

        public String getPpolicesystemno() {
            return ppolicesystemno;
        }

        public void setPpolicesystemno(String ppolicesystemno) {
            this.ppolicesystemno = ppolicesystemno;
        }

        public String getLockdevicebpms() {
            return lockdevicebpms;
        }

        public void setLockdevicebpms(String lockdevicebpms) {
            this.lockdevicebpms = lockdevicebpms;
        }

        public String getLockdevicefpms() {
            return lockdevicefpms;
        }

        public void setLockdevicefpms(String lockdevicefpms) {
            this.lockdevicefpms = lockdevicefpms;
        }

        public String getLockdeviceno() {
            return lockdeviceno;
        }

        public void setLockdeviceno(String lockdeviceno) {
            this.lockdeviceno = lockdeviceno;
        }

        public String getLockdevicewxopenno() {
            return lockdevicewxopenno;
        }

        public void setLockdevicewxopenno(String lockdevicewxopenno) {
            this.lockdevicewxopenno = lockdevicewxopenno;
        }

        public String getRoomfeatureno() {
            return roomfeatureno;
        }

        public void setRoomfeatureno(String roomfeatureno) {
            this.roomfeatureno = roomfeatureno;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "rpmsno='" + rpmsno + '\'' +
                    ", roomno='" + roomno + '\'' +
                    ", rtpmsno='" + rtpmsno + '\'' +
                    ", bpmsno='" + bpmsno + '\'' +
                    ", fpmsno='" + fpmsno + '\'' +
                    ", lockno='" + lockno + '\'' +
                    ", ppolicesystemno='" + ppolicesystemno + '\'' +
                    ", lockdevicebpms='" + lockdevicebpms + '\'' +
                    ", lockdevicefpms='" + lockdevicefpms + '\'' +
                    ", lockdeviceno='" + lockdeviceno + '\'' +
                    ", lockdevicewxopenno='" + lockdevicewxopenno + '\'' +
                    ", roomfeatureno='" + roomfeatureno + '\'' +
                    ", flag='" + flag + '\'' +
                    '}';
        }
    }
}
