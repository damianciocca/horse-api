package com.etermax.spacehorse.core.battle.action;

import com.etermax.spacehorse.core.battle.exception.BattleNotFinishedException;
import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.FinishPlayerBattleDomainService;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.battle.resource.response.FinishBattlePlayerResponse;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.error.BattleNotFoundException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class FinishPlayerBattleAction {

	private final BattleRepository battleRepository;
	private final PlayerRepository playerRepository;
	private final CatalogRepository catalogRepository;
	private final FinishPlayerBattleDomainService finishPlayerBattleDomainService;

	public FinishPlayerBattleAction(BattleRepository battleRepository, PlayerRepository playerRepository, CatalogRepository catalogRepository,
			FinishPlayerBattleDomainService finishPlayerBattleDomainService) {
		this.battleRepository = battleRepository;
		this.playerRepository = playerRepository;
		this.catalogRepository = catalogRepository;
		this.finishPlayerBattleDomainService = finishPlayerBattleDomainService;
	}

	public FinishBattlePlayerResponse finishPlayerBattle(String loginId, String battleId) {
		Player player = playerRepository.find(loginId);
		Catalog catalog = catalogRepository.getActiveCatalogWithTag(player.getAbTag());

		if (!battleId.equals(player.getLastBattleId())) {
			throw new BattleNotFoundException("Battle not found.");
		}

		Battle battle = battleRepository.find(battleId).orElseThrow(() -> new BattleNotFoundException("Battle not found."));

		if (!battle.getFinished()) {
			throw new BattleNotFinishedException();
		}

		return finishPlayerBattleDomainService.finishBattlePlayer(player, catalog, battle);
	}

}
