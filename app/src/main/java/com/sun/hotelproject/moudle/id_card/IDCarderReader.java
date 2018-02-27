
package com.sun.hotelproject.moudle.id_card;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

import com.guoguang.jni.JniCall;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class IDCarderReader
{
	private final static String TAG = "IDCarderReader";

	private ReadIDThread readerThread;
	
	private ExecutorService excutorService = Executors.newCachedThreadPool();
	
	private IDCardReaderCallBack cardReaderCallBack;
	
	/**
	 * 
	 * @param callBack 
	 * @param iMode����ģʽ��1���ζ�����2���������3ֹͣ�������
	 */
	public void startReaderIDCard(final IDCardReaderCallBack callBack,final int iMode){
		
		this.cardReaderCallBack =  callBack;
		
		readerThread = new ReadIDThread();
		readerThread.setWorkMode(iMode);
		readerThread.setSerialPort(SerialPortUtils.getInstance());
		readerThread.setCallback(new ReadIdCallback(){

			@Override
			public void onReadIdComplete(int iMode, int iCmdType,
					byte[] bysReadResp, int iSize) {
			
				if(iCmdType==3){
					try {
						System.out.println(new String(bysReadResp,"GB2312"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(1);
				handlerReadMessage(iMode,iCmdType,bysReadResp,iSize);
			}
		});
		
		excutorService.execute(readerThread);
	}
	
	//��ʾӦ����Ϣ
	private void handlerReadMessage(final int  iMode, final int iCmdType, final byte[] bysReadResp, final int iSize)
	{
		if(1==iMode || 2 == iMode) //���֤ģʽ
		{
			if (1== iCmdType) //Ѱ��
			{
//				onFindIdResp(iMode, iCmdType, bysReadResp, iSize);
			}
			else if (2== iCmdType) //ѡ��
			{
//				onSelectIdResp(iMode, iCmdType, bysReadResp, iSize);
			}
			else if (3== iCmdType) //����
			{
				if(cardReaderCallBack != null){
					
					IDCardInfo cardInfo = null;
					try {
						cardInfo = this.readCardInfo(iMode, iCmdType, bysReadResp, iSize);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cardReaderCallBack.onReadIdComplete(iMode, cardInfo);
				}
			}
		}
	}
	//��ʾӦ����Ϣ
	private IDCardInfo readCardInfo(final int  iMode, final int iCmdType, final byte[] bysReadResp, final int iSize) throws UnsupportedEncodingException
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
		
		IDCardInfo cardInfo = new IDCardInfo(); 
		
		byte SW1 = bysReadResp[7];
		byte SW2 = bysReadResp[8];
		byte SW3 = bysReadResp[9];
		
		if(iSize<1024)
		{
			if(2 == iMode)
			{
				return null;
			}
			
			if(iSize > 0)
			{
				Log.i(TAG,"��ݴ�С����.\n\n");
			}
			else
			{
				Log.i(TAG,"���Ϊ��.\n\n");
			}
			
			return null;
		}
		
		if( (0x0 != SW1)  ||  (0x0 != SW2) ||  ( ((byte)0x90) != SW3) )
		{
			if(2 == iMode)
			{
				return null;
			}
			Log.i(TAG,"״̬�벻��.\n\n");
			return null;
		}
		
		/*
		////////////////////////////////////////////////
		//����
		ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);
		//toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
		toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK );
		toneGenerator.stopTone(); //û����һ�У���������.
		toneGenerator.release();
		*/
		
		///////////////////////////////////////////////
		
		int i = 0;
		int j = 0;
		
		int iLen1Len2 = 0;
		int iOffset = 16;
		int iTextSize = 0;
		int iPhotoSize = 0;
		int iFingerSize = 0;
					
		byte bysName[] = new byte[30];
		byte bysSexCode[] = new byte[2];
		byte bysNationCode[] = new byte[4];
		byte bysBirth[] = new byte[16];
		byte bysAddr[] = new byte[70];
		byte bysIdCode[] = new byte[36];
		byte bysIssue[] = new byte[30];
		byte bysBeginDate[] = new byte[16];
		byte bysEndDate[] = new byte[16];
		
		iLen1Len2 = bysReadResp[5] << 8;
		iLen1Len2 += bysReadResp[6];
		
		iTextSize = bysReadResp[10] << 8 + bysReadResp[11];
		iPhotoSize = bysReadResp[12] << 8 + bysReadResp[13];
		iFingerSize = bysReadResp[14] << 8 + bysReadResp[15];
		
		//��ȡ����
		j = 0;
		for(i=iOffset; i<(iOffset+30); i++)
		{
			bysName[j] = bysReadResp[i];
			j++;
		}
		
		
		try 
		{
			cardInfo.setStrName(new String(bysName, "UTF-16LE"));
		} 
		catch(UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		
		iOffset += 30;
		
		//��ȡ�Ա����
		j = 0;
		for(i=iOffset; i<(iOffset+2); i++)
		{
			bysSexCode[j] = bysReadResp[i];
			j++;
		}
		try {
			cardInfo.setStrSex(new String(bysSexCode, "UTF-16LE"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		iOffset += 2;
		
		//��ȡ�������
		j = 0;
		for(i=iOffset; i<(iOffset+4); i++)
		{
			bysNationCode[j] = bysReadResp[i];
			j++;
		}
		try {
			cardInfo.setStrNation(new String(bysNationCode, "UTF-16LE"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		iOffset += 4;
		
		//��ȡ����
		j = 0;
		for(i=iOffset; i<(iOffset+16); i++)
		{
			bysBirth[j] = bysReadResp[i];
			j++;
		}
		cardInfo.setStrBirth(new String(bysBirth,"UTF-16LE"));
		
		iOffset += 16;
		//��ȡ��ַ
		j = 0;
		for(i=iOffset; i<(iOffset+70); i++)
		{
			bysAddr[j] = bysReadResp[i];
			j++;
		}
		try 
		{
			cardInfo.setStrAddr(new String(bysAddr, "UTF-16LE"));
		} 
		catch(UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		
		iOffset += 70;
		
		//��ȡ���֤��
		j = 0;
		for(i=iOffset; i<(iOffset+36); i++)
		{
			bysIdCode[j] = bysReadResp[i];
			j++;
		}
		
		try {
			cardInfo.setStrIdCode(new String(bysIdCode,"UTF-16LE"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		iOffset += 36;
		
		//��ȡǩ�����
		j = 0;
		for(i=iOffset; i<(iOffset+30); i++)
		{
			bysIssue[j] = bysReadResp[i];
			j++;
		}
		
		try 
		{
			cardInfo.setStrIssue(new String(bysIssue, "UTF-16LE"));
		} 
		catch(UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		iOffset += 30;
		
		//��ȡ��Ч�ڿ�ʼ����
		j = 0;
		for(i=iOffset; i<(iOffset+16); i++)
		{
			bysBeginDate[j] = bysReadResp[i];
			j++;
		}
		cardInfo.setStrBeginDate(new String(bysBeginDate,"UTF-16LE"));
		
		iOffset += 16;
		
		//��ȡ��Ч�ڽ�������
		j = 0;
		for(i=iOffset; i<(iOffset+16); i++)
		{
			bysEndDate[j] = bysReadResp[i];
			j++;
		}
		
		if(bysEndDate[0] >= '0' && bysEndDate[0] <= '9')
		{
			cardInfo.setStrEndDate(new String(bysEndDate,"UTF-16LE"));
		}
		else
		{
			try 
			{
				cardInfo.setStrEndDate(new String(bysEndDate, "UTF-16LE"));
			} 
			catch(UnsupportedEncodingException e) 
			{
				e.printStackTrace();
			}
		}
		iOffset += 16;
		
		//��Ƭ
		byte[] wlt = new byte[1024];
		byte[] bmp = new byte[14 + 40 + 308 * 126];
		
		for(i=0; i<iPhotoSize; i++)
		{
			wlt[i] = bysReadResp[16+iTextSize+i];
		}
		
		JniCall.hxgc_Wlt2Bmp(wlt, bmp, 0);
		Bitmap bitmap = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
		cardInfo.setBitmapIdPhoto(bitmap);
		
		//ָ��
//		if(0 == iFingerSize)
//		{
//			showString("ָ��: ��.\n", showReception);
//		}
//		else
//		{
//			showString("ָ��: ��.\n", showReception);
//		}
		//byte[] bysFinger = new byte[1024];
		//for(i=0; i<iFingerSize; i++)
		//{
		//	bysFinger[i] = mbysRecvBuffer[16+iTextSize+iPhotoSize+i];
		//}
		//showString("ָ��: ");
		//showString(data2hexstring(bysFinger, iFingerSize));
		//showString("\n");
//		showString("\n\n", showReception);
		
		////////////////////////////////////////////////
		//����
		ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);
		//toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
		toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK );
		toneGenerator.stopTone(); //û����һ�У���������.
		toneGenerator.release();
		
		return cardInfo;		
	
	}
	
} 

