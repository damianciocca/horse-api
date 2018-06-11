package com.etermax.spacehorse.core.quest.model;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;

public class QuestBoardUpdater {

	private final QuestFactory questFactory;
	private final QuestBoardRepository questBoardRepository;

	public QuestBoardUpdater(QuestFactory questFactory, QuestBoardRepository questBoardRepository) {
		this.questFactory = questFactory;
		this.questBoardRepository = questBoardRepository;
	}

	public Quest addOrUpdateNextQuest(Player player, Catalog catalog, String slotId, QuestBoard questBoard) {
		Quest quest = nextQuest(player, catalog, slotId, questBoard);
		questBoard.putFreeQuest(slotId, quest);
		questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
		return quest;
	}

	public Quest addOrUpdateNextQuestPaying(Player player, Catalog catalog, String slotId, QuestBoard questBoard) {
		Quest quest = nextQuest(player, catalog, slotId, questBoard);
		questBoard.putPayingQuest(slotId, quest);
		questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
		return quest;
	}

	public Quest putOrUpdateDailyQuest(Player player, Catalog catalog, QuestBoard questBoard) {
		Quest dailyQuest = questFactory.currentDailyQuest(createDailyQuestSelectorConfiguration(catalog));
		questBoard.putDailyQuest(dailyQuest);
		questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
		return dailyQuest;
	}

	private Quest nextQuest(Player player, Catalog catalog, String slotId, QuestBoard questBoard) {
		int playerMapNumber = player.getMapNumber();
		return questFactory.nextQuest(createConfigurationFrom(catalog, playerMapNumber), slotId, questBoard.getQuestSequenceBy(slotId));
	}

	private NextQuestSelectorConfiguration createConfigurationFrom(Catalog catalog, int playerMapNumber) {
		return new NextQuestSelectorConfiguration(catalog.getQuestCycleListCollection(), catalog.getQuestChancesListCollection(),
				catalog.getGameConstants().getDefaultQuestCycleSequenceId(), playerMapNumber);
	}

	private DailyQuestSelectorConfiguration createDailyQuestSelectorConfiguration(Catalog catalog) {
		return new DailyQuestSelectorConfiguration(catalog.getDailyQuestCollection().getEntries(), catalog.getGameConstants().getCurrentDailyQuestId());
	}
}
