package com.etermax.spacehorse.core.player.resource.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SelectedCardsRequest {

	@JsonProperty("selectedCardsIds")
	private List<Long> selectedCardsIds;

	@JsonProperty("stockId")
	private Integer stockId;

	public SelectedCardsRequest(@JsonProperty("selectedCardsIds") List<Long> selectedCardsIds, @JsonProperty("stockId") Integer stockId) {
		this.selectedCardsIds = selectedCardsIds;
		this.stockId = stockId;
	}

	public List<Long> getSelectedCardsIds() {
		return selectedCardsIds;
	}

	public Integer getStockId() {
		return stockId;
	}
}
