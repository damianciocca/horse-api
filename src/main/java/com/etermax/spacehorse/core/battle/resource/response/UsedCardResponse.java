package com.etermax.spacehorse.core.battle.resource.response;

import com.etermax.spacehorse.core.battle.model.UsedCardInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UsedCardResponse {
	@JsonProperty("cardType")
	private String cardType;

	@JsonProperty("useAmount")
	private int useAmount;

	public UsedCardResponse(String cardType, int useAmount) {
		this.cardType = cardType;
		this.useAmount = useAmount;
	}

	public String getCardType() {
		return cardType;
	}

	public int getUseAmount() {
		return useAmount;
	}

	public static UsedCardResponse fromUsedCardInfo(UsedCardInfo usedCardInfo) {
		return new UsedCardResponse(usedCardInfo.getCardType(), usedCardInfo.getUseAmount());
	}
}
