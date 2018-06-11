package com.etermax.spacehorse.core.quest.model.unlock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import java.util.List;

import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.mock.MockUtils;

public class QuestSlotDifficultiesConfigurationTest {

	private static final int PLAYER_LEVEL_0 = 0;
	private static final int PLAYER_LEVEL_4 = 4;
	private static final int PLAYER_LEVEL_7 = 7;

	@Test
	public void whenCreateAConfigurationFromCatalogThenTheConfigurationWasCreatedSuccessfully() throws Exception {
		// given
		Catalog catalog = MockUtils.mockCatalog();
		// when
		QuestSlotDifficultiesConfiguration configuration = QuestSlotDifficultiesConfiguration
				.create(catalog.getFeatureByPlayerLvlDefinitionCollection().getEntries());
		// then
		thenAssertThatConfigurationWasCreatedSuccessfully(configuration);
	}

	private void thenAssertThatConfigurationWasCreatedSuccessfully(QuestSlotDifficultiesConfiguration configuration) {
		assertThat(configuration.getAvailableQuestDifficultiesByPlayerLvl()).hasSize(3);
		assertThat(configuration.getAvailableQuestDifficultiesByPlayerLvl())//
				.extracting( //
						AvailableQuestSlotDifficultiesByPlayerLevel::getPlayerLevel, //
						AvailableQuestSlotDifficultiesByPlayerLevel::getAvailableSlotDifficulties)//
				.containsExactly(//
						tuple(PLAYER_LEVEL_0, expectedDifficultiesForLvl0()), //
						tuple(PLAYER_LEVEL_4, expectedDifficultiesForLvl4()),//
						tuple(PLAYER_LEVEL_7, expectedDifficultiesForLvl7()));
	}

	private List<QuestDifficultyType> expectedDifficultiesForLvl0() {
		return QuestDifficultyType.getTypesUpTo(QuestDifficultyType.EASY.toString());
	}

	private List<QuestDifficultyType> expectedDifficultiesForLvl4() {
		return QuestDifficultyType.getTypesUpTo(QuestDifficultyType.MEDIUM.toString());
	}

	private List<QuestDifficultyType> expectedDifficultiesForLvl7() {
		return QuestDifficultyType.getTypesUpTo(QuestDifficultyType.HARD.toString());
	}
}
