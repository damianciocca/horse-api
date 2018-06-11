package com.etermax.spacehorse.core.quest.model;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.TimeZone;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockCatalog;
import com.google.common.collect.Lists;

public class QuestFactoryTest {

	private NextQuestSelectorConfiguration nextQuestSelectorConfiguration;
	private QuestFactory questFactory;
	private DailyQuestSelectorConfiguration dailyQuestSelectorConfiguration;
	private FixedServerTimeProvider timeProvider;

	@Before
	public void setUp() throws Exception {
		timeProvider = new FixedServerTimeProvider();
		Catalog catalog = MockCatalog.buildCatalog();
		nextQuestSelectorConfiguration = new NextQuestSelectorConfiguration(catalog.getQuestCycleListCollection(), catalog.getQuestChancesListCollection(),
				catalog.getGameConstants().getDefaultQuestCycleSequenceId(), 0);
		dailyQuestSelectorConfiguration = createDailyQuestSelectorConfiguration(catalog);
		questFactory = new QuestFactory(timeProvider);
	}

	@Test
	public void givenThreeQuestInCatalogWhenCreateNextQuestForSequenceOneThenTheFirstQuestShouldBeCreated() {

		Quest quest = questFactory.nextQuest(nextQuestSelectorConfiguration, EASY.toString(), 0);

		theTheFirstQuestIsCreated(quest);
	}

	@Test
	public void givenThreeQuestInCatalogWhenCreateNextQuestForSequenceThreeThenARandomQuestShouldBeCreated() {

		IntStream.range(0, 4).forEach(value -> questFactory.nextQuest(nextQuestSelectorConfiguration, EASY.toString(), value));

		Quest quest = questFactory.nextQuest(nextQuestSelectorConfiguration, EASY.toString(), 4);

		assertThat(expectedQuestIds().contains(quest.getQuestId())).isTrue();
		assertThat(quest.getRefreshTimeInSeconds()).isEqualTo(0);
		assertThat(quest.getState()).isEqualTo(Quest.INITIAL_STATE);
	}

	@Test
	public void givenACurrentDailyQuestThenDailyQuestIsCreated() {

		Quest dailyQuest = questFactory.currentDailyQuest(dailyQuestSelectorConfiguration);

		thenDailyQuestIsCreated(dailyQuest);
	}

	private DailyQuestSelectorConfiguration createDailyQuestSelectorConfiguration(Catalog catalog) {
		return new DailyQuestSelectorConfiguration(catalog.getDailyQuestCollection().getEntries(), catalog.getGameConstants().getCurrentDailyQuestId());
	}

	private void theTheFirstQuestIsCreated(Quest quest) {
		assertThat(quest.getQuestId()).isEqualTo("quest_ShipsDestroyedQuest_EASY");
		assertThat(quest.getQuestType()).isEqualTo("ShipsDestroyedQuest");
		assertThat(quest.getRefreshTimeInSeconds()).isEqualTo(0);
		assertThat(quest.getState()).isEqualTo(Quest.INITIAL_STATE);
	}

	private void thenDailyQuestIsCreated(Quest quest) {
		assertThat(quest.getQuestId()).isEqualTo("daily_quest_definition_1");
		assertThat(quest.getQuestType()).isEqualTo("ShipsDestroyedQuest");
		assertThat(quest.getRefreshTimeInSeconds()).isGreaterThan(0);
		assertThat(quest.getState()).isEqualTo(Quest.STARTED_STATE);
	}

	private List expectedQuestIds() {
		return Lists.newArrayList("quest_ShipsDestroyedQuest_EASY", "quest_SimpleVictoriesQuest_EASY", "quest_FullScoreVictoriesQuest_EASY");
	}
}
