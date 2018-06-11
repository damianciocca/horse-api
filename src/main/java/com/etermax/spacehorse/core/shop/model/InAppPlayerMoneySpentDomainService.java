package com.etermax.spacehorse.core.shop.model;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.InAppDefinition;
import com.etermax.spacehorse.core.player.model.Player;

public class InAppPlayerMoneySpentDomainService {
	public static void accumulate(Player player, String shopItemId, List<InAppDefinition> inAppDefinitions) {
		inAppDefinitions.stream().filter(inAppDefinition -> inAppDefinition.getItemId().equals(shopItemId)).findFirst().ifPresent(inAppDefinition -> {
			int priceInUsdCents = inAppDefinition.getInAppPriceInUsdCents();
			player.getPlayerStats().accumulateMoneySpent(priceInUsdCents);
		});
	}
}
