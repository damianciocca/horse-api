package com.etermax.spacehorse.core.player.cheat;

import java.util.Optional;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;

public class SetCardLevelCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "setCardLevel";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		long id = getParameterLong(parameters, 0);
		int level = getParameterInt(parameters, 1);

		if (level >= 0) {
			Optional<Card> card = player.getDeck().findCardById(id);
			if (card.isPresent()) {
				if (card.get().cheatSetLevel(level, catalog)) {
					return new CheatResponse(new CardResponse(card.get()));
				}
			}
		}
		return null;
	}
}
