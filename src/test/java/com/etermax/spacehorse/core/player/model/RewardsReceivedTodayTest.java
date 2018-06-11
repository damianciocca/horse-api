package com.etermax.spacehorse.core.player.model;

import com.etermax.spacehorse.core.player.model.progress.RewardsReceivedToday;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTime;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RewardsReceivedTodayTest {

	private final int ONE_DAY_IN_SECONDS = 24 * 60 * 60;
	private final int ONE_YEAR_IN_SECONDS = ONE_DAY_IN_SECONDS * 365;
	private FixedServerTimeProvider serverTimeProvider;

	@Before
	public void setUp() throws Exception {
		serverTimeProvider = new FixedServerTimeProvider();
	}

	private RewardsReceivedToday buildRewardsReceivedToday() {
		return new RewardsReceivedToday(serverTimeProvider.getTimeNowAsSeconds());
	}

	@Test
	public void getGoldRewardsReceivedToday() {
		RewardsReceivedToday rewardsReceivedToday = buildRewardsReceivedToday();
		rewardsReceivedToday.setGoldRewardsReceivedToday(4);
		long serverTimeNow = serverTimeProvider.getTimeNowAsSeconds();
		assertThat(rewardsReceivedToday).extracting("expirationServerTime")
				.contains(ServerTime.roundToEndOfDay(serverTimeNow));
		assertThat(rewardsReceivedToday).extracting("goldRewardsReceivedToday").contains(4);
	}

	@Test
	public void getGoldRewardsReceivedTodayWithDay() {
		long serverTimeNow = serverTimeProvider.getTimeNowAsSeconds();
		RewardsReceivedToday rewardsReceivedToday = buildRewardsReceivedToday();
		rewardsReceivedToday.setGoldRewardsReceivedToday(5);
		Integer goldRewardsReceivedToday = rewardsReceivedToday.getGoldRewardsReceivedToday();
		assertThat(goldRewardsReceivedToday).isEqualTo(5);
		assertThat(rewardsReceivedToday).extracting("expirationServerTime")
				.contains(ServerTime.roundToEndOfDay(serverTimeNow));
	}

	@Test
	public void incrementGoldRewardsReceivedToday() {
		RewardsReceivedToday rewardsReceivedToday = buildRewardsReceivedToday();
		long serverTimeNow = serverTimeProvider.getTimeNowAsSeconds();
		rewardsReceivedToday.incrementGoldRewardReceivedToday(serverTimeProvider.getTimeNowAsSeconds());
		assertThat(rewardsReceivedToday).extracting("goldRewardsReceivedToday").contains(1);
		assertThat(rewardsReceivedToday).extracting("expirationServerTime")
				.contains(ServerTime.roundToEndOfDay(serverTimeNow));
	}

	@Test
	public void incrementGoldRewardsReceivedTodayNextYear() {
		RewardsReceivedToday rewardsReceivedToday = buildRewardsReceivedToday();
		rewardsReceivedToday.setGoldRewardsReceivedToday(1);
		long serverTimeFuture = serverTimeProvider.getTimeNowAsSeconds() + ONE_YEAR_IN_SECONDS;
		Integer goldRewardsReceivedToday = rewardsReceivedToday.getGoldRewardsReceivedToday(serverTimeFuture);
		assertThat(goldRewardsReceivedToday).isEqualTo(0);
		assertThat(rewardsReceivedToday).extracting("expirationServerTime")
				.isNotEqualTo(ServerTime.roundToEndOfDay(serverTimeFuture));
	}

	@Test
	public void getGoldRewardsReceivedTodayAfterOneDay() {
		RewardsReceivedToday rewardsReceivedToday = buildRewardsReceivedToday();
		rewardsReceivedToday.setGoldRewardsReceivedToday(10);
		long serverTimeFuture = rewardsReceivedToday.getExpirationServerTime() + ONE_DAY_IN_SECONDS;
		Integer goldRewardsReceivedToday = rewardsReceivedToday.getGoldRewardsReceivedToday(serverTimeFuture);
		assertThat(goldRewardsReceivedToday).isEqualTo(0);
		assertThat(rewardsReceivedToday).extracting("expirationServerTime")
				.isNotEqualTo(ServerTime.roundToEndOfDay(serverTimeFuture));
	}

}
