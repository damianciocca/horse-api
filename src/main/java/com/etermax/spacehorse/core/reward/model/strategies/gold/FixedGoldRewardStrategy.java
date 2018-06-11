package com.etermax.spacehorse.core.reward.model.strategies.gold;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collections;
import java.util.List;

import com.etermax.spacehorse.core.reward.model.strategies.RewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class FixedGoldRewardStrategy implements RewardStrategy {

	private final int gold;

	public FixedGoldRewardStrategy(int gold) {
		this.gold = gold;
	}

	@Override
	public List<Reward> getRewards() {
		if (gold > 0) {
			return newArrayList(new Reward(RewardType.GOLD, gold));
		}
		return Collections.emptyList();
	}

}
