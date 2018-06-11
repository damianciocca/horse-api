package com.etermax.spacehorse.core.specialoffer.model;

import org.joda.time.DateTime;

import com.etermax.spacehorse.core.servertime.model.ServerTime;

public class NextSpecialOffer {

	private final SpecialOffer specialOffer;
	private final DateTime nextRefreshTime;

	public NextSpecialOffer(SpecialOffer specialOffer, DateTime nextRefreshTime) {

		this.specialOffer = specialOffer;
		this.nextRefreshTime = nextRefreshTime;
	}

	public String getId() {
		return specialOffer.getId();
	}

	public long getNextRefreshTimeInSeconds() {
		return ServerTime.fromDate(nextRefreshTime);
	}
}
