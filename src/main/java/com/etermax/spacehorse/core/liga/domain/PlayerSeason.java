package com.etermax.spacehorse.core.liga.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

public class PlayerSeason {

	private DateTime initialDateTime;
	private Integer mmr;
	private boolean rewardPending;

	public PlayerSeason(DateTime initialDateTime, Integer mmr) {
		this.initialDateTime = initialDateTime;
		this.mmr = mmr;
	}

	public PlayerSeason(DateTime initialDateTime, Integer mmr, boolean rewardPending ) {
		this.initialDateTime = initialDateTime;
		this.mmr = mmr;
		this.rewardPending = rewardPending;
	}

	public DateTime getInitialDateTime() {
		return initialDateTime;
	}

	public Integer getMmr() {
		return mmr;
	}

	public boolean isRewardPending() {
		return rewardPending;
	}

	public void rewardIsPending() {
		rewardPending = true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	public void claimReward() {
		rewardPending = false;
	}
}
