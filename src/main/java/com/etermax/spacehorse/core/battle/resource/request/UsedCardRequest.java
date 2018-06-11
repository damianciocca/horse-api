package com.etermax.spacehorse.core.battle.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsedCardRequest {
	@JsonProperty("cardType")
	private String cardType;

	@JsonProperty("useAmount")
	private int useAmount;

	public UsedCardRequest(@JsonProperty("cardType") String cardType, @JsonProperty("useAmount") int useAmount) {
		this.cardType = cardType;
		this.useAmount = useAmount;
	}

	public String getCardType() {
		return cardType;
	}

	public int getUseAmount() {
		return useAmount;
	}
}
