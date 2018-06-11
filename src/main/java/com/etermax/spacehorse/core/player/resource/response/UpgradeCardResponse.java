package com.etermax.spacehorse.core.player.resource.response;

import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpgradeCardResponse {

	@JsonProperty("card")
	private CardResponse card;

	public CardResponse getCard() {
		return card;
	}

	public UpgradeCardResponse(CardResponse card) {
		this.card = card;
	}
}
