package com.sun.hotelproject.moudle.id_card;

import android.graphics.Bitmap;

public class IDCardInfo {
	private String strName;//姓名
	private String strSex;//姓别
	private String strNation;//
	private String strBirth;//生日
	private String strAddr;//地址ַ
	private String strIdCode;//身份证号
	private String strIssue;//签发机关
	private String strBeginDate;//有效开始日期
	private String strEndDate;//有效结束日期
	private Bitmap bitmapIdPhoto;//身份证上的图
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getStrSex() {
		return strSex;
	}
	public void setStrSex(String strSex) {
		this.strSex = strSex;
	}
	public String getStrNation() {
		return strNation;
	}
	public void setStrNation(String strNation) {
		this.strNation = strNation;
	}
	public String getStrBirth() {
		return strBirth;
	}
	public void setStrBirth(String strBirth) {
		this.strBirth = strBirth;
	}
	public String getStrAddr() {
		return strAddr;
	}
	public void setStrAddr(String strAddr) {
		this.strAddr = strAddr;
	}
	public String getStrIdCode() {
		return strIdCode;
	}
	public void setStrIdCode(String strIdCode) {
		this.strIdCode = strIdCode;
	}
	public String getStrIssue() {
		return strIssue;
	}
	public void setStrIssue(String strIssue) {
		this.strIssue = strIssue;
	}
	public String getStrBeginDate() {
		return strBeginDate;
	}
	public void setStrBeginDate(String strBeginDate) {
		this.strBeginDate = strBeginDate;
	}
	public String getStrEndDate() {
		return strEndDate;
	}
	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}
	public Bitmap getBitmapIdPhoto() {
		return bitmapIdPhoto;
	}
	public void setBitmapIdPhoto(Bitmap bitmapIdPhoto) {
		this.bitmapIdPhoto = bitmapIdPhoto;
	}
	
	public IDCardInfo(String strName, String strSex, String strNation,
                      String strBirth, String strAddr, String strIdCode, String strIssue,
                      String strBeginDate, String strEndDate, Bitmap bitmapIdPhoto) {
		
		this.strName = strName;
		this.strSex = strSex;
		this.strNation = strNation;
		this.strBirth = strBirth;
		this.strAddr = strAddr;
		this.strIdCode = strIdCode;
		this.strIssue = strIssue;
		this.strBeginDate = strBeginDate;
		this.strEndDate = strEndDate;
		this.bitmapIdPhoto = bitmapIdPhoto;
	}
	public IDCardInfo() {
		
	}
	@Override
	public String toString() {
		return "IDCardInfo [strName=" + strName + ", strSex=" + strSex
				+ ", strNation=" + strNation + ", strBirth=" + strBirth
				+ ", strAddr=" + strAddr + ", strIdCode=" + strIdCode
				+ ", strIssue=" + strIssue + ", strBeginDate=" + strBeginDate
				+ ", strEndDate=" + strEndDate + ", bitmapIdPhoto="
				+ bitmapIdPhoto + "]";
	}
	

}
