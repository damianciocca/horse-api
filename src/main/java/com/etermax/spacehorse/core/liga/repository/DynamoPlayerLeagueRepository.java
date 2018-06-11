package com.etermax.spacehorse.core.liga.repository;

import java.util.Optional;

import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueRepository;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class DynamoPlayerLeagueRepository implements PlayerLeagueRepository {

	private final DynamoDao<DynamoPlayerLeague> dynamoDao;

	public DynamoPlayerLeagueRepository(DynamoDao<DynamoPlayerLeague> dynamoDao) {
		this.dynamoDao = dynamoDao;
	}

	@Override
	public Optional<PlayerLeague> findBy(String playerId) {
		DynamoPlayerLeague dynamoPlayerLeague = dynamoDao.find(DynamoPlayerLeague.class, playerId);
		return Optional.ofNullable(dynamoPlayerLeague).map(DynamoPlayerLeague::toPlayerSeasons);
	}

	@Override
	public void put(String playerId, PlayerLeague playerLeagueUpdated) {
		DynamoPlayerLeague dynamoPlayerLeague = new DynamoPlayerLeague(playerId, playerLeagueUpdated);
		dynamoDao.add(dynamoPlayerLeague);
	}
}
