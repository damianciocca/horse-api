package com.etermax.spacehorse.core.reward.model.unlock;

public class MaxChestSlotsByPlayerLevel {

	private final int playerLevel;
	private final int maxSlotsAvailable;

	public MaxChestSlotsByPlayerLevel(int playerLevel, int maxSlotsAvailable) {
		this.playerLevel = playerLevel;
		this.maxSlotsAvailable = maxSlotsAvailable;
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public int getMaxSlotsAvailable() {
		return maxSlotsAvailable;
	}
}
