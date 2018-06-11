package com.etermax.spacehorse.core.player.resource.response.player.deck;

import com.etermax.spacehorse.core.player.model.deck.Card;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardResponse {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("cardType")
	private String cardType;

	@JsonProperty("cardLevel")
	private int cardLevel;

	public CardResponse() {
	}

	public CardResponse(Card card) {
		this.id = card.getId();
		this.cardType = card.getCardType();
		this.cardLevel = card.getLevel();
	}

	public CardResponse(Long id, String cardType, int cardLevel) {
		this.id = id;
		this.cardType = cardType;
		this.cardLevel = cardLevel;
	}

	public Long getId() {
		return id;
	}

	public String getCardType() {
		return cardType;
	}

	public int getCardLevel() {
		return cardLevel;
	}
}
