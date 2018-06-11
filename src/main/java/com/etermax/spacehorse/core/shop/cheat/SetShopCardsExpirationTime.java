package com.etermax.spacehorse.core.shop.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;

public class SetShopCardsExpirationTime  extends Cheat {

	@Override
	public String getCheatId() {
		return "setShopCardsExpirationTime";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		int serverTime = getParameterInt(parameters, 0);
		if (serverTime >= 0) {
			player.getDynamicShop().getShopCards().cheatSetExpirationTime(serverTime);
			return new CheatResponse(serverTime);
		}
		return null;
	}
}
