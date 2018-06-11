package com.etermax.spacehorse.core.reward.model.strategies;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.CardsDropRate;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestChancesDefinition;
import com.etermax.spacehorse.core.catalog.model.ChestChancesDefinitionBuilder;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.model.inventory.cardparts.CardParts;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.RandomCardPartsRewardStrategy;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.RandomCardPartsRewardStrategyConfiguration;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils.CardDropRateCalculatorConfiguration;
import com.etermax.spacehorse.mock.MockUtils;

public class RandomCardPartsRewardStrategyTest {

	private Player player;

	private CardDropRateCalculatorConfiguration cardDropRateCalculatorConfiguration;

	private List<CardsDropRate> cardsDropRateCollection;

	public RandomCardPartsRewardStrategyTest() {
		this.cardDropRateCalculatorConfiguration = new CardDropRateCalculatorConfiguration(2, 2, 5, 45, 3);
		PlayerRepository playerRepository = mock(PlayerRepository.class);
		this.player = MockUtils.mockPlayerWithGems("id", 500, playerRepository);

		Inventory inventory = mock(Inventory.class);
		when(player.getInventory()).thenReturn(inventory);
		CardParts cardParts = mock(CardParts.class);
		when(inventory.getCardParts()).thenReturn(cardParts);

		Map<String, Integer> amounts = new HashMap();
		amounts.put("card_fighter", 900);
		amounts.put("card_bomber", 60);
		amounts.put("card_corvette", 900);
		amounts.put("card_corvette_bomber", 155);
		amounts.put("card_frigate", 1300);
		amounts.put("card_cruiser", 150);
		amounts.put("card_laser", 50);
		amounts.put("card_missile", 50);
		amounts.put("card_decoy", 300);
		when(cardParts.getAmounts()).thenReturn(amounts);

		Deck deck = mock(Deck.class);
		when(player.getDeck()).thenReturn(deck);
		when(deck.isCardSelected("card_fighter")).thenReturn(true);
		when(deck.isCardSelected("card_bomber")).thenReturn(false);
		when(deck.isCardSelected("card_corvette")).thenReturn(true);
		when(deck.isCardSelected("card_corvette_bomber")).thenReturn(true);
		when(deck.isCardSelected("card_frigate")).thenReturn(false);
		when(deck.isCardSelected("card_cruiser")).thenReturn(true);
		when(deck.isCardSelected("card_laser")).thenReturn(true);
		when(deck.isCardSelected("card_missile")).thenReturn(false);
		when(deck.isCardSelected("card_decoy")).thenReturn(false);

		cardsDropRateCollection = new ArrayList();
		cardsDropRateCollection.add(new CardsDropRate("1", "card_fighter", 0, 980));
		cardsDropRateCollection.add(new CardsDropRate("2", "card_bomber", 0, 50));
		cardsDropRateCollection.add(new CardsDropRate("3", "card_corvette", 0, 980));
		cardsDropRateCollection.add(new CardsDropRate("4", "card_corvette_bomber", 0, 155));
		cardsDropRateCollection.add(new CardsDropRate("5", "card_frigate", 0, 980));
		cardsDropRateCollection.add(new CardsDropRate("6", "card_cruiser", 0, 155));
		cardsDropRateCollection.add(new CardsDropRate("7", "card_laser", 0, 50));
		cardsDropRateCollection.add(new CardsDropRate("8", "card_missile", 0, 50));
		cardsDropRateCollection.add(new CardsDropRate("9", "card_decoy", 0, 155));
	}

	@Test
	public void testNoRewardsForNullValues() {
		// given
		RandomCardPartsRewardStrategyConfiguration chestRewardStrategyParams = mock(RandomCardPartsRewardStrategyConfiguration.class);
		RandomCardPartsRewardStrategy strategy = new RandomCardPartsRewardStrategy(player, player.getMapNumber(), chestRewardStrategyParams);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertEquals(0, rewards.size());
	}

	@Test
	public void testRetrieveCommonCardsRewards() {
		// given
		final int common = 21;
		final int commonParts = 5;

		ChestChancesDefinition chestChancesDefinition = new ChestChancesDefinitionBuilder().setMapNumber(0).setCommon(common)
				.setCommonMaxPartitions(commonParts).create();
		CatalogEntriesCollection<ChestChancesDefinition> catalogEntriesCollection = new CatalogEntriesCollection();
		catalogEntriesCollection.addEntry(chestChancesDefinition);

		List<CardDefinition> cardsDefinitionsList = MockUtils.mockCardsDefinitionsListWithTwoRarities(CardRarity.COMMON, CardRarity.RARE);

		RandomCardPartsRewardStrategyConfiguration chestRewardStrategyParams = new RandomCardPartsRewardStrategyConfiguration(cardsDefinitionsList,
				cardDropRateCalculatorConfiguration, cardsDropRateCollection, chestChancesDefinition, false);

		RandomCardPartsRewardStrategy strategy = new RandomCardPartsRewardStrategy(player, player.getMapNumber(), chestRewardStrategyParams);

		// when
		List<Reward> rewards = strategy.getRewards();

		// then
		assertTrue(rewards.size() > 0 && rewards.size() <= commonParts);
		int sum = rewards.stream().mapToInt(Reward::getAmount).sum();
		assertEquals(common, sum);
		thenAllCardRewardsAre(rewards, cardsDefinitionsList, CardRarity.COMMON);
	}

	@Test
	public void testRetrieveRareCardsRewards() {
		// given
		final int rare = 101;
		final int rareParts = 3;
		final int rareAdditional = 10;
		final int rareAdditionalChance = 90;
		ChestChancesDefinition chestChancesDefinition = new ChestChancesDefinitionBuilder().setMapNumber(0).setRare(rare)
				.setRareMaxPartitions(rareParts).setRareAdditionalAmount(rareAdditional).setRareAdditionalChances(rareAdditionalChance).create();
		CatalogEntriesCollection<ChestChancesDefinition> catalogEntriesCollection = new CatalogEntriesCollection();
		catalogEntriesCollection.addEntry(chestChancesDefinition);

		List<CardDefinition> cardsDefinitionsList = MockUtils.mockCardsDefinitionsListWithTwoRarities(CardRarity.RARE, CardRarity.COMMON);

		RandomCardPartsRewardStrategyConfiguration chestRewardStrategyParams = new RandomCardPartsRewardStrategyConfiguration(cardsDefinitionsList,
				cardDropRateCalculatorConfiguration, cardsDropRateCollection, chestChancesDefinition, false);
		RandomCardPartsRewardStrategy strategy = new RandomCardPartsRewardStrategy(player, player.getMapNumber(), chestRewardStrategyParams);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertTrue(1 <= rewards.size() && rewards.size() <= rareParts);
		int sum = rewards.stream().mapToInt(Reward::getAmount).sum();
		assertTrue(rare == sum || sum == (rare + rareAdditional));
		thenAllCardRewardsAre(rewards, cardsDefinitionsList, CardRarity.RARE);
	}

	@Test
	public void testRetrieveEpicCardsRewards() {
		// given
		final int epic = 5;
		final int epicParts = 2;
		final int epicAdditional = 1;
		final int epicAdditionalChance = 10;
		ChestChancesDefinition chestChancesDefinition = new ChestChancesDefinitionBuilder().setMapNumber(0).setEpic(epic)
				.setEpicMaxPartitions(epicParts).setEpicAdditionalAmount(epicAdditional).setEpicAdditionalChances(epicAdditionalChance).create();
		CatalogEntriesCollection<ChestChancesDefinition> catalogEntriesCollection = new CatalogEntriesCollection();
		catalogEntriesCollection.addEntry(chestChancesDefinition);

		List<CardDefinition> cardsDefinitionsList = MockUtils.mockCardsDefinitionsListWithTwoRarities(CardRarity.EPIC, CardRarity.COMMON);

		RandomCardPartsRewardStrategyConfiguration chestRewardStrategyParams = new RandomCardPartsRewardStrategyConfiguration(cardsDefinitionsList,
				cardDropRateCalculatorConfiguration, cardsDropRateCollection, chestChancesDefinition, false);
		RandomCardPartsRewardStrategy strategy = new RandomCardPartsRewardStrategy(player, player.getMapNumber(), chestRewardStrategyParams);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertThat(rewards.size()).isGreaterThan(0).isLessThanOrEqualTo(epicParts);
		int sum = rewards.stream().mapToInt(Reward::getAmount).sum();
		assertTrue(sum == epic || sum == (epic + epicAdditional));
		thenAllCardRewardsAre(rewards, cardsDefinitionsList, CardRarity.EPIC);
	}

	@Test
	public void testRetrieveLegendaryCardsRewards() {
		// given
		final int legendary = 5;
		final int legendaryParts = 2;
		final int legendaryAdditional = 0;
		final int legendaryAdditionalChance = 0;
		ChestChancesDefinition chestChancesDefinition = new ChestChancesDefinitionBuilder().setMapNumber(0).setLegendary(legendary)
				.setLegendaryMaxPartitions(legendaryParts).setLegendaryAdditionalAmount(legendaryAdditional)
				.setLegendaryAdditionalChances(legendaryAdditionalChance).create();
		CatalogEntriesCollection<ChestChancesDefinition> catalogEntriesCollection = new CatalogEntriesCollection();
		catalogEntriesCollection.addEntry(chestChancesDefinition);

		List<CardDefinition> cardsDefinitionsList = MockUtils.mockCardsDefinitionsListWithTwoRarities(CardRarity.LEGENDARY, CardRarity.COMMON);

		RandomCardPartsRewardStrategyConfiguration chestRewardStrategyParams = new RandomCardPartsRewardStrategyConfiguration(cardsDefinitionsList,
				cardDropRateCalculatorConfiguration, cardsDropRateCollection, chestChancesDefinition, false);
		RandomCardPartsRewardStrategy strategy = new RandomCardPartsRewardStrategy(player, player.getMapNumber(), chestRewardStrategyParams);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertThat(rewards.size()).isGreaterThan(0).isLessThanOrEqualTo(legendaryParts);
		int sum = rewards.stream().mapToInt(Reward::getAmount).sum();
		assertTrue(sum == legendary);
		thenAllCardRewardsAre(rewards, cardsDefinitionsList, CardRarity.LEGENDARY);
	}

	@Test
	public void testRetrieveLegendaryCardsRewardsWithAdditional() {
		// given
		final int legendary = 5;
		final int legendaryParts = 2;
		final int legendaryAdditional = 2;
		final int legendaryAdditionalChance = 10000;
		ChestChancesDefinition chestChancesDefinition = new ChestChancesDefinitionBuilder().setMapNumber(0).setLegendary(legendary)
				.setLegendaryMaxPartitions(legendaryParts).setLegendaryAdditionalAmount(legendaryAdditional)
				.setLegendaryAdditionalChances(legendaryAdditionalChance).create();
		CatalogEntriesCollection<ChestChancesDefinition> catalogEntriesCollection = new CatalogEntriesCollection();
		catalogEntriesCollection.addEntry(chestChancesDefinition);

		List<CardDefinition> cardsDefinitionsList = MockUtils.mockCardsDefinitionsListWithTwoRarities(CardRarity.LEGENDARY, CardRarity.COMMON);

		RandomCardPartsRewardStrategyConfiguration chestRewardStrategyParams = new RandomCardPartsRewardStrategyConfiguration(cardsDefinitionsList,
				cardDropRateCalculatorConfiguration, cardsDropRateCollection, chestChancesDefinition, false);
		RandomCardPartsRewardStrategy strategy = new RandomCardPartsRewardStrategy(player, player.getMapNumber(), chestRewardStrategyParams);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertThat(rewards.size()).isGreaterThan(0).isLessThanOrEqualTo(legendaryParts);
		int sum = rewards.stream().mapToInt(Reward::getAmount).sum();
		assertTrue(sum == legendary + legendaryAdditional);
		thenAllCardRewardsAre(rewards, cardsDefinitionsList, CardRarity.LEGENDARY);
	}

	@Test
	public void testLowerRaritiesParititonsAreDisacaredWhenMaxPartitionsIsExceeded() {
		ChestChancesDefinition chestChancesDefinition = new ChestChancesDefinitionBuilder().setMapNumber(0).setCommon(100)
				.setCommonMaxPartitions(1000).setLegendary(100).setLegendaryMinPartitions(ChestChancesDefinition.MAX_PARTITIONS)
				.setLegendaryMaxPartitions(ChestChancesDefinition.MAX_PARTITIONS).create();

		List<CardDefinition> cardsDefinitionsList = MockUtils.mockCardsDefinitionsListWithTwoRarities(CardRarity.LEGENDARY, CardRarity.COMMON);

		RandomCardPartsRewardStrategyConfiguration chestRewardStrategyParams = new RandomCardPartsRewardStrategyConfiguration(cardsDefinitionsList,
				cardDropRateCalculatorConfiguration, cardsDropRateCollection, chestChancesDefinition, false);
		RandomCardPartsRewardStrategy strategy = new RandomCardPartsRewardStrategy(player, player.getMapNumber(), chestRewardStrategyParams);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertThat(rewards.size()).isEqualTo(ChestChancesDefinition.MAX_PARTITIONS);
		int sum = rewards.stream().mapToInt(Reward::getAmount).sum();
		assertThat(sum).isEqualTo(100);
		thenAllCardRewardsAre(rewards, cardsDefinitionsList, CardRarity.LEGENDARY);
	}

	private void thenAllCardRewardsAre(List<Reward> rewards, List<CardDefinition> cardsDefinitionsList, CardRarity rarity) {
		for (Reward reward : rewards) {
			assertThat(cardsDefinitionsList.stream().filter(x -> x.getId().equals(reward.getRewardId())).findFirst().get().getCardRarity())
					.isEqualByComparingTo(rarity);
		}
	}

	@Test
	public void testRetrieveEpicCardsRewardsWithZeroEpicAndAdditionalChance() {
		// given
		final int epic = 0;
		final int epicParts = 1;
		final int epicAdditional = 1;
		final int epicAdditionalChance = 100;
		ChestChancesDefinition chestChancesDefinition = new ChestChancesDefinitionBuilder().setMapNumber(0).setEpic(epic)
				.setEpicMaxPartitions(epicParts).setEpicAdditionalAmount(epicAdditional).setEpicAdditionalChances(epicAdditionalChance).create();
		CatalogEntriesCollection<ChestChancesDefinition> catalogEntriesCollection = new CatalogEntriesCollection();
		catalogEntriesCollection.addEntry(chestChancesDefinition);

		List<CardDefinition> cardsDefinitionsList = MockUtils.mockCardsDefinitionsListWithTwoRarities(CardRarity.EPIC, CardRarity.COMMON);

		RandomCardPartsRewardStrategyConfiguration chestRewardStrategyParams = new RandomCardPartsRewardStrategyConfiguration(cardsDefinitionsList,
				cardDropRateCalculatorConfiguration, cardsDropRateCollection, chestChancesDefinition, false);
		RandomCardPartsRewardStrategy strategy = new RandomCardPartsRewardStrategy(player, player.getMapNumber(), chestRewardStrategyParams);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertTrue(1 <= rewards.size() && rewards.size() <= epicParts);
		int sum = rewards.stream().mapToInt(reward -> reward.getAmount()).sum();
		assertTrue(sum == epic || sum == (epic + epicAdditional));
		thenAllCardRewardsAre(rewards, cardsDefinitionsList, CardRarity.EPIC);
	}

	@Test
	public void testRetrieveEpicCardsRewardsWithZeroEpicAndAdditionalChanceZero() {
		// given
		final int epic = 0;
		final int epicParts = 1;
		final int epicAdditional = 1;
		final int epicAdditionalChance = 0;
		ChestChancesDefinition chestChancesDefinition = new ChestChancesDefinitionBuilder().setMapNumber(0).setEpic(epic)
				.setEpicMaxPartitions(epicParts).setEpicAdditionalAmount(epicAdditional).setEpicAdditionalChances(epicAdditionalChance).create();
		CatalogEntriesCollection<ChestChancesDefinition> catalogEntriesCollection = new CatalogEntriesCollection();
		catalogEntriesCollection.addEntry(chestChancesDefinition);

		List<CardDefinition> cardsDefinitionsList = MockUtils.mockCardsDefinitionsListWithTwoRarities(CardRarity.EPIC, CardRarity.COMMON);

		RandomCardPartsRewardStrategyConfiguration chestRewardStrategyParams = new RandomCardPartsRewardStrategyConfiguration(cardsDefinitionsList,
				cardDropRateCalculatorConfiguration, cardsDropRateCollection, chestChancesDefinition, false);

		RandomCardPartsRewardStrategy strategy = new RandomCardPartsRewardStrategy(player, player.getMapNumber(), chestRewardStrategyParams);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertTrue(0 <= rewards.size() && rewards.size() <= epicParts);
	}
}
