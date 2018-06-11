package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.CardsDropRateResponse;

public class CardsDropRate extends CatalogEntry implements CatalogEntryWithMapInformation {

	private final String cardId;

	private final int mapNumber;

	private final int intendedCards;

	public CardsDropRate(String id, String cardId, int mapNumber, int intendedCards) {
		super(id);
		this.cardId = cardId;
		this.mapNumber = mapNumber;
		this.intendedCards = intendedCards;
	}

	public CardsDropRate(String id) {
		super(id);
		this.cardId = "";
		this.mapNumber = 0;
		this.intendedCards = 0;
	}

	public CardsDropRate(CardsDropRateResponse cardsDropRateResponse) {
		super(cardsDropRateResponse.getId());
		this.cardId = cardsDropRateResponse.getCardId();
		this.mapNumber = cardsDropRateResponse.getMapNumber();
		this.intendedCards = cardsDropRateResponse.getIntendedCards();
	}

	public String getCardId() {
		return cardId;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public int getIntendedCards() {
		return intendedCards;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(cardId != null, "cardId == null");
		validateParameter(mapNumber >= 0, "mapNumber < 0");
		validateParameter(intendedCards >= 0, "intendedCards < 0");
	}

}
