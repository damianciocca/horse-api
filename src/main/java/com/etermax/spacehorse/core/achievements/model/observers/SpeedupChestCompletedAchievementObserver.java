package com.etermax.spacehorse.core.achievements.model.observers;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.model.observers.base.BaseAchievementObserver;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;

public class SpeedupChestCompletedAchievementObserver extends BaseAchievementObserver {

	private final CatalogEntriesCollection<ChestDefinition> chestDefinitionsCollection;
	private final Chest chest;

	public SpeedupChestCompletedAchievementObserver(CompleteAchievementAction completeAchievementAction,
			CatalogEntriesCollection<ChestDefinition> chestDefinitionsCollection, Chest chest) {
		super(completeAchievementAction);
		this.chestDefinitionsCollection = chestDefinitionsCollection;
		this.chest = chest;
	}

	@Override
	public AchievementType getAchievementType() {
		return AchievementType.COMPLETE_AT_LEAST_ONE_SPEEDUP_COURIER;
	}

	@Override
	public boolean achievementIsReached(Player player) {
		ChestDefinition chestDefinition = chestDefinitionsCollection.findByIdOrFail(chest.getChestType());
		return isNotTutorialChest(chestDefinition);
	}

	@Override
	public String getErrorMsg() {
		return "====> Unexpected error when trying to update speedup chest achievements type";
	}

	private boolean isNotTutorialChest(ChestDefinition chestDefinition) {
		return !chestDefinition.getTutorialOnly();
	}

}
