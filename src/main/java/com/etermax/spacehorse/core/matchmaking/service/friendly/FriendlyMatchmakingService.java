package com.etermax.spacehorse.core.matchmaking.service.friendly;

import javax.ws.rs.container.AsyncResponse;

import com.etermax.spacehorse.core.abtest.model.ABTagUtils;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.matchmaking.model.MatchStarterDomainService;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.matchmaking.service.MatchmakingQueueEntry;
import com.etermax.spacehorse.core.matchmaking.service.MatchmakingService;
import com.etermax.spacehorse.core.matchmaking.model.match.Match;

public class FriendlyMatchmakingService extends MatchmakingService<FriendlyMatchQueueEntry> {

	private final MatchStarterDomainService matchStarterDomainService;

	public FriendlyMatchmakingService(CatalogRepository catalogRepository, MatchStarterDomainService matchStarterDomainService) {
		super(catalogRepository);
		this.matchStarterDomainService = matchStarterDomainService;
	}

	public void enqueueRequest(BattlePlayer player, String opponentId, AsyncResponse asyncResponse, String extraData, PlayerWinRate playerWinRate) {
		enqueueRequest(new FriendlyMatchQueueEntry(player, opponentId, asyncResponse, extraData, playerWinRate, System.currentTimeMillis(),
				player.getAbTag(), ABTagUtils.getAbTagBattleCompatible(catalogRepository, player.getAbTag())));
	}

	@Override
	protected boolean doPlayersMatch(Catalog catalog, FriendlyMatchQueueEntry e1, FriendlyMatchQueueEntry e2) {
		return arePlayersLookingForEachOther(e1, e2);
	}

	private boolean arePlayersLookingForEachOther(FriendlyMatchQueueEntry e1, FriendlyMatchQueueEntry e2) {
		return e1.getPlayer().getUserId().equals(e2.getOpponentId()) && e2.getPlayer().getUserId().equals(e1.getOpponentId());
	}

	@Override
	protected Match startMatch(Catalog catalog, FriendlyMatchQueueEntry e1, MatchmakingQueueEntry e2) {
		return matchStarterDomainService.startMatch(catalog, e1, e2, MatchType.FRIENDLY);
	}

	@Override
	protected boolean hasBeenWaitingTooLong(Catalog latestCatalog, MatchmakingQueueEntry e1) {
		long now = System.currentTimeMillis();
		long diff = (Math.abs(now - e1.getQueuedTimeMs())) / 1000L;
		return diff >= 15;
	}

	@Override
	protected Match onHasBeenWaitingTooLong(Catalog catalog, FriendlyMatchQueueEntry queueEntry) {
		queueEntry.notFound();
		return null;
	}
}