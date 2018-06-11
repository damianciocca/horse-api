package com.etermax.spacehorse.core.battle.resource.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishBattlePlayerRequest {
	@JsonProperty("battleId")
	private String battleId;

	public String getBattleId() {
		return battleId;
	}

	public FinishBattlePlayerRequest(@JsonProperty("battleId") String battleId) {
		this.battleId = battleId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
