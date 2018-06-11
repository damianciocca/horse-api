package com.etermax.spacehorse.core.ads.videorewards.actions;

import com.etermax.spacehorse.core.ads.videorewards.model.VideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.VideoRewardFactory;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;
import com.etermax.spacehorse.core.player.model.Player;

public class GetAvailableVideoRewardAction {

	private final VideoRewardFactory videoRewardFactory;
	private final QuotaVideoRewardRepository quotaVideoRewardRepository;

	public GetAvailableVideoRewardAction(QuotaVideoRewardRepository quotaVideoRewardRepository) {
		this.quotaVideoRewardRepository = quotaVideoRewardRepository;
		this.videoRewardFactory = new VideoRewardFactory();
	}

	public boolean hasAvailable(Player player, String videoRewardId, Catalog catalog) {
		VideoRewardDefinition videoRewardDefinition = getDefinitionByVideoRewardId(videoRewardId, catalog);
		VideoReward videoReward = videoRewardFactory.create(videoRewardDefinition);
		QuotaVideoReward quotaVideoReward = quotaVideoRewardRepository.findOrDefaultBy(player, videoReward.getPlaceName());
		return quotaVideoReward.hasAvailable(videoReward);
	}

	private VideoRewardDefinition getDefinitionByVideoRewardId(String videoRewardId, Catalog catalog) {
		return catalog.getVideoRewardDefinitionsCollection().findByIdOrFail(videoRewardId);
	}

}
