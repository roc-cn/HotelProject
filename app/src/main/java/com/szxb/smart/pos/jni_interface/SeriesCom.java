package com.szxb.smart.pos.jni_interface;

import android.util.Log;

public class SeriesCom extends halTrans {
	static {
		try {
			System.loadLibrary("SeriesCom");
		} catch (Throwable e) {
			Log.e("jni", "i can't find SeriesCom so!");
			e.printStackTrace();
		}
	}
	
	protected  native int SeriesComOpen(String devName,int baud);			
	protected  native int SeriesComClose(int handle);			
	protected  native byte[] SeriesComTrans(int handle,byte[] sendData,int len,int nTimeOut);
	
}
