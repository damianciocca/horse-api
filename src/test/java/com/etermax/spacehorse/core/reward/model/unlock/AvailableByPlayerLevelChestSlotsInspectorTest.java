package com.etermax.spacehorse.core.reward.model.unlock;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chests;
import com.etermax.spacehorse.core.reward.model.unlock.strategies.AvailableByPlayerLevelChestSlotsInspector;
import com.google.common.collect.Lists;

public class AvailableByPlayerLevelChestSlotsInspectorTest {

	private static final int PLAYER_LEVEL_2 = 2;
	private static final int PLAYER_LEVEL_1 = 1;
	private static final int PLAYER_LEVEL_3 = 3;
	private static final int PLAYER_LEVEL_4 = 4;
	private static final int PLAYER_LEVEL_5 = 5;
	private static final int PLAYER_LEVEL_6 = 6;
	private static final int MAX_CHEST_SLOTS = 4;

	private AvailableByPlayerLevelChestSlotsInspector chestSlotInspector;

	@Before
	public void setUp() throws Exception {
		chestSlotInspector = anAvailableChestSlotInspector();
	}

	@Test
	public void emptyChest() throws Exception {
		// given
		Chests emptyChestSlots = anEmptyChestSlots();
		// when
		boolean hasAvailableSlots = chestSlotInspector.hasAvailable(PLAYER_LEVEL_2, emptyChestSlots);
		// then
		assertThat(hasAvailableSlots).isTrue();
	}

	@Test
	public void filledChestSlotsAndPlayerLevelNotReached() throws Exception {
		// given
		Chests aChestSlots = aTwoFilledChestSlots();
		// when
		boolean hasAvailableSlots = chestSlotInspector.hasAvailable(PLAYER_LEVEL_1, aChestSlots);
		// then
		assertThat(hasAvailableSlots).isFalse();
	}

	@Test
	public void filledChestSlotsAndPlayerLevelReachedCase1() throws Exception {
		// given
		Chests aChestSlots = aTwoFilledChestSlots();
		// when
		boolean hasAvailableSlots = chestSlotInspector.hasAvailable(PLAYER_LEVEL_2, aChestSlots);
		// then
		assertThat(hasAvailableSlots).isTrue();
	}

	@Test
	public void filledChestSlotsAndPlayerLevelReachedCase2() throws Exception {
		// given
		Chests aChestSlots = aTwoFilledChestSlots();
		// when
		boolean hasAvailableSlots = chestSlotInspector.hasAvailable(PLAYER_LEVEL_3, aChestSlots);
		// then
		assertThat(hasAvailableSlots).isTrue();
	}

	@Test
	public void filledChestSlotsAndPlayerLevelReachedCase3() throws Exception {
		// given
		Chests aChestSlots = aTwoFilledChestSlots();
		// when
		boolean hasAvailableSlots = chestSlotInspector.hasAvailable(PLAYER_LEVEL_4, aChestSlots);
		// then
		assertThat(hasAvailableSlots).isTrue();
	}

	@Test
	public void filledChestSlotsAndPlayerLevelReachedCase4() throws Exception {
		// given
		Chests aChestSlots = aTwoFilledChestSlots();
		// when
		boolean hasAvailableSlots = chestSlotInspector.hasAvailable(PLAYER_LEVEL_5, aChestSlots);
		// then
		assertThat(hasAvailableSlots).isTrue();
	}

	@Test
	public void filledChestSlotsAndPlayerLevelReachedCase5() throws Exception {
		// given
		Chests aChestSlots = aTwoFilledChestSlots();
		// when
		boolean hasAvailableSlots = chestSlotInspector.hasAvailable(PLAYER_LEVEL_6, aChestSlots);
		// then
		assertThat(hasAvailableSlots).isTrue();
	}

	@Test
	public void filledChestSlotsAndPlayerLevelReachedCase6() throws Exception {
		// given
		Chests aChestSlots = aThreeFilledChestSlots();
		// when
		boolean hasAvailableSlots = chestSlotInspector.hasAvailable(PLAYER_LEVEL_1, aChestSlots);
		// then
		assertThat(hasAvailableSlots).isFalse();
	}

	private Chests aTwoFilledChestSlots() {
		Chests aChestSlots = new Chests();
		aChestSlots.setChests(Lists.newArrayList(aChest(1L), aChest(2L)));
		return aChestSlots;
	}

	private Chests aThreeFilledChestSlots() {
		Chests aChestSlots = new Chests();
		aChestSlots.setChests(Lists.newArrayList(aChest(1L), aChest(2L), aChest(3L)));
		return aChestSlots;
	}

	private Chest aChest(long id) {
		return new Chest(id, "courier_Mysterious", 1, 1);
	}

	private Chests anEmptyChestSlots() {
		return new Chests();
	}

	private AvailableByPlayerLevelChestSlotsInspector anAvailableChestSlotInspector() {
		MaxChestSlotsByPlayerLevel maxChestSlotsByPlayerLevel_0 = new MaxChestSlotsByPlayerLevel(0, 2);
		MaxChestSlotsByPlayerLevel maxChestSlotsByPlayerLevel_2 = new MaxChestSlotsByPlayerLevel(2, 3);
		MaxChestSlotsByPlayerLevel maxChestSlotsByPlayerLevel_5 = new MaxChestSlotsByPlayerLevel(5, 4);
		ChestSlotsConfiguration chestSlotsConfiguration = new ChestSlotsConfiguration(MAX_CHEST_SLOTS,
				Lists.newArrayList(maxChestSlotsByPlayerLevel_0, maxChestSlotsByPlayerLevel_2, maxChestSlotsByPlayerLevel_5));
		return new AvailableByPlayerLevelChestSlotsInspector(chestSlotsConfiguration);
	}
}
