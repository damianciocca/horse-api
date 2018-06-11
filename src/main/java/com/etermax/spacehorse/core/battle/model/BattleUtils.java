package com.etermax.spacehorse.core.battle.model;

import java.util.Optional;

import com.etermax.spacehorse.core.player.model.Player;

public class BattleUtils {

	private static final int PLAYER_1_INDEX = 0;
	private static final int PLAYER_2_INDEX = 1;

	static public int getOpponentShipsDestroyed(Player player, Battle battle) {
		return getTeamStats(player, battle).map(x -> x.getScore()).orElse(0);
	}

	static public Optional<BattlePlayer> findBattlePlayer(Player player, Battle battle) {
		return battle.getPlayers().stream().filter(x -> x.getUserId().equals(player.getUserId())).findFirst();
	}

	static public Optional<BattlePlayer> findNonBattlePlayer(Player player, Battle battle) {
		return battle.getPlayers().stream().filter(x -> !x.getUserId().equals(player.getUserId())).findFirst();
	}

	static public boolean playerIsLoser(Player player, Battle battle) {
		if (!battle.getFinished()) {
			return true; //Finishing an unfinished battle counts as losing it
		}

		return battle.getWinnerLoginId() != null && battle.getWinnerLoginId().length() > 0 && !battle.getWinnerLoginId().equals(player.getUserId());
	}

	static public boolean playerIsWinner(Player player, Battle battle) {
		if (!battle.getFinished()) {
			return false; //Finishing an unfinished battle counts as losing it
		}

		return battle.getWinnerLoginId() != null && battle.getWinnerLoginId().equals(player.getUserId());
	}

	public static Optional<TeamStats> getTeamStats(Player player, Battle battle) {
		Optional<BattlePlayer> playerBattlePlayer = findBattlePlayer(player, battle);

		TeamStats teamStats = playerBattlePlayer.map(battlePlayer -> {
			int index = battle.getPlayers().indexOf(battlePlayer);

			if (index == PLAYER_1_INDEX) {
				return battle.getTeam1Stats();
			} else if (index == PLAYER_2_INDEX) {
				return battle.getTeam2Stats();
			}

			return null;
		}).orElse(null);

		return Optional.ofNullable(teamStats);
	}
}
