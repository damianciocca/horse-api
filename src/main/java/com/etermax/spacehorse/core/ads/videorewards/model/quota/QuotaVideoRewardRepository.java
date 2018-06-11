package com.etermax.spacehorse.core.ads.videorewards.model.quota;

import com.etermax.spacehorse.core.player.model.Player;

public interface QuotaVideoRewardRepository {

	QuotaVideoReward findOrDefaultBy(Player player, String placeName);

	void addOrUpdate(QuotaVideoReward quotaVideoReward);
}
