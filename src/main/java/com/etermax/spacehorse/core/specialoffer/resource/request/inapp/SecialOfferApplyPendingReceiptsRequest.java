package com.etermax.spacehorse.core.specialoffer.resource.request.inapp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class SecialOfferApplyPendingReceiptsRequest {

	@JsonProperty("receipts")
	private List<Object> receipts;

	public SecialOfferApplyPendingReceiptsRequest(@JsonProperty("receipts") List<Object> receipts) {
		this.receipts = receipts;
	}

	public SecialOfferApplyPendingReceiptsRequest(Object receipt) {
		this.receipts = Lists.newArrayList();
		this.receipts.add(receipt);
	}

	public List<Object> getReceipts() {
		return this.receipts;
	}

}