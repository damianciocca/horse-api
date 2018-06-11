package com.etermax.spacehorse.core.liga.domain;

import java.util.Optional;

public interface PlayerLeagueRepository {

	Optional<PlayerLeague> findBy(String userId);

	void put(String playerId, PlayerLeague playerLeagueUpdated);
}
