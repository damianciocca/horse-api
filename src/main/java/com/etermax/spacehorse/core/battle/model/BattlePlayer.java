package com.etermax.spacehorse.core.battle.model;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.capitain.infrastructure.slots.DynamoCaptainSlot;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.google.common.collect.Lists;

@DynamoDBDocument
public class BattlePlayer {

	@DynamoDBAttribute(attributeName = "userId")
	private String userId;

	@DynamoDBAttribute(attributeName = "selectedCards")
	private List<Card> selectedCards;

	@DynamoDBAttribute(attributeName = "isBot")
	private boolean isBot = false;

	@DynamoDBAttribute(attributeName = "mmr")
	private int mmr = 0;

	@DynamoDBAttribute(attributeName = "botMmr")
	private int botMmr = 0;

	@DynamoDBAttribute(attributeName = "catalogId")
	private String catalogId;

	@DynamoDBAttribute(attributeName = "abTag")
	private String abTag = "";

	@DynamoDBAttribute(attributeName = "name")
	private String name;

	@DynamoDBAttribute(attributeName = "level")
	private int level;

	@DynamoDBAttribute(attributeName = "selectedCaptainId")
	private String selectedCaptainId;

	@DynamoDBAttribute(attributeName = "captainSlots")
	private List<DynamoCaptainSlot> captainSlots;

	public BattlePlayer() {
	}

	public BattlePlayer(String userId, List<Card> selectedCards, boolean isBot, int mmr, int botMmr, String catalogId, String abTag, String name,
			int level, String selectedCaptainId, List<DynamoCaptainSlot> captainSlots) {
		this.userId = userId;
		this.selectedCards = selectedCards;
		this.isBot = isBot;
		this.mmr = mmr;
		this.botMmr = botMmr;
		this.catalogId = catalogId;
		this.abTag = abTag;
		this.name = name;
		this.level = level;
		this.selectedCaptainId = selectedCaptainId;
		this.captainSlots = captainSlots;
	}

	public BattlePlayer(String userId) {
		this.userId = userId;
		this.selectedCards = new ArrayList<>();
		this.isBot = false;
		this.mmr = 0;
		this.botMmr = 0;
		this.catalogId = "";
		this.abTag = "";
		this.name = userId;
		this.level = 0;
		this.selectedCaptainId = "";
		this.captainSlots = Lists.newArrayList();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Card> getSelectedCards() {
		return selectedCards;
	}

	public void setSelectedCards(List<Card> selectedCards) {
		this.selectedCards = selectedCards;
	}

	public boolean getBot() {
		return isBot;
	}

	public void setBot(boolean bot) {
		isBot = bot;
	}

	public int getMmr() {
		return mmr;
	}

	public void setMmr(int mmr) {
		this.mmr = mmr;
	}

	public int getBotMmr() {
		return botMmr;
	}

	public void setBotMmr(int botMmr) {
		this.botMmr = botMmr;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getAbTag() {
		return abTag;
	}

	public void setAbTag(String abTag) {
		this.abTag = abTag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getSelectedCaptainId() {
		return selectedCaptainId;
	}

	public void setSelectedCaptainId(String selectedCaptainId) {
		this.selectedCaptainId = selectedCaptainId;
	}

	public List<DynamoCaptainSlot> getCaptainSlots() {
		return captainSlots;
	}

	public void setCaptainSlots(List<DynamoCaptainSlot> captainSlots) {
		this.captainSlots = captainSlots;
	}
}
