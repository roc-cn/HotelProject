package com.szxb.smart.pos.jni_interface;

public class Card_Sender extends SeriesCom {
private static int Handle;
private static int fd;
	public int Card_Sender_Open() {
		fd = Com4052.Com4052Open();
		System.out.println("hhh-hhhh------%d"+fd);
		Com4052.Com4052Control(fd, 2);
		Handle =SeriesComOpen(dev, 9600);
		return  Handle;
	}

	public int Card_Sender_Close() {
		int ret;
		ret = SeriesComClose(Handle);
		Com4052.Com4052Close(fd);
		return ret;
	}

	private boolean Card_Sender_Cmd(byte[] pCmd, int nCmdLen, byte[] pResponse,
			int[] pRespLen) {
		byte[] _buf = new byte[128];

		byte crc = 0;

		_buf[0] = 0x02; // STX
		_buf[1] = 0; // ADDR
		_buf[2] = (byte) (nCmdLen % 256); // LEN

		System.arraycopy(pCmd, 0, _buf, 3, nCmdLen);

		for (int i = 0, j = 1; i < nCmdLen + 2; i++) {
			crc ^= _buf[j + i];
		}

		_buf[nCmdLen + 3] = crc;

		byte[] _resp = SeriesComTrans(Handle,_buf, 4+nCmdLen, 2000);

		if (null == _resp) {
			return false;
		}

		if (pRespLen != null) {
			pRespLen[0] = _resp.length;
		}

		if (pResponse != null) {
			System.arraycopy(_resp, 0, pResponse, 0, _resp.length);
		}

		if (_resp.length < 4)
			return false;

		if ((_resp[3] == 0 || _resp[3] == 0x16)) {
			return true;
		}

		return false;
	}

	public boolean TY_SetRF(boolean bOn) {
		byte[] cmd = new byte[16];
		int nCmdLen = 2;
		cmd[0] = 0x2A;
		cmd[1] = (byte) (bOn ? 0x01 : 0x00);
		return Card_Sender_Cmd(cmd, nCmdLen, null, null);
	}

	public boolean  TY_Beep(int nMSeconds) {
		byte[] cmd = new byte[16];
		int nCmdLen = 2;
		cmd[0] = 0x2C;
		cmd[1] = (byte) ((nMSeconds + 5) / 10);
		if (cmd[1] == 0)
			cmd[1] = 1;

		return Card_Sender_Cmd(cmd, nCmdLen, null, null);
	}

	// 设备复位

	public boolean TY_Reset() {
		byte[] cmd = new byte[16];
		int nCmdLen = 1;
		cmd[0] = 0x2D;
		return Card_Sender_Cmd(cmd, nCmdLen, null, null);
	}

	boolean TY_Request(int nMode, int[] pCardType) {
		byte[] cmd = new byte[16];
		byte[] resp = new byte[16];
		int nCmdLen = 2;
		int[] nRespLen = new int[1];
		cmd[0] = 0x31;
		cmd[1] = (byte) (nMode % 256);
		boolean bRet = Card_Sender_Cmd(cmd, nCmdLen, resp, nRespLen);
		if (bRet && pCardType != null) {
			pCardType[0] = resp[BYTE_IDX_DATA];
		}
		return bRet;
	}

	boolean TY_Anticoll(int nLevel, int[] pCardSn) {
		byte[] cmd = new byte[16];
		byte[] resp = new byte[16];
		int nCmdLen = 2;
		int[] nRespLen = new int[1];
		

		cmd[0] = 0x32;
		cmd[1] = (byte) nLevel;
		boolean bRet = Card_Sender_Cmd(cmd, nCmdLen, resp, nRespLen);
		if (bRet && pCardSn != null) {
			
		
			pCardSn[0] = resp[BYTE_IDX_DATA]&0xff;
			pCardSn[0] |= ((resp[BYTE_IDX_DATA+1] << 8) & 0xffff);
			pCardSn[0] |= ((resp[BYTE_IDX_DATA+2] << 16) & 0xffffff);
			pCardSn[0] |= ((resp[BYTE_IDX_DATA+3] << 24) & 0xffffffff);
//			pCardSn[0] = 0xE2C5DAE7;
		}
		return bRet;
	}

	// 选卡

	boolean TY_Select(int nLevel, int[] nCardSn, byte[] pSize) {
		byte[] cmd = new byte[16];
		byte[] resp = new byte[16];
		int nCmdLen = 6;
		int[] nRespLen = new int[1];

		cmd[0] = 0x33;
		cmd[1] = (byte) nLevel;
		cmd[2] = (byte) (nCardSn[0] & 0xff);
		cmd[3] = (byte) ((nCardSn[0] >> 8 ) & 0xff);
		cmd[4] = (byte) ((nCardSn[0] >> 16)  & 0xff);
		cmd[5] = (byte) ((nCardSn[0] >> 24)  & 0xff);
		boolean bRet = Card_Sender_Cmd(cmd, nCmdLen, resp, nRespLen);
		if (bRet && pSize != null) {
			pSize[0] = resp[BYTE_IDX_DATA];
		}
		return bRet;
	}

	boolean TY_Halt() {
		byte[] cmd = new byte[16];
		int nCmdLen = 1;
		cmd[0] = 0x34;
		return Card_Sender_Cmd(cmd, nCmdLen, null, null);
	}

	// 加载密码

	boolean TY_LoadKey(byte[] pKey) {
		byte[] cmd = new byte[16];
		int nCmdLen = 7;

		if (pKey == null)
			return false;

		cmd[0] = 0x35;
		cmd[1] = pKey[0];
		cmd[2] = pKey[1];
		cmd[3] = pKey[2];
		cmd[4] = pKey[3];
		cmd[5] = pKey[4];
		cmd[6] = pKey[5];

		return Card_Sender_Cmd(cmd, nCmdLen, null, null);
	}

	// 校验密码

	boolean TY_Authentication(int nMode, int nBlockAddr, int[] nCardSn) {
		byte[] cmd = new byte[16];
		int nCmdLen = 7;

		cmd[0] = 0x37;
		cmd[1] = (byte) nMode;
		cmd[2] = (byte) nBlockAddr;
		cmd[3] = (byte) (nCardSn[0] & 0xff);
		cmd[4] = (byte) ((nCardSn[0]) >> 8 & 0xff);
		cmd[5] = (byte) ((nCardSn[0]) >> 16 & 0xff);
		cmd[6] = (byte) ((nCardSn[0]) >> 24 & 0xff);

		return Card_Sender_Cmd(cmd, nCmdLen, null, null);
	}

	// 读卡

	boolean TY_Read(int nBlockAddr, int nBlockCount, byte[] pData) {
		byte[] cmd = new byte[16];
		byte[] resp = new byte[64];
		int nCmdLen = 3;
		int[] nRespLen = new int[] { 0 };

		if (pData == null || nBlockCount <= 0 || nBlockCount > 4)
			return false;

		cmd[0] = 0x38;
		cmd[1] = (byte) nBlockAddr;
		cmd[2] = (byte) nBlockCount;

		boolean bRet = Card_Sender_Cmd(cmd, nCmdLen, resp, nRespLen);
		if (bRet) {
			// memcpy(pData, resp + BYTE_IDX_DATA, nBlockCount * 16);
			System.arraycopy(resp, BYTE_IDX_DATA, pData, 0, nBlockCount * 16);
		}
		return bRet;
	}

	// 写卡

	boolean TY_Write(int nBlockAddr, int nBlockCount, byte[] pData) {
		byte[] cmd = new byte[96];
		int nCmdLen = 3 + nBlockCount * 16;
		;

		if (pData == null || nBlockCount <= 0 || nBlockCount > 4)
			return false;

		cmd[0] = 0x39;
		cmd[1] = (byte) (nBlockAddr % 256);
		cmd[2] = (byte) (nBlockCount % 256);
		// memcpy(cmd + 3, pData, nBlockCount * 16);
		System.arraycopy(pData, 0, cmd, 3, nBlockCount * 16);
		return Card_Sender_Cmd(cmd, nCmdLen, null, null);
	}

	// 移动卡片

	public boolean TY_MoveCard(int nAction) {
		byte[] cmd = new byte[16];
		int nCmdLen = 2;
		cmd[0] = 0x2F;
		cmd[1] = (byte) (nAction);
		boolean bRet = Card_Sender_Cmd(cmd, nCmdLen, null, null);
		return bRet;
	}

	// 状态查询

  public boolean TY_GetStatus(int[] pStatus) {
		byte[] cmd = new byte[16];
		byte[] resp = new byte[16];
		int nCmdLen = 2;
		int[] nRespLen = new int[1];

		if (pStatus == null)
			return false;

		cmd[0] = 0x2F;
		cmd[1] = 0x45;

		resp[3] = (byte) 255;
		boolean bRet = Card_Sender_Cmd(cmd, nCmdLen, resp, nRespLen);

		if (bRet && (nRespLen[0] > BYTE_IDX_DATA + 2)) {
			pStatus[0] = ((resp[BYTE_IDX_DATA] & 0x0F) << 8)
					| ((resp[BYTE_IDX_DATA + 1] & 0x0F) << 4)
					| (resp[BYTE_IDX_DATA + 2] & 0x0F);
			return true;
		} else {

			// ofstream outfile;
			// outfile.open("log.txt",ios::app);
			// char str[32];
			// sprintf(str,"Get Status Error, resp:Ox%02X",resp[3]);
			// WriteInfoToLog(outfile, str);

			pStatus[0] = 0;
			return false;
		}
	}

	public boolean TY_Card(int[] pCardSn, int[] pErrNo) {
		int[] nCardSn = new int[1];
		int[] nCardType = new int[1];
		//int err = ERROR_SUCCESS;
		byte[] cardSize = new byte[1];

		TY_Halt();

		if (!TY_Request(TY_REQUEST_ALL, nCardType)) {
			if (pErrNo != null)
				pErrNo[0] = ERROR_REQUEST_FAIL;

			return false;
		}

		if (!TY_Anticoll(CHOOSE_CARD_LEVEL1, nCardSn)) {
			if (pErrNo != null)
				pErrNo[0] = ERROR_ANTICOLL_FAIL;

			return false;
		}

		if (pCardSn != null)
			pCardSn[0] = nCardSn[0];

		if (!TY_Select(CHOOSE_CARD_LEVEL1, nCardSn, cardSize)) {
			if (pErrNo != null)
				pErrNo[0] = ERROR_SELECT_FAIL;

			return false;
		}

		if (pErrNo != null)
			pErrNo[0] = ERROR_SUCCESS;

		return true;
	}

