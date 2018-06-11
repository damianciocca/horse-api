package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.CardsDropRate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardsDropRateResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("CardId")
	private String cardId;

	@JsonProperty("MapNumber")
	private int mapNumber;

	@JsonProperty("IntendedCards")
	private int intendedCards;

	public CardsDropRateResponse() {
	}

	public CardsDropRateResponse(CardsDropRate cardsDropRate) {
		this.id = cardsDropRate.getId();
		this.cardId = cardsDropRate.getCardId();
		this.mapNumber = cardsDropRate.getMapNumber();
		this.intendedCards = cardsDropRate.getIntendedCards();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public void setMapNumber(int mapNumber) {
		this.mapNumber = mapNumber;
	}

	public int getIntendedCards() {
		return intendedCards;
	}

	public void setIntendedCards(int intendedCards) {
		this.intendedCards = intendedCards;
	}

}
