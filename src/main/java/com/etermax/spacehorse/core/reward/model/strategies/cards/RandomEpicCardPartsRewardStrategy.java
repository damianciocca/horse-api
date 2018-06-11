package com.etermax.spacehorse.core.reward.model.strategies.cards;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Random;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.reward.model.strategies.RewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.google.common.collect.Lists;

public class RandomEpicCardPartsRewardStrategy implements RewardStrategy {

	private static final int CARD_EPIC_AMOUNT = 0;
	private final int epicCardAmount;
	private final List<CardDefinition> cardDefinitions;
	private final int mapNumber;

	public RandomEpicCardPartsRewardStrategy(int epicCardAmount, List<CardDefinition> cardDefinitions, int mapNumber) {
		validateParameters(epicCardAmount, cardDefinitions);
		this.epicCardAmount = epicCardAmount;
		this.cardDefinitions = cardDefinitions;
		this.mapNumber = mapNumber;
	}

	@Override
	public List<Reward> getRewards() {
		if (epicCardAmount <= CARD_EPIC_AMOUNT) {
			return newArrayList();
		}
		List<CardDefinition> epicCardDefinitions = getEpicCardDefinitions(cardDefinitions, mapNumber);
		if (epicCardDefinitions.isEmpty()) {
			return newArrayList();
		}
		CardDefinition cardDefinition = chooseOneRandomEpicCardDefinition(epicCardDefinitions);
		return createCardPartsReward(epicCardAmount, cardDefinition);
	}

	private void validateParameters(int epicCardAmount, List<CardDefinition> cardDefinitionEntry) {
		checkArgument(epicCardAmount >= 0, "the epicCardAmount can't be negative");
		checkArgument(cardDefinitionEntry != null, "the cardDefinitionEntry should not be null");
	}

	private List<CardDefinition> getEpicCardDefinitions(List<CardDefinition> cardDefinitions, int mapNumber) {
		List<CardDefinition> epicCardDefinitions = newArrayList();
		cardDefinitions.forEach(card -> {
			if (existAvailableEpicCard(card, mapNumber)) {
				epicCardDefinitions.add(card);
			}
		});
		return epicCardDefinitions;
	}

	private CardDefinition chooseOneRandomEpicCardDefinition(List<CardDefinition> epicCards) {
		return epicCards.get(new Random().nextInt(epicCards.size()));
	}

	private List<Reward> createCardPartsReward(int randomEpicAmount, CardDefinition cardDefinition) {
		Reward reward = new Reward(RewardType.CARD_PARTS, cardDefinition.getId(), randomEpicAmount);
		return Lists.newArrayList(reward);
	}

	private boolean existAvailableEpicCard(CardDefinition card, Integer mapNumber) {
		return card.getEnabled() && isEpic(card) && cardIsAvailableForMap(card, mapNumber);
	}

	private boolean cardIsAvailableForMap(CardDefinition card, Integer mapNumber) {
		return card.getAvailableFromMapId() <= mapNumber;
	}

	private boolean isEpic(CardDefinition card) {
		return card.getCardRarity().equals(CardRarity.EPIC);
	}

}
