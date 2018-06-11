package com.etermax.spacehorse.core.matchmaking.resource.response;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.capitain.infrastructure.slots.DynamoCaptainSlot;
import com.etermax.spacehorse.core.capitain.resource.skins.CaptainSlotResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchmakingResponseOpponentData {

	@JsonProperty("id")
	private String id;

	@JsonProperty("level")
	private Integer level;

	@JsonProperty("name")
	private String name;

	@JsonProperty("isBot")
	private Boolean isBot = false;

	@JsonProperty("captainId")
	private String captainId;

	@JsonProperty("captainSlots")
	private List<CaptainSlotResponse> captainSlots;

	public MatchmakingResponseOpponentData(BattlePlayer opponent) {
		this.id = opponent.getUserId();
		this.level = opponent.getLevel();
		this.name = opponent.getName();
		this.isBot = opponent.getBot();
		this.captainId = opponent.getSelectedCaptainId();
		this.captainSlots = opponent.getCaptainSlots().stream().map(toCaptainSlotResponse()).collect(Collectors.toList());
	}

	private Function<DynamoCaptainSlot, CaptainSlotResponse> toCaptainSlotResponse() {
		return captainSlot -> new CaptainSlotResponse(captainSlot.getSlotNumber(), captainSlot.getCaptainSkinId());
	}

	public String getId() {
		return id;
	}

	public Integer getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public Boolean getIsBot() {
		return isBot;
	}

	public Boolean getBot() {
		return isBot;
	}

	public String getCaptainId() {
		return captainId;
	}

	public List<CaptainSlotResponse> getCaptainSlots() {
		return captainSlots;
	}
}