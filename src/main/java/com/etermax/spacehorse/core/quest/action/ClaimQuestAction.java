package com.etermax.spacehorse.core.quest.action;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.QuestRewardAssigner;
import com.etermax.spacehorse.core.quest.model.ClaimQuestConfiguration;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.resource.response.QuestClaimResponse;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class ClaimQuestAction {

	private final QuestRewardAssigner questRewardAssigner;
	private final QuestBoardRepository questBoardRepository;
	private final ServerTimeProvider timeProvider;

	public ClaimQuestAction(QuestRewardAssigner questRewardAssigner, QuestBoardRepository questBoardRepository, ServerTimeProvider timeProvider) {
		this.questRewardAssigner = questRewardAssigner;
		this.questBoardRepository = questBoardRepository;
		this.timeProvider = timeProvider;
	}

	public QuestClaimResponse claim(Player player, Catalog catalog, String slotId) {
		validateInputParameters(player, catalog, slotId);
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		Quest activeQuest = questBoard.claimQuest(slotId, getRefreshTimeInSecondsFor(slotId, catalog.getGameConstants()));
		QuestDefinition questDefinition = catalog.getQuestCollection().findByIdOrFail(activeQuest.getQuestId());
		List<RewardResponse> rewardResponses = questRewardAssigner.applyRewardsToPlayer(player, catalog, questDefinition);
		questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
		return new QuestClaimResponse(rewardResponses, activeQuest.getRefreshTimeInSeconds());
	}

	private long getRefreshTimeInSecondsFor(String difficulty, GameConstants gameConstants) {
		ClaimQuestConfiguration configuration = new ClaimQuestConfiguration(gameConstants);
		return timeProvider.getTimeNowAsSeconds() + configuration.getRefreshTimeInSecondsBy(difficulty);
	}

	private void validateInputParameters(Player player, Catalog catalog, String slotId) {
		checkArgument(player != null, "The player should not be null");
		checkArgument(catalog != null, "The catalog should not be null");
		checkArgument(isNotBlank(slotId), "The slotId should not be blank");
	}

}
