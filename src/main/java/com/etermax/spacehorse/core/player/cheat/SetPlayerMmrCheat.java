package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.battle.model.DefaultPlayerMapNumberCalculator;
import com.etermax.spacehorse.core.battle.model.PlayerWinRateConfiguration;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;

public class SetPlayerMmrCheat extends Cheat {

	private PlayerWinRateRepository playerWinRateRepository;
	private final AchievementsObserver achievementObserver;

	public SetPlayerMmrCheat(PlayerWinRateRepository playerWinRateRepository, AchievementsObserver achievementObserver) {
		this.playerWinRateRepository = playerWinRateRepository;
		this.achievementObserver = achievementObserver;
	}

	@Override
	public String getCheatId() {
		return "setPlayerMmr";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		int currentMmr = playerWinRateRepository.findOrCrateDefault(player.getUserId()).getMmr();
		int mmr = getParameterInt(parameters, 0);
		playerWinRateRepository.updateMmr(player.getUserId(), mmr - currentMmr, PlayerWinRateConfiguration.create(catalog));
		player.setMapNumber(new DefaultPlayerMapNumberCalculator(catalog.getMapsCollection().getEntries()).getMapNumber(mmr));
		achievementObserver.update(player, catalog.getAchievementsDefinitionsCollection().getEntries());
		return new CheatResponse(mmr);
	}
}
