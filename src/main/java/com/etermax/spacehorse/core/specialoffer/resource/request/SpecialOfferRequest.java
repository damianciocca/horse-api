package com.etermax.spacehorse.core.specialoffer.resource.request;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialOfferRequest {

	@JsonProperty("specialOfferId")
	private String specialOfferId;

	public SpecialOfferRequest(@JsonProperty("specialOfferId") String specialOfferId) {
		checkArgument(isNotBlank(specialOfferId), "the special offer id should not be blank");
		this.specialOfferId = specialOfferId;
	}

	public String getSpecialOfferId() {
		return this.specialOfferId;
	}

}
