package com.etermax.spacehorse.core.liga.action;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Comparator.comparing;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.league.LeagueRewardsDefinition;
import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueRepository;
import com.etermax.spacehorse.core.liga.domain.PlayerSeason;
import com.etermax.spacehorse.core.liga.exception.ClaimLeagueRewardsException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.unlock.AvailableQuestSlotDifficultiesByPlayerLevel;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class ClaimLeagueRewardsAction {

	private final GetRewardsDomainService getRewardsDomainService;
	private final ApplyRewardDomainService applyRewardDomainService;
	private final ServerTimeProvider serverTimeProvider;
	private final PlayerRepository playerRepository;
	private final PlayerLeagueRepository playerLeagueRepository;

	public ClaimLeagueRewardsAction(GetRewardsDomainService getRewardsDomainService, ApplyRewardDomainService applyRewardDomainService,
			ServerTimeProvider serverTimeProvider, PlayerRepository playerRepository, PlayerLeagueRepository playerLeagueRepository) {
		this.getRewardsDomainService = getRewardsDomainService;
		this.applyRewardDomainService = applyRewardDomainService;
		this.serverTimeProvider = serverTimeProvider;
		this.playerRepository = playerRepository;
		this.playerLeagueRepository = playerLeagueRepository;
	}

	public List<RewardResponse> claim(Player player, Catalog catalog) {

		if(!catalog.getGameConstants().isLeagueEnabled()){
			throw new ClaimLeagueRewardsException("League feature is disabled");
		}

		GetRewardConfiguration configuration = GetRewardConfiguration.createBy(catalog, serverTimeProvider);
		PlayerLeague playerLeague = getPlayerLeague(player);
		PlayerSeason previousPlayerSeason = getPreviousPlayerSeason(playerLeague);
		ChestDefinition chestDefinition = getChestDefinition(catalog, previousPlayerSeason);

		List<Reward> rewards = getRewardsDomainService.getRewards(player, chestDefinition, player.getMapNumber(), configuration);

		ApplyRewardConfiguration applyRewardConfiguration = ApplyRewardConfiguration.createBy(catalog);

		List<RewardResponse> rewardResponses = applyRewardDomainService
				.applyRewards(player, rewards, RewardContext.fromMapNumber(player.getMapNumber()), applyRewardConfiguration, newArrayList());

		playerRepository.update(player);

		updatePlayerLeague(player, playerLeague, previousPlayerSeason);

		return rewardResponses;

	}

	private void updatePlayerLeague(Player player, PlayerLeague playerLeague, PlayerSeason previousPlayerSeason) {
		previousPlayerSeason.claimReward();
		if(playerLeague.getCurrent().isPresent()){
			playerLeagueRepository.put(player.getUserId(), new PlayerLeague(playerLeague.getCurrent().get(), previousPlayerSeason));
		}else{
			playerLeagueRepository.put(player.getUserId(), new PlayerLeague(null, previousPlayerSeason));
		}
	}

	private ChestDefinition getChestDefinition(Catalog catalog, PlayerSeason previousPlayerSeason) {
		Integer mmrReached = previousPlayerSeason.getMmr();

		List<LeagueRewardsDefinition> leagueRewardsDefinitions = catalog.getLeagueRewardsDefinitionCollection().getEntries();
		leagueRewardsDefinitions.sort(byMmr());
		LeagueRewardsDefinition leagueRewardsDefinition = null;

		for (LeagueRewardsDefinition leagueRewardsDefinitionToEvaluate: leagueRewardsDefinitions) {
			if (leagueRewardsDefinitionToEvaluate.getMmr() <= mmrReached) {
				leagueRewardsDefinition = leagueRewardsDefinitionToEvaluate;
			}
		}

		return catalog.getChestDefinitionsCollection().findByIdOrFail(leagueRewardsDefinition.getReward());
	}

	private Comparator<LeagueRewardsDefinition> byMmr() {
		return comparing(LeagueRewardsDefinition::getMmr);
	}

	private PlayerSeason getPreviousPlayerSeason(PlayerLeague playerLeague) {
		PlayerSeason previousPlayerSeason = playerLeague.getPrevious()
				.orElseThrow(() -> new ClaimLeagueRewardsException("Previous player league not found"));
		checkArgument(previousPlayerSeason.isRewardPending(), "League Reward is already claimed");
		return previousPlayerSeason;
	}

	private PlayerLeague getPlayerLeague(Player player) {
		return playerLeagueRepository.findBy(player.getUserId())
					.orElseThrow(() -> new ClaimLeagueRewardsException("Player League not found"));
	}
}
