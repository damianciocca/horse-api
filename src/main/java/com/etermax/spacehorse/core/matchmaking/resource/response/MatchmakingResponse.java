package com.etermax.spacehorse.core.matchmaking.resource.response;

import java.util.List;

import com.etermax.spacehorse.core.capitain.resource.skins.CaptainSlotResponse;
import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchmakingResponse {

	@JsonProperty("battleId")
	private String battleId;

	@JsonProperty("playerCards")
	private List<CardResponse> playerCards;

	@JsonProperty("opponentCards")
	private List<CardResponse> opponentCards;

	@JsonProperty("opponent")
	private MatchmakingResponseOpponentData opponent;

	@JsonProperty("seed")
	private int seed;

	@JsonProperty("mapId")
	private String mapId;

	@JsonProperty("realtimeServerUrl")
	private String realtimeServerUrl;

	@JsonProperty("realtimeServerSessionKey")
	private String realtimeServerSessionKey;

	@JsonProperty("extraData")
	private String extraData;

	@JsonProperty("playerCaptainId")
	private String playerCaptainId;

	@JsonProperty("playerCaptainSlots")
	private List<CaptainSlotResponse> playerCaptainSlots;

	@JsonProperty("matchType")
	private String matchType;

	public List<CardResponse> getPlayerCards() {
		return playerCards;
	}

	public List<CardResponse> getOpponentCards() {
		return opponentCards;
	}

	public MatchmakingResponseOpponentData getOpponent() {
		return opponent;
	}

	public int getSeed() {
		return seed;
	}

	public String getMapId() {
		return mapId;
	}

	public String getRealtimeServerUrl() {
		return realtimeServerUrl;
	}

	public String getRealtimeServerSessionKey() {
		return realtimeServerSessionKey;
	}

	public String getBattleId() {
		return battleId;
	}

	public String getExtraData() {
		return extraData;
	}

	public String getPlayerCaptainId() {
		return playerCaptainId;
	}

	public List<CaptainSlotResponse> getPlayerCaptainSlots() {
		return playerCaptainSlots;
	}

	public String getMatchType() {
		return matchType;
	}

	public MatchmakingResponse(String battleId, List<CardResponse> playerCards, List<CardResponse> opponentCards,
			MatchmakingResponseOpponentData opponent, int seed, String mapId, String realtimeServerSessionKey, String realtimeServerUrl,
			String extraData, String playerCaptainId, List<CaptainSlotResponse> playerCaptainSlots, String matchType) {
		this.battleId = battleId;
		this.playerCards = playerCards;
		this.opponent = opponent;
		this.opponentCards = opponentCards;
		this.seed = seed;
		this.mapId = mapId;
		this.realtimeServerUrl = realtimeServerUrl;
		this.realtimeServerSessionKey = realtimeServerSessionKey;
		this.extraData = extraData;
		this.playerCaptainId = playerCaptainId;
		this.playerCaptainSlots = playerCaptainSlots;
		this.matchType = matchType;
	}
}