package com.etermax.spacehorse.core.servertime.model;

import java.util.Date;

import org.joda.time.DateTime;

public interface ServerTimeProvider {

	DateTime getDateTime();

	Date getDate();

	long getTimeNowAsSeconds();
}