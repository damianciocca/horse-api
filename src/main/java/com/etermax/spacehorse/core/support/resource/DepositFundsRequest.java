package com.etermax.spacehorse.core.support.resource;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepositFundsRequest {

	@JsonProperty("supportLoginId")
	private String supportLoginId;

	@JsonProperty("supportPassword")
	private String supportPassword;

	@JsonProperty("playerId")
	private String playerId;

	@JsonProperty("gold")
	private int goldAmount;

	@JsonProperty("gems")
	private int gemsAmount;

	public DepositFundsRequest(@JsonProperty("supportLoginId") String supportLoginId, @JsonProperty("supportPassword") String supportPassword,
			@JsonProperty("playerId") String playerId, @JsonProperty("gold") int goldAmount, @JsonProperty("gems") int gemsAmount) {
		this.supportLoginId = supportLoginId;
		this.supportPassword = supportPassword;
		this.playerId = playerId;
		this.goldAmount = goldAmount;
		this.gemsAmount = gemsAmount;
	}

	public String getSupportLoginId() {
		return supportLoginId;
	}

	public String getSupportPassword() {
		return supportPassword;
	}

	public String getPlayerId() {
		return playerId;
	}

	public int getGoldAmount() {
		return goldAmount;
	}

	public int getGemsAmount() {
		return gemsAmount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
