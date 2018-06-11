package com.etermax.spacehorse.core.reward.model.strategies.cards;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CardWithAmount;
import com.etermax.spacehorse.core.reward.model.strategies.RewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class FixedCardPartsRewardStrategy implements RewardStrategy {

	private final List<CardWithAmount> cardWithAmounts;

	public FixedCardPartsRewardStrategy(List<CardWithAmount> cardWithAmounts) {
		this.cardWithAmounts = cardWithAmounts;
	}

	@Override
	public List<Reward> getRewards() {
		List<Reward> rewards = newArrayList();
		cardWithAmounts.forEach(cardWithAmount -> rewards.add(createRewardWith(cardWithAmount)));
		return rewards;
	}

	private Reward createRewardWith(CardWithAmount cardWithAmount) {
		return new Reward(RewardType.CARD_PARTS, cardWithAmount.getCard().getCardType(), cardWithAmount.getAmount());
	}

}
