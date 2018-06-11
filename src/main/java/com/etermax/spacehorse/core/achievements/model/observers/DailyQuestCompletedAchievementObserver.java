package com.etermax.spacehorse.core.achievements.model.observers;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.model.observers.base.BaseAchievementObserver;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;

public class DailyQuestCompletedAchievementObserver extends BaseAchievementObserver {

	private final QuestBoard questBoard;

	public DailyQuestCompletedAchievementObserver(CompleteAchievementAction completeAchievementAction, QuestBoard questBoard) {
		super(completeAchievementAction);
		this.questBoard = questBoard;
	}

	@Override
	public AchievementType getAchievementType() {
		return AchievementType.COMPLETE_AT_LEAST_ONE_DAILY_QUEST;
	}

	@Override
	public boolean achievementIsReached(Player player) {
		Quest dailyQuest = questBoard.getDailyQuest();
		return isReadyToClaim(dailyQuest);
	}

	@Override
	public String getErrorMsg() {
		return "====> Unexpected error when trying to update daily quest achievements types";
	}

	private boolean isReadyToClaim(Quest dailyQuest) {
		return !dailyQuest.isInitial() && dailyQuest.isCompleted() && questBoard.isNotClaimed(dailyQuest);
	}
}
