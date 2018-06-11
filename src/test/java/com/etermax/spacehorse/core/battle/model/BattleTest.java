package com.etermax.spacehorse.core.battle.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;

public class BattleTest {

	private List<BattlePlayer> players = new ArrayList<>();
	private String userId = "userId";
	private BattlePlayer battlePlayer = new BattlePlayerBuilder().setUserId(userId).createBattlePlayer();
	private String catalogId = "catalogId";
	private Integer seed = 1;
	private String mapId = "mapId";
	private Date createdAt = new Date(1l);
	private String extraData = "";

	@Before
	public void setup() {
		players.add(battlePlayer);
	}

	@Test
	public void testBattleIsFinishedWhenWinnerIdIsAmongBattlePlayers() {
		Battle battle = new Battle(players, catalogId, seed, mapId, extraData, MatchType.CHALLENGE, createdAt);
		Date finishedAt = new Date(5l);
		String winnerLoginId = "userId";

		assertThat(battle.finish(finishedAt, winnerLoginId, false, new TeamStats(), new TeamStats())).isTrue();
	}

	@Test
	public void testBattleIsNotFinishedWhenWinnerIdIsNotInBattlePlayers() {
		Battle battle = new Battle(players, catalogId, seed, mapId, extraData, MatchType.CHALLENGE, createdAt);
		Date finishedAt = new Date(5l);
		String winnerLoginId = "anotherUser";

		assertThat(battle.finish(finishedAt, winnerLoginId, false, new TeamStats(), new TeamStats())).isFalse();
	}

	@Test
	public void testWhenCreateBattleWithFinishedBattleIsNotFinished() {
		String battleId = "battleId";
		Boolean started = true;
		Date startedAt = new Date(5l);
		Boolean finished = true;
		Date finishedAt = new Date(5l);
		String winnerLoginId = "anotherUser";
		Battle battle = new Battle(battleId, players, catalogId, seed, mapId, createdAt, started, startedAt, finished, finishedAt, winnerLoginId,
				extraData, MatchType.CHALLENGE, false, new TeamStats(), new TeamStats());

		assertThat(battle.finish(finishedAt, winnerLoginId, false, new TeamStats(), new TeamStats())).isFalse();
	}

}
