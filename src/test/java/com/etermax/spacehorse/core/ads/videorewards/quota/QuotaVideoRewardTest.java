package com.etermax.spacehorse.core.ads.videorewards.quota;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.ads.videorewards.exceptions.quota.QuotaVideoRewardExceededException;
import com.etermax.spacehorse.core.ads.videorewards.model.SpeedupVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.VideoRewardScenarioBuilder;

public class QuotaVideoRewardTest {

	private static final String PLACE_NAME = "placeName";
	private static final String USER_ID = "10";

	private FixedServerTimeProvider timeProvider;

	@Before
	public void setUp() throws Exception {
		timeProvider = new FixedServerTimeProvider();
	}

	@Test
	public void givenARecentlyCreatedQuotaWhenConsumeThenTheCounterShouldBeZero() throws Exception {
		// given - when
		QuotaVideoReward quotaVideoReward = aQuotaVideoReward();
		// then
		assertThat(quotaVideoReward.getCounter()).isEqualTo(0);
	}

	@Test
	public void givenARecentlyCreatedQuotaWhenConsumeThenTheCounterShouldBeOne() throws Exception {
		// given
		SpeedupVideoReward speedupVideoReward = new VideoRewardScenarioBuilder(PLACE_NAME).build();
		QuotaVideoReward quotaVideoReward = aQuotaVideoReward();
		// when
		quotaVideoReward.consume(speedupVideoReward);
		// then
		assertThat(quotaVideoReward.getCounter()).isEqualTo(1);
	}

	@Test
	public void givenARecentlyCreatedQuotaWhenAskIfHasAvailableThenTheResponseIsTrue() throws Exception {
		// given
		SpeedupVideoReward speedupVideoReward = new VideoRewardScenarioBuilder(PLACE_NAME).build();
		QuotaVideoReward quotaVideoReward = aQuotaVideoReward();
		// when
		boolean hasAvailable = quotaVideoReward.hasAvailable(speedupVideoReward);
		// then
		assertThat(hasAvailable).isTrue();
	}

	@Test
	public void givenARecentlyCreatedQuotaWhenConsumeThenTheCounterShouldBeTwo() throws Exception {
		// given
		SpeedupVideoReward speedupVideoReward = new VideoRewardScenarioBuilder(PLACE_NAME).build();
		QuotaVideoReward quotaVideoReward = aQuotaVideoReward();
		quotaVideoReward.consume(speedupVideoReward);
		// when
		quotaVideoReward.consume(speedupVideoReward);
		// then
		assertThat(quotaVideoReward.getCounter()).isEqualTo(2);
	}

	@Test
	public void givenARecentlyCreatedQuotaWithOneConsumedWhenAskIfHasAvailableThenTheResponseIsTrue() throws Exception {
		// given
		SpeedupVideoReward speedupVideoReward = new VideoRewardScenarioBuilder(PLACE_NAME).build();
		QuotaVideoReward quotaVideoReward = aQuotaVideoReward();
		quotaVideoReward.consume(speedupVideoReward);
		// when
		boolean hasAvailable = quotaVideoReward.hasAvailable(speedupVideoReward);
		// then
		assertThat(hasAvailable).isTrue();
	}

	@Test
	public void whenMaxQuotaIsReachedThenAnExceptionWasThrown() throws Exception {
		// given
		SpeedupVideoReward speedupVideoReward = new VideoRewardScenarioBuilder(PLACE_NAME).build();
		QuotaVideoReward quotaVideoReward = aQuotaVideoReward();
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		// when
		Throwable exception = catchThrowable(() -> quotaVideoReward.consume(speedupVideoReward));
		// then
		assertThat(exception).isInstanceOf(QuotaVideoRewardExceededException.class);
	}

	@Test
	public void givenARecentlyCreatedQuotaWithAllQuotaConsumedWhenAskIfHasAvailableThenTheResponseIsFalse() throws Exception {
		// given
		SpeedupVideoReward speedupVideoReward = new VideoRewardScenarioBuilder(PLACE_NAME).build();
		QuotaVideoReward quotaVideoReward = aQuotaVideoReward();
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		// when
		boolean hasAvailable = quotaVideoReward.hasAvailable(speedupVideoReward);
		// then
		assertThat(hasAvailable).isFalse();
	}

	@Test
	public void whenMaxQuotaIsReachedAndSystemTimeIncreasedThenTheCounterShouldBeOne() throws Exception {
		// given
		SpeedupVideoReward speedupVideoReward = new VideoRewardScenarioBuilder(PLACE_NAME).build();
		QuotaVideoReward quotaVideoReward = aQuotaVideoReward();
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		increaseSecondsFrom(timeProvider.getDateTime(), 301);
		// when
		quotaVideoReward.consume(speedupVideoReward);
		// then
		assertThat(quotaVideoReward.getCounter()).isEqualTo(1);
	}

	@Test
	public void whenMaxQuotaIsReachedAndSystemTimeIncreasedThenQuotaIsAvailableAgain() throws Exception {
		// given
		SpeedupVideoReward speedupVideoReward = new VideoRewardScenarioBuilder(PLACE_NAME).build();
		QuotaVideoReward quotaVideoReward = aQuotaVideoReward();
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		quotaVideoReward.consume(speedupVideoReward);
		increaseSecondsFrom(timeProvider.getDateTime(), 361); // (time frame configured 300 seconds + 60 seconds)
		// when
		boolean hasAvailable = quotaVideoReward.hasAvailable(speedupVideoReward);
		// then
		assertThat(hasAvailable).isTrue();
	}

	private QuotaVideoReward aQuotaVideoReward() {
		return new QuotaVideoReward(USER_ID, PLACE_NAME, timeProvider.getDateTime(), timeProvider);
	}

	private void increaseSecondsFrom(DateTime now, int seconds) {
		DateTime nextActivationTime = now.plusSeconds(seconds);
		timeProvider.changeTime(nextActivationTime);
	}
}
