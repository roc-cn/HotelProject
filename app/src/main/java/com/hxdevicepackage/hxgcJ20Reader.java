package com.hxdevicepackage;


import com.sun.hotelproject.moudle.id_card.ShellExe;
import com.szxb.smart.pos.jni_interface.Com4052;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;


public class hxgcJ20Reader
{
	static private SerialPort m_oSerialPort = null;
	static private OutputStream m_oOutputStream = null;
	static private InputStream m_oInputStream = null;
	static private int fd;
	//�򿪴���
	//public boolean openPort(String _i_str_port)  
	public boolean openPort()
	{
		boolean bResult = true;
		
		do
		{	
			fd = Com4052.Com4052Open();
			Com4052.Com4052Control(fd, 1);
			if (null == m_oSerialPort) 
			{
				//String path = "/dev/ttyS3";
				String path = "/dev/ttyS1";
				//String path =  _i_str_port;
				int baudrate = 115200;
	
				try 
				{
					m_oSerialPort = new SerialPort(new File(path), baudrate, 0);
				} 
				catch (SecurityException e) 
				{
					e.printStackTrace();
					bResult = false;
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
					bResult = false;
				}
			}
			
			
			if(!bResult)
			{
				break;
			}
			
			if (null == m_oSerialPort)
			{
				bResult = false;
				break;
			}
			
			m_oOutputStream = m_oSerialPort.getOutputStream();
			if(null == m_oOutputStream)
			{
				bResult = false;
				break;
			}
			m_oInputStream = m_oSerialPort.getInputStream();
			if(null == m_oInputStream)
			{
				bResult = false;
				break;
			}
		
		}while(false);
		
		if(!bResult)
		{
			closePort();
		}
		
		return bResult;
	}
	
	//�رմ���
	public void closePort() 
	{
		if(null != m_oOutputStream)
		{
			try 
			{
				m_oOutputStream.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			m_oOutputStream = null;
		}
		
		if(null != m_oInputStream)
		{
			try 
			{
				m_oInputStream.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			m_oInputStream = null;
		}
		
		if (null != m_oSerialPort) 
		{
			m_oSerialPort.close();
			m_oSerialPort = null;
		}
		Com4052.Com4052Close(fd);
		System.out.println("Com4052Close");
	}
	
	//�ϵ�
	public boolean powerOn()
	{
		int iResult = 0;
		String[] cmdx = new String[]{ "/system/bin/sh", "-c", "echo 1 > sys/HxReaderID_apk/hxreaderid" };
		
		try 
		{
			iResult = ShellExe.execCommand(cmdx);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			
			return false;
		}
		
		if(0 != iResult)
		{
			return false;
		}
		
		return true;
	}
	
	//�µ�
	public boolean powerOff()
	{
		int iResult = 0;
		
		String[] cmdx = new String[]{ "/system/bin/sh", "-c", "echo 0 > sys/HxReaderID_apk/hxreaderid" };
		
		try
		{
			iResult = ShellExe.execCommand(cmdx);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			
			return false;
		}
		
		if(0 != iResult)
		{
			return false;
		}
		
		return true;
	}
	
	public boolean write(byte[] _i_bys_send)
	{
		if(null == m_oOutputStream)
		{
			return false;
		}
		
		if(null == _i_bys_send)
		{
			return false;
		}
		
		boolean bResult = true;
		
		do
		{
			try 
			{
				m_oOutputStream.write(_i_bys_send);
				m_oOutputStream.flush();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				
				bResult = false;
			}
			
		}while(false);
		
		return bResult;
	}
	
	public int readAvailable()
	{
		int iCount = 0;
		
		try 
		{
			iCount = m_oInputStream.available();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			
			iCount = -1;
		}
		return iCount;
	}
	
	public byte[] read(long _i_l_waitMillis, long _i_l_intervalMillis)
	{
		byte[] bysBuf = null;
		int iReadNum = 0;
		long lCurMillis = 0;

		boolean bIsWait = true;
		
		if(_i_l_waitMillis <=0 || _i_l_intervalMillis <= 0)
		{
			bIsWait = false;
		}
		
		try 
		{
			if(bIsWait)
			{
				while(iReadNum <= 0)
				{
					iReadNum = m_oInputStream.available();
					Thread.sleep(_i_l_intervalMillis);
					lCurMillis += _i_l_intervalMillis;
					if(lCurMillis >= _i_l_waitMillis)
					{
						break;
					}
				}
			}
			else
			{
				iReadNum = m_oInputStream.available();
			}
			
			if(iReadNum > 0)
			{
				bysBuf = new byte[iReadNum];
				if(null != bysBuf)
				{
					iReadNum = m_oInputStream.read(bysBuf);
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		return bysBuf;
	}
}



