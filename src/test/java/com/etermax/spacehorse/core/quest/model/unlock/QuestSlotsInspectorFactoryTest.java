package com.etermax.spacehorse.core.quest.model.unlock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.quest.model.unlock.strategies.AvailableByPlayerLevelQuestSlotsInspector;
import com.etermax.spacehorse.core.quest.model.unlock.strategies.DefaultQuestSlotsInspector;
import com.etermax.spacehorse.core.quest.model.unlock.strategies.QuestSlotsInspector;
import com.etermax.spacehorse.core.quest.model.unlock.strategies.QuestSlotsInspectorFactory;

public class QuestSlotsInspectorFactoryTest {

	private QuestSlotsInspectorFactory questSlotsInspectorFactory;
	private QuestSlotDifficultiesConfiguration configuration;

	@Before
	public void setUp() throws Exception {
		questSlotsInspectorFactory = new QuestSlotsInspectorFactory();
		configuration = mock(QuestSlotDifficultiesConfiguration.class);
	}

	@Test
	public void whenEnableFeatureByPlayerLvlIsTrueThenTheCreationsShouldBeProperly() throws Exception {
		// given
		boolean enableFeaturesByPlayerLvl = true;
		// when
		QuestSlotsInspector questSlotsInspector = questSlotsInspectorFactory.create(enableFeaturesByPlayerLvl, configuration);
		// then
		assertThat(questSlotsInspector).isInstanceOf(AvailableByPlayerLevelQuestSlotsInspector.class);
	}

	@Test
	public void whenEnableFeatureByPlayerLvlIsFalseThenTheCreationsShouldBeProperly() throws Exception {
		// given
		boolean enableFeaturesByPlayerLvl = false;
		// when
		QuestSlotsInspector questSlotsInspector = questSlotsInspectorFactory.create(enableFeaturesByPlayerLvl, configuration);
		// then
		assertThat(questSlotsInspector).isInstanceOf(DefaultQuestSlotsInspector.class);
	}
}
