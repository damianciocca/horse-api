package com.etermax.spacehorse.core.battle.action;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class StartBattleAction {

	private final BattleRepository battleRepository;
	private final ServerTimeProvider serverTimeProvider;

	public StartBattleAction(BattleRepository battleRepository, ServerTimeProvider serverTimeProvider) {
		this.battleRepository = battleRepository;
		this.serverTimeProvider = serverTimeProvider;
	}

	public boolean startBattle(String battleId) {
		if (isBlank(battleId)) {
			return false;
		}
		return battleRepository.find(battleId).map(this::startBattle).orElse(false);
	}

	private boolean startBattle(Battle battle) {
		if (isAlreadyStarted(battle)) {
			return false;
		}
		battleRepository.update(battle);
		return true;
	}

	private boolean isAlreadyStarted(Battle battle) {
		return !battle.start(serverTimeProvider.getDate());
	}

}
