package com.szxb.smart.pos.jni_interface;

public class CameraIO {
	static {
		try{
			System.loadLibrary("CameraIO");
		}catch(Throwable e){
			
		}
	}
	
	public native static int CameraIOOpen();
	public native static int CameraIOControl(int handle, int cmd, long arg);
	public native static int CameraIOClose(int handle);
}
