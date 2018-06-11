package com.etermax.spacehorse.core.reward.model;

import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils.CardDropRateCalculatorConfiguration;
import com.etermax.spacehorse.core.catalog.model.CardsDropRate;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ChestChancesDefinition;
import com.etermax.spacehorse.core.catalog.model.tutorialprogress.TutorialProgressEntry;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class GetRewardConfiguration {

	private final List<TutorialProgressEntry> tutorialProgress;
	private final CardDropRateCalculatorConfiguration cardDropRateCalculatorConfiguration;
	private final List<CardsDropRate> cardsDropRate;
	private final List<ChestChancesDefinition> chestChancesDefinition;
	private final List<CardDefinition> cardDefinition;

	private GetRewardConfiguration(List<TutorialProgressEntry> tutorialProgress,
			CardDropRateCalculatorConfiguration cardDropRateCalculatorConfiguration, List<CardsDropRate> cardsDropRate,
			List<ChestChancesDefinition> chestChancesDefinition, List<CardDefinition> cardDefinition) {
		this.tutorialProgress = tutorialProgress;
		this.cardDropRateCalculatorConfiguration = cardDropRateCalculatorConfiguration;
		this.cardsDropRate = cardsDropRate;
		this.chestChancesDefinition = chestChancesDefinition;
		this.cardDefinition = cardDefinition;
	}

	public static GetRewardConfiguration createBy(Catalog catalog, ServerTimeProvider serverTimeProvider) {
		List<TutorialProgressEntry> tutorialProgress = catalog.getTutorialProgressCollection().getEntries();
		CardDropRateCalculatorConfiguration cardDropRateCalculatorConfiguration = catalog.getGameConstants().getCardDropRateCalculatorConfiguration();
		List<CardsDropRate> cardsDropRate = catalog.getCardsDropRateCollection().getEntries();
		List<ChestChancesDefinition> chestChancesDefinition = catalog.getChestChancesDefinitionsCollection().getEntries();
		List<CardDefinition> cardDefinition = filterActiveCards(catalog.getCardDefinitionsCollection().getEntries(), serverTimeProvider);
		return new GetRewardConfiguration(tutorialProgress, cardDropRateCalculatorConfiguration, cardsDropRate,
				chestChancesDefinition, cardDefinition);
	}

	public List<TutorialProgressEntry> getTutorialProgress() {
		return tutorialProgress;
	}

	public CardDropRateCalculatorConfiguration getCardDropRateCalculatorConfiguration() {
		return cardDropRateCalculatorConfiguration;
	}

	public List<CardsDropRate> getCardsDropRate() {
		return cardsDropRate;
	}

	public List<ChestChancesDefinition> getChestChancesDefinition() {
		return chestChancesDefinition;
	}

	public List<CardDefinition> getCardDefinition() {
		return cardDefinition;
	}


	private static List<CardDefinition> filterActiveCards(List<CardDefinition> cardDefinitions, ServerTimeProvider serverTimeProvider) {
		return cardDefinitions.stream().filter( cardDefinition -> cardDefinition.isActiveFor(serverTimeProvider.getDateTime())).collect(Collectors.toList());
	}
}
