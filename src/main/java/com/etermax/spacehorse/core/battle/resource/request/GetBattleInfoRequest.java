package com.etermax.spacehorse.core.battle.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetBattleInfoRequest {
	@JsonProperty("battleId")
	private String battleId;

	public GetBattleInfoRequest(@JsonProperty("battleId") String battleId) {
		this.battleId = battleId;
	}

	public String getBattleId() {
		return battleId;
	}
}
