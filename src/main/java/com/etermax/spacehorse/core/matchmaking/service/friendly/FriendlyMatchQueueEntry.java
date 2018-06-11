package com.etermax.spacehorse.core.matchmaking.service.friendly;

import javax.ws.rs.container.AsyncResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.matchmaking.action.ChallengeMatchmakingAction;
import com.etermax.spacehorse.core.matchmaking.service.MatchmakingQueueEntry;

public class FriendlyMatchQueueEntry extends MatchmakingQueueEntry {

	private static final Logger logger = LoggerFactory.getLogger(ChallengeMatchmakingAction.class);

	private final String opponentId;

	public String getOpponentId() {
		return opponentId;
	}

	public FriendlyMatchQueueEntry(BattlePlayer player, String opponentId, AsyncResponse asyncResponse, String extraData, PlayerWinRate playerWinRate,
			long queuedTimeMs, String abTag, boolean abTagBattleCompatible) {
		super(player, asyncResponse, extraData, playerWinRate, queuedTimeMs, abTag, abTagBattleCompatible);
		this.opponentId = opponentId;
	}
}