package com.etermax.spacehorse.core.player.resource.response.player.progress;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.player.model.progress.PlayerProgress;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerProgressResponse {

	@JsonProperty("level")
	private Integer level;

	@JsonProperty("xp")
	private Integer xp;

	public PlayerProgressResponse() {
	}

	public PlayerProgressResponse(PlayerProgress playerProgress) {
		this.level = playerProgress.getLevel();
		this.xp = playerProgress.getXp();
	}

	public Integer getLevel() {
		return level;
	}

	public Integer getXp() {
		return xp;
	}


	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
