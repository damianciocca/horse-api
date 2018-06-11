package com.etermax.spacehorse.core.quest.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.QuestCycleEntry;
import com.etermax.spacehorse.core.catalog.model.QuestCycleList;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.mock.MockCatalog;

public class QuestCycleSequenceMapperTest {

	public static final String QUEST_LIST_ID = "ListA";
	private CatalogEntriesCollection<QuestCycleList> questCycleCollection;

	@Before
	public void setUp() throws Exception {
		Catalog catalog = MockCatalog.buildCatalog();
		questCycleCollection = catalog.getQuestCycleListCollection();
	}

	@Test
	public void whenMapQuestCyclesFromEasyQuestDifficultyThenTheQuestCycleEntriesWasFound() throws Exception {

		QuestCycleSequenceMapper questCycleSequenceMapper = new QuestCycleSequenceMapper();

		List<QuestCycleEntry> questCycleEntries = questCycleSequenceMapper
				.mapQuestCyclesFrom(QuestDifficultyType.EASY.toString(), QUEST_LIST_ID, questCycleCollection);

		assertThat(questCycleEntries).extracting(QuestCycleEntry::getQuestDefinition).extracting(QuestDefinition::getId)
				.containsExactly("quest_ShipsDestroyedQuest_EASY", "quest_SimpleVictoriesQuest_EASY", "quest_FullScoreVictoriesQuest_EASY");

	}

	@Test
	public void whenMapQuestCyclesFromMediumQuestDifficultyThenTheQuestCycleEntriesWasFound() throws Exception {

		QuestCycleSequenceMapper questCycleSequenceMapper = new QuestCycleSequenceMapper();

		List<QuestCycleEntry> questCycleEntries = questCycleSequenceMapper
				.mapQuestCyclesFrom(QuestDifficultyType.MEDIUM.toString(), QUEST_LIST_ID, questCycleCollection);

		assertThat(questCycleEntries).extracting(QuestCycleEntry::getQuestDefinition).extracting(QuestDefinition::getId)
				.containsExactly("quest_ShipsDestroyedQuest_MEDIUM", "quest_SimpleVictoriesQuest_MEDIUM", "quest_FullScoreVictoriesQuest_MEDIUM");

	}

	@Test
	public void whenMapQuestCyclesFromHardQuestDifficultyThenTheQuestCycleEntriesWasFound() throws Exception {

		QuestCycleSequenceMapper questCycleSequenceMapper = new QuestCycleSequenceMapper();

		List<QuestCycleEntry> questCycleEntries = questCycleSequenceMapper
				.mapQuestCyclesFrom(QuestDifficultyType.HARD.toString(), QUEST_LIST_ID, questCycleCollection);

		assertThat(questCycleEntries).extracting(QuestCycleEntry::getQuestDefinition).extracting(QuestDefinition::getId)
				.containsExactly("quest_ShipsDestroyedQuest_HARD", "quest_SimpleVictoriesQuest_HARD", "quest_FullScoreVictoriesQuest_HARD");

	}
}
