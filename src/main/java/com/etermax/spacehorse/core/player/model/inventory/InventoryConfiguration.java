package com.etermax.spacehorse.core.player.model.inventory;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class InventoryConfiguration {

	private final int startingGems;
	private final int startingGold;

	public InventoryConfiguration(int startingGems, int startingGold) {
		this.startingGems = startingGems;
		this.startingGold = startingGold;
	}

	public int getStartingGems() {
		return startingGems;
	}

	public int getStartingGold() {
		return startingGold;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
