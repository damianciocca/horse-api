package com.etermax.spacehorse.core.reward.model.strategies.gems;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.etermax.spacehorse.core.reward.model.strategies.RewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class FixedGemsRewardStrategy implements RewardStrategy {

	private final int gems;

	public FixedGemsRewardStrategy(int gems) {
		this.gems = gems;
	}

	@Override
	public List<Reward> getRewards() {
		if (gems > 0) {
			return newArrayList(new Reward(RewardType.GEMS, gems));
		}
		return newArrayList();
	}

}
