package com.szxb.smart.pos.jni_interface;

import android.util.Log;

public class Com4052 {
	private static final String LOG_TAG = "Com4052";
	static {
		try{
			System.loadLibrary("Com4052");
		}
		catch(Throwable e){
			Log.e(LOG_TAG, "Com4052 library not found!");
		}
	}
	
	public native static int Com4052Open();
	public native static int Com4052Control(int handle, int cmd);
	public native static int Com4052Close(int handle);
	
}
