package com.etermax.spacehorse.core.shop.resource.response;

import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopBuyCardResponse {
	@JsonProperty("unlockedCard")
	private CardResponse unlockedCard;

	public CardResponse getUnlockedCard() {
		return unlockedCard;
	}

	public ShopBuyCardResponse() {
	}

	public ShopBuyCardResponse(CardResponse unlockedCard) {
		this.unlockedCard = unlockedCard;
	}
}
