package com.etermax.spacehorse.core.battle.resource.response;

import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BattleResponse {

	@JsonProperty("battleId")
	private String battleId;

	@JsonProperty("players")
	private List<BattlePlayerResponse> players;

	@JsonProperty("catalogId")
	private String catalogId;

	@JsonProperty("seed")
	private int seed;

	@JsonProperty("mapId")
	private String mapId;

	@JsonProperty("startedAt")
	private long startedAt;

	@JsonProperty("finished")
	private boolean finished;

	@JsonProperty("finishedAt")
	private long finishedAt;

	@JsonProperty("winnerLoginId")
	private String winnerLoginId;

	@JsonProperty("extraData")
	private String extraData;

	public String getBattleId() {
		return battleId;
	}

	public List<BattlePlayerResponse> getPlayers() {
		return players;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public int getSeed() {
		return seed;
	}

	public String getMapId() {
		return mapId;
	}

	public long getStartedAt() {
		return startedAt;
	}

	public boolean getFinished() {
		return finished;
	}

	public long getFinishedAt() {
		return finishedAt;
	}

	public String getWinnerLoginId() {
		return winnerLoginId;
	}

	public String getExtraData() {
		return extraData;
	}

	public BattleResponse() {
	}

	public BattleResponse(String battleId, List<BattlePlayerResponse> players, String catalogId, int seed, String mapId, long startedAt,
			boolean finished, long finishedAt, String winnerLoginId, String extraData) {
		this.battleId = battleId;
		this.players = players;
		this.catalogId = catalogId;
		this.seed = seed;
		this.mapId = mapId;
		this.startedAt = startedAt;
		this.finished = finished;
		this.finishedAt = finishedAt;
		this.winnerLoginId = winnerLoginId;
		this.extraData = extraData;
	}

	public BattleResponse(Battle battle) {
		this.battleId = battle.getBattleId();
		this.players = battle.getPlayers().stream().map(BattlePlayerResponse::new).collect(Collectors.toList());
		this.catalogId = battle.getCatalogId();
		this.seed = battle.getSeed();
		this.mapId = battle.getMapId();
		this.startedAt = battle.getStartedAt() != null ? battle.getStartedAt().getTime() : 0;
		this.finished = battle.getFinished();
		this.finishedAt = battle.getFinishedAt() != null ? battle.getFinishedAt().getTime() : 0;
		this.winnerLoginId = battle.getWinnerLoginId();
		this.extraData = battle.getExtraData();
	}

}
