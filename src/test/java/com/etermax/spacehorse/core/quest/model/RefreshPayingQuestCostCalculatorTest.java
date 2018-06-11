package com.etermax.spacehorse.core.quest.model;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;

public class RefreshPayingQuestCostCalculatorTest {

	private FixedServerTimeProvider fixedServerTimeProvider;
	private RefreshPayingQuestCostCalculator refreshPayingQuestCostCalculator;
	private QuestBoardConfiguration configuration;
	private int gemsCost;

	@Before
	public void setUp() {
		DateTime dateTime = new DateTime(Date.from(Instant.EPOCH)).plusDays(1).withZone(DateTimeZone.UTC);
		fixedServerTimeProvider = new FixedServerTimeProvider(dateTime);
		refreshPayingQuestCostCalculator = new RefreshPayingQuestCostCalculator(fixedServerTimeProvider);
		configuration = new QuestBoardConfiguration(100, 10, 720, 1);
	}

	@Test
	public void whenNextQuestRemainingTimeIs720ThenGemsCostIs1() {
		QuestSlot questSlot = givenQuestSlotWithRefreshTime(fixedServerTimeProvider.getTimeNowAsSeconds() + 720);

		whenCalculateGemsCost(questSlot);

		thenGemsCostIs(1);
	}

	@Test
	public void whenNextQuestRemainingTimeIs721ThenGemsCostIs2() {
		QuestSlot questSlot = givenQuestSlotWithRefreshTime(fixedServerTimeProvider.getTimeNowAsSeconds() + 721);

		whenCalculateGemsCost(questSlot);

		thenGemsCostIs(2);
	}

	@Test
	public void whenNextQuestRemainingTimeIsNegativeThenGemsCostIs0() {
		QuestSlot questSlot = givenQuestSlotWithRefreshTime(fixedServerTimeProvider.getTimeNowAsSeconds() - 3);

		whenCalculateGemsCost(questSlot);

		thenGemsCostIs(0);
	}

	@Test
	public void whenNextQuestRemainingTimeIs100ThenGemsCostIs1() {
		QuestSlot questSlot = givenQuestSlotWithRefreshTime(fixedServerTimeProvider.getTimeNowAsSeconds() + 100);

		whenCalculateGemsCost(questSlot);

		thenGemsCostIs(1);
	}

	private QuestSlot givenQuestSlotWithRefreshTime(long refreshTime) {
		QuestSlot questSlot = mock(QuestSlot.class);
		when(questSlot.getRefreshTimeInSeconds()).thenReturn(refreshTime);
		return questSlot;
	}

	private void thenGemsCostIs(int expectedGemsCost) {
		assertThat(gemsCost).isEqualTo(expectedGemsCost);
	}

	private void whenCalculateGemsCost(QuestSlot questSlot) {
		gemsCost = refreshPayingQuestCostCalculator.calculate(questSlot, configuration);
	}
}
