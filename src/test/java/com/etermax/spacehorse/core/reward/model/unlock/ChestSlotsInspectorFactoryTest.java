package com.etermax.spacehorse.core.reward.model.unlock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.reward.model.unlock.strategies.AvailableByPlayerLevelChestSlotsInspector;
import com.etermax.spacehorse.core.reward.model.unlock.strategies.ChestSlotInspectorFactory;
import com.etermax.spacehorse.core.reward.model.unlock.strategies.ChestSlotsInspector;
import com.etermax.spacehorse.core.reward.model.unlock.strategies.DefaultChestSlotsInspector;

public class ChestSlotsInspectorFactoryTest {

	private ChestSlotInspectorFactory chestSlotInspectorFactory;
	private ChestSlotsConfiguration configuration;

	@Before
	public void setUp() throws Exception {
		chestSlotInspectorFactory = new ChestSlotInspectorFactory();
		configuration = mock(ChestSlotsConfiguration.class);
	}

	@Test
	public void whenEnableFeatureByPlayerLvlIsTrueThenTheCreationsShouldBeProperly() throws Exception {
		// given
		boolean enableFeaturesByPlayerLvl = true;
		// when
		ChestSlotsInspector chestSlotsInspector = chestSlotInspectorFactory.create(enableFeaturesByPlayerLvl, configuration);
		// then
		assertThat(chestSlotsInspector).isInstanceOf(AvailableByPlayerLevelChestSlotsInspector.class);
	}

	@Test
	public void whenEnableFeatureByPlayerLvlIsFalseThenTheCreationsShouldBeProperly() throws Exception {
		// given
		boolean enableFeaturesByPlayerLvl = false;
		// when
		ChestSlotsInspector chestSlotsInspector = chestSlotInspectorFactory.create(enableFeaturesByPlayerLvl, configuration);
		// then
		assertThat(chestSlotsInspector).isInstanceOf(DefaultChestSlotsInspector.class);
	}
}
