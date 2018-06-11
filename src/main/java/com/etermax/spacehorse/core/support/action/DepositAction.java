package com.etermax.spacehorse.core.support.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class DepositAction {

	private static final Logger logger = LoggerFactory.getLogger(DepositAction.class);

	private final PlayerRepository playerRepository;

	public DepositAction(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	public void deposit(Player player, int gemsAmount, int goldAmount) {
		if (gemsAmount > 0) {
			player.getInventory().getGems().add(gemsAmount);
		}
		if (goldAmount > 0) {
			player.getInventory().getGold().add(goldAmount);
		}

		logger.info("Support user deposit to user id [ {} ]: [ {} ] gems and [ {} ] golds ", player.getUserId(), gemsAmount, goldAmount);
		playerRepository.update(player);
	}
}
