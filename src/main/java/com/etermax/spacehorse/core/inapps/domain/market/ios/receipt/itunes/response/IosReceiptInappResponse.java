package com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IosReceiptInappResponse {

	@JsonProperty("product_id")
	private String productId;

	@JsonProperty("transaction_id")
	private String transactionId;

	@JsonProperty("purchase_date")
	private String purchaseDate;

	public IosReceiptInappResponse() {
	}

	public String getProductId() {
		return productId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}
}
