package com.etermax.spacehorse.core.battle.model;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.capitain.infrastructure.slots.DynamoCaptainSlot;
import com.etermax.spacehorse.core.player.model.deck.Card;

public class BattlePlayerBuilder {

	private String userId = "";
	private List<Card> selectedCards = new ArrayList<>();
	private Boolean isBot = false;
	private Integer mmr = 0;
	private Integer botMmr = 0;
	private String catalogId = "";
	private String abTag = "";
	private String name = "";
	private Integer level = 0;
	private String selectedCaptainId = "";
	private List<DynamoCaptainSlot> captainSlots = newArrayList();

	public BattlePlayerBuilder setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public BattlePlayerBuilder setSelectedCards(List<Card> selectedCards) {
		this.selectedCards = selectedCards;
		return this;
	}

	public BattlePlayerBuilder setIsBot(Boolean isBot) {
		this.isBot = isBot;
		return this;
	}

	public BattlePlayerBuilder setMmr(Integer mmr) {
		this.mmr = mmr;
		return this;
	}

	public BattlePlayerBuilder setBotMmr(Integer botMmr) {
		this.botMmr = botMmr;
		return this;
	}

	public BattlePlayerBuilder setCatalogId(String catalogId) {
		this.catalogId = catalogId;
		return this;
	}

	public BattlePlayerBuilder setAbTag(String abTag) {
		this.abTag = abTag;
		return this;
	}

	public BattlePlayerBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public BattlePlayerBuilder setLevel(Integer level) {
		this.level = level;
		return this;
	}

	public BattlePlayerBuilder setSelectedCaptainId(String captainId) {
		this.selectedCaptainId = captainId;
		return this;
	}

	public BattlePlayerBuilder setCaptainSlots(List<DynamoCaptainSlot> captainSlots) {
		this.captainSlots = captainSlots;
		return this;
	}

	public BattlePlayer createBattlePlayer() {
		return new BattlePlayer(userId, selectedCards, isBot, mmr, botMmr, catalogId, abTag, name, level, selectedCaptainId, captainSlots);
	}
}