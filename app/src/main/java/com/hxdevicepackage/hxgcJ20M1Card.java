package com.hxdevicepackage;

public class hxgcJ20M1Card
{

	private hxgcJ20Reader m_oJ20Reader = null;
	
	public hxgcJ20M1Card(hxgcJ20Reader _i_o_J20Reader)
	{
		m_oJ20Reader =  _i_o_J20Reader;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	//public
	
	//
	//��  ��: ������
	//��  ��: _o_bys_cardtype [���] ������ 0x0A - A��, 0x0B - B��.
	//          _o_bys_carduid   [���] ��UID
	//��  ��: true �ɹ�, false ʧ��.
	//
	public boolean ActiveCard(byte[] _o_bys_cardtype, byte[] _o_bys_carduid)
	{
		boolean bResult = true;
		byte[] bysCommand = {0x32, 0x41};
		byte[] bysParameter = {0x00, 0x00};
		bResult = SendPackage(bysCommand, bysParameter, 2);
		if(!bResult)
		{
			return false;
		}
		
		byte[] bysRespPkg = null;
		bysRespPkg = RecivePackage(1000);
		if(null == bysRespPkg)
		{
			return false;
		}
		System.out.println(byte2HexStr(bysRespPkg)+"xiekai");
		_o_bys_cardtype[0] = bysRespPkg[4];
		if(bysRespPkg.length<=5){
		return false;
		}
		_o_bys_carduid[0] = bysRespPkg[5];
		_o_bys_carduid[1] = bysRespPkg[6];
		_o_bys_carduid[2] = bysRespPkg[7];
		_o_bys_carduid[3] = bysRespPkg[8];
		
			return true;
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
	
	
	//
	//��  ��: ������֤
	//��  ��: _i_by_sectorno [����] ������
	//          _i_by_keytype  [����] ��Կ����: 0x60 A����Կ; 0x61 B����Կ; 0x40 ���ܺ��A����Կ; 0x41 ���ܺ��B��Կ.
	//          _i_bys_key       [����] ��Կ �Ǽ���ģʽʱΪ6�ֽ�, ����ģʽΪ8�ֽ�.
	//          _o_bys_sw       [���] ״̬��. ���ֽ�. 
	//��  ��: true �ɹ�, false ʧ��.
	//
	public boolean AuthCard(byte _i_by_sectorno, byte _i_by_keytype, byte[] _i_bys_key, byte[] _o_bys_sw)
	{
		int i = 0;
		byte[] bysCommand = {0x32, 0x42};
		byte[] bysParameter = null; //new byte[12];
		byte[] bysSW = {0x00, 0x00};
		int iParameterLen = 0;
		
		if(_o_bys_sw.length < 2)
		{
			return false;
		}
		
		if((_i_by_keytype & 0xF0) == 0x60)
		{
			if(_i_bys_key.length < 6)
			{
				return false;
			}
			
			bysParameter = new byte[8];
			bysParameter[0] = _i_by_sectorno;
			bysParameter[1] = _i_by_keytype;
			for(i=0; i<6; i++)
			{
				bysParameter[2+i] = _i_bys_key[i];
			}
			iParameterLen = 8;
		}
		else if((_i_by_keytype & 0xF0) == 0x40)
		{
			if(_i_bys_key.length < 8)
			{
				return false;
			}
			
			bysParameter = new byte[10];
			bysParameter[0] = _i_by_sectorno;
			bysParameter[1] = _i_by_keytype;
			for(i=0; i<8; i++)
			{
				bysParameter[2+i] = _i_bys_key[i];
			}
			iParameterLen = 10;
		}
		else
		{
			return false;
		}
		
		boolean bResult = true;
		
		bResult = SendPackage(bysCommand, bysParameter,  iParameterLen);
		if(!bResult)
		{
			return false;
		}
		
		byte[] bysRespPkg = null;
		bysRespPkg = RecivePackage(1000);
		if(null == bysRespPkg)
		{
			return false;
		}
		
		_o_bys_sw[0] = bysSW[0] = bysRespPkg[2];
		_o_bys_sw[1] = bysSW[1] = bysRespPkg[3];
		
		if(0 != bysSW[0] || 0  != bysSW[1])
		{
			return false;
		}
		
		return true;
	}

	//
	//��  ��: ��ȡ����һ���������
	//��  ��: _i_by_sectorno [����] ������
	//          _i_by_blockno  [����] ���
	//          _o_bys_data    [���] ����. 16�ֽ�.
	//          _o_bys_sw       [���] ״̬��. ���ֽ�. 
	//��  ��: true �ɹ�, false ʧ��.
	//
	public boolean ReadBlock(byte _i_by_sectorno, byte _i_by_blockno, byte[] _o_bys_data, byte[] _o_bys_sw)
	{
		if(_o_bys_data.length < 16)
		{
			return false;
		}
		
		int i = 0;
		
		byte[] bysCommand = {0x32, 0x43};
		byte[] bysParameter = {0x00};
		byte[] bysSW = {0x00, 0x00};
		int iParameterLen = 0;
		byte byBlockIdx = (byte)(_i_by_sectorno*4 + _i_by_blockno);
		
		bysParameter[0] = byBlockIdx;
		iParameterLen = 1;
		
		boolean bResult = true;
		
		bResult = SendPackage(bysCommand, bysParameter,  iParameterLen);
		if(!bResult)
		{
			return false;
		}
		
		byte[] bysRespPkg = null;
		bysRespPkg = RecivePackage(1000);
		if(null == bysRespPkg)
		{
			return false;
		}
		
		_o_bys_sw[0] = bysSW[0] = bysRespPkg[2];
		_o_bys_sw[1] = bysSW[1] = bysRespPkg[3];
		
		if(0 != bysSW[0] || 0  != bysSW[1])
		{
			return false;
		}
		
		for(i=0; i<16; i++)
		{
			_o_bys_data[i] = bysRespPkg[4+i];
		}
		
		return true;
	}

	//
	//��  ��: д�뿨��һ���������
	//��  ��: _i_by_sectorno [����] ������
	//          _i_by_blockno  [����] ���
	//          _i_bys_data      [����] ����. ����16�ֽ�.
	//          _o_bys_sw       [���] ״̬��. ���ֽ�. 
	//��  ��: true �ɹ�, false ʧ��.
	//
	public boolean WriteBlock(byte _i_by_sectorno, byte _i_by_blockno, byte[] _i_bys_data, byte[] _o_bys_sw)
	{
		if(16 != _i_bys_data.length)
		{
			return false;
		}
		
		int i = 0;
		byte[] bysCommand = {0x32, 0x44};
		byte[] bysParameter = new byte[1+16];
		byte[] bysSW = {0x00, 0x00};
		int iParameterLen = 0;
		byte byBlockIdx = (byte)(_i_by_sectorno*4 + _i_by_blockno);
		
		bysParameter[0] = byBlockIdx;
		for(i=0; i<16; i++)
		{
			bysParameter[1+i] = _i_bys_data[i];
		}
		iParameterLen = 1 + 16;
		
		boolean bResult = true;
		
		bResult = SendPackage(bysCommand, bysParameter,  iParameterLen);
		if(!bResult)
		{
			return false;
		}
		
		byte[] bysRespPkg = null;
		bysRespPkg = RecivePackage(1500);
		if(null == bysRespPkg)
		{
			return false;
		}
		
		_o_bys_sw[0] = bysSW[0] = bysRespPkg[2];
		_o_bys_sw[1] = bysSW[1] = bysRespPkg[3];
		
		if(0 != bysSW[0] || 0  != bysSW[1])
		{
			return false;
		}
		
		return true;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	//private
	
	private boolean SendPackage(byte[] _i_bys_Command, byte[] _i_bys_Parameter, int _i_i_ParameterLen)
	{
		boolean bResult = true;
		
		int i = 0;
		
		byte[] bysCmdBuf = null;
		byte[] bysTmp = null;
		int iLen = 0;
		int iPkgOffset = 0;
		
		byte[] bysLrc = new byte[1];
		byte[] bysLen = new byte[2];
		
		int iPkgLen = 1 + (2+2+_i_i_ParameterLen+1)*2 + 1;  //(7 + _i_i_ParameterLen) * 2;
		bysCmdBuf = new byte[iPkgLen];
		
		///////////////////////////////////////////////////
		//ת����ʽ
		
		//�����У����ݰ��г������ݰ�ͷ��STX�������ݰ�β��ETX�����������ֶ�Ҫ�����ֽڲ�֣�ÿ���ֽڲ�ָ�4λ����4λ�����֣��ֱ����0x30���γ������ֽڡ�
		//���ݣ�       1�ֽ����ݰ�ͷ + 2�ֽ����ݵ�Ԫ���� + 2�ֽ����������� + N�ֽڴ�������                       +1�ֽ�����У��ֵ(LRC) + 1�ֽ����ݰ�β
	    //��֤����:       0x02            00     0A              32     42              00     60     FF     FF    FF    FF    FF     FF         10                       03
	    //�ֽڲ�ֺ�:    0x02            3030 303A           3332 3432           3030 3630 3F3F 3F3F 3F3F 3F3F 3F3F 3F3F      3130                    03
		
		iPkgOffset = 0;
		
		//- 1�ֽ����ݰ�ͷ
		bysCmdBuf[iPkgOffset] = 0x02;
		iPkgOffset ++;
		//- 2�ֽ����ݵ�Ԫ����
		bysLen[0] = (byte)((_i_i_ParameterLen + 2) / 256);
		bysLen[1] = (byte)((_i_i_ParameterLen + 2) % 256);
		bysTmp = Format(bysLen);
		iLen = bysTmp.length;
		for(i=0; i<iLen; i++)
		{
			bysCmdBuf[iPkgOffset+i] = bysTmp[i]; 
		}
		iPkgOffset += bysTmp.length;
		//- 2�ֽ�����������
		bysTmp = Format(_i_bys_Command);
		iLen = bysTmp.length;
		for(i=0; i<iLen; i++)
		{
			bysCmdBuf[iPkgOffset+i] = bysTmp[i]; 
		}
		iPkgOffset += bysTmp.length;
		//-N�ֽڴ�������(����)
		bysTmp = Format(_i_bys_Parameter);
		iLen = bysTmp.length;
		for(i=0; i<iLen; i++)
		{
			bysCmdBuf[iPkgOffset+i] = bysTmp[i]; 
		}
		iPkgOffset += bysTmp.length;
		//-1�ֽ�����У��ֵ(LRC)
		bysLrc[0] = CalcLRC(bysLrc[0], _i_bys_Command);
		bysLrc[0] = CalcLRC(bysLrc[0], _i_bys_Parameter);
		bysTmp = Format(bysLrc);
		iLen = bysTmp.length;
		for(i=0; i<iLen; i++)
		{
			bysCmdBuf[iPkgOffset+i] = bysTmp[i]; 
		}
		iPkgOffset += bysTmp.length;
		//-1�ֽ����ݰ�β
		bysCmdBuf[iPkgOffset] = 0x03;
		iPkgOffset ++;
		
		///////////////////////////////////////////////////
		//����ָ��
		
		bResult = m_oJ20Reader.write(bysCmdBuf);
		
		return bResult;
	}
	
	private byte[] RecivePackage(long _i_l_timeOut)
	{
		byte[] bysRetBuf = null;
		
		int i = 0;
		byte[] bysData = null;
		byte[] bysRecvBuf = new byte[128];
		byte[] bysResp = null;
		int iOffset = 0;
		
		long lWaitTime = 0;
		
		do
		{
			//��������
			bysData = m_oJ20Reader.read(10000, 0);
			if(null == bysData)
			{
				try 
				{
					Thread.sleep(30);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				
				lWaitTime += 30;
				if(lWaitTime > _i_l_timeOut)
				{
					break;
				}
				else
				{
					continue;
				}
			}
			
			for(i=0; i<bysData.length; i++)
			{
				bysRecvBuf[iOffset+i] = bysData[i]; 
			}
			
			iOffset += bysData.length;
			
			if(0x03 == bysRecvBuf[iOffset-1])
			{
				break;
			}
			
		}while(true);
		
		//ת����ʽ
		if((iOffset) > 2 && (0 == iOffset % 2) && (0x02 == bysRecvBuf[0]) && (0x03 == bysRecvBuf[iOffset-1]))
		{
			bysResp = new byte[iOffset];
			for(i=0; i<iOffset; i++)
			{
				bysResp[i] =  bysRecvBuf[i];
			}
			bysRetBuf = UnPackage(bysResp, 1, iOffset-2);
		}
		
		return bysRetBuf;
	}
	
	private byte[] Format(byte[] _i_bys_in)
	{
		byte[] bysOut = null;
		
		int i = 0;
		int j = 0;
		int iLenIn = _i_bys_in.length;
		byte byTmp = 0;
		
		bysOut = new byte[iLenIn*2];
		
		for(i=0; i<iLenIn; i++)
		{
			byTmp = (byte)((_i_bys_in[i] >> 4) & 0x0F);
			byTmp += 0x30;
			bysOut[j] = byTmp;
			j ++;
			
			byTmp = (byte)(_i_bys_in[i] & 0x0F);
			byTmp += 0x30;
			bysOut[j] = byTmp;
			j++;
		}
		
		return bysOut; 
	}
	
	private byte[] UnPackage(byte[] _i_bys_resp, int _i_i_offset, int _i_i_len)
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
	}
	
	private byte CalcLRC(byte _i_by_orglrc, byte[] _i_bys_data)
	{
		byte byLRC = _i_by_orglrc;
		
		int i = 0;
		for(i=0; i<_i_bys_data.length; i++)
		{
			byLRC ^= _i_bys_data[i];
		}
		
		return byLRC;
	}
	
	
	
} // End

