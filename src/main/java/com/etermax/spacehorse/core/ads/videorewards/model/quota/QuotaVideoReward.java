package com.etermax.spacehorse.core.ads.videorewards.model.quota;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.ads.videorewards.exceptions.quota.QuotaVideoRewardExceededException;
import com.etermax.spacehorse.core.ads.videorewards.model.VideoReward;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class QuotaVideoReward {

	private static final Logger logger = LoggerFactory.getLogger(QuotaVideoReward.class);
	private static final int INITIAL_COUNTER_VALUE = 0;
	private static final int RESET_COUNTER_VALUE = 1;

	private final String userId;
	private final String placeName;
	private final ServerTimeProvider timeProvider;

	private DateTime creationTime;
	private int counter;

	public QuotaVideoReward(String userId, String placeName, DateTime creationTime, ServerTimeProvider timeProvider) {
		this.userId = userId;
		this.placeName = placeName;
		this.creationTime = creationTime;
		this.counter = INITIAL_COUNTER_VALUE;
		this.timeProvider = timeProvider;
	}

	public static QuotaVideoReward restore(String userId, String placeName, long creationTimeInSeconds, int counter,
			ServerTimeProvider timeProvider) {
		DateTime creationTime = ServerTime.toDateTime(creationTimeInSeconds);
		return new QuotaVideoReward(userId, placeName, creationTime, counter, timeProvider);
	}

	private QuotaVideoReward(String userId, String placeName, DateTime creationTime, int counter, ServerTimeProvider timeProvider) {
		this.userId = userId;
		this.placeName = placeName;
		this.creationTime = creationTime;
		this.counter = counter;
		this.timeProvider = timeProvider;
	}

	public boolean hasAvailable(VideoReward videoReward) {
		int maxQuotaOfViewsPerTimeFrame = videoReward.getMaximumNumberOfViews();
		int timeFrameInSeconds = videoReward.getTimeFrameInSeconds();
		return isQuotaExpired(timeFrameInSeconds, videoReward.getExpectedMaxDurationInSeconds()) || maxQuotaIsNotReached(maxQuotaOfViewsPerTimeFrame);
	}

	public void consume(VideoReward videoReward) {
		int maxQuotaOfViewsPerTimeFrame = videoReward.getMaximumNumberOfViews();
		int timeFrameInSeconds = videoReward.getTimeFrameInSeconds();
		if (isQuotaExpired(timeFrameInSeconds, 0)) {
			resetCounter();
		} else {
			incrementCounter();
			checkIfQuotaIsExceeded(maxQuotaOfViewsPerTimeFrame);
		}
	}

	public String getUserId() {
		return userId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public long getCreationTimeInSeconds() {
		return ServerTime.fromDate(getCreationTime());
	}

	public int getCounter() {
		return counter;
	}

	private void checkIfQuotaIsExceeded(int maxQuotaOfViewsPerTimeFrame) {
		if (maxQuotaIsReached(maxQuotaOfViewsPerTimeFrame)) {
			logger.error("Quota of video reward was exceeded");
			throw new QuotaVideoRewardExceededException(this);
		}
	}

	private boolean isQuotaExpired(int timeFrameInSeconds, int maxVideoRewardDurationInSeconds) {
		DateTime quotaCreationTime = this.creationTime;
		DateTime quotaExpirationTime = quotaCreationTime.plusSeconds(timeFrameInSeconds).plusSeconds(maxVideoRewardDurationInSeconds);
		DateTime now = timeProvider.getDateTime();
		return now.isAfter(quotaExpirationTime);
	}

	private void incrementCounter() {
		this.counter++;
	}

	private void resetCounter() {
		this.counter = RESET_COUNTER_VALUE;
		this.creationTime = timeProvider.getDateTime();
	}

	private boolean maxQuotaIsReached(int maxQuotaOfViewsPerTimeFrame) {
		return this.counter > maxQuotaOfViewsPerTimeFrame;
	}

	private boolean maxQuotaIsNotReached(int maxQuotaOfViewsPerTimeFrame) {
		return this.counter < maxQuotaOfViewsPerTimeFrame;
	}
}
