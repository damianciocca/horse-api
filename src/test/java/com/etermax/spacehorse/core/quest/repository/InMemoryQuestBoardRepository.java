package com.etermax.spacehorse.core.quest.repository;

import java.util.Map;

import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.DailyQuestSelectorConfiguration;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.mock.QuestScenarioBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class InMemoryQuestBoardRepository implements QuestBoardRepository {

	private final ServerTimeProvider timeProvider;
	private Map<String, QuestBoard> questBoardsByUserIds = Maps.newConcurrentMap();
	private QuestFactory questFactory;

	public InMemoryQuestBoardRepository(ServerTimeProvider timeProvider) {
		this.timeProvider = timeProvider;
		questFactory = new QuestFactory(timeProvider);
	}

	@Override
	public QuestBoard findOrDefaultBy(Player player) {
		if (notExistFor(player.getUserId())) {
			Quest dailyQuest = createDefaultDailyQuest();
			QuestBoard questBoard = new QuestBoard(timeProvider, getQuestBoardConfiguration(), dailyQuest);
			questBoardsByUserIds.put(player.getUserId(), questBoard);
		}
		return questBoardsByUserIds.get(player.getUserId());
	}

	@Override
	public void addOrUpdate(String userId, QuestBoard questBoard) {
		questBoardsByUserIds.put(userId, questBoard);
	}

	private Quest createDefaultDailyQuest() {
		DailyQuestSelectorConfiguration dailyQuestSelectorConfiguration = getDailyQuestSelectorConfiguration();

		return questFactory.currentDailyQuest(dailyQuestSelectorConfiguration);
	}

	private DailyQuestSelectorConfiguration getDailyQuestSelectorConfiguration() {
		String dailyQuestId = "daily_quest_definition_1";
		QuestDefinition questDefinition = new QuestDefinition(dailyQuestId, "ShipsDestroyedQuest", "courier_Quest", 1, 0, "EASY");
		return new DailyQuestSelectorConfiguration(Lists.newArrayList(questDefinition), dailyQuestId);
	}

	private boolean notExistFor(String userId) {
		return !questBoardsByUserIds.containsKey(userId);
	}

	private QuestBoardConfiguration getQuestBoardConfiguration() {
		return new QuestBoardConfiguration(100, 10, 720, 1);
	}
}
