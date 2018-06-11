package com.etermax.spacehorse.core.player.model.chest;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.tuple;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.ChestList;
import com.etermax.spacehorse.core.catalog.model.ChestListEntry;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chests;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.model.unlock.MaxChestSlotsByPlayerLevel;
import com.etermax.spacehorse.core.reward.model.unlock.ChestSlotsConfiguration;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class ApplyRewardDomainServiceTest {

	private static final String CHEST_ID = "courier_Free";
	private static final String LOGIN_ID = "1";
	private static final int MAX_CHEST_SLOTS = 4;

	private Player player;
	private ApplyRewardDomainService applyRewardDomainService;

	@Before
	public void setUp() throws Exception {
		player = new PlayerScenarioBuilder(LOGIN_ID).withDefaultChest().build();
		applyRewardDomainService = new ApplyRewardDomainService();
	}

	@Test
	public void givenAnEmptyRewardsWhenApplyRewardsThenTheResponseShouldBeEmpty() throws Exception {
		ApplyRewardConfiguration configuration = givenAConfiguration();
		List<Reward> rewards = givenAnEmptyRewards();

		List<RewardResponse> rewardResponses = whenApplyRewards(configuration, rewards);

		thenTheRewardResponseIsEmpty(rewardResponses);
	}

	@Test
	public void givenAGemsRewardsWhenApplyRewardsThenTheResponseShouldHaveGems() throws Exception {
		ApplyRewardConfiguration configuration = givenAConfiguration();
		List<Reward> rewards = givenGemsAsRewards();
		int gemsBeforeApplyRewards = givenInitialGems();

		List<RewardResponse> rewardResponses = whenApplyRewards(configuration, rewards);

		thenTheRewardResponseContainsGems(rewardResponses, gemsBeforeApplyRewards);
	}

	@Test
	public void givenAGemsAndGoldRewardsWhenApplyRewardsThenTheResponseShouldHaveGemsAndGold() throws Exception {
		ApplyRewardConfiguration configuration = givenAConfiguration();
		List<Reward> rewards = givenGemsAndGoldAsRewards();
		int gemsBeforeApplyRewards = givenInitialGems();
		int goldBeforeApplyRewards = givenInitialGold();

		List<RewardResponse> rewardResponses = whenApplyRewards(configuration, rewards);

		thenTheRewardResponseContainsGemsAndGold(rewardResponses, gemsBeforeApplyRewards, goldBeforeApplyRewards);
	}

	@Test
	public void givenAGemsAndChestRewardsWhenApplyRewardsThenTheResponseShouldHaveGemsAndChest() throws Exception {
		ApplyRewardConfiguration configuration = givenAConfiguration();
		List<Reward> rewards = givenGemsAndChestAsRewards();

		List<RewardResponse> rewardResponses = whenApplyRewards(configuration, rewards);

		thenTheRewardResponseContainsGemsAndChest(rewardResponses);
	}

	@Test
	public void givenANextChestRewardsWhenApplyRewardsThenTheResponseShouldHaveNextChest() throws Exception {
		ApplyRewardConfiguration configuration = givenAConfiguration();
		List<Reward> rewards = givenANextChestAsRewards();

		List<RewardResponse> rewardResponses = whenApplyRewards(configuration, rewards);

		thenTheRewardResponseContainsNextChest(rewardResponses);
	}

	@Test
	public void givenACompleteChestBoardWhenApplyRewardsThenShouldNotBeApplied() throws Exception {
		ApplyRewardConfiguration configuration = givenAConfiguration();
		List<Reward> rewards = givenANextChestAsRewards();
		givenACompleteChestBoard(configuration, rewards);
		Chests chestBeforeApplyRewards = givenAChestBoardBeforeApplyRewards();

		List<RewardResponse> rewardResponses = whenApplyRewards(configuration, rewards);

		thenTheRewardsShouldNoBeUpdated(chestBeforeApplyRewards, rewardResponses);

	}

	private void givenACompleteChestBoard(ApplyRewardConfiguration configuration, List<Reward> rewards) {
		whenApplyRewards(configuration, rewards);
		whenApplyRewards(configuration, rewards);
		whenApplyRewards(configuration, rewards);
		whenApplyRewards(configuration, rewards);
	}

	private void thenTheRewardsShouldNoBeUpdated(Chests chestBeforeApplyRewards, List<RewardResponse> rewardResponses) {
		assertThat(rewardResponses).hasSize(0);
		assertThat(player.getInventory().getChests()).isEqualToComparingFieldByField(chestBeforeApplyRewards);
	}

	private ApplyRewardConfiguration givenAConfiguration() {
		ApplyRewardConfiguration configuration = mock(ApplyRewardConfiguration.class);
		aGameConstants(configuration);
		aChestDefinition(configuration);
		aCardDefinition(configuration);
		aChestListDefinition(configuration);
		anUnlockableChestSlots(configuration);
		return configuration;
	}

	private Chests givenAChestBoardBeforeApplyRewards() {
		return player.getInventory().getChests();
	}

	private List<Reward> givenAnEmptyRewards() {
		return newArrayList();
	}

	private List<Reward> givenANextChestAsRewards() {
		return newArrayList(new Reward(RewardType.NEXT_CHEST, CHEST_ID));
	}

	private List<Reward> givenGemsAsRewards() {
		return newArrayList(new Reward(RewardType.GEMS, 10));
	}

	private List<Reward> givenGemsAndChestAsRewards() {
		return newArrayList(new Reward(RewardType.GEMS, 10), new Reward(RewardType.CHEST, "100"));
	}

	private List<Reward> givenGemsAndGoldAsRewards() {
		return newArrayList(new Reward(RewardType.GEMS, 10), new Reward(RewardType.GOLD, 70));
	}

	private int givenInitialGems() {
		return player.getInventory().getGems().getAmount();
	}

	private int givenInitialGold() {
		return player.getInventory().getGold().getAmount();
	}

	private void aGameConstants(ApplyRewardConfiguration configuration) {
		GameConstants gameConstants = mock(GameConstants.class);
		when(configuration.getGameConstants()).thenReturn(gameConstants);
		when(configuration.getGameConstants().getMaxChests()).thenReturn(MAX_CHEST_SLOTS);
		when(configuration.getGameConstants().getDefaultFreeGemsCycleSequenceId()).thenReturn("1");
	}

	private void aChestDefinition(ApplyRewardConfiguration configuration) {
		CatalogEntriesCollection cardDefinitionCollection = mock(CatalogEntriesCollection.class);

		ChestDefinition chestDefinition = mock(ChestDefinition.class);
		when(chestDefinition.getId()).thenReturn(CHEST_ID);

		when(cardDefinitionCollection.findById(any())).thenReturn(Optional.of(chestDefinition));
		when(configuration.getChestDifinition()).thenReturn(cardDefinitionCollection);
	}

	private void aCardDefinition(ApplyRewardConfiguration configuration) {
		CatalogEntriesCollection cardDefinitionCollection = mock(CatalogEntriesCollection.class);
		CardDefinition cardDefinition = mock(CardDefinition.class);
		when(cardDefinitionCollection.findById(any())).thenReturn(Optional.of(cardDefinition));
		when(configuration.getCardDefinition()).thenReturn(cardDefinitionCollection);
	}

	private void aChestListDefinition(ApplyRewardConfiguration configuration) {
		CatalogEntriesCollection chestListCollection = mock(CatalogEntriesCollection.class);

		ChestListEntry chestListEntry = mock(ChestListEntry.class);
		when(chestListEntry.getSequenceOrder()).thenReturn(0);
		when(chestListEntry.getChestId()).thenReturn(CHEST_ID);
		ChestList chestList = new ChestList(CHEST_ID, newArrayList(chestListEntry));

		when(chestListCollection.findById(any())).thenReturn(Optional.of(chestList));
		when(configuration.getChestList()).thenReturn(chestListCollection);
	}

	private void anUnlockableChestSlots(ApplyRewardConfiguration configuration) {
		ChestSlotsConfiguration unbockableChestSlotsConfiguration = new ChestSlotsConfiguration(MAX_CHEST_SLOTS,
				newArrayList(new MaxChestSlotsByPlayerLevel(0, MAX_CHEST_SLOTS)));
		when(configuration.getChestSlotsConfiguration()).thenReturn(unbockableChestSlotsConfiguration);
	}

	private List<RewardResponse> whenApplyRewards(ApplyRewardConfiguration configuration, List<Reward> rewards) {
		return applyRewardDomainService.applyRewards(player, rewards, RewardContext.fromMapNumber(1), configuration, newArrayList());
	}

	private void thenTheRewardResponseContainsGems(List<RewardResponse> rewardResponses, int gemsBeforeApplyRewards) {
		assertThat(rewardResponses).extracting(RewardResponse::getRewardType, RewardResponse::getAmount).containsExactly(tuple(RewardType.GEMS, 10));
		assertGems(gemsBeforeApplyRewards);
	}

	private void thenTheRewardResponseContainsGemsAndGold(List<RewardResponse> rewardResponses, int gemsBeforeApplyRewards,
			int goldBeforeApplyRewards) {
		assertThat(rewardResponses).extracting(RewardResponse::getRewardType, RewardResponse::getAmount)
				.containsExactly(tuple(RewardType.GEMS, 10), tuple(RewardType.GOLD, 70));
		assertGems(gemsBeforeApplyRewards);
		assertGold(goldBeforeApplyRewards);
	}

	private void thenTheRewardResponseContainsGemsAndChest(List<RewardResponse> rewardResponses) {
		assertThat(rewardResponses).extracting(RewardResponse::getRewardType, RewardResponse::getAmount)
				.containsExactly(tuple(RewardType.GEMS, 10), tuple(RewardType.CHEST, 1));
	}

	private void thenTheRewardResponseContainsNextChest(List<RewardResponse> rewardResponses) {
		long rewardId = 2L;
		int slotNumber = 1;
		assertThat(rewardResponses).extracting(RewardResponse::getChest)
				.extracting(ChestResponse::getChestType, ChestResponse::getId, ChestResponse::getSlotNumber)
				.containsExactly(tuple(CHEST_ID, rewardId, slotNumber));
	}

	private void assertGems(int gemsBeforeApplyRewards) {
		assertThat(player.getInventory().getGems().getAmount()).isNotEqualTo(gemsBeforeApplyRewards);
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(110);
	}

	private void assertGold(int goldBeforeApplyRewards) {
		assertThat(player.getInventory().getGold().getAmount()).isNotEqualTo(goldBeforeApplyRewards);
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(170);
	}

	private void thenTheRewardResponseIsEmpty(List<RewardResponse> rewardResponses) {
		assertThat(rewardResponses).isEmpty();
	}
}
