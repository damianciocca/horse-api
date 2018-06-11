package com.etermax.spacehorse.core.quest.action;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.resource.response.player.progress.QuestResponse;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestBoardUpdater;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.resource.response.QuestSkipResponse;

public class SkipQuestAction {

	private final QuestBoardRepository questBoardRepository;
	private final QuestBoardUpdater questBoardUpdater;

	public SkipQuestAction(QuestBoardRepository questBoardRepository, QuestFactory questFactory) {
		this.questBoardRepository = questBoardRepository;
		questBoardUpdater = new QuestBoardUpdater(questFactory, questBoardRepository);
	}

	public QuestSkipResponse skip(Player player, Catalog catalog, String slotId) {
		validateInputParameters(player, catalog, slotId);
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		questBoard.freeSkip(slotId);
		Quest quest = questBoardUpdater.addOrUpdateNextQuest(player, catalog, slotId, questBoard);
		return new QuestSkipResponse(new QuestResponse(quest), questBoard.getSkipTimeInSeconds());
	}

	private void validateInputParameters(Player player, Catalog catalog, String slotId) {
		checkArgument(player != null, "The player should not be null");
		checkArgument(catalog != null, "The catalog should not be null");
		checkArgument(isNotBlank(slotId), "The slotId should not be blank");
	}
}
