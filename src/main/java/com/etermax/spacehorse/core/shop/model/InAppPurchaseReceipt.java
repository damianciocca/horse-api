package com.etermax.spacehorse.core.shop.model;

import com.etermax.spacehorse.core.inapps.domain.Receipt;
import com.etermax.spacehorse.core.user.model.Platform;

public class InAppPurchaseReceipt {

	private String userId;
	private Platform platform;
	private Receipt receipt;

	public InAppPurchaseReceipt(Platform platform, Receipt receipt, String userId) {
		this.platform = platform;
		this.userId = userId;
		this.receipt = receipt;
	}

	public String getUserId() {
		return userId;
	}

	public Platform getPlatform() {
		return platform;
	}

	public Receipt getReceipt() {
		return receipt;
	}
}
