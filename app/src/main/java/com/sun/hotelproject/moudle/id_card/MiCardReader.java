package com.sun.hotelproject.moudle.id_card;


import com.hxdevicepackage.hxgcJ20M1Card;
import com.hxdevicepackage.hxgcJ20Reader;

/**
�� J20������������������������硣
�� J20 �ػ��󣬲�����������硣
�� �������ϵ����µ綼��APP���ơ���APP�ϵ�����˳�δ�µ磬�������һֱ�����ϵ�
    ״̬��ֱ��J20�ػ���������
�� �������������µ紦��ʾ�����롣����APP������APP����ɼ������������ϵ磻APP
   ���治�ɼ������������µ磺
   һ���ڳ�������(OnCreate)�ͳ������ɼ�(OnStart)ʱ�ϵ硣
   һ���ڳ����˳�(OnDestroy)�ͳ�����治�ɼ�(OnStop)ʱ�µ硣
 * @author Administrator
 *
 */
public class MiCardReader {

	private hxgcJ20Reader m_oJ20Reader = null;
	
	private hxgcJ20M1Card m_oJ20M1Card = null;
	
	public MiCardReader(){
		
		m_oJ20Reader = new hxgcJ20Reader();
		m_oJ20M1Card = new hxgcJ20M1Card(m_oJ20Reader);
		
		
	}
	
	public void open(){
		
		m_oJ20Reader.openPort();
		
		//�ϵ�
		if(null != m_oJ20Reader)
		{
			m_oJ20Reader.powerOn();
		}
	}
	
	public void close(){
		
		m_oJ20Reader.closePort();
		m_oJ20Reader.powerOff();
	}
	
	
	//��ȡ����
	//cardType ������   key��Կ       sector����     block��
	public byte[] selectM1Card()
	{
		
		boolean bResult = true;
	
		byte[] bysCardType = new byte[1]; //������ 0x0A, 0x0B
		byte[] bysCardUid = new byte[4]; //��UID
		
//		byte[] bysKey = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
		
//		byte[] bysWrite = {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x00, 0x00, 0x00, 0x00,0x00, 0x00, 0x00, 0x00, 0x39};
//		byte[] bysRead = new byte[16];//��ȡ��������
//		
//		byte[] bysSW = {0x00, 0x00};//��֤��
		///////////////////////////////////////////////
		do
		{
			//
			//���¸�����˵���뿴hxgcJ20M1Card��Ĵ���ע��.
			//
			
			//���Ƭ
			bResult = m_oJ20M1Card.ActiveCard(bysCardType, bysCardUid);
			if(bResult)
			{
				break;
			}
			
			
			
		}while(!bResult);
			
		///////////////////////////////////////////////
		
		return bysCardUid;
	}
	
	
	
	//����
	//   key��Կ       sector����     block��
	public byte[] readM1Card(byte[] key,byte sector, byte block)
	{
		
		boolean bResult = true;
		
		byte[] bysCardType = new byte[1]; //������ 0x0A, 0x0B
		byte[] bysCardUid = new byte[4]; //��UID
		
//		byte[] bysKey = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
		
//		byte[] bysWrite = {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x00, 0x00, 0x00, 0x00,0x00, 0x00, 0x00, 0x00, 0x39};
		byte[] bysRead = new byte[16];//��ȡ��������
		
		byte[] bysSW = {0x00, 0x00};//��֤��
		///////////////////////////////////////////////
		do
		{
			//
			//���¸�����˵���뿴hxgcJ20M1Card��Ĵ���ע��.
			//
			
			//���Ƭ
			bResult = m_oJ20M1Card.ActiveCard(bysCardType, bysCardUid);
			if(!bResult)
			{
				break;
			}
			
			//������֤
			bResult = m_oJ20M1Card.AuthCard(sector, (byte)0x60, key, bysSW);
			if(!bResult)
			{
				break;
			}
			
		
			
			//���� - ÿ�ζ�һ��, 16�ֽ�
			bResult = m_oJ20M1Card.ReadBlock(sector, block, bysRead, bysSW);
			if(!bResult)
			{
				break;
			}
			
		}while(false);
			
		///////////////////////////////////////////////
		
		return bysRead;
	}
	
	//д�� 
	//   key��Կ       sector����     block��  bysWriteд��
		public boolean WriteM1Card(byte[] key,byte sector, byte block,byte[] bysWrite)
		{
			
			boolean bResult = true;
			
			byte[] bysCardType = new byte[1]; //������ 0x0A, 0x0B
			byte[] bysCardUid = new byte[4]; //��UID
			
//			byte[] bysKey = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
			
//			byte[] bysWrite = {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x00, 0x00, 0x00, 0x00,0x00, 0x00, 0x00, 0x00, 0x39};
//			byte[] bysRead = new byte[16];
			
			byte[] bysSW = {0x00, 0x00};
			
			///////////////////////////////////////////////
			do
			{
				//
				//���¸�����˵���뿴hxgcJ20M1Card��Ĵ���ע��.
				//
				
				//���Ƭ
				bResult = m_oJ20M1Card.ActiveCard(bysCardType, bysCardUid);
				if(!bResult)
				{
					break;
				}
				
				//������֤
				bResult = m_oJ20M1Card.AuthCard(sector, (byte)0x60, key, bysSW);
				if(!bResult)
				{
					break;
				}
				
				//д�� - ÿ��дһ��,16�ֽ�
				bResult = m_oJ20M1Card.WriteBlock(sector, block, bysWrite, bysSW);
				if(!bResult)
				{
					break;
				}
				
			
				
			}while(false);
				
			///////////////////////////////////////////////
			
			return true;
		}
}
