package com.etermax.spacehorse.core.quest.action.daily;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestRewardAssigner;
import com.etermax.spacehorse.core.quest.resource.response.QuestClaimResponse;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;

public class ClaimDailyQuestAction {

	private final QuestRewardAssigner questRewardAssigner;
	private final QuestBoardRepository questBoardRepository;

	public ClaimDailyQuestAction(QuestRewardAssigner questRewardAssigner, QuestBoardRepository questBoardRepository) {
		this.questRewardAssigner = questRewardAssigner;
		this.questBoardRepository = questBoardRepository;
	}

	public QuestClaimResponse claim(Player player, Catalog catalog) {
		validateInputParameters(player, catalog);
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		questBoard.claimDailyQuest();
		Quest dailyQuest = questBoard.getDailyQuest();
		QuestDefinition questDefinition = catalog.getDailyQuestCollection().findByIdOrFail(dailyQuest.getQuestId());
		List<RewardResponse> rewardResponses = questRewardAssigner.applyRewardsToPlayer(player, catalog, questDefinition);
		questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
		return new QuestClaimResponse(rewardResponses, 0);
	}

	private void validateInputParameters(Player player, Catalog catalog) {
		checkArgument(player != null, "The player should not be null");
		checkArgument(catalog != null, "The catalog should not be null");
	}

}
