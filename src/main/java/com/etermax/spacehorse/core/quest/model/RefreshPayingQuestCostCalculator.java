package com.etermax.spacehorse.core.quest.model;

import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class RefreshPayingQuestCostCalculator {

	private final ServerTimeProvider timeProvider;

	public RefreshPayingQuestCostCalculator(ServerTimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public int calculate(QuestSlot questSlot, QuestBoardConfiguration configuration) {
		long remainingTime = questSlot.getRefreshTimeInSeconds() - timeProvider.getTimeNowAsSeconds();
		if (remainingTime > 0) {
			return ((int) Math.ceil((double) remainingTime / (double) configuration.getNextQuestRemainingTimeDividerFactor())) * configuration
					.getNextQuestRemainingTimeGemsCostFactor();
		}
		return 0;
	}
}
