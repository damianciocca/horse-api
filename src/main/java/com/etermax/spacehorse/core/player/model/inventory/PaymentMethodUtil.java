package com.etermax.spacehorse.core.player.model.inventory;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.currency.Currency;
import com.etermax.spacehorse.core.shop.exception.NotEnoughGemsException;
import com.etermax.spacehorse.core.shop.exception.NotEnoughGoldException;

public class PaymentMethodUtil {

	private static final int COST_ZERO = 0;

	public static void payWith(Player player, int gems, int golds) {
		Currency playerGems = player.getInventory().getGems();
		Currency playerGold = player.getInventory().getGold();
		if (playerGems.getAmount() < gems) {
			throw new NotEnoughGemsException();
		}
		if (playerGold.getAmount() < golds) {
			throw new NotEnoughGoldException();
		}
		playerGems.remove(gems);
		playerGold.remove(golds);
	}

	public static void payWithGems(Player player, int gems) {
		payWith(player, gems, COST_ZERO);
	}

	public static void payWithGold(Player player, int gold) {
		payWith(player, COST_ZERO, gold);
	}
}
