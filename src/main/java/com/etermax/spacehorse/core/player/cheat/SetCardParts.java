package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;

public class SetCardParts extends Cheat {

	@Override
	public String getCheatId() {
		return "setCardParts";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		String cardPartId = getParameterString(parameters, 0);
		int amount = getParameterInt(parameters, 1);
		if (catalog.getCardDefinitionsCollection().findById(cardPartId).isPresent()) {
			int finalAmount = player.getInventory().getCardParts().cheatSetAmount(cardPartId, amount);
			return new CheatResponse(finalAmount);
		}
		return null;
	}
}
