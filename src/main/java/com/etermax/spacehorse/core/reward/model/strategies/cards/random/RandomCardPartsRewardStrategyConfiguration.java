package com.etermax.spacehorse.core.reward.model.strategies.cards.random;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils.CardDropRateCalculatorConfiguration;
import com.etermax.spacehorse.core.catalog.model.CardsDropRate;
import com.etermax.spacehorse.core.catalog.model.ChestChancesDefinition;

public class RandomCardPartsRewardStrategyConfiguration {

	private final List<CardDefinition> cardDefinitions;
	private final CardDropRateCalculatorConfiguration cardDropRateCalculatorConfiguration;
	private final List<CardsDropRate> cardsDropRates;
	private final ChestChancesDefinition chestChancesDefinition;
	private final boolean ignoreCardsPerMap;

	public RandomCardPartsRewardStrategyConfiguration(List<CardDefinition> cardDefinitions,
			CardDropRateCalculatorConfiguration cardDropRateCalculatorConfiguration, List<CardsDropRate> cardsDropRates,
			ChestChancesDefinition chestChancesDefinition, boolean ignoreCardsPerMap) {
		this.cardDropRateCalculatorConfiguration = cardDropRateCalculatorConfiguration;
		this.cardsDropRates = cardsDropRates;
		this.chestChancesDefinition = chestChancesDefinition;
		this.ignoreCardsPerMap = ignoreCardsPerMap;
		this.cardDefinitions = cardDefinitions;
	}

	public List<CardDefinition> getCards(){
		return cardDefinitions;
	}

	public CardDropRateCalculatorConfiguration getCardDropRateCalculatorConfiguration() {
		return cardDropRateCalculatorConfiguration;
	}

	public List<CardsDropRate> getCardsDropRates() {
		return cardsDropRates;
	}

	public ChestChancesDefinition getChestChancesDefinition() {
		return chestChancesDefinition;
	}

	public boolean getIgnoreCardsPerMap() {
		return ignoreCardsPerMap;
	}

}

