package com.etermax.spacehorse.core.shop.resource.response;

import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.shop.model.ShopCards;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopCardsResponse {

	@JsonProperty("cards")
	private List<ShopCardResponse> cards;

	@JsonProperty("expirationServerTime")
	private long expirationServerTime = 0L;

	public ShopCardsResponse(ShopCards shopCards) {
		this.cards = shopCards.getCards().stream().map(ShopCardResponse::new).collect(Collectors.toList());
		this.expirationServerTime = shopCards.getExpirationServerTime();
	}

	public ShopCardsResponse() {
	}

	public List<ShopCardResponse> getCards() {
		return cards;
	}

	public long getExpirationServerTime() {
		return expirationServerTime;
	}

}
