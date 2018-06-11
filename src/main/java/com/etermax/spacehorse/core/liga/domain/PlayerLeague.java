package com.etermax.spacehorse.core.liga.domain;

import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PlayerLeague {

	private PlayerSeason current;
	private PlayerSeason previous;

	public PlayerLeague(PlayerSeason current) {
		this.current = current;
		previous = null;
	}

	public PlayerLeague(PlayerSeason current, PlayerSeason previous) {
		this.current = current;
		this.previous = previous;
	}

	public PlayerLeague() {
	}

	public Optional<PlayerSeason> getCurrent() {
		return Optional.ofNullable(current);
	}

	public Optional<PlayerSeason> getPrevious() {
		return Optional.ofNullable(previous);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
