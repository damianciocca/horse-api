package com.etermax.spacehorse.core.matchmaking.service.challenge;

import static java.lang.String.format;

import javax.ws.rs.container.AsyncResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.abtest.model.ABTagUtils;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.matchmaking.model.MatchStarterDomainService;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.matchmaking.model.MatchmakingAlgorithm;
import com.etermax.spacehorse.core.matchmaking.model.MatchmakingAlgorithmConfiguration;
import com.etermax.spacehorse.core.matchmaking.service.MatchmakingQueueEntry;
import com.etermax.spacehorse.core.matchmaking.service.MatchmakingService;
import com.etermax.spacehorse.core.matchmaking.model.match.Match;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class ChallengeMatchmakingService extends MatchmakingService<MatchmakingQueueEntry> {

	private static final Logger logger = LoggerFactory.getLogger(ChallengeMatchmakingService.class);

	private final MatchStarterDomainService matchStarterDomainService;
	private final MatchmakingAlgorithm matchmakingAlgorithm;
	private final ServerTimeProvider serverTimeProvider;

	public ChallengeMatchmakingService(CatalogRepository catalogRepository, MatchStarterDomainService matchStarterDomainService,
			ServerTimeProvider serverTimeProvider) {
		super(catalogRepository);
		this.matchStarterDomainService = matchStarterDomainService;
		this.serverTimeProvider = serverTimeProvider;
		this.matchmakingAlgorithm = new MatchmakingAlgorithm();
	}

	public void enqueueRequest(BattlePlayer player, AsyncResponse asyncResponse, String extraData, PlayerWinRate playerWinRate) {
		enqueueRequest(new MatchmakingQueueEntry(player, asyncResponse, extraData, playerWinRate, System.currentTimeMillis(), player.getAbTag(),
				ABTagUtils.getAbTagBattleCompatible(catalogRepository, player.getAbTag())));
	}

	public void forceStartBotMatch(Catalog catalog, MatchmakingQueueEntry queueEntry) {
		logger.info(format("force to play with bot. Current player MMR: [ %s ]", queueEntry.getPlayer().getMmr()));
		matchStarterDomainService.startBotMatch(catalog, queueEntry, serverTimeProvider);
	}

	@Override
	protected Match startMatch(Catalog catalog, MatchmakingQueueEntry e1, MatchmakingQueueEntry e2) {
		return matchStarterDomainService.startMatch(catalog, e1, e2, MatchType.CHALLENGE);
	}

	@Override
	protected boolean doPlayersMatch(Catalog catalog, MatchmakingQueueEntry e1, MatchmakingQueueEntry e2) {
		return arePlayersOnSameLevel(catalog.getGameConstants().getMatchmakingAlgorithmConfiguration(), e1, e2);
	}

	private boolean arePlayersOnSameLevel(MatchmakingAlgorithmConfiguration configuration, MatchmakingQueueEntry e1, MatchmakingQueueEntry e2) {
		return matchmakingAlgorithm.invokeWith(configuration, e1.getPlayerWinRate(), e2.getPlayerWinRate());
	}

	@Override
	protected boolean hasBeenWaitingTooLong(Catalog catalog, MatchmakingQueueEntry e1) {
		long now = System.currentTimeMillis();
		long diff = (Math.abs(now - e1.getQueuedTimeMs())) / 1000L;
		return diff >= catalog.getGameConstants().getMatchMakingTimeoutInSeconds();
	}

	@Override
	protected Match onHasBeenWaitingTooLong(Catalog catalog, MatchmakingQueueEntry queueEntry) {
		return matchStarterDomainService.startBotMatch(catalog, queueEntry, serverTimeProvider);
	}
}