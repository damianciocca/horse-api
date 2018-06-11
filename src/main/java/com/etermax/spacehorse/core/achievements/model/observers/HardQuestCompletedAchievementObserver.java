package com.etermax.spacehorse.core.achievements.model.observers;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.model.observers.base.BaseAchievementObserver;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.model.QuestSlot;

public class HardQuestCompletedAchievementObserver extends BaseAchievementObserver {

	private final QuestBoard questBoard;

	public HardQuestCompletedAchievementObserver(CompleteAchievementAction completeAchievementAction, QuestBoard questBoard) {
		super(completeAchievementAction);
		this.questBoard = questBoard;
	}

	@Override
	public AchievementType getAchievementType() {
		return AchievementType.COMPLETE_AT_LEAST_ONE_HARD_QUEST;
	}

	@Override
	public boolean achievementIsReached(Player player) {
		QuestSlot questSlot = questBoard.getSlot(QuestDifficultyType.HARD.name());
		return isReadyToClaim(questSlot);
	}

	@Override
	public String getErrorMsg() {
		return "====> Unexpected error when trying to update hard quests achievements types";
	}

	private boolean isReadyToClaim(QuestSlot questSlot) {
		return !questSlot.hasInitialQuest() && !questSlot.hasClaimedQuest() && questSlot.hasCompletedQuest();
	}
}
