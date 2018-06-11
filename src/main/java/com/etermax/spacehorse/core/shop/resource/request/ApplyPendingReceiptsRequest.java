package com.etermax.spacehorse.core.shop.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ApplyPendingReceiptsRequest {

	@JsonProperty("receipts")
	private List<Object> receipts;

	public ApplyPendingReceiptsRequest(@JsonProperty("receipts") List<Object> receipts) {
		this.receipts = receipts;
	}

	public ApplyPendingReceiptsRequest(Object receipt) {
		this.receipts = new ArrayList<>();
		this.receipts.add(receipt);
	}

	public List<Object> getReceipts() {
		return this.receipts;
	}

}