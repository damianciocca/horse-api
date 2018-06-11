package com.etermax.spacehorse.core.specialoffer.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialOfferResponse {

	@JsonProperty("id")
	private String id;

	@JsonProperty("expirationTimeInSeconds")
	private long expirationTimeInSeconds;

	@JsonProperty("availableAmountUntilExpiration")
	private int availableAmountUntilExpiration;

	public SpecialOfferResponse(@JsonProperty("id") String id, @JsonProperty("expirationTimeInSeconds") long expirationTimeInSeconds,
			@JsonProperty("availableAmountUntilExpiration") int availableAmountUntilExpiration) {
		this.id = id;
		this.expirationTimeInSeconds = expirationTimeInSeconds;
		this.availableAmountUntilExpiration = availableAmountUntilExpiration;
	}

	public String getId() {
		return id;
	}

	public long getExpirationTimeInSeconds() {
		return expirationTimeInSeconds;
	}

	public int getAvailableAmountUntilExpiration() {
		return availableAmountUntilExpiration;
	}
}
