package com.etermax.spacehorse.mock;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.google.common.collect.Lists;

public class BattleScenarioBuilder {

	private static final String DEFAULT_MAP_ID = "1";
	private static final int DEFAULT_SEED = 1;
	private static final String DEFAULT_EXTRA_DATA = "";
	private static final MatchType DEFAULT_MATCH_TYPE = MatchType.CHALLENGE;
	private static final String CATALOG_ID = "1507298083884";
	private static final String DEFAULT_BATTLE_PLAYER_ID_1 = "100";
	private static final String DEFAULT_BATTLE_PLAYER_ID_2 = "101";
	private Battle battle;

	public BattleScenarioBuilder(FixedServerTimeProvider serverTimeProvider) {
		List<BattlePlayer> players = getDefaultBattlePlayers();
		battle = new Battle(players, CATALOG_ID, DEFAULT_SEED, DEFAULT_MAP_ID, DEFAULT_EXTRA_DATA, DEFAULT_MATCH_TYPE, serverTimeProvider.getDate());
	}

	public BattleScenarioBuilder(FixedServerTimeProvider serverTimeProvider, String winnerLoginId, int winnerScore) {
		List<BattlePlayer> players = getDefaultBattlePlayers();
		battle = new Battle("1", players, CATALOG_ID, DEFAULT_SEED, DEFAULT_MAP_ID, serverTimeProvider.getDate(), false, serverTimeProvider.getDate(),
				true, serverTimeProvider.getDate(), winnerLoginId, "", MatchType.CHALLENGE, true,
				new TeamStats(winnerScore, false, Lists.newArrayList()), new TeamStats(0, true, Lists.newArrayList()));
	}

	private List<BattlePlayer> getDefaultBattlePlayers() {
		BattlePlayer battlePlayer1 = new BattlePlayerScenarioBuilder(DEFAULT_BATTLE_PLAYER_ID_1).build();
		BattlePlayer battlePlayer2 = new BattlePlayerScenarioBuilder(DEFAULT_BATTLE_PLAYER_ID_2).build();
		return newArrayList(battlePlayer1, battlePlayer2);
	}

	public Battle build() {
		return battle;
	}

}
