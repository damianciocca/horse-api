package com.etermax.spacehorse.core.specialoffer.resource.request.inapp;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialOfferInAppSpecialOfferRequest {

	@JsonProperty("specialOfferId")
	private String specialOfferId;

	@JsonProperty("receipt")
	private Object receipt;

	public SpecialOfferInAppSpecialOfferRequest(@JsonProperty("specialOfferId") String specialOfferId, @JsonProperty("receipt") Object receipt) {
		validateParameters(specialOfferId, receipt);
		this.specialOfferId = specialOfferId;
		this.receipt = receipt;
	}

	public String getSpecialOfferId() {
		return specialOfferId;
	}

	public Object getReceipt() {
		return receipt;
	}

	private void validateParameters(@JsonProperty("specialOfferId") String specialOfferId, @JsonProperty("receipt") Object receipt) {
		checkArgument(isNotBlank(specialOfferId), "the special offer id should not be blank");
		checkArgument(receipt != null, "the receipt should not be null");
	}

}
