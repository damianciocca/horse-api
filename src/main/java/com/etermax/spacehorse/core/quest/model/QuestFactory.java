package com.etermax.spacehorse.core.quest.model;

import org.joda.time.DateTime;

import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class QuestFactory {

	private final NextQuestSelector nextQuestSelector;
	private final ServerTimeProvider serverTime;

	public QuestFactory(ServerTimeProvider serverTime) {
		this.serverTime = serverTime;
		nextQuestSelector = new DefaultNextQuestSelector();
	}

	public Quest nextQuest(NextQuestSelectorConfiguration configuration, String slotId, int slotSequence) {
		QuestDefinition nextQuestDefinition = nextQuestSelector.getNextQuest(configuration, slotId, slotSequence);
		return new Quest(nextQuestDefinition);
	}

	public Quest currentDailyQuest(DailyQuestSelectorConfiguration configuration) {
		QuestDefinition dailyQuestDefinition =  nextQuestSelector.getDailyQuest(configuration);
		DateTime dateTime = ServerTime.roundToStartOfNextDay(serverTime.getDateTime());
		return new Quest(dailyQuestDefinition, ServerTime.fromDate(dateTime));
	}
}
