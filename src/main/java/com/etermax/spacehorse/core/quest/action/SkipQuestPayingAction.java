package com.etermax.spacehorse.core.quest.action;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.player.resource.response.player.progress.QuestResponse;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestBoardUpdater;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.resource.response.QuestSkipResponse;

public class SkipQuestPayingAction {

	private final QuestBoardRepository questBoardRepository;
	private final PlayerRepository playerRepository;
	private final QuestBoardUpdater questBoardUpdater;

	public SkipQuestPayingAction(QuestBoardRepository questBoardRepository, QuestFactory questFactory, PlayerRepository playerRepository) {
		this.questBoardRepository = questBoardRepository;
		this.playerRepository = playerRepository;
		this.questBoardUpdater = new QuestBoardUpdater(questFactory, questBoardRepository);
	}

	public QuestSkipResponse skip(Player player, Catalog catalog, String slotId) {
		validateInputParameters(player, catalog, slotId);
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		questBoard.skipPaying(slotId, player);
		Quest quest = questBoardUpdater.addOrUpdateNextQuest(player, catalog, slotId, questBoard);
		playerRepository.update(player);
		return new QuestSkipResponse(new QuestResponse(quest), questBoard.getSkipTimeInSeconds());
	}

	private void validateInputParameters(Player player, Catalog catalog, String slotId) {
		checkArgument(player != null, "The player should not be null");
		checkArgument(catalog != null, "The catalog should not be null");
		checkArgument(isNotBlank(slotId), "The slotId should not be blank");
	}

}
