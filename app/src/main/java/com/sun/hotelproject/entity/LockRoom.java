package com.sun.hotelproject.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by a'su's on 2018/3/26.
 */

public class LockRoom implements Serializable {

    private String result;
    private String rescode;
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
        private String lockres;//锁定结果
        private String locksign;//锁房标记值

        public String getLockres() {
            return lockres;
        }

        public void setLockres(String lockres) {
            this.lockres = lockres;
        }

        public String getLocksign() {
            return locksign;
        }

        public void setLocksign(String locksign) {
            this.locksign = locksign;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "lockres='" + lockres + '\'' +
                    ", locksign='" + locksign + '\'' +
                    '}';
        }
    }
}
