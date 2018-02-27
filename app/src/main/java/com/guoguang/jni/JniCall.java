package com.guoguang.jni;

/**
 * 本类必须是 com.guoguang.jni 的目录结构.
 * 且文件名和类名不能改.
 */
public class JniCall
{
	private static native int wlt2bmp(byte[] wlt, byte[] bmp, int bmpSave);
	
	static 
	{
		try
		{
		    System.loadLibrary("dewlt2-jni");
		}
		catch(Throwable ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static int hxgc_Wlt2Bmp(byte[] wlt, byte[] bmp, int bmpSave) 
	{
		return wlt2bmp(wlt, bmp, bmpSave);
	}
}

/*
public class JniCall
{
	private static native int unpack(char[] wlt, char[] bmp, int bmpSave);
	
	static 
	{
		try
		{
		    System.loadLibrary("wlt2bmp");
		}
		catch(Throwable ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static int hxgc_unpack(char[] wlt, char[] bmp, int bmpSave)
	{
		return unpack(wlt, bmp, bmpSave);
	}
}
*/