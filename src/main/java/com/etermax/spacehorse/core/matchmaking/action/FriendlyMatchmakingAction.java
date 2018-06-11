package com.etermax.spacehorse.core.matchmaking.action;

import javax.ws.rs.container.AsyncResponse;

import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.matchmaking.service.friendly.FriendlyMatchmakingService;
import com.etermax.spacehorse.core.player.model.Player;

public class FriendlyMatchmakingAction {

	final private FriendlyMatchmakingService matchmakingService;
	final private PlayerWinRateRepository playerWinRateRepository;

	final private boolean allowExtraData;
	private final CaptainCollectionRepository captainCollectionRepository;

	public FriendlyMatchmakingAction(PlayerWinRateRepository playerWinRateRepository, Boolean allowExtraData,
			CaptainCollectionRepository captainCollectionRepository, FriendlyMatchmakingService matchmakingService) {
		this.allowExtraData = allowExtraData;
		this.playerWinRateRepository = playerWinRateRepository;
		this.matchmakingService = matchmakingService;
		this.captainCollectionRepository = captainCollectionRepository;
	}

	public void match(Player player, String opponentId, AsyncResponse asyncResponse, String extraData) {

		PlayerWinRate playerWinRate = playerWinRateRepository.findOrCrateDefault(player.getUserId());
		CaptainsCollection captainCollection = captainCollectionRepository.findOrDefaultBy(player);
		BattlePlayer battlePlayer = BattlePlayerFactory.buildBattlePlayer(player, playerWinRate, captainCollection);

		startMatch(asyncResponse, extraData, playerWinRate, battlePlayer, opponentId);
	}

	private void startMatch(AsyncResponse asyncResponse, String extraData, PlayerWinRate playerWinRate, BattlePlayer battlePlayer,
			String opponentId) {
		if (!allowExtraData) {
			extraData = "";
		}
		matchmakingService.enqueueRequest(battlePlayer, opponentId, asyncResponse, extraData, playerWinRate);
	}
}