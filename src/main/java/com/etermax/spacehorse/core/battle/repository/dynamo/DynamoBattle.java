package com.etermax.spacehorse.core.battle.repository.dynamo;

import java.util.Date;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattleFactory;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;

@DynamoDBTable(tableName = "battle")
public class DynamoBattle implements AbstractDynamoDAO {

	@DynamoDBAutoGeneratedKey
	@DynamoDBHashKey(attributeName = "battleId")
	private String battleId;

	@DynamoDBAttribute(attributeName = "players")
	private List<BattlePlayer> players;

	@DynamoDBAttribute(attributeName = "catalogId")
	private String catalogId;

	@DynamoDBAttribute(attributeName = "seed")
	private int seed;

	@DynamoDBAttribute(attributeName = "mapId")
	private String mapId;

	@DynamoDBAttribute(attributeName = "createdAt")
	private Date createdAt;

	@DynamoDBAttribute(attributeName = "started")
	private boolean started;

	@DynamoDBAttribute(attributeName = "startedAt")
	private Date startedAt;

	@DynamoDBAttribute(attributeName = "finished")
	private boolean finished;

	@DynamoDBAttribute(attributeName = "finishedAt")
	private Date finishedAt;

	@DynamoDBAttribute(attributeName = "winnerLoginId")
	private String winnerLoginId;

	@DynamoDBAttribute(attributeName = "isWinFullScore")
	private boolean isWinFullScore;

	@DynamoDBAttribute(attributeName = "extraData")
	private String extraData;

	@DynamoDBAttribute(attributeName = "isTutorialBattle")
	private boolean isTutorialBattle;

	@DynamoDBAttribute(attributeName = "isFriendlyBattle")
	private boolean isFriendlyBattle;

	@DynamoDBAttribute(attributeName = "team1Stats")
	private TeamStats team1Stats;

	@DynamoDBAttribute(attributeName = "team2Stats")
	private TeamStats team2Stats;

	@DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
	private Date createdDate;

	public DynamoBattle(String battleId, List<BattlePlayer> players, String catalogId, int seed, String mapId, Date createdAt, boolean started,
			Date startedAt, boolean finished, Date finishedAt, String winnerLoginId, String extraData, boolean isTutorialBattle,
			boolean isFriendlyBattle, boolean isWinFullScore, TeamStats team1Stats, TeamStats team2Stats) {
		this.players = players;
		this.catalogId = catalogId;
		this.seed = seed;
		this.mapId = mapId;
		this.createdAt = createdAt;
		this.started = started;
		this.startedAt = startedAt;
		this.finished = finished;
		this.finishedAt = finishedAt;
		this.battleId = battleId;
		this.winnerLoginId = winnerLoginId;
		this.extraData = extraData;
		this.isTutorialBattle = isTutorialBattle;
		this.isFriendlyBattle = isFriendlyBattle;
		this.isWinFullScore = isWinFullScore;
		this.team1Stats = team1Stats;
		this.team2Stats = team2Stats;
	}

