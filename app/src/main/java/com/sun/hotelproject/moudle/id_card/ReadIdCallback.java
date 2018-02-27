package com.sun.hotelproject.moudle.id_card;

public interface ReadIdCallback
{
	/**
	 * 
	 * @param iMode ����ģʽ��1���ζ�����2���������3ֹͣ�������
	 * @param iCmdType ������ȣ�1��Ѱ����2��ѡ����3�Ƕ���
	 * @param bysReadResp ��������Ϣ
	 * @param iSize ��Ϣ�ĳ���
	 */
	public void onReadIdComplete(final int iMode, final int iCmdType, final byte[] bysReadResp, final int iSize); 
}

