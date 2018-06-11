package com.etermax.spacehorse.core.player.resource.response.player.deck;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeckResponse {

	@JsonProperty("ownedCards")
	private List<CardResponse> ownedCards;
	@JsonProperty("stocks")
	private List<StockResponse> stocks;
	@JsonProperty("selectedStockId")
	private Integer selectedStockId;

	public List<CardResponse> getOwnedCards() {
		return ownedCards;
	}

	public List<StockResponse> getStocks() {
		return stocks;
	}

	public Integer getSelectedStockId() {
		return selectedStockId;
	}

	public DeckResponse(Deck deck) {
		this.ownedCards = deck.getOwnedCards().stream().map(CardResponse::new).collect(Collectors.toList());
		this.selectedStockId = deck.getSelectedStockId();
		this.stocks = deck.getStocks().stream().map(x -> new StockResponse(x)).collect(Collectors.toList());
	}

	public DeckResponse() {
	}

	public DeckResponse(List<CardResponse> ownedCards, List<StockResponse> stocks, Integer selectedStockId) {
		this.ownedCards = ownedCards;
		this.stocks = stocks;
		this.selectedStockId = selectedStockId;
	}


	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
