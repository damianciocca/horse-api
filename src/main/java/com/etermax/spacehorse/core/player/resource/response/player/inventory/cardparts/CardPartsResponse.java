package com.etermax.spacehorse.core.player.resource.response.player.inventory.cardparts;

import java.util.HashMap;
import java.util.Map;

import com.etermax.spacehorse.core.player.model.inventory.cardparts.CardParts;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardPartsResponse {

	@JsonProperty("amounts")
	private Map<String, Integer> amounts;

	public CardPartsResponse(CardParts cardParts) {
		this.amounts = cardParts.getAmounts();
	}

	public CardPartsResponse(HashMap<String, Integer> amounts) {
		this.amounts = amounts;
	}

	public CardPartsResponse() {
	}

}