	public DynamoBattle() {
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

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public int getSeed() {
		return seed;
	}

	public String getMapId() {
		return mapId;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public static DynamoBattle createFromBattle(Battle battle) {
		String battleId = battle.getBattleId();
		List<BattlePlayer> players = battle.getPlayers();
		String catalogId = battle.getCatalogId();
		int seed = battle.getSeed();
		String mapId = battle.getMapId();
		Date createdAt = battle.getCreatedAt();
		boolean started = battle.getStarted();
		Date startedAt = battle.getStartedAt();
		boolean finished = battle.getFinished();
		Date finishedAt = battle.getFinishedAt();
		String winnerLoginId = battle.getWinnerLoginId();
		String extraData = battle.getExtraData();
		boolean isTutorialBattle = battle.getMatchType().equals(MatchType.TUTORIAL);
		boolean isFriendlyBattle = battle.getMatchType().equals(MatchType.FRIENDLY);
		boolean isWinFullScore = battle.getWinFullScore();
		TeamStats team1Stats = battle.getTeam1Stats();
		TeamStats team2Stats = battle.getTeam2Stats();
		return new DynamoBattle(battleId, players, catalogId, seed, mapId, createdAt, started, startedAt, finished, finishedAt, winnerLoginId,
				extraData, isTutorialBattle, isFriendlyBattle, isWinFullScore, team1Stats, team2Stats);
	}

	public static Battle mapToBattle(DynamoBattle dynamoBattle, BattleFactory battleFactory) {
		List<BattlePlayer> players = dynamoBattle.getPlayers();
		String catalogId = dynamoBattle.getCatalogId();
		int seed = dynamoBattle.getSeed();
		String mapId = dynamoBattle.getMapId();
		Date createdAt = dynamoBattle.getCreatedAt();
		boolean started = dynamoBattle.getStarted();
		Date startedAt = dynamoBattle.getStartedAt();
		String battleId = dynamoBattle.getBattleId();
		boolean finished = dynamoBattle.getFinished();
		Date finishedAt = dynamoBattle.getFinishedAt();
		String winnerLoginId = dynamoBattle.getWinnerLoginId();
		String extraData = dynamoBattle.getExtraData();
		boolean isTutorialBattle = dynamoBattle.getTutorialBattle();
		boolean isWinFullScore = dynamoBattle.getWinFullScore();
		TeamStats team1Stats = dynamoBattle.getTeam1Stats();
		TeamStats team2Stats = dynamoBattle.getTeam2Stats();

		if (team1Stats == null) {
			team1Stats = new TeamStats();
		}

		if (team2Stats == null) {
			team2Stats = new TeamStats();
		}

		MatchType matchType;

		if (dynamoBattle.isTutorialBattle) {
			matchType = MatchType.TUTORIAL;
		} else if (dynamoBattle.isFriendlyBattle) {
			matchType = MatchType.FRIENDLY;
		} else {
			matchType = MatchType.CHALLENGE;
		}

		Battle battle = battleFactory
				.buildBattle(battleId, players, catalogId, seed, mapId, createdAt, started, startedAt, finished, finishedAt, winnerLoginId, extraData,
						matchType, isWinFullScore, team1Stats, team2Stats);

		return battle;
	}

	public void setBattleId(String battleId) {
		this.battleId = battleId;
	}

	public void setPlayers(List<BattlePlayer> players) {
		this.players = players;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public boolean getFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(Date finishedAt) {
		this.finishedAt = finishedAt;
	}

	public String getWinnerLoginId() {
		return winnerLoginId;
	}

	public void setWinnerLoginId(String winnerLoginId) {
		this.winnerLoginId = winnerLoginId;
	}

	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	@Override
	public void setId(String id) {
		this.battleId = id;
	}

	@Override
	public String getId() {
		return battleId;
	}

	public boolean getTutorialBattle() {
		return isTutorialBattle;
	}

	public void setTutorialBattle(boolean tutorialBattle) {
		isTutorialBattle = tutorialBattle;
	}

	public boolean getFriendlyBattle() {
		return isFriendlyBattle;
	}

	public void setFriendlyBattle(boolean friendlyBattle) {
		isFriendlyBattle = friendlyBattle;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean getWinFullScore() {
		return isWinFullScore;
	}

	public void setWinFullScore(boolean winFullScore) {
		isWinFullScore = winFullScore;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean getStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public TeamStats getTeam1Stats() {
		return team1Stats;
	}

	public void setTeam1Stats(TeamStats team1Stats) {
		this.team1Stats = team1Stats;
	}

	public TeamStats getTeam2Stats() {
		return team2Stats;
	}

	public void setTeam2Stats(TeamStats team2Stats) {
		this.team2Stats = team2Stats;
	}


}
