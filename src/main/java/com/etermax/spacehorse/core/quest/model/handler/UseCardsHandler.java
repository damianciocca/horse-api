package com.etermax.spacehorse.core.quest.model.handler;

import java.util.List;
import java.util.Optional;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattleUtils;
import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.etermax.spacehorse.core.battle.model.UsedCardInfo;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;

public abstract class UseCardsHandler implements QuestProgressHandler {

	private final List<CardDefinition> cardDefinitionList;
	private final List<QuestDefinition> questDefinitionList;

	public UseCardsHandler(List<CardDefinition> cardsDefinitionList, List<QuestDefinition> questDefinitionList) {
		this.cardDefinitionList = cardsDefinitionList;
		this.questDefinitionList = questDefinitionList;
	}

	@Override
	public boolean handleBattleEnded(Quest quest, Battle battle, Player player) {

		Optional<TeamStats> teamStats = BattleUtils.getTeamStats(player, battle);

		int questParameter = (int) getQuestParameter(quest.getQuestId());

		int usedCards = teamStats.map(stats -> calculateUsedCards(stats.getUsedCards(), questParameter)).orElse(0);

		if (usedCards > 0) {
			for (int i = 0; i < usedCards; i++) {
				quest.incrementProgress();
			}
			return true;
		}

		return false;
	}

	private int getQuestParameter(String questId) {
		return questDefinitionList.stream().filter(x -> x.getId().equals(questId)).findFirst().map(x -> x.getParameter()).orElse(0);
	}

	private int calculateUsedCards(List<UsedCardInfo> usedCards, int questParameter) {
		return usedCards.stream().filter(usedCardInfo -> filterCard(usedCardInfo.getCardType(), questParameter))
				.mapToInt(usedCardInfo -> usedCardInfo.getUseAmount()).sum();
	}

	private boolean filterCard(String cardType, int questParameter) {
		return findCardDefinition(cardType).map(cardDefinition -> filterCard(cardDefinition, questParameter)).orElse(false);
	}

	protected abstract boolean filterCard(CardDefinition cardDefinition, int questParameter);

	private Optional<CardDefinition> findCardDefinition(String cardType) {
		return cardDefinitionList.stream().filter(x -> x.getId().equals(cardType)).findFirst();
	}
}
