package com.etermax.spacehorse.core.liga.response;

import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerLeagueResponse {

	@JsonProperty("current")
	private PlayerSeasonResponse current;
	@JsonProperty("previous")
	private PlayerSeasonResponse previous;

	public PlayerLeagueResponse(PlayerLeague playerLeague) {
		current = playerLeague.getCurrent().map(PlayerSeasonResponse::new).orElse(null);
		previous = playerLeague.getPrevious().map(PlayerSeasonResponse::new).orElse(null);
	}

	public PlayerSeasonResponse getCurrent() {
		return current;
	}

	public PlayerSeasonResponse getPrevious() {
		return previous;
	}
}
