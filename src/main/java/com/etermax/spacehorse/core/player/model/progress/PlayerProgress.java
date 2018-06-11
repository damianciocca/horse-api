package com.etermax.spacehorse.core.player.model.progress;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

import com.etermax.spacehorse.core.catalog.model.PlayerLevelsCollection;
import com.etermax.spacehorse.core.common.exception.ApiException;

@DynamoDBDocument
public class PlayerProgress {

	@DynamoDBAttribute(attributeName = "level")
	private int level;

	@DynamoDBAttribute(attributeName = "xp")
	private int xp;

	public int getLevel() {
		return level;
	}

	public int getXp() {
		return xp;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public PlayerProgress() {
	}

	public void addXp(int amount, PlayerLevelsCollection playerLevelsCollection) {
		if (amount < 0) {
			throw new ApiException("amount < 0");
		}
		xp += amount;
		while (hasNextLevel(playerLevelsCollection) && xp >= getNextLevelRequiredXp(playerLevelsCollection)) {
			xp -= getNextLevelRequiredXp(playerLevelsCollection);
			level++;
		}
		if (!hasNextLevel(playerLevelsCollection)) {
			xp = 0;
		}
	}

	@DynamoDBIgnore
	private Integer getNextLevelRequiredXp(PlayerLevelsCollection playerLevelsCollection) {
		return playerLevelsCollection.findByLevel(level + 1).orElseThrow(() -> new ApiException("Missing player level")).getXp();
	}

	@DynamoDBIgnore
	private boolean hasNextLevel(PlayerLevelsCollection playerLevelsCollection) {
		return level + 1 < playerLevelsCollection.getMaxLevel();
	}

	public void cheatSetXp(int newXp, PlayerLevelsCollection playerLevels) {
		if (hasNextLevel(playerLevels)) {
			newXp = Math.min(newXp, getNextLevelRequiredXp(playerLevels) - 1);
			this.xp = newXp;
		}
	}

	public void cheatSetLevel(int level, PlayerLevelsCollection playerLevels) {
		if (0 <= level && level < playerLevels.getMaxLevel()) {
			this.level = level;
		}
	}

}
