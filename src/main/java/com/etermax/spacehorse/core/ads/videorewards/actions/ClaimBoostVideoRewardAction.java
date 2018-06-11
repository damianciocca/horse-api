package com.etermax.spacehorse.core.ads.videorewards.actions;

import com.etermax.spacehorse.core.ads.videorewards.model.BoostVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.VideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.VideoRewardFactory;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class ClaimBoostVideoRewardAction {

	private final VideoRewardFactory videoRewardFactory;
	private final QuotaVideoRewardRepository quotaVideoRewardRepository;
	private final PlayerRepository playerRepository;

	public ClaimBoostVideoRewardAction(QuotaVideoRewardRepository quotaVideoRewardRepository, PlayerRepository playerRepository) {
		this.quotaVideoRewardRepository = quotaVideoRewardRepository;
		this.playerRepository = playerRepository;
		this.videoRewardFactory = new VideoRewardFactory();
	}

	public int claim(Player player, String videoRewardId, Catalog catalog) {
		VideoRewardDefinition videoRewardDefinition = findOrFailsDefinitionByVideoRewardId(videoRewardId, catalog);
		BoostVideoReward boostVideoReward = (BoostVideoReward) videoRewardFactory.create(videoRewardDefinition);
		consumeQuotaOfVideoReward(player, boostVideoReward);
		player.getInventory().getGold().add(boostVideoReward.getCoins());
		playerRepository.update(player);
		return boostVideoReward.getCoins();
	}

	private VideoRewardDefinition findOrFailsDefinitionByVideoRewardId(String videoRewardId, Catalog catalog) {
		return catalog.getVideoRewardDefinitionsCollection().findByIdOrFail(videoRewardId);
	}

	private void consumeQuotaOfVideoReward(Player player, VideoReward videoReward) {
		QuotaVideoReward quotaVideoReward = quotaVideoRewardRepository.findOrDefaultBy(player, videoReward.getPlaceName());
		quotaVideoReward.consume(videoReward);
		quotaVideoRewardRepository.addOrUpdate(quotaVideoReward);
	}
}