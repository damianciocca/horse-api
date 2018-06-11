package com.etermax.spacehorse.core.battle.model;

import java.util.Date;
import java.util.List;

import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;

public interface BattleFactory {
	Battle buildBattle(List<BattlePlayer> players, String catalogId, int seed, MapDefinition map, String extraData, Date startingDate,
			MatchType matchType);

	Battle buildBattle(String battleId, List<BattlePlayer> players, String catalogId, Integer seed, String mapId, Date createdAt, Boolean started,
			Date startedAt, Boolean finished, Date finishedAt, String winnerLoginId, String extraData, MatchType matchType, Boolean isWinFullScore,
			TeamStats team1Stats, TeamStats team2Stats);
}
