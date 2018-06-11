package com.etermax.spacehorse.core.inapps.repository;

import com.etermax.spacehorse.core.inapps.domain.Receipt;
import com.etermax.spacehorse.core.inapps.domain.market.android.AndroidReceipt;
import com.etermax.spacehorse.core.inapps.domain.market.ios.IosReceipt;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class MarketRepository {

	private final DynamoDao dynamoDao;

	public MarketRepository(DynamoDao dao) {
		this.dynamoDao = dao;
	}

	public void addAndroidReceipt(AndroidReceipt receipt, String userId) {
		AndroidPurchaseReceipt androidPurchaseReceipt = new AndroidPurchaseReceipt(receipt, userId);
		dynamoDao.add(androidPurchaseReceipt);
	}

	private Boolean checkDuplicateforAndroid(AndroidReceipt receipt) {
		AndroidPurchaseReceipt androidPurchaseReceipt = new AndroidPurchaseReceipt(receipt.getTransactionId());
		return dynamoDao.isDuplicated(androidPurchaseReceipt);
	}

	public void addIosReceipt(IosReceipt receipt, String userId) {
		IosPurchaseReceipt iosPurchaseReceipt = new IosPurchaseReceipt(receipt, userId);
		dynamoDao.add(iosPurchaseReceipt);
	}

	private Boolean checkDuplicateforIos(IosReceipt receipt) {
		IosPurchaseReceipt iosPurchaseReceipt = new IosPurchaseReceipt(receipt.getTransactionId());
		return dynamoDao.isDuplicated(iosPurchaseReceipt);
	}

	public Boolean isDuplicated(Receipt receipt, Platform platform) {
		Boolean isDuplicated = true;
		switch (platform) {
			case ANDROID:
				isDuplicated = checkDuplicateforAndroid((AndroidReceipt) receipt);
				break;
			case IOS:
				isDuplicated = checkDuplicateforIos((IosReceipt) receipt);
				break;
			case EDITOR:
				isDuplicated = false;
				break;
		}
		return isDuplicated;
	}
}
