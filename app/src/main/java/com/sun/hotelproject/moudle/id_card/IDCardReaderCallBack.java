package com.sun.hotelproject.moudle.id_card;

public interface IDCardReaderCallBack {

	void onReadIdComplete(final int iMode, IDCardInfo idCardInfo);
}
