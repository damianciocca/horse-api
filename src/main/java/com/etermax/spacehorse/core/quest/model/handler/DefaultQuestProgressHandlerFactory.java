package com.etermax.spacehorse.core.quest.model.handler;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.quest.model.QuestType;

public class DefaultQuestProgressHandlerFactory implements QuestProgressHandlerFactory {

	private final List<CardDefinition> cards;
	private final List<QuestDefinition> quests;

	public DefaultQuestProgressHandlerFactory() {
		cards = new ArrayList<>();
		quests = new ArrayList<>();
	}

	public DefaultQuestProgressHandlerFactory(List<CardDefinition> cards, List<QuestDefinition> quests) {
		this.cards = cards;
		this.quests = quests;
	}

	public QuestProgressHandler build(String questType) {
		switch (questType) {
			case QuestType.QUEST_SHIPS_DESTROYED:
				return new ShipsDestroyedHandler();
			case QuestType.QUEST_SIMPLE_VICTORIES:
				return new SimpleVictoriesHandler();
			case QuestType.QUEST_FULL_SCORE_VICTORIES:
				return new FullScoreVictoriesHandler();
			case QuestType.QUEST_USE_CARDS_WITH_RARITY:
				return new UseCardsWithRarityHandler(cards, quests);
			case QuestType.QUEST_USE_CARDS_ABOVE_ENERGY:
				return new UseCardsAboveEnergyHandler(cards, quests);
			case QuestType.QUEST_USE_CARDS_BELOW_ENERGY:
				return new UseCardsBelowEnergyHandler(cards, quests);
			case QuestType.QUEST_USE_CARDS_WITH_ARCHETYPE:
				return new UseCardsWithArchetypeHandler(cards, quests);
			case QuestType.QUEST_USE_ALL_CARDS_AT_LEAST_ONCE:
				return new UseAllCardsAtLeastOnceHandler();
			case QuestType.QUEST_VICTORIES_WITH_MAIN_SHIP_NOT_DAMAGED:
				return new VictoriesWithMainShipNotDamagedHandler();
		}
		throw new ApiException("QuestProgressHandler not implemented for questType " + questType);
	}

}
