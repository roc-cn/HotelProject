
package com.sun.hotelproject.moudle.id_card;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;


public class ReadIDThread extends Thread 
{

	protected SerialPortUtils m_Application = null;
	protected SerialPort m_SerialPort = null;
	protected InputStream m_InputStream = null;
	protected OutputStream m_OutputStream = null;;
	
	protected int m_iRecvSize = 0;
	protected int m_iRecvOffset = 0;
	
	protected int m_iRecvBufSize = 1024*3;
	protected byte m_bysRecvBuffer[] = new byte[m_iRecvBufSize];
	
	protected int m_iCmdSize = 0;
	protected byte m_bysCmd[] = null;
	
	protected ReadIdCallback m_readCallback = null;
	
	protected int m_iWorkMode = 0; //-1: �˳��߳�; -2: ���˳�; 0: ������; 1: ���ζ����֤; 2: ��������֤; 3: ֹͣ��������֤.
	protected int m_iWorkModeOld = 0; 
	
	protected boolean m_bIsPowerOn = false;

	public byte[] ReadCardUID(){
//	String path = "/dev/ttyS3";
//	int baudrate = 115200;
        m_Application=new SerialPortUtils();
        boolean bIsOK = false;
		
	boolean s=  initDevice();

	System.out.println(s);//  sp.openSerialPort();
		int i = 0;
		byte bysRecv02Data03[] = null;
		byte bysRecvUnPackage[] = null;
		byte bysCardUID[] = new byte[8];
		byte bysCmdFind[] = {(byte)0xAA, (byte)0xAA, (byte)0xAA, (byte)0x96, 0x69, 0x00, 0x03, 0x20, 0x01, 0x22};
		try 
		{
			m_OutputStream.write(bysCmdFind);
			m_OutputStream.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			
		}
		
		
		///////////////////////////////////////////////////////////////////////
		//给非接卡上电
		byte bysCmdCardPowerOn[] = {0x02, 0x30, 0x30, 0x30, 0x34, 0x33, 0x32, 0x32, 0x34, 0x30, 0x30, 0x30, 0x30, 0x31, 0x36, 0x03};
		try 
		{
			m_OutputStream.write(bysCmdCardPowerOn);
			m_OutputStream.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
		bysRecv02Data03 = Recv02Data03();
		
		///////////////////////////////////////////////////////////////////////
		//发送指令
		byte bysCmdReadCardUID[] = {0x02, 0x30, 0x30, 0x30, 0x38, 0x33, 0x32, 0x32, 0x36, 0x3f, 0x3f, 0x30, 0x30, 0x33, 0x36, 0x30, 0x30, 0x30, 0x30, 0x30, 0x38, 0x3d, 0x35, 0x03};//@在此填入指令
		
		try 
		{
			m_OutputStream.write(bysCmdReadCardUID);
			m_OutputStream.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		bysRecv02Data03 = Recv02Data03();
		if(null == bysRecv02Data03)
		{
			System.out.println("没有了返回值");
			return null;
		}
		bysRecvUnPackage = UnPackage(bysRecv02Data03, 1, bysRecv02Data03.length-2);
		if(bysRecvUnPackage.length < 12)
		{
			System.out.println("没有了返回值");
			return null;
		}
		System.out.println("有了返回值");
		byte bySW1 = bysRecvUnPackage[bysRecvUnPackage.length-3];
		byte bySW2 = bysRecvUnPackage[bysRecvUnPackage.length-2];
		if((byte)0x90 != bySW1 || 0x00 != bySW2)
		{
			return null;
		}
		for(i=0; i<8; i++)
		{
			bysCardUID[i] = bysRecvUnPackage[4+i];
		}
		//r.closePort();
		return bysCardUID;
		
	} //~ReadIDCode2()
	
	protected byte[] Recv02Data03()
	{
		boolean bIsReadOK = false;
		
		int i = 0;
		int iCanReadSize = 0;
		int iReadSize = 0;
		int iRecvSize = 0;
		byte buffer[] = new byte[1024];
		byte bysRecv[] = null;
		
		long lWaitToatl = 1000;
		long lWaitStep = 50;
		long lCurWait = 0;
		
		try 
		{
			while(true)
			{
				iCanReadSize = m_InputStream.available();
				if(iCanReadSize > 0)
				{
					iReadSize = m_InputStream.read(buffer);
					iRecvSize += iReadSize; 
					if(0x03 == buffer[iReadSize-1])
					{
						bIsReadOK = true;
						break;
					}
				}
				else
				{
					if(lCurWait >= lWaitToatl)
					{
						break;
					}
					MySleep(lWaitStep);
					lCurWait += lWaitStep;
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if(!bIsReadOK)
		{
			return null;
		}
		
		bysRecv = new byte[iRecvSize];
		for(i=0; i<iRecvSize; i++)
		{
			bysRecv[i] = buffer[i];
		}
		
		return bysRecv;
		
	} //~Recv02Data03()
	
	protected byte[] UnPackage(byte[] _i_bys_resp, int _i_i_offset, int _i_i_len)
	{
		int iLen = _i_i_len/2;
		byte[] bysRet = new byte[iLen];
		
		int i = 0;
		int j = _i_i_offset;

		byte byT = 0;
		byte byH = 0;
		byte byL = 0;
		
		for(i=0; i<bysRet.length; i++)
		{
			byT = (byte)(_i_bys_resp[j] -0x30);
			byH = (byte)(byT << 4);
			j++;
			
			byL = (byte)(_i_bys_resp[j] -0x30);
			j++;
			
			bysRet[i] = (byte) (byH+byL); 
		}
		
		return bysRet;
		
	} //~UnPackage
	@Override
	public void run()
	{
		super.run();
		
		initDevice();
		
		//while(!isInterrupted())
		while(true)
		{
			if(-1 == m_iWorkMode)
			{
				break;
			}
			
			switch(m_iWorkMode)
			{
			case 0: //������
				MySleep(50);
				break;
			case 1: //���ζ����֤
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ReadID(m_iWorkMode);
				m_iWorkMode = 0;
				//if(2 == m_iWorkModeOld)
				//{
				//	m_iWorkMode = 2; //��֮ǰ�������δͣ, �����ת�����������.
				//}
				//else
				//{
				//	m_iWorkMode = 0;
				//}
				break;
			case 2: //��������֤
				ReadID(m_iWorkMode);
				MySleep(100);
				break;
			case 3: //ֹͣ��������֤
				m_iWorkMode = 0;
				break;
			case 12:
				readSamID();
				m_iWorkMode = 0;
				break;
			default:
				m_iWorkMode = 0;
				break;
			};
		}
		
		unInitDevice();
		
		m_iWorkMode = -2;
		
	} //public void run()
	
	public void setSerialPort(SerialPortUtils a)
	{
		m_Application = a;
	}
	
	public void setCallback(final ReadIdCallback a)
	{
		m_readCallback = a;
	}
	
	public void setWorkMode(final int iWorkMode)
	{
		m_iWorkModeOld = m_iWorkMode; 
		m_iWorkMode = iWorkMode;
	}
	
	public int getWorkMode()
	{
		return m_iWorkMode; 
	}
	
	public int inputCmd(final byte[] _bysCmd)
	{
		if(0 != m_iCmdSize)
		{
			return 1; //��һ��ָ��δִ����
		}
		
		if(0 == _bysCmd.length)
		{
			return 2; //�����˿�ָ��
		}
		
		m_iCmdSize = _bysCmd.length;
		m_bysCmd = _bysCmd.clone();
		
		return 0;
	}
	
	//��ʱ
	public void MySleep(long time)
	{
		try
		{
		    sleep(time);
		}
		catch (InterruptedException e)
        {
            e.printStackTrace(); 
        }
		
		return;
	}
	
	

	public static String byte2HexStr(byte[] b) {  
	    String hs = "";  
	    String stmp = "";  
	    for (int n = 0; n < b.length; n++) {  
	        stmp = (Integer.toHexString(b[n] & 0XFF));  
	        if (stmp.length() == 1)  
	            hs = hs + "0" + stmp;  
	        else  
	            hs = hs + stmp;  
	        // if (n<b.length-1) hs=hs+":";  
	    }  
	    return hs.toUpperCase();  
	}  
	//�����֤
	protected void ReadID(int iMode)
	{
		boolean bIsOK = false;

		/////////////////////////////////////////////////////////////////////
		//Ѱ��
		//����ָ��
		byte bysCmdFind[] = {(byte)0xAA, (byte)0xAA, (byte)0xAA, (byte)0x96, 0x69, 0x00, 0x03, 0x20, 0x01, 0x22};
		try 
		{
			m_OutputStream.write(bysCmdFind);

			m_OutputStream.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		//��ȡӦ��
		getIDWholeResp(5000, 100); //�п��ܻ�ȴ��ϵ����, �����������. 
		bIsOK = onFindIdResp();
		if(null != m_readCallback)
		{
			m_readCallback.onReadIdComplete(iMode, 1, m_bysRecvBuffer, m_iRecvSize);
	
		}
		if(!bIsOK)
		{
			return;
		}
		
        /////////////////////////////////////////////////////////////////////
        //ѡ��
		byte bysCmdSelect[] = {(byte)0xAA, (byte)0xAA,  (byte)0xAA, (byte)0x96, 0x69, 0x00, 0x03, 0x20, 0x02, 0x21};
		//����ָ��
		try 
		{
			m_OutputStream.write(bysCmdSelect);
	
			m_OutputStream.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		//��ȡӦ��
		getIDWholeResp(200, 100);
		bIsOK = onSelectIdResp();
		if(null != m_readCallback)
		{  
			m_readCallback.onReadIdComplete(iMode, 2, m_bysRecvBuffer, m_iRecvSize);
	
			System.out.println(m_bysRecvBuffer);
			System.out.println(byte2HexStr(m_bysRecvBuffer));
		}
		if(!bIsOK)
		{
			return;
		} 
		 
        /////////////////////////////////////////////////////////////////////		
		//����
		byte bysCmdRead[] = {(byte)0xAA, (byte)0xAA, (byte)0xAA, (byte)0x96, 0x69, 0x00, 0x03, 0x30, 0x10, 0x23};
		//����ָ��
		try 
		{
			m_OutputStream.write(bysCmdRead);
	
			m_OutputStream.flush();
		
		} 
		catch (IOException e) 
		{
			
			e.printStackTrace();
			return;
		}
		//��ȡӦ��
		getIDWholeResp(1000, 100);
	
		//����Ӧ��
		if(null != m_readCallback)
		{
			m_readCallback.onReadIdComplete(iMode, 3, m_bysRecvBuffer, m_iRecvSize);
			System.out.println(m_bysRecvBuffer);
			System.out.println(byte2HexStr(m_bysRecvBuffer));
		}
		
		return;
	}
	
	//��ʼ��������
	protected boolean initDevice()
	{
		powerOn();
		
		m_SerialPort = m_Application.openSerialPort();
		if(null == m_SerialPort)
		{
			return false;
		}
		
		m_InputStream = m_SerialPort.getInputStream();
		m_OutputStream = m_SerialPort.getOutputStream();
		
		return true;
	}
	
	//����ʼ���豸
	protected void unInitDevice()
	{
		m_Application.closeSerialPort();
		m_Application = null;
		m_SerialPort = null;
		m_OutputStream = null;
		
		powerOff();
	}
	
	//�ϵ�
	protected int powerOn()
	{
		if(m_bIsPowerOn)
		{
			return 0;
		}
		
		int iResult = -1;
		//String strErrtxt = null;
		String[] cmdx = new String[]{ "/system/bin/sh", "-c", "echo 1 > sys/HxReaderID_apk/hxreaderid" };
		
		try
		{
			iResult = ShellExe.execCommand(cmdx);
            //strErrtxt = ShellExe.getOutput();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			//strErrtxt = "ERR.JE";
		}
		
		if(0 == iResult)
		{
			m_bIsPowerOn = true;
		}
		
		return iResult;
	}
	
	//�µ�
	protected int powerOff()
	{
		if(!m_bIsPowerOn)
		{
			return 0;
		}
		
		int iResult = -1;
		//String strErrtxt = null;
		String[] cmdx = new String[]{ "/system/bin/sh", "-c", "echo 0 > sys/HxReaderID_apk/hxreaderid" };
		
		try
		{
			iResult = ShellExe.execCommand(cmdx);
            //strErrtxt = ShellExe.getOutput();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			//strErrtxt = "ERR.JE";
		}
		
		m_bIsPowerOn = false;
		
		return iResult;
	}
	
	protected void getIDWholeResp(final long lWaitTotal,  final long lWaitStep)
	{
		//������ݴ����ʽ
		//-������봫��֡��ʽ
		//Preamble | Len1 | Len2 | CMD | Para | Data | CHK_SUM
		//-����������֡��ʽ
		//Preamble | Len1|Len2 | SW1 | SW2 | SW3 | Data |CHK_SUM
		//���ֶκ���
		//1. Preamble: ��֡��ݵ�֡ͷ. 5�ֽ�, 0xAA, 0xAA, 0xAA, 0x96, 0x69
		//2. Len1, Len2: ���֡����Ч��ݳ���, ��Ϊ1�ֽ�. Len1Ϊ��ݳ��ȸ��ֽ�; Len2Ϊ���ֽ�.
		//   ������ݳ���Ϊ: CMD, para, Data, CHK_SUM�ֶ��ֽ���֮��.
		//   �����ݳ���Ϊ:SW1, SW2, SW3, Data, CHK_SUM�ֶ��ֽ���֮�͡�
		//3. CHK_SUM: У���, 1�ֽ�.
		//   ���֡�г�֡ͷ��У���֮���������ֽڰ�λ���Ľ��.
		
		int i = 0;
		byte[] buffer = new byte[1024];
		int size = 0;
		
		int iReadTimes = 0;
		int iLen1Len2 = 0;
		
		long lWait = 0;
		
		int iCanReadSize = 0;
		
		boolean bIsDataExits = true;
		
		try 
		{
			while(true) //1
			{
				while(true) //2
				{
					iCanReadSize = m_InputStream.available();
					if(iCanReadSize > 0)
					{
						break;
					}

					//����ݳ�ʱ, ���.
					if(lWait > lWaitTotal)
					{
						bIsDataExits = false;
						break;
					}
					MySleep(lWaitStep);
					lWait += lWaitStep;
				} //while(true) //2
				
				if(!bIsDataExits)
				{
					break;
				}
				
				size = m_InputStream.read(buffer);
				if(size <= 0)
				{
					continue;
				}
				else
				{
					iReadTimes ++;
				}
				
				if(1 == iReadTimes)
				{
					m_iRecvSize = 0;
					m_iRecvOffset = 0;			
                }
				
				for(i=0; i<size; i++)
				{
					m_bysRecvBuffer[m_iRecvOffset+i] = buffer[i];
				}
				m_iRecvOffset += size;
				m_iRecvSize = m_iRecvOffset;
			
				//��ȡӦ�𳤶�
				if((0 == iLen1Len2) && (m_iRecvSize >= 7))
				{
					iLen1Len2 = m_bysRecvBuffer[5] << 8;
					iLen1Len2 += m_bysRecvBuffer[6];
				}
				
				//����ݶ���, ���.
				if((0 != iLen1Len2) && (m_iRecvSize >= (iLen1Len2+7)))
				{
						//iCanReadSize = mInputStream.available();
						//if(0 == iCanReadSize)
						//{
						//	break;
						//}
					    break;
				}
				
			} //while(true) //1
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return;
	}
	
	//�������֤Ѱ��Ӧ��
	protected boolean onFindIdResp()
	{
	    //������ݴ����ʽ
		//-������봫��֡��ʽ
		//Preamble | Len1 | Len2 | CMD | Para | Data | CHK_SUM
		//-����������֡��ʽ
		//Preamble | Len1|Len2 | SW1 | SW2 | SW3 | Data |CHK_SUM
		//���ֶκ���
		//1. Preamble: ��֡��ݵ�֡ͷ. 5�ֽ�, 0xAA, 0xAA, 0xAA, 0x96, 0x69
		//2. Len1, Len2: ���֡����Ч��ݳ���, ��Ϊ1�ֽ�. Len1Ϊ��ݳ��ȸ��ֽ�; Len2Ϊ���ֽ�.
		//   ������ݳ���Ϊ: CMD, para, Data, CHK_SUM�ֶ��ֽ���֮��.
		//   �����ݳ���Ϊ:SW1, SW2, SW3, Data, CHK_SUM�ֶ��ֽ���֮�͡�
		//3. CHK_SUM: У���, 1�ֽ�.
		//   ���֡�г�֡ͷ��У���֮���������ֽڰ�λ���Ľ��.
		
		byte SW1 = m_bysRecvBuffer[7];
		byte SW2 = m_bysRecvBuffer[8];
		byte SW3 = m_bysRecvBuffer[9];
		
		if( (0x0 != SW1)  ||  (0x0 != SW2) ||  ( ((byte)0x9F) != SW3) )
		{
			return false;
		}
		
		return true;	
	}
	
	//�������֤ѡ��Ӧ��
	protected boolean onSelectIdResp()
	{
		//������ݴ����ʽ
		//-������봫��֡��ʽ
		//Preamble | Len1 | Len2 | CMD | Para | Data | CHK_SUM
		//-����������֡��ʽ
		//Preamble | Len1|Len2 | SW1 | SW2 | SW3 | Data |CHK_SUM
		//���ֶκ���
		//1. Preamble: ��֡��ݵ�֡ͷ. 5�ֽ�, 0xAA, 0xAA, 0xAA, 0x96, 0x69
		//2. Len1, Len2: ���֡����Ч��ݳ���, ��Ϊ1�ֽ�. Len1Ϊ��ݳ��ȸ��ֽ�; Len2Ϊ���ֽ�.
		//   ������ݳ���Ϊ: CMD, para, Data, CHK_SUM�ֶ��ֽ���֮��.
		//   �����ݳ���Ϊ:SW1, SW2, SW3, Data, CHK_SUM�ֶ��ֽ���֮�͡�
		//3. CHK_SUM: У���, 1�ֽ�.
		//   ���֡�г�֡ͷ��У���֮���������ֽڰ�λ���Ľ��.
		
		byte SW1 = m_bysRecvBuffer[7];
		byte SW2 = m_bysRecvBuffer[8];
		byte SW3 = m_bysRecvBuffer[9];
		
		if( (0x0 != SW1)  ||  (0x0 != SW2) ||  ( ((byte)0x90) != SW3) )
		{
			return false;
		}
		
		return true;
	}
	
	protected void getCmdWholeResp(final long lWaitTotal,  final long lWaitStep)
	{
		int i = 0;
		byte[] buffer = new byte[1024];
		int size = 0;
		
		//int iReadTimes = 0;
		
		long lWait = 0;
		
		int iCanReadSize = 0;
		
		boolean bIsDataExits = true;
		
		try 
		{
			m_iRecvSize = 0;
			m_iRecvOffset = 0;
			
			while(true) //1
			{
				while(true) //2
				{
					iCanReadSize = m_InputStream.available();
					if(iCanReadSize > 0)
					{
						break;
					}

					//����ݳ�ʱ, ���.
					if(lWait > lWaitTotal)
					{
						//m_iCmdSize = 0;
						bIsDataExits = false;
						break;
					}
					MySleep(lWaitStep);
					lWait += lWaitStep;
				} //while(true) //2
				
				if(!bIsDataExits)
				{
					break;
				}
				
				size = m_InputStream.read(buffer);
				if(size <= 0)
				{
					continue;
				}
				//else
				//{
				//	iReadTimes ++;
				//}
				
				//if(1 == iReadTimes)
				//{
				//	m_iRecvSize = 0;
				//	m_iRecvOffset = 0;			
                //}
				
				for(i=0; i<size; i++)
				{
					m_bysRecvBuffer[m_iRecvOffset+i] = buffer[i];
				}
				m_iRecvOffset += size;
				m_iRecvSize = m_iRecvOffset;
				
			} //while(true) //1
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return;
	}
	
	//ȥȥ��ȫģ���
	protected void readSamID()
	{
		byte byscmd[] = {(byte)0xAA, (byte)0xAA, (byte)0xAA, (byte)0x96, 0x69, 0x00, 0x03, 0x12, (byte)0xFF, (byte)0xEE};
		
		try 
		{
			m_OutputStream.write(byscmd);
			m_OutputStream.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		
		getCmdWholeResp(300,  100);
		m_readCallback.onReadIdComplete(12, 1, m_bysRecvBuffer, m_iRecvSize);
		m_iCmdSize = 0;
	}
	
} //public class ReadIDThread extends Thread
