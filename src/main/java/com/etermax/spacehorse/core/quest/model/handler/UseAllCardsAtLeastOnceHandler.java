package com.etermax.spacehorse.core.quest.model.handler;

import java.util.List;
import java.util.Optional;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.BattleUtils;
import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.etermax.spacehorse.core.battle.model.UsedCardInfo;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.quest.model.Quest;

public class UseAllCardsAtLeastOnceHandler implements QuestProgressHandler {
	@Override
	public boolean handleBattleEnded(Quest quest, Battle battle, Player player) {

		Optional<BattlePlayer> battlePlayer = BattleUtils.findBattlePlayer(player, battle);
		Optional<TeamStats> teamStats = BattleUtils.getTeamStats(player, battle);

		if (battlePlayer.isPresent() && teamStats.isPresent()) {
			if (allCardsUsed(battlePlayer.get().getSelectedCards(), teamStats.get().getUsedCards())) {
				quest.incrementProgress();
				return true;
			}
		}

		return false;
	}

	private boolean allCardsUsed(List<Card> selectedCards, List<UsedCardInfo> usedCards) {
		return selectedCards.stream().allMatch(card -> isCardUsed(usedCards, card));
	}

	private boolean isCardUsed(List<UsedCardInfo> usedCards, Card card) {
		return usedCards.stream().filter(usedCard -> usedCard.getUseAmount() > 0 && usedCard.getCardType().equals(card.getCardType())).findFirst()
				.isPresent();
	}
}
