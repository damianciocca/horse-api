package com.etermax.spacehorse.core.battle.resource.response;

import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BattlePlayerResponse {

	@JsonProperty("userId")
	private String userId;

	@JsonProperty("selectedCards")
	private List<CardResponse> selectedCards;

	@JsonProperty("isBot")
	private boolean isBot = false;

	@JsonProperty("mmr")
	private int mmr = 0;

	@JsonProperty("catalogId")
	private String catalogId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("level")
	private int level;

	public String getUserId() {
		return userId;
	}

	public List<CardResponse> getSelectedCards() {
		return selectedCards;
	}

	public boolean getBot() {
		return isBot;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getMmr() {
		return mmr;
	}

	public BattlePlayerResponse() {
	}

	public BattlePlayerResponse(String userId, List<CardResponse> selectedCards, boolean isBot, int mmr, String catalogId, String name, int level) {
		this.userId = userId;
		this.selectedCards = selectedCards;
		this.isBot = isBot;
		this.mmr = mmr;
		this.catalogId = catalogId;
		this.name = name;
		this.level = level;
	}

	public BattlePlayerResponse(BattlePlayer battlePlayer) {
		this.userId = battlePlayer.getUserId();
		this.selectedCards = battlePlayer.getSelectedCards().stream().map(CardResponse::new).collect(Collectors.toList());
		this.isBot = battlePlayer.getBot();
		this.mmr = battlePlayer.getMmr();
		this.catalogId = battlePlayer.getCatalogId();
		this.name = battlePlayer.getName();
		this.level = battlePlayer.getLevel();
	}
}
