package com.etermax.spacehorse.core.reward.model;

import static java.lang.Math.toIntExact;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.ChestChancesDefinition;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.tutorialprogress.TutorialProgressEntry;
import com.etermax.spacehorse.core.player.action.TutorialException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.strategies.gems.FixedGemsRewardStrategy;
import com.etermax.spacehorse.core.reward.model.strategies.gold.FixedGoldRewardStrategy;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.RandomCardPartsRewardStrategy;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.RandomCardPartsRewardStrategyConfiguration;
import com.etermax.spacehorse.core.reward.model.strategies.cards.RandomEpicCardPartsRewardStrategy;
import com.etermax.spacehorse.core.reward.model.strategies.gems.RandomGemsRewardStrategy;
import com.etermax.spacehorse.core.reward.model.strategies.gold.RandomGoldRewardStrategy;
import com.etermax.spacehorse.core.reward.model.strategies.RewardStrategy;
import com.etermax.spacehorse.core.reward.model.strategies.cards.FixedCardPartsRewardStrategy;
import com.google.common.collect.Lists;

public class GetRewardsDomainService {

	private static final Logger logger = LoggerFactory.getLogger(GetRewardsDomainService.class);

	// TODO: 11/2/17 el chestDefinition podriamos moverlo dentro del GetRewardConfiguration
	public List<Reward> getRewards(Player player, ChestDefinition chestDefinition, int mapNumber, GetRewardConfiguration configuration) {

		List<RewardStrategy> rewardStrategies = collectRewardStrategies(player, chestDefinition, mapNumber, configuration);
		List<Reward> rewards = Lists.newArrayList();
		rewardStrategies.forEach(rewardStrategy -> rewards.addAll(rewardStrategy.getRewards()));
		logger.debug("get rewards [ {} ]", rewards);

		return rewards;
	}

	private List<RewardStrategy> collectRewardStrategies(Player player, ChestDefinition chestDefinition, int mapNumber,
			GetRewardConfiguration configuration) {

		final List<RewardStrategy> rewardStrategies = Lists.newArrayList();

		if (isOnlyTutorial(chestDefinition)) {
			List<RewardStrategy> strategies = collectTutorialRewardStrategiesWith(chestDefinition, mapNumber, configuration);
			rewardStrategies.addAll(strategies);
		} else {
			List<RewardStrategy> strategies = collectRewardStrategiesWith(player, chestDefinition, mapNumber, configuration);
			rewardStrategies.addAll(strategies);
		}

		return rewardStrategies;
	}

	private List<RewardStrategy> collectTutorialRewardStrategiesWith(ChestDefinition chestDefinition, int mapNumber,
			GetRewardConfiguration configuration) {

		TutorialProgressEntry tutorialProgressEntry = findTutorialProgressEntry(chestDefinition.getId(), configuration.getTutorialProgress());
		List<CardDefinition> cardDefinitions = configuration.getCardDefinition();
		List<RewardStrategy> rewardStrategies = buildTutorialRewardStrategiesWith(mapNumber, tutorialProgressEntry, cardDefinitions);
		return rewardStrategies;
	}

	private List<RewardStrategy> collectRewardStrategiesWith(Player player, ChestDefinition chestDefinition, int mapNumber,
			GetRewardConfiguration configuration) {

		List<RewardStrategy> rewardStrategies = Lists.newArrayList();
		Optional<ChestChancesDefinition> chestChancesDefinitionOptional = configuration.getChestChancesDefinition().stream()
				.filter(byChestIdAndMapNumber(chestDefinition, mapNumber)).findFirst();
		chestChancesDefinitionOptional.ifPresent(chestChancesDefinition -> {
			List<RewardStrategy> strategies = buildRewardStrategiesWith(player, mapNumber, configuration, chestChancesDefinition,
					chestDefinition);
			rewardStrategies.addAll(strategies);

		});
		return rewardStrategies;
	}

	private Boolean isOnlyTutorial(ChestDefinition chestDefinition) {
		return chestDefinition.getTutorialOnly();
	}

	private List<RewardStrategy> buildRewardStrategiesWith(Player player, int mapNumber, GetRewardConfiguration configuration,
			ChestChancesDefinition chestChancesDefinition, ChestDefinition chestDefinition) {

		List<RewardStrategy> rewardStrategies = Lists.newArrayList();
		rewardStrategies.add(new RandomGoldRewardStrategy(chestChancesDefinition.getGold(), chestChancesDefinition.getGoldTolerance()));
		rewardStrategies.add(new RandomGemsRewardStrategy(chestChancesDefinition.getGems(), chestChancesDefinition.getGemsTolerance()));

		RandomCardPartsRewardStrategyConfiguration rewardStrategyConfiguration = mapConfigurationFrom(configuration, chestChancesDefinition,
				chestDefinition);
		rewardStrategies.add(new RandomCardPartsRewardStrategy(player, mapNumber, rewardStrategyConfiguration));
		return rewardStrategies;
	}

	private List<RewardStrategy> buildTutorialRewardStrategiesWith(int mapNumber, TutorialProgressEntry tutorialProgressEntry,
			List<CardDefinition> cardDefinitions) {

		List<RewardStrategy> rewardStrategies = Lists.newArrayList();
		rewardStrategies.add(new FixedGemsRewardStrategy(toIntExact(tutorialProgressEntry.getGems())));
		rewardStrategies.add(new FixedGoldRewardStrategy(toIntExact(tutorialProgressEntry.getGold())));
		rewardStrategies.add(new FixedCardPartsRewardStrategy(tutorialProgressEntry.getCardsWithAmount()));
		rewardStrategies.add(new RandomEpicCardPartsRewardStrategy(tutorialProgressEntry.getRandomEpic(), cardDefinitions, mapNumber));
		return rewardStrategies;
	}

	private RandomCardPartsRewardStrategyConfiguration mapConfigurationFrom(GetRewardConfiguration configuration,
			ChestChancesDefinition chestChancesDefinition, ChestDefinition chestDefinition) {
		return new RandomCardPartsRewardStrategyConfiguration(configuration.getCardDefinition(), configuration.getCardDropRateCalculatorConfiguration(),
				configuration.getCardsDropRate(), chestChancesDefinition, chestDefinition.getIgnoreCardsPerMap());
	}

	private TutorialProgressEntry findTutorialProgressEntry(String chestId, List<TutorialProgressEntry> tutorialProgression) {
		return tutorialProgression.stream().filter(byChestId(chestId)).findFirst()
				.orElseThrow(() -> new TutorialException("No tutorial progress entry found using the given chest id " + chestId));
	}

	private Predicate<ChestChancesDefinition> byChestIdAndMapNumber(ChestDefinition chestDefinition, int mapNumber) {
		return def -> def.getChestType().equals(chestDefinition.getId()) && ((Integer) def.getMapNumber()).equals(mapNumber);
	}

	private Predicate<TutorialProgressEntry> byChestId(String chestId) {
		return entry -> entry.getChestId().equals(chestId);
	}

}
