package com.etermax.spacehorse.core.player.model.deck;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.fasterxml.jackson.annotation.JsonIgnore;

@DynamoDBDocument
public class Card {

	@DynamoDBAttribute(attributeName = "id")
	private long id;

	@DynamoDBAttribute(attributeName = "cardType")
	private String cardType;

	@DynamoDBAttribute(attributeName = "level")
	private int level;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Card() {
	}

	public Card(long id, String cardType, int level) {
		this.id = id;
		this.cardType = cardType;
		this.level = level;
	}

	public void upgrade() {
		level++;
	}

	@DynamoDBIgnore
	@JsonIgnore
	public CardDefinition getDefinition(Catalog catalog) {
		return catalog.getCardDefinitionsCollection().findByIdOrFail(cardType);
	}

	public boolean cheatSetLevel(int level, Catalog catalog) {
		if (!hasLevel(level, catalog)) {
			return false;
		}
		this.level = level;
		return true;
	}

	@DynamoDBIgnore
	@JsonIgnore
	public boolean hasLevel(int level, Catalog catalog) {
		CardDefinition definition = getDefinition(catalog);
		return 0 <= level && level < catalog.getCardParameterLevelsCollection().getMaxCardLevelByRarity(definition.getCardRarity());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	public void checkAndFixIntegrity(Catalog catalog) {
		capLevelToMaxLevel(catalog);
	}

	private void capLevelToMaxLevel(Catalog catalog) {
		CardDefinition definition = getDefinition(catalog);
		if (level >= definition.getMaxCardLevelByRarity(catalog))
			level = definition.getMaxCardLevelByRarity(catalog) - 1;
	}
}