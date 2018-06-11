package com.etermax.spacehorse.core.reward.model;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chests;
import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.etermax.spacehorse.core.reward.model.unlock.strategies.ChestSlotInspectorFactory;
import com.etermax.spacehorse.core.reward.model.unlock.strategies.ChestSlotsInspector;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.google.common.collect.Lists;

public class ApplyRewardDomainService {

	private static final Logger logger = LoggerFactory.getLogger(ApplyRewardDomainService.class);

	public List<RewardResponse> applyRewards(Player player, List<Reward> rewards, RewardContext rewardContext, ApplyRewardConfiguration configuration,
			List<AchievementsObserver> achievementsObservers) {
		List<RewardResponse> responses = Lists.newArrayList();
		rewards.forEach(reward -> applyReward(player, reward, rewardContext, configuration).ifPresent(responses::add));
		updateAchievementObservers(player, configuration, achievementsObservers);
		return responses;
	}

	private Optional<RewardResponse> applyReward(Player player, Reward reward, RewardContext rewardContext, ApplyRewardConfiguration configuration) {
		Optional<RewardResponse> response = empty();
		if (isValid(reward, configuration) || player.hasActiveTutorial()) {
			switch (reward.getRewardType()) {
				case CHEST:
					response = tryToApplyChestTo(player, reward, rewardContext, configuration);
					break;
				case NEXT_CHEST:
					response = tryToApplyNextSequentialChestTo(player, rewardContext, configuration);
					break;
				case GOLD:
					response = of(applyGoldRewardTo(player, reward));
					break;
				case GEMS:
					response = of(applyGemsRewardTo(player, reward));
					break;
				case CARD_PARTS: {
					response = of(applyCardsRewardsTo(player, reward));
					break;
				}
			}
		}
		return response;
	}

	private void updateAchievementObservers(Player player, ApplyRewardConfiguration configuration, List<AchievementsObserver> achievementsObservers) {
		achievementsObservers.forEach(achievementsObserver -> achievementsObserver.update(player, configuration.getAchievementDefinitions()));
	}

	private Optional<RewardResponse> tryToApplyNextSequentialChestTo(Player player, RewardContext rewardContext,
			ApplyRewardConfiguration configuration) {

		ChestSlotsInspector chestSlotsInspector = newChestSlotsInspector(configuration);
		if (canReceiveChest(player, chestSlotsInspector)) {
			ChestDefinition nextChest = getNextChestDefinition(player, configuration);
			logger.debug("applying next seq chest [ {} ]", nextChest);
			Chest chest = getChestBoardOf(player)
					.addChest(nextChest.getId(), rewardContext.getMapNumber(), configuration.getGameConstants(), getPlayerLevelOf(player),
							chestSlotsInspector);
			return of(new RewardResponse(new ChestResponse(chest)));
		}
		logger.debug("discarding applying next seq chest..");
		return empty();
	}

	private Optional<RewardResponse> tryToApplyChestTo(Player player, Reward reward, RewardContext rewardContext,
			ApplyRewardConfiguration configuration) {

		ChestSlotsInspector chestSlotsInspector = newChestSlotsInspector(configuration);
		if (canReceiveChest(player, chestSlotsInspector)) {
			logger.debug("applying fixed chest [ {} ]", reward);
			Chest chest = getChestBoardOf(player)
					.addChest(reward.getRewardId(), rewardContext.getMapNumber(), configuration.getGameConstants(), getPlayerLevelOf(player),
							chestSlotsInspector);
			return of(new RewardResponse(new ChestResponse(chest)));
		}
		return empty();
	}

	private ChestSlotsInspector newChestSlotsInspector(ApplyRewardConfiguration configuration) {
		return new ChestSlotInspectorFactory()
				.create(configuration.getGameConstants().isUseFeaturesByPlayerLvl(), configuration.getChestSlotsConfiguration());
	}

	private RewardResponse applyGoldRewardTo(Player player, Reward reward) {
		logger.debug("applying gold [ {} ]", reward);
		player.getInventory().getGold().add(reward.getAmount());
		return new RewardResponse(reward.getRewardType(), reward.getAmount());
	}

	private RewardResponse applyGemsRewardTo(Player player, Reward reward) {
		logger.debug("applying gems [ {} ]", reward);
		player.getInventory().getGems().add(reward.getAmount());
		return new RewardResponse(reward.getRewardType(), reward.getAmount());
	}

	private RewardResponse applyCardsRewardsTo(Player player, Reward reward) {
		logger.debug("applying cards [ {} ]", reward);
		Card unlockedCard = null;
		if (player.getDeck().findCardByCardType(reward.getRewardId()) == null) {
			unlockedCard = player.getDeck().addNewCard(reward.getRewardId());
		}
		player.getInventory().getCardParts().add(reward.getRewardId(), reward.getAmount());
		return new RewardResponse(reward.getRewardType(), reward.getRewardId(), reward.getAmount(),
				unlockedCard != null ? new CardResponse(unlockedCard) : null);
	}

	private boolean canReceiveChest(Player player, ChestSlotsInspector chestSlotsInspector) {
		return chestSlotsInspector.hasAvailable(getPlayerLevelOf(player), player.getInventory().getChests());
	}

	private ChestDefinition getNextChestDefinition(Player player, ApplyRewardConfiguration configuration) {
		GameConstants gameConstants = configuration.getGameConstants();
		return player.getPlayerRewards().getNextChestReward(gameConstants.getDefaultFreeGemsCycleSequenceId(), configuration.getChestList(),
				configuration.getChestDifinition());
	}

	private boolean isValid(Reward reward, ApplyRewardConfiguration configuration) {
		return reward.isValid(configuration.getChestDifinition(), configuration.getCardDefinition());
	}

	private Chests getChestBoardOf(Player player) {
		return player.getInventory().getChests();
	}

	private int getPlayerLevelOf(Player player) {
		return player.getProgress().getLevel();
	}
}
