package com.etermax.spacehorse.core.battle.model;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class TeamStats {

	@DynamoDBAttribute(attributeName = "score")
	private Integer score = 0;

	@DynamoDBAttribute(attributeName = "motherShipDamaged")
	private Boolean motherShipDamaged = false;

	@DynamoDBAttribute(attributeName = "usedCards")
	private List<UsedCardInfo> usedCards = new ArrayList<>();

	public TeamStats() {
	}

	public TeamStats(Integer score, Boolean motherShipDamaged, List<UsedCardInfo> usedCards) {
		this.score = score;
		this.motherShipDamaged = motherShipDamaged;
		this.usedCards = usedCards;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Boolean getMotherShipDamaged() {
		return motherShipDamaged;
	}

	public void setMotherShipDamaged(Boolean motherShipDamaged) {
		this.motherShipDamaged = motherShipDamaged;
	}

	public List<UsedCardInfo> getUsedCards() {
		return usedCards;
	}

	public void setUsedCards(List<UsedCardInfo> usedCards) {
		this.usedCards = usedCards;
	}

}