	// 发一张房卡！
	public boolean TY_SendRoomCard(int nLockType, byte[] pData, int[] pCardSn,
			int[] pErrNo) {

		int hFile = -1;
	//	byte[] str = new byte[32];
		int[] nStatus = new int[1];

		// 把卡移动到读头位置
		boolean bRet = false, bWriteBlock3 = false, bMoveCard = false;
		int[] err = new int[1];

		int[] nCardSn = new int[1];
		int nBlockAddr = 0, nBlockAddr_3 = 0, nBlockCount = 0, KeyMode = 0;

		byte[] Key_Default = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
		byte Key_Lock[] = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x07, (byte) 0x80, 0x69,
				(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF };

		do {
			if (nLockType < 0 || nLockType > 2 || pData == null) {

				err[0] = ERROR_INVALID_PARAMS;
				break;
			}

			switch (nLockType) {
			case 0: // 科新 199404281970
				nBlockAddr = 1;
				nBlockAddr_3 = 3;
				nBlockCount = 2;
				KeyMode = TY_KEYMODE_KEYA;
				Key_Lock[0] = 0x19;
				Key_Lock[1] = (byte) 0x94;
				Key_Lock[2] = 0x04;
				Key_Lock[3] = 0x28;
				Key_Lock[4] = 0x19;
				Key_Lock[5] = 0x70;
				break;

			case 1: // 必达 189705211860，提供内容的明文，寻卡后进行加密
				nBlockAddr = 9;
				nBlockAddr_3 = 11;
				nBlockCount = 2;
				KeyMode = TY_KEYMODE_KEYA;
				Key_Lock[0] = 0x18;
				Key_Lock[1] = (byte) 0x97;
				Key_Lock[2] = 0x05;
				Key_Lock[3] = 0x21;
				Key_Lock[4] = 0x18;
				Key_Lock[5] = 0x60;
				break;

			case 2: // 创佳，制卡比较麻烦！
				KeyMode = TY_KEYMODE_KEYB;
				break;
			}

			if ((hFile = Card_Sender_Open()) <= 0) {
				err[0] = ERROR_OPENPORT_FAIL;
				break;
			}

			// 获取发卡盒状态

			if (TY_GetStatus(nStatus)) {
				// 卡嘴有卡，则无法将卡移动到 reader 位置（2016年2月2日 补充）
				/*
				 * if((nStatus & ST_OC2_HASCARD) == 0 && (nStatus &
				 * ST_OC3_HASCARD)) { err = ERROR_SEND_OC3HASCARD; break; }
				 */

				// 如果读头处有卡，则发卡箱为空也没关系，可以就使用读头位置的卡
				if (((nStatus[0] & ST_OC2_HASCARD) == 0)
						&& (boolean) ((nStatus[0] & ST_CARDBOX_EMPTY) != 0)) {
					err[0] = ERROR_CARDBOX_EMPTY;
					break;
				}
			} else {

				err[0] = ERROR_DEV_ABSENT;
				break;
			}

			// 把卡移动到读头位置

			if (!TY_MoveCard(MC_TO_READER)) {
				err[0] = ERROR_MOVECARD_FAIL;
				break;
			}
			bMoveCard = true;

			// 打开天线
			if (!TY_SetRF(true)) {
				err[0] = ERROR_SETRF_FAIL;
				break;
			}

			// 从发卡槽，到读头位置，需要 300~350 ms 的时间
			// 从卡嘴到读头位置，实际上不需要时间（因为在卡嘴的时候，读头是能读到卡的）
			TY_GetStatus(nStatus);
			int nWaitTimes = 0;
			// if((nStatus & ST_OC2_HASCARD) == 0)
			// {
			//
			//
			// Sleep(250);
			// TY_GetStatus(hFile, &nStatus);

			while ((nStatus[0] & ST_OC2_HASCARD) == 0) {
				// 等待卡送到读头位置，大约需要 300 ms
				if (nWaitTimes >= 20) {

					// WriteInfoToLog(outfile,
					// "Waiting for card moving to reader.time out");
					// 移动卡到 reader 超时（2016-2-2 补充）
					err[0] = ERROR_WAIT_TIMEOUT;
					break;
				}
				++nWaitTimes;
				// Sleep(50);

				TY_GetStatus(nStatus);
			}
			// }

			// 寻卡
			if (!TY_Card(nCardSn, err)) {
				// WriteInfoToLog(outfile, "TY_Card Func Error");
				break;
			}

			// #ifdef _DEBUG
			// t1 = GetTickCount();
			// printf("Find card Complete. (%ld ms)\n", t1 - t0);
			// #endif

			if (pCardSn != null)
				pCardSn[0] = nCardSn[0];

			// 加载门锁密码
			if (!TY_LoadKey(Key_Lock)) {
				// WriteInfoToLog(outfile, "Loading key fail");
				err[0] = ERROR_LOADKEY_FAIL;
				break;
			}

			// 校验是否是白卡
			if (TY_Authentication(KeyMode, nBlockAddr, nCardSn)) {
				bWriteBlock3 = false;
			} else {
				// #ifdef _DEBUG
				// printf("Finding card...\n");
				// t0 = GetTickCount();
				// #endif

				if (!TY_Card(nCardSn, err))
					break;

				// #ifdef _DEBUG
				// t1 = GetTickCount();
				// printf("Find card Complete. (%ld ms)\n", t1 - t0);
				// #endif

				// 校验是否是门锁密码
				if (TY_LoadKey(Key_Default)
						&& TY_Authentication(TY_KEYMODE_KEYA, nBlockAddr,
								nCardSn)) {
					bWriteBlock3 = true;
				} else {

					// WriteInfoToLog(outfile,
					// "Loading key or Authenticate card fail");
					err[0] = ERROR_AUTH_FAIL;
					break;
				}
			}

			// 写卡
			// #ifdef _DEBUG
			// printf("Writing card...\n");
			// t0 = GetTickCount();
			// #endif

			if (TY_Write(nBlockAddr, nBlockCount, pData)) {
				if (bWriteBlock3) {
					if (TY_Write(nBlockAddr_3, 1, Key_Lock))
						bRet = true;
				} else
					bRet = true;
			} else {
				// WriteInfoToLog(outfile, "Writing card fail");
				err[0] = ERROR_WRITE_FAIL;
				break;
			}

			// #ifdef _DEBUG
			// t1 = GetTickCount();
			// printf("Write card Complete. (%ld ms)\n", t1 - t0);
			// #endif
		} while (false);

		if (hFile >= 0) {

			if (err[0] != ERROR_DEV_ABSENT) {
				TY_Halt();
				TY_SetRF(false);
			}
			// 移动卡，如果成功，送到卡嘴。
			// 如果失败，且回收槽不满，送回到回收槽
			if (bMoveCard) {
				// #ifdef _DEBUG
				// printf("Moving card...\n");
				// t0 = GetTickCount();
				// #endif
				if (bRet) {
					TY_MoveCard(MC_TO_HOLDER_MID); // MC_TO_HOLDER_MID);
				} else {
					if ((nStatus[0] & ST_OT_RECYCLE_HASCARD) != 0) {

						TY_MoveCard(MC_TO_HOLDER_MID); // MC_TO_HOLDER_MID);
					} else {
						TY_MoveCard(MC_TO_RECYCLEBOX);
					}
				}

				// #ifdef _DEBUG
				// t1 = GetTickCount();
				// printf("Move card Complete.(%ld ms)\n", t1 - t0);
				// #endif
			}

			Card_Sender_Close();
		}

		if (pErrNo != null) {
			pErrNo[0] = err[0];
		}

		return bRet;
	}

	// 发一张房卡！

	//private static final String dev = "/dev/ttyS4";
	private static final String dev = "/dev/ttyS1";
//	private static final int BYTE_IDX_STX = 0;
//	private static final int BYTE_IDX_ADDR = 1;
//	private static final int BYTE_IDX_LEN = 2;
//	private static final int BYTE_IDX_CMD_STU = 3;
	private static final int BYTE_IDX_DATA = 4;// 是 DATA 的起始索引

	private static final int ERROR_SUCCESS = 0;
	private static final int ERROR_INVALID_PARAMS = 1; // 参数无效
	private static final int ERROR_OPENPORT_FAIL = 2; // 打开串口失败
	private static final int ERROR_CARDBOX_EMPTY = 3; // 发卡槽无卡
//	private static final int ERROR_RECYCLEBOX_FULL = 4; // 收卡槽已满
	private static final int ERROR_SETRF_FAIL = 5; // 打开天线失败
	private static final int ERROR_REQUEST_FAIL = 6; // 寻卡失败（读头位置无卡）
	private static final int ERROR_ANTICOLL_FAIL = 7; // 防冲突失败
	private static final int ERROR_SELECT_FAIL = 8; // 寻卡失败
	private static final int ERROR_LOADKEY_FAIL = 9; // 加载密码失败
	private static final int ERROR_AUTH_FAIL = 10; // 验证密码失败（密码错误）
//	private static final int ERROR_READ_FAIL = 11;// 读卡失败
	private static final int ERROR_WRITE_FAIL = 12; // 写卡失败
	private static final int ERROR_MOVECARD_FAIL = 13; // 移动卡失败
	private static final int ERROR_WAIT_TIMEOUT = 14; // 超时（例如收房卡时）
	private static final int ERROR_DEV_ABSENT = 15; // 设备没有连接
	private static final int ERROR_SEND_OC3HASCARD = 16; // 发卡时，卡嘴位置有卡（2016年2月2日
															// 补充）
	private static final int ERROR_SEND_OC2NOCARD = 17; // 吐卡时，读头位置无卡（2016年2月4日
														// 补充）
	private static final int ERROR_REV_OC23NOCARD = 18; // 收卡时，卡嘴或读头位置无卡（2016年2月5日
														// 补充）
	private static final int ERROR_REV_CARD = 19; // 收卡时，出错（2016年2月6日 补充）
	private static final int ERROR_SEND_CARD = 20; // 发卡时，出错（2016年2月6日 补充）
	private static final int ERROR_RESET = 21; // 复位卡机失败

	private static final int TY_REQUEST_ALL = 0x52;// 寻找所有的卡
	private static final int TY_REQUEST_NOT_IDLE = 0x26;// 寻找不在 IDLE 状态的卡

	private static final int CHOOSE_CARD_LEVEL1 = 0x93;
	private static final int CHOOSE_CARD_LEVEL2 = 0x95;
	private static final int CHOOSE_CARD_LEVEL3 = 0x97;

	private static final int TY_KEYMODE_KEYA = 0x60;
	private static final int TY_KEYMODE_KEYB = 0x61;

	private static final int ST_OC3_HASCARD = 0x0001; // Bit0: 光耦 3 有卡 （卡嘴位置）
	private static final int ST_OC2_HASCARD = 0x0002;// Bit1: 光耦 2 有卡 （读头天线上方）
	private static final int ST_OC1_HASCARD = 0x0004;// Bit2: 光耦 1 有卡
	private static final int ST_CARDBOX_EMPTY = 0x0008;// Bit3: 卡筒卡空
	private static final int ST_CARD_QUANTITY_LOW = 0x0010; // Bit4: 卡量不足
	private static final int ST_CARD_BLOCKING = 0x0020; // Bit5: 塞卡（什么意思？不懂）
	private static final int ST_OT_RECYCLE_HASCARD = 0x0040; // Bit6:
																// 回收光耦有卡（可能是回收盒已满的意思）
	private static final int ST_RESERVED = 0x0080; // Bit7: 保留
	private static final int ST_RECEIVE_CARD_ERROR = 0x0100; // Bit8: 收卡出错
	private static final int ST_SEND_CARD_ERROR = 0x0200; // Bit9: 发卡出错
	private static final int ST_RECEIVING_CARD = 0x0400; // BitA: 正在收卡
	private static final int ST_SENDING_CARD = 0x0800; // BitB: 正在发卡

	private static final int MC_TO_READER = 0x40; // 卡移动到读头位置（卡机中部）
	private static final int MC_TO_HOLDER_DIRECT = 0x41; // 全出卡（一次将卡送到卡嘴，在读头处不停留）
	private static final int MC_TO_RECYCLEBOX = 0x42; // 回收卡（卡必须位于读头/卡嘴位置。
	private static final int MC_TO_HOLDER_MID = 0x43;// 半出卡（读头位置发到卡嘴）
	private static final int MC_TO_HOLDER_FLY = 0x46; // 飞出卡片
	private static final int MC_TO_RESET = 0x47;// 卡机整机复位（相当于重新上电）
	private static final int MC_TO_RECEIVECARD = 0x48;// 强制吸卡到读头位置（卡机拨码 4
														// 被拨上时起效）
}
