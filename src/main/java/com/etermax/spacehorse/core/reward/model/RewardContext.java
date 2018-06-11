package com.etermax.spacehorse.core.reward.model;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;

public class RewardContext {
	private int mapNumber;

	public int getMapNumber() {
		return mapNumber;
	}

	private RewardContext() {

	}

	private RewardContext(int mapNumber) {
		this.mapNumber = mapNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		RewardContext that = (RewardContext) o;

		return mapNumber == that.mapNumber;
	}

	@Override
	public int hashCode() {
		return mapNumber;
	}

	static public RewardContext fromChest(Chest chest) {
		return  new RewardContext(chest.getMapNumber());
	}

	public static RewardContext fromMapNumber(int mapNumber) {
		return new RewardContext(mapNumber);
	}

	static public RewardContext empty() {
		return new RewardContext(0);
	}
}
