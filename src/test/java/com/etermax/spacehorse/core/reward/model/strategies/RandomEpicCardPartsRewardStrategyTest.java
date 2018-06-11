package com.etermax.spacehorse.core.reward.model.strategies;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.reward.model.strategies.cards.RandomEpicCardPartsRewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.mock.MockUtils;

public class RandomEpicCardPartsRewardStrategyTest {

	private static final String CARD_ID = "card_electric_duo";

	private CatalogEntriesCollection<CardDefinition> cardDefinitionEntry;

	@Before
	public void setUp() {
		Catalog catalog = MockUtils.mockCatalog();
		cardDefinitionEntry = catalog.getCardDefinitionsCollection();
	}

	@Test
	public void testNoReward() {
		// given
		int randomEpic = 0;
		RandomEpicCardPartsRewardStrategy strategy = new RandomEpicCardPartsRewardStrategy(randomEpic, cardDefinitionEntry.getEntries(), 1);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertThat(rewards).isEmpty();
	}

	@Test
	public void testOneReward() {
		// given
		int randomEpic = 1;
		RandomEpicCardPartsRewardStrategy strategy = new RandomEpicCardPartsRewardStrategy(randomEpic, cardDefinitionEntry.getEntries(), 1);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertThat(rewards).hasSize(1);
		assertEquals(rewards.stream().findFirst().get().getRewardId(),
				cardDefinitionEntry.findById(rewards.stream().findFirst().get().getRewardId()).get().getId());
		assertEquals(randomEpic, rewards.stream().findFirst().get().getAmount());
	}

	@Test
	public void testManyRewards() {
		// given
		int randomEpic = 10;
		RandomEpicCardPartsRewardStrategy strategy = new RandomEpicCardPartsRewardStrategy(randomEpic, cardDefinitionEntry.getEntries(), 1);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertThat(rewards).hasSize(1);
		assertEquals(rewards.stream().findFirst().get().getRewardId(),
				cardDefinitionEntry.findById(rewards.stream().findFirst().get().getRewardId()).get().getId());
		assertEquals(randomEpic, rewards.stream().findFirst().get().getAmount());
	}

	@Test
	public void givenAConfigurationWithNoEpicCardWhenGetRewardThenShouldRetrievedAnEmptyList() {
		// given
		int randomEpic = 1;
		CatalogEntriesCollection<CardDefinition> cardDefinitionEntry = aCardDefinitionEntryWIthoutEpicCardDefinition();
		RandomEpicCardPartsRewardStrategy strategy = new RandomEpicCardPartsRewardStrategy(randomEpic, cardDefinitionEntry.getEntries(), 1);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		thenAssertThatRewardsAreEmpty(rewards);
	}

	@Test
	public void givenAConfigurationWithNoCardDefinitionsWhenGetRewardThenShouldRetrievedAnEmptyList() {
		// given
		int randomEpic = 1;
		CatalogEntriesCollection<CardDefinition> cardDefinitionEntry = anEmptyCardDefinitionEntry();
		RandomEpicCardPartsRewardStrategy strategy = new RandomEpicCardPartsRewardStrategy(randomEpic, cardDefinitionEntry.getEntries(), 1);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		thenAssertThatRewardsAreEmpty(rewards);
	}

	private CatalogEntriesCollection<CardDefinition> anEmptyCardDefinitionEntry() {
		return new CatalogEntriesCollection<>();
	}

	private CatalogEntriesCollection<CardDefinition> aCardDefinitionEntryWIthoutEpicCardDefinition() {
		CatalogEntriesCollection<CardDefinition> cardDefinitionEntry = new CatalogEntriesCollection<>();
		cardDefinitionEntry.addEntry(new CardDefinition(CARD_ID, CardRarity.COMMON, 1));
		return cardDefinitionEntry;
	}

	private void thenAssertThatRewardsAreEmpty(List<Reward> rewards) {
		assertThat(rewards).isEmpty();
	}

}
