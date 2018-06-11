package com.etermax.spacehorse.core.matchmaking.service;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.common.resource.response.ErrorResponse;
import com.etermax.spacehorse.core.matchmaking.action.ChallengeMatchmakingAction;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.matchmaking.resource.response.MatchmakingResponse;

public class MatchmakingQueueEntry {

	private static final Logger logger = LoggerFactory.getLogger(ChallengeMatchmakingAction.class);

	private final AsyncResponse asyncResponse;
	private final BattlePlayer player;
	private final String extraData;
	private final PlayerWinRate playerWinRate;
	private final long queuedTimeMs;
	private final String abTag;
	private final boolean abTagBattleCompatible;

	private boolean responseSent;

	public BattlePlayer getPlayer() {
		return player;
	}

	public String getExtraData() {
		return extraData;
	}

	public PlayerWinRate getPlayerWinRate() {
		return playerWinRate;
	}

	public long getQueuedTimeMs() {
		return queuedTimeMs;
	}

	public String getAbTag() {
		return abTag;
	}

	public boolean getAbTagBattleCompatible() {
		return abTagBattleCompatible;
	}

	public boolean isResponseSent() {
		return responseSent;
	}

	public MatchmakingQueueEntry(BattlePlayer player, AsyncResponse asyncResponse, String extraData, PlayerWinRate playerWinRate, long queuedTimeMs,
			String abTag, boolean abTagBattleCompatible) {
		this.asyncResponse = asyncResponse;
		this.player = player;
		this.extraData = extraData;
		this.playerWinRate = playerWinRate;
		this.queuedTimeMs = queuedTimeMs;
		this.abTag = abTag;
		this.abTagBattleCompatible = abTagBattleCompatible;
	}

	public void notFound() {
		try {
			responseSent = true;
			asyncResponse
					.resume(Response.status(Response.Status.NOT_FOUND).entity(ErrorResponse.build("opponent not found"))
							.build());
		} catch (Exception ex) {
			logger.error("While dispatching 'opponent not found'", ex);
		}
	}

	public void found(MatchmakingResponse response) {
		try {
			responseSent = true;
			asyncResponse.resume(Response.ok(response).build());
		} catch (Exception ex) {
			logger.error("While dispatching 'opponent found'", ex);
		}
	}

	@Override
	public String toString() {
		return String.valueOf(playerWinRate.getMmr());
	}

}