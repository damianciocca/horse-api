package com.etermax.spacehorse.core.reward.resource.response;

import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RewardResponse {
	@JsonProperty("rewardType")
	private RewardType rewardType;

	@JsonProperty("rewardId")
	private String rewardId;

	@JsonProperty("amount")
	private Integer amount;

	@JsonProperty("chest")
	private ChestResponse chest;

	@JsonProperty("card")
	private CardResponse card;

	private RewardResponse() {
	}

	public RewardResponse(RewardType rewardType, String rewardId, Integer amount) {
		this.rewardType = rewardType;
		this.rewardId = rewardId;
		this.amount = amount;
	}

	public RewardResponse(RewardType rewardType, Integer amount) {
		this.rewardType = rewardType;
		this.amount = amount;
	}

	public RewardResponse(ChestResponse chest) {
		this.rewardType = RewardType.CHEST;
		this.chest = chest;
		this.amount = 1;
	}

	public RewardResponse(RewardType rewardType, String rewardId, Integer amount, CardResponse card) {
		this.rewardType = rewardType;
		this.rewardId = rewardId;
		this.amount = amount;
		this.card = card;
	}

	public RewardType getRewardType() {
		return rewardType;
	}

	public String getRewardId() {
		return rewardId;
	}

	public Integer getAmount() {
		return amount;
	}

	public ChestResponse getChest() {
		return chest;
	}

	public CardResponse getCard() {
		return card;
	}

}
