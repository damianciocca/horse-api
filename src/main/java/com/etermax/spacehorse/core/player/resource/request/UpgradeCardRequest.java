package com.etermax.spacehorse.core.player.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpgradeCardRequest {

	@JsonProperty("cardId")
	private Long cardId;

	public Long getCardId() {
		return cardId;
	}

	public UpgradeCardRequest(@JsonProperty("cardId") Long cardId) {
		this.cardId = cardId;
	}
}
