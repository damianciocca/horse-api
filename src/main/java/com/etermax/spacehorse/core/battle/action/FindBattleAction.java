package com.etermax.spacehorse.core.battle.action;

import java.util.Optional;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;

public class FindBattleAction {

	private final BattleRepository battleRepository;

	public FindBattleAction(BattleRepository battleRepository) {
		this.battleRepository = battleRepository;
	}

	public Optional<Battle> findBattle(String battleId) {
		return battleRepository.find(battleId);
	}
}
