package com.etermax.spacehorse.core.battle.resource.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetBattleInfoResponse {

	@JsonProperty("battle")
	private BattleResponse battle;

	@JsonProperty("parameters")
	private BattleParametersResponse parameters;

	@JsonProperty("cardsInfos")
	private List<CardInfoResponse> cardsInfos;

	public BattleResponse getBattle() {
		return battle;
	}

	public BattleParametersResponse getParameters() {
		return parameters;
	}

	public List<CardInfoResponse> getCardsInfos() {
		return cardsInfos;
	}

	public GetBattleInfoResponse() {
	}

	public GetBattleInfoResponse(BattleResponse battle, BattleParametersResponse parameters, List<CardInfoResponse> cardsInfos) {
		this.battle = battle;
		this.parameters = parameters;
		this.cardsInfos = cardsInfos;
	}
}
