package com.etermax.spacehorse.core.player.cheat;

import java.util.Optional;

import com.etermax.spacehorse.core.achievements.model.observers.CardsUnlockedReachedAchievementObserver;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;

public class AddCardCheat extends Cheat {

	private CardsUnlockedReachedAchievementObserver achievementObserver;

	public AddCardCheat(CardsUnlockedReachedAchievementObserver achievementObserver) {
		this.achievementObserver = achievementObserver;
	}

	@Override
	public String getCheatId() {
		return "addCard";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		String cardId = getParameterString(parameters, 0);
		Optional<CardDefinition> cardDefinition = catalog.getCardDefinitionsCollection().findById(cardId);
		if (cardDefinition.isPresent() && player.getDeck().findCardByCardType(cardId) == null) {
			Card card = player.getDeck().addNewCard(cardId);
			achievementObserver.update(player, catalog.getAchievementsDefinitionsCollection().getEntries());
			return new CheatResponse(new CardResponse(card));
		}
		return null;
	}
}
