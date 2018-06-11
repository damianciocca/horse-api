package com.etermax.spacehorse.core.battle.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardInfoResponse {

	@JsonProperty("cardId")
	private String cardId;

	@JsonProperty("energyCost")
	private int energyCost;

	public String getCardId() {
		return cardId;
	}

	public int getEnergyCost() {
		return energyCost;
	}

	public CardInfoResponse() {
	}

	public CardInfoResponse(String cardId, int energyCost) {
		this.cardId = cardId;
		this.energyCost = energyCost;
	}
}
