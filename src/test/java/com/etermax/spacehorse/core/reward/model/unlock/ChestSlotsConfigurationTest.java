package com.etermax.spacehorse.core.reward.model.unlock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.mock.MockUtils;

public class ChestSlotsConfigurationTest {

	@Test
	public void whenCreateAConfigurationFromCatalogThenTheMaxAvailableChestSlotsByPlayerLvlIsThree() throws Exception {
		// given
		Catalog catalog = MockUtils.mockCatalog();
		// when
		ChestSlotsConfiguration chestSlotsConfiguration = ChestSlotsConfiguration.create(
				catalog.getFeatureByPlayerLvlDefinitionCollection().getEntries(), catalog.getGameConstants().getMaxChests());
		// then
		thenAssertThatChestSlotConfigurationWasCreatedSuccessfully(chestSlotsConfiguration);
	}

	private void thenAssertThatChestSlotConfigurationWasCreatedSuccessfully(ChestSlotsConfiguration chestSlotsConfiguration) {
		assertThat(chestSlotsConfiguration.getMaxAvailableChestSlotsByPlayerLevel()).hasSize(3);
		assertThat(chestSlotsConfiguration.getMaxAvailableChestSlotsByPlayerLevel())
				.extracting(MaxChestSlotsByPlayerLevel::getPlayerLevel, MaxChestSlotsByPlayerLevel::getMaxSlotsAvailable)
				.containsExactly(tuple(0, 2), tuple(3, 3), tuple(5, 4));
		assertThat(chestSlotsConfiguration.getMaxChestSlots()).isEqualTo(4);
	}
}
