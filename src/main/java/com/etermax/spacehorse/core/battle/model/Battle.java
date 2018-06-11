package com.etermax.spacehorse.core.battle.model;

import java.util.Date;
import java.util.List;

import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;

public class Battle {

	private String battleId;
	private List<BattlePlayer> players;
	private String catalogId;
	private int seed;
	private String mapId;
	private Date createdAt;
	private boolean started;
	private Date startedAt;
	private boolean finished;
	private Date finishedAt;
	private String winnerLoginId;
	private String extraData;
	private MatchType matchType;
	private boolean isWinFullScore;

	private TeamStats team1Stats;
	private TeamStats team2Stats;

	public Battle(List<BattlePlayer> players, String catalogId, int seed, String mapId, String extraData, MatchType matchType, Date createdAt) {
		this.players = players;
		this.catalogId = catalogId;
		this.seed = seed;
		this.mapId = mapId;
		this.createdAt = createdAt;
		this.extraData = extraData;
		this.started = false;
		this.finished = false;
		this.matchType = matchType;
		this.team1Stats = new TeamStats();
		this.team2Stats = new TeamStats();
	}

	public Battle(String battleId, List<BattlePlayer> players, String catalogId, int seed, String mapId, Date createdAt, boolean started,
			Date startedAt, boolean finished, Date finishedAt, String winnerLoginId, String extraData, MatchType matchType, boolean isWinFullScore,
			TeamStats team1Stats, TeamStats team2Stats) {
		this.battleId = battleId;
		this.players = players;
		this.catalogId = catalogId;
		this.seed = seed;
		this.mapId = mapId;
		this.createdAt = createdAt;
		this.started = started;
		this.startedAt = startedAt;
		this.finished = finished;
		this.finishedAt = finishedAt;
		this.winnerLoginId = winnerLoginId;
		this.extraData = extraData;
		this.matchType = matchType;
		this.isWinFullScore = isWinFullScore;
		this.team1Stats = team1Stats;
		this.team2Stats = team2Stats;
	}

	public boolean start(Date startedAt) {
		if (started) {
			return false;
		}

		this.started = true;
		this.startedAt = startedAt;
		return true;
	}

	public boolean finish(Date finishedAt, String winnerLoginId, boolean isWinFullScore, TeamStats team1Stats, TeamStats team2Stats) {
		if (!winnerLoginId.isEmpty() && !hasPlayer(winnerLoginId)) {
			return false;
		}
		if (finished) {
			return false;
		}
		this.finished = true;
		this.finishedAt = finishedAt;
		this.winnerLoginId = winnerLoginId;
		this.isWinFullScore = isWinFullScore;
		this.team1Stats = team1Stats;
		this.team2Stats = team2Stats;

		return true;
	}

	private boolean hasPlayer(String loginId) {
		return getPlayers().stream().anyMatch(battlePlayer -> battlePlayer.getUserId().equals(loginId));
	}

	public String getBattleId() {
		return battleId;
	}

	public List<BattlePlayer> getPlayers() {
		return players;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public int getSeed() {
		return seed;
	}

	public String getMapId() {
		return mapId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public boolean getStarted() {
		return started;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public boolean getFinished() {
		return finished;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public String getWinnerLoginId() {
		return winnerLoginId;
	}

	public String getExtraData() {
		return extraData;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public boolean getWinFullScore() {
		return isWinFullScore;
	}

	public TeamStats getTeam1Stats() {
		return team1Stats;
	}

	public TeamStats getTeam2Stats() {
		return team2Stats;
	}
}
