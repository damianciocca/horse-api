package com.etermax.spacehorse.core.battle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.tutorialprogress.TutorialProgressEntry;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.player.action.TutorialException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class BattleRewardsStrategy {

	private ServerTimeProvider serverTimeProvider;

	public BattleRewardsStrategy(ServerTimeProvider serverTimeProvider) {
		this.serverTimeProvider = serverTimeProvider;
	}

	public List<Reward> getBattleRewards(Player player, Battle battle, Catalog catalog) {
		List<Reward> rewards = new ArrayList<>();

		if (!playerIsWinner(player, battle)) {
			return rewards;
		}

		MapDefinition map = catalog.getMapsCollection().findByIdOrFail(battle.getMapId());

		switch (battle.getMatchType()) {
			case TUTORIAL:
				if (player.hasActiveTutorial()) {
					String activeTutorialId = player.getActiveTutorial();
					Optional<TutorialProgressEntry> tutorialProgressEntryOpt = catalog.getTutorialProgressCollection().getEntries().stream()
							.filter(tutorialProgressEntry -> tutorialProgressEntry.getId().equals(activeTutorialId)).findFirst();
					TutorialProgressEntry tutorialProgressEntry = tutorialProgressEntryOpt
							.orElseThrow(() -> new TutorialException("Tutorial id doesn't exist."));
					rewards.add(new Reward(RewardType.CHEST, tutorialProgressEntry.getChestId()));
				}
				break;

			case CHALLENGE:
				addRewards(rewards, player, map);
				break;

			case FRIENDLY:
				//Nothing to do
				break;
		}

		return rewards;
	}

	private void addRewards(List<Reward> rewards, Player player, MapDefinition map) {
		int victoryRewardsPerDay = map.getVictoryRewardsPerDay();
		getGoldReward(map, player, victoryRewardsPerDay).map(rewards::add);
		rewards.add(getChestReward());
	}

	private Optional<Reward> getGoldReward(MapDefinition map, Player player, Integer goldRewardsLimitPerDay) {
		Optional<Reward> reward = Optional.empty();
		long timeNowInSeconds = serverTimeProvider.getTimeNowAsSeconds();
		if (player.getRewardsReceivedToday().canReceiveGoldRewardTodayWithLimit(goldRewardsLimitPerDay, timeNowInSeconds)) {
			reward = Optional.of(new Reward(RewardType.GOLD, map.getVictoryGold()));
			player.getRewardsReceivedToday().incrementGoldRewardReceivedToday(serverTimeProvider.getTimeNowAsSeconds());
		}
		return reward;
	}

	private Reward getChestReward() {
		return new Reward(RewardType.NEXT_CHEST, 1);
	}

	private boolean playerIsWinner(Player player, Battle battle) {
		if (!battle.getFinished()) {
			return false; //Finishing an unfinished battle counts as losing it
		}

		return battle.getWinnerLoginId() != null && battle.getWinnerLoginId().equals(player.getUserId());
	}

}
