package com.etermax.spacehorse.core.reward.model.strategies.cards.random;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.ChestChancesCardsDefinition;
import com.etermax.spacehorse.core.catalog.model.ChestChancesDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.model.strategies.RewardStrategy;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils.CardDropRateCalculator;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils.RandomCardsSelectorInPartitions;

public class RandomCardPartsRewardStrategy implements RewardStrategy {

	private final Player player;
	private final RandomCardPartsRewardStrategyConfiguration configuration;
	private final int mapNumber;

	public RandomCardPartsRewardStrategy(Player player, int mapNumber, RandomCardPartsRewardStrategyConfiguration configuration) {
		this.player = player;
		this.configuration = configuration;
		this.mapNumber = mapNumber;
	}

	@Override
	public List<Reward> getRewards() {

		if (isConfigurationInvalid()) {
			return newArrayList();
		}

		List<CardsRewardConfiguration> cardsConfigurations = buildCardsConfigurations();

		capPartitionsSumToMaxParititions(cardsConfigurations);

		List<Reward> rewards = calculateRewards(cardsConfigurations);

		return rewards;
	}

	private boolean isConfigurationInvalid() {
		return configuration == null || configuration.getChestChancesDefinition() == null || configuration.getCards() == null
				|| configuration.getCards().size() == 0;
	}

	private List<Reward> calculateRewards(List<CardsRewardConfiguration> cardsConfigurations) {

		List<Reward> rewards = cardsConfigurations.stream()
				.map(cardsConfiguration -> getRewards(cardsConfiguration, calculateCardsDropRate(cardsConfiguration.getCards())))
				.flatMap(x -> x.stream()).collect(Collectors.toList());

		return rewards;
	}

	private List<CardsRewardConfiguration> buildCardsConfigurations() {
		List<CardsRewardConfiguration> cardsConfigurations = Arrays.stream(CardRarity.values()).map(this::buildCardsConfiguration)
				.collect(Collectors.toList());
		return cardsConfigurations;
	}

	private CardsRewardConfiguration buildCardsConfiguration(CardRarity rarity) {
		int amount = getRandomCardsAmount(rarity);

		List<CardDefinition> cardDefinitionsWithRarity = getCardDefinitionsWithRarity(rarity);
		int partitions = getRandomPartitions(rarity, amount, cardDefinitionsWithRarity);

		return new CardsRewardConfiguration(rarity, cardDefinitionsWithRarity, amount, partitions);
	}

	private int getRandomPartitions(CardRarity rarity, int cardsAmount, List<CardDefinition> cardDefinitionsWithRarity) {

		if (cardsAmount == 0) {
			return 0;
		}

		int minPartitions = configuration.getChestChancesDefinition().getCardsDefinition(rarity).getMinPartitions();
		int maxPartitions = configuration.getChestChancesDefinition().getCardsDefinition(rarity).getMaxPartitions();

		minPartitions = Math.max(1, minPartitions);
		maxPartitions = Math.max(1, maxPartitions);

		int partitions = ThreadLocalRandom.current().nextInt(minPartitions, maxPartitions + 1);

		partitions = Math.min(cardDefinitionsWithRarity.size(), partitions);

		return partitions;
	}

	private int getRandomCardsAmount(CardRarity rarity) {

		ChestChancesCardsDefinition cardsDefinition = configuration.getChestChancesDefinition().getCardsDefinition(rarity);

		int amount = cardsDefinition.getAmount() + getAdditionalAmount(cardsDefinition);

		return amount;
	}

	private int getAdditionalAmount(ChestChancesCardsDefinition cardsDefinition) {

		int chances = cardsDefinition.getAdditionalAmountChances();
		int chancesBase = cardsDefinition.getAdditionalAmountChancesBase();

		if (chances > 0 && ThreadLocalRandom.current().nextInt(0, chancesBase) < chances) {
			return cardsDefinition.getAdditionalAmount();
		}

		return 0;
	}

	private Map<CardDefinition, Double> calculateCardsDropRate(List<CardDefinition> cards) {
		return CardDropRateCalculator
				.calculateCardsDropRate(player, mapNumber, configuration.getCardDropRateCalculatorConfiguration(), getCardsAvailable(cards),
						configuration.getCardsDropRates());
	}

	private List<CardDefinition> getCardsAvailable(List<CardDefinition> cards) {
		return configuration.getIgnoreCardsPerMap() ?
				cards :
				cards.stream().filter(card -> isCardAvailableInMap(mapNumber, card)).collect(Collectors.toList());
	}

	private static boolean isCardAvailableInMap(int mapNumber, CardDefinition card) {
		return card.getEnabled() && mapNumber >= card.getAvailableFromMapId();
	}

	private void capPartitionsSumToMaxParititions(List<CardsRewardConfiguration> cardsConfigurations) {

		//The configurations are in increasing order of priority by rarity (COMMON, RARE, EPIC, LEGENDARY, etc..), which means that if
		//the max number of partitions is exceeded, we start discarding from the lower partitions until the excess is removed.

		int cardPartitionsSum = cardsConfigurations.stream().mapToInt(x -> x.getPartitions()).sum();

		if (cardPartitionsSum > ChestChancesDefinition.MAX_PARTITIONS) {
			int excess = cardPartitionsSum - ChestChancesDefinition.MAX_PARTITIONS;

			for (CardsRewardConfiguration cardsConfiguration : cardsConfigurations) {
				int partitions = cardsConfiguration.getPartitions();

				if (partitions >= excess) {
					cardsConfiguration.setPartitions(partitions - excess);
					excess = 0;
				} else {
					cardsConfiguration.setPartitions(0);
					excess -= partitions;
				}
			}
		}
	}

	private List<CardDefinition> getCardDefinitionsWithRarity(CardRarity rarity) {
		return configuration.getCards().stream()
				.filter(cardDefinition -> cardDefinition.getEnabled() && cardDefinition.getCardRarity().equals(rarity)).collect(Collectors.toList());
	}

	private List<Reward> getRewards(CardsRewardConfiguration cardConfiguration, Map<CardDefinition, Double> cardsDropRate) {
		if (!cardConfiguration.isValid()) {
			return Collections.emptyList();
		}

		Map<CardDefinition, Integer> chooseCards = RandomCardsSelectorInPartitions
				.chooseCards(cardsDropRate, cardConfiguration.getPartitions(), cardConfiguration.getAmount());

		List<Reward> rewards = new ArrayList<>();
		for (Map.Entry<CardDefinition, Integer> entry : chooseCards.entrySet()) {
			rewards.add(new Reward(RewardType.CARD_PARTS, entry.getKey().getId(), entry.getValue()));
		}
		return rewards;
	}

}
