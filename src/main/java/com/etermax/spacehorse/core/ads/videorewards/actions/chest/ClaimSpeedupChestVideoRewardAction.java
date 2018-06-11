package com.etermax.spacehorse.core.ads.videorewards.actions.chest;

import java.util.function.Predicate;

import com.etermax.spacehorse.core.ads.videorewards.model.SpeedupVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.VideoRewardFactory;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardRepository;
import com.etermax.spacehorse.core.catalog.exception.CatalogEntryNotFound;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class ClaimSpeedupChestVideoRewardAction {

	private final VideoRewardFactory videoRewardFactory;
	private final QuotaVideoRewardRepository quotaVideoRewardRepository;
	private final ServerTimeProvider timeProvider;
	private final PlayerRepository playerRepository;

	public ClaimSpeedupChestVideoRewardAction(QuotaVideoRewardRepository quotaVideoRewardRepository, ServerTimeProvider timeProvider,
			PlayerRepository playerRepository) {
		this.quotaVideoRewardRepository = quotaVideoRewardRepository;
		this.timeProvider = timeProvider;
		this.playerRepository = playerRepository;
		this.videoRewardFactory = new VideoRewardFactory();
	}

	public Chest claim(Player player, String placeName, long chestId, Catalog catalog) {
		VideoRewardDefinition videoRewardDefinition = findOrFailsDefinitionByPlaceName(placeName, catalog);
		SpeedupVideoReward speedupVideoReward = (SpeedupVideoReward) videoRewardFactory.create(videoRewardDefinition);

		QuotaVideoReward quotaVideoReward = consumeQuotaOfVideoReward(player, speedupVideoReward);
		Chest chest = speedupChestOpeningEndTime(player, chestId, speedupVideoReward);

		quotaVideoRewardRepository.addOrUpdate(quotaVideoReward);
		playerRepository.update(player);

		return chest;
	}

	private VideoRewardDefinition findOrFailsDefinitionByPlaceName(String placeName, Catalog catalog) {
		return catalog.getVideoRewardDefinitionsCollection().getEntries().stream().filter(byPlaceName(placeName)).findFirst()
				.orElseThrow(() -> new CatalogEntryNotFound("The video reward with placeName [ " + placeName + " ] not found in catalog "));
	}

	private Predicate<VideoRewardDefinition> byPlaceName(String placeName) {
		return definition -> definition.getPlaceName().equals(placeName);
	}

	private QuotaVideoReward consumeQuotaOfVideoReward(Player player, SpeedupVideoReward speedupVideoReward) {
		QuotaVideoReward quotaVideoReward = quotaVideoRewardRepository.findOrDefaultBy(player, speedupVideoReward.getPlaceName());
		quotaVideoReward.consume(speedupVideoReward);
		return quotaVideoReward;
	}

	private Chest speedupChestOpeningEndTime(Player player, long chestId, SpeedupVideoReward speedupVideoReward) {
		Chest chest = findChestBy(chestId, player);
		chest.speedupOpening(timeProvider.getDate(), speedupVideoReward.getSpeedupTimeInSeconds());
		return chest;
	}

	private Chest findChestBy(long chestId, Player player) {
		return player.getInventory().getChests().findChestById(chestId).orElseThrow(() -> new ApiException("Unknown chest"));
	}
}
