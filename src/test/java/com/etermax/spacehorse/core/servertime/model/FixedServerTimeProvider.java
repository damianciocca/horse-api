package com.etermax.spacehorse.core.servertime.model;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class FixedServerTimeProvider implements ServerTimeProvider {

	private DateTime date;

	public FixedServerTimeProvider() {
		// do nothing!
	}

	public FixedServerTimeProvider(DateTime date) {
		this.date = date;
	}

	@Override
	public DateTime getDateTime() {
		if (date == null) {
			date = new DateTime().withZone(DateTimeZone.UTC);
		}
		return date;
	}

	@Override
	public Date getDate() {
		return getDateTime().toDate();
	}

	@Override
	public long getTimeNowAsSeconds() {
		return getDateTime().getMillis() / 1000L;
	}

	public void changeTime(DateTime date) {
		this.date = date;
	}
}
