package com.etermax.spacehorse.core.inapps.domain.market.ios.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IosReceiptJson {
	@JsonProperty("Store")
	public String store;

	@JsonProperty("TransactionID")
	public String transactionId;

	@JsonProperty("Payload")
	public String payload;
}
