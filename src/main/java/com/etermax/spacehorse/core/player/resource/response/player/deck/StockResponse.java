package com.etermax.spacehorse.core.player.resource.response.player.deck;

import java.util.List;

import com.etermax.spacehorse.core.player.model.deck.Stock;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StockResponse {
	@JsonProperty("stockId")
	private Integer stockId;
	@JsonProperty("selectedCardsIds")
	private List<Long> selectedCardsIds;

	public StockResponse(Stock stock) {
		this.stockId = stock.getStockId();
		this.selectedCardsIds = stock.getSelectedCardsIds();
	}

	public Integer getStockId() {
		return stockId;
	}

	public List<Long> getSelectedCardsIds() {
		return selectedCardsIds;
	}

	public StockResponse() {
	}

	public StockResponse(Integer stockId, List<Long> selectedCardsIds) {
		this.stockId = stockId;
		this.selectedCardsIds = selectedCardsIds;
	}
}
