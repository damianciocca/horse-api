package com.etermax.spacehorse.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.ChestConstants;
import com.etermax.spacehorse.core.reward.model.unlock.strategies.AvailableByPlayerLevelChestSlotsInspector;
import com.etermax.spacehorse.core.reward.model.unlock.MaxChestSlotsByPlayerLevel;
import com.etermax.spacehorse.core.reward.model.unlock.ChestSlotsConfiguration;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.google.common.collect.Lists;

public class PlayerScenarioBuilder {

	private static final String CHEST_TYPE_COURIER_MYSTERIOUS = "courier_Mysterious";
	private static final String FIRST_BATTLE = "FirstBattle";
	private Player player;

	public PlayerScenarioBuilder(String id) {
		this(id, new FixedServerTimeProvider(), ABTag.emptyABTag());
	}

	public PlayerScenarioBuilder(String id, ABTag abTag) {
		this(id, new FixedServerTimeProvider(), abTag);
	}

	public PlayerScenarioBuilder(String id, FixedServerTimeProvider serverTimeProvider) {
		this(id, serverTimeProvider, ABTag.emptyABTag());
	}

	public PlayerScenarioBuilder(String id, FixedServerTimeProvider serverTimeProvider, ABTag abTag) {
		NewPlayerConfiguration newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
		this.player = Player.buildNewPlayer(id, abTag, serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
	}

	public PlayerScenarioBuilder withDefaultChest() {
		return withChest(CHEST_TYPE_COURIER_MYSTERIOUS);
	}

	public PlayerScenarioBuilder withChest(String chestId) {
		ChestConstants chestConstants = aChestConstants();
		player.getInventory().getChests().addChest(chestId, 1, chestConstants, 0,
				new AvailableByPlayerLevelChestSlotsInspector(getChestSlotsConfiguration()));
		return this;
	}

	public PlayerScenarioBuilder withGold(int gold) {
		player.getInventory().getGold().setAmount(gold);
		return this;
	}

	public PlayerScenarioBuilder withGems(int gems) {
		player.getInventory().getGems().setAmount(gems);
		return this;
	}

	public PlayerScenarioBuilder withLevel(int level) {
		player.getProgress().setLevel(level);
		return this;
	}

	public PlayerScenarioBuilder withActiveTutorial() {
		player.getTutorialProgress().setActiveTutorial(FIRST_BATTLE);
		return this;
	}

	public Player build() {
		return player;
	}

	private ChestConstants aChestConstants() {
		ChestConstants chestConstants = mock(ChestConstants.class);
		when(chestConstants.getMaxChests()).thenReturn(4);
		return chestConstants;
	}

	public PlayerScenarioBuilder withMapNumber(int mapNumber) {
		player.setMapNumber(mapNumber);
		return this;
	}

	private ChestSlotsConfiguration getChestSlotsConfiguration() {
		return new ChestSlotsConfiguration(4, Lists.newArrayList(new MaxChestSlotsByPlayerLevel(0, 4)));
	}
}
