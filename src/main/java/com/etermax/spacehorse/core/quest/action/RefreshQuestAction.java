package com.etermax.spacehorse.core.quest.action;

import static com.google.common.base.Preconditions.checkArgument;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestBoardUpdater;
import com.etermax.spacehorse.core.quest.model.QuestFactory;

public class RefreshQuestAction {

	private final QuestBoardRepository questBoardRepository;
	private final QuestBoardUpdater questBoardUpdater;

	public RefreshQuestAction(QuestBoardRepository questBoardRepository, QuestFactory questFactory) {
		this.questBoardRepository = questBoardRepository;
		questBoardUpdater = new QuestBoardUpdater(questFactory, questBoardRepository);
	}

	public Quest refresh(Player player, Catalog catalog, String slotId) {
		validateInputParameters(player, catalog);
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		return questBoardUpdater.addOrUpdateNextQuest(player, catalog, slotId, questBoard);
	}

	private void validateInputParameters(Player player, Catalog catalog) {
		checkArgument(player != null, "The player should not be null");
		checkArgument(catalog != null, "The catalog should not be null");
	}

}
