package com.etermax.spacehorse.core.reward.model.strategies;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.CardWithAmount;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.reward.model.strategies.cards.FixedCardPartsRewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.google.common.collect.Lists;

public class FixedCardPartsRewardStrategyTest {

	@Test
	public void testNoReward() {
		// given
		List<CardWithAmount> cardWithAmountList = Lists.newArrayList();
		FixedCardPartsRewardStrategy strategy = new FixedCardPartsRewardStrategy(cardWithAmountList);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertThat(rewards).isEmpty();
	}

	@Test
	public void testOneReward() {
		//given
		Card card = new Card(0L, "card_corvette", 0);
		int amount = 4;
		List<CardWithAmount> cardWithAmountList = Lists.newArrayList(new CardWithAmount(card, amount));
		FixedCardPartsRewardStrategy strategy = new FixedCardPartsRewardStrategy(cardWithAmountList);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertThat(rewards).hasSize(1);
	}

}
