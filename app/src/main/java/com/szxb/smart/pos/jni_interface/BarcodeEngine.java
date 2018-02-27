package com.szxb.smart.pos.jni_interface;

import android.util.Log;

public class BarcodeEngine extends halTrans {
	
	static {
		try {
			System.loadLibrary("BarcodeEngine");
		} catch (Throwable e) {
			Log.e("jni", "i can't find BarcodeEngine so!");
			e.printStackTrace();
		}
	}
	
	
	public native static int BarcodeEngineOpen();

	public native static int BarcodeEngineClose();
	
	public native static String BarcodeEngineTrans(String cmd,int nTimeOutMs);

}
