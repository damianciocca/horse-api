package com.etermax.spacehorse.core.inapps.domain.market.ios;

import com.etermax.spacehorse.core.inapps.domain.Receipt;
import com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.response.IosReceiptInappResponse;
import com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.response.IosReceiptResponse;

public class IosReceipt extends Receipt {

	private String bundleId;
	private String productId;
	private String transactionId;
	private String data;

	public IosReceipt(IosReceiptResponse iosReceiptResponse, IosReceiptInappResponse inAppReceiptResponse, String data) {

		this.bundleId = iosReceiptResponse.getBundleId();
		this.productId = inAppReceiptResponse.getProductId();
		this.transactionId = inAppReceiptResponse.getTransactionId();
		this.data = data;

		if (productId.isEmpty() || bundleId.isEmpty())
			setValid(false);
	}

	@Override
	public String getProductId() {
		return productId;
	}

	public String getBundleId() {
		return bundleId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getData() {
		return data;
	}
}
