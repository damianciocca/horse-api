package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;

public class SetPlayerLevelCheat extends Cheat {

	private final AchievementsObserver achievementsObserver;

	public SetPlayerLevelCheat(AchievementsObserver achievementsObserver) {
		this.achievementsObserver = achievementsObserver;
	}

	@Override
	public String getCheatId() {
		return "setPlayerLevel";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		int level = getParameterInt(parameters, 0);
		if (level >= 0) {
			player.getProgress().cheatSetLevel(level, catalog.getPlayerLevelsCollection());
			achievementsObserver.update(player, catalog.getAchievementsDefinitionsCollection().getEntries());
			return new CheatResponse(player.getProgress().getLevel());
		}
		return null;
	}
}
