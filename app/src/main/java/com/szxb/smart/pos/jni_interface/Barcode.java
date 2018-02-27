package com.szxb.smart.pos.jni_interface;

public class Barcode extends SeriesCom{

	String dev = "/dev/ttyS1";
	public static int Handle;
	public static int fd;
	
	public int BarcodeOpen()
	{
		fd = Com4052.Com4052Open();
		if(fd < 0)
		{
			
			return fd;
		}
//		Com4052.Com4052Control(fd, 11);
		Com4052.Com4052Control(fd, 0);
		if(fd < 0)
		{
			return fd;
		}
		Handle=SeriesComOpen(dev,115200);
		return Handle;
	}
	
	public int BarcodeClose()
	{
		int ret;
		
		ret = SeriesComClose(Handle);
		if(ret < 0)
		{
			
			System.out.println("Barcode ComClose failed\n");
			return ret;
		}
		ret = Com4052.Com4052Close(fd);
		if(ret < 0)
		{
			
			System.out.println("Barcode Com4052Close failed\n");
		}
		return ret;
	}
	
	
	//public int BarcodeTrans(byte[]sendbyte, byte recvbyte[], int length)
	public String BarcodeTrans(byte[]sendbyte, int length, int nTimeOutMs)
	{
		String res;
//		recvbyte = SeriesComTrans(Handle, sendbyte, length, 10000);
		//byte[] recvbyte = new byte[256];
		byte[] recvbyte = SeriesComTrans(Handle, sendbyte, length, nTimeOutMs);
		
		if(recvbyte != null)
		{
			System.out.println(recvbyte.length);
			for(int i = 0; i < recvbyte.length; i++)
			{
				if(recvbyte[i] != 0)
				System.out.println(recvbyte[i]);
			}
			res = new String(recvbyte);
		}
		else
		 res = ""; 
		System.out.println(res);
		return res;
	}
}
