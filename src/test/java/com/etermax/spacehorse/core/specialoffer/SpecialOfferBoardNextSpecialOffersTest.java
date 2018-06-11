package com.etermax.spacehorse.core.specialoffer;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.specialoffer.model.NextSpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.etermax.spacehorse.mock.SpecialOfferDefinitionScenarioBuilder;
import com.etermax.spacehorse.mock.SpecialOfferScenarioBuilder;
import com.google.common.collect.Lists;

public class SpecialOfferBoardNextSpecialOffersTest {

	public static final int FREQUENCY_IN_DAYS = 10;
	private static final String OFFER_STARTER_PACK_ID = "starterPack1";
	private static final String OFFER_EPIC_CHEST_ID = "epicChest";
	private static final String OFFER_EPIC_CHEST_BY_MAP_0_ID = "epicChest_0";
	private static final String OFFER_EPIC_CHEST_BY_MAP_1_ID = "epicChest_1";
	private static final String OFFER_EPIC_CHEST_BY_MAP_2_ID = "epicChest_2";
	private static final int SECONDS_IN_ONE_DAYS = 60 * 60 * 24;
	private FixedServerTimeProvider timeProvider;
	private Player player;
	private DateTime activationTimeInThreeDays;

	@Before
	public void setUp() throws Exception {
		timeProvider = new FixedServerTimeProvider();
		player = new PlayerScenarioBuilder("10").withMapNumber(1).build();
		activationTimeInThreeDays = ServerTime.roundToStartOfDay(timeProvider.getDateTime().plusDays(3));
	}

	@Test
	public void givenAnEmptySpecialOffersConfiguredWhenGetNextSpecialOffersThenNoNextSpecialOffersShouldBeReturned() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = Lists.newArrayList();
		// when
		List<NextSpecialOffer> nextSpecialOffers = specialOfferBoard.getNextSpecialOffers(player, specialOfferDefinitions);
		// then
		assertThat(nextSpecialOffers).isEmpty();
	}

	@Test
	public void givenSpecialOffersConfiguredWhenGetNextSpecialOffersThenNextSpecialOffersShouldBeReturnedCase1() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenTwoSpecialOfferDefinitions();
		// when
		List<NextSpecialOffer> nextSpecialOffers = specialOfferBoard.getNextSpecialOffers(player, specialOfferDefinitions);
		// then
		assertThat(nextSpecialOffers).isNotEmpty();
		assertNextRefreshTime(nextSpecialOffers, activationTimeInThreeDays);
	}

	@Test
	public void givenSpecialOffersConfiguredWhenGetNextSpecialOffersThenNextSpecialOffersShouldBeReturnedCase2() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenTwoSpecialOfferDefinitions();
		increaseTimeInDays(3); // move to same day of activation time
		// when
		List<NextSpecialOffer> nextSpecialOffers = specialOfferBoard.getNextSpecialOffers(player, specialOfferDefinitions);
		// then
		assertThat(nextSpecialOffers).isNotEmpty();
		DateTime expectedNextTime = activationTimeInThreeDays.plusDays(FREQUENCY_IN_DAYS);
		assertNextRefreshTime(nextSpecialOffers, expectedNextTime);
	}

	@Test
	public void givenSpecialOffersConfiguredWhenGetNextSpecialOffersThenNextSpecialOffersShouldBeReturnedCase3() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenTwoSpecialOfferDefinitions();
		increaseTimeInDays(4); // move to next day of activation time
		// when
		List<NextSpecialOffer> nextSpecialOffers = specialOfferBoard.getNextSpecialOffers(player, specialOfferDefinitions);
		// then
		assertThat(nextSpecialOffers).isNotEmpty();
		DateTime expectedNextTime = activationTimeInThreeDays.plusDays(FREQUENCY_IN_DAYS);
		assertNextRefreshTime(nextSpecialOffers, expectedNextTime);
	}

	@Test
	public void givenSpecialOffersConfiguredWhenGetNextSpecialOffersThenNextSpecialOffersShouldBeReturnedCase4() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenTwoSpecialOfferDefinitions();
		increaseTimeInDays(13); //move to next day of activation time (3 days) + the day of first lapse frequency (10 days)
		// when
		List<NextSpecialOffer> nextSpecialOffers = specialOfferBoard.getNextSpecialOffers(player, specialOfferDefinitions);
		// then
		assertThat(nextSpecialOffers).isNotEmpty();
		DateTime expectedNextTime = activationTimeInThreeDays.plusDays(FREQUENCY_IN_DAYS).plusDays(FREQUENCY_IN_DAYS);
		assertNextRefreshTime(nextSpecialOffers, expectedNextTime);
	}

	@Test
	public void givenSpecialOffersConfiguredWhenGetNextSpecialOffersThenNextSpecialOffersShouldBeReturnedCase5() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenTwoSpecialOfferDefinitions();
		increaseTimeInDays(23); //move to next day of activation time (3 days) + the day of second lapse frequency (20 days)
		// when
		List<NextSpecialOffer> nextSpecialOffers = specialOfferBoard.getNextSpecialOffers(player, specialOfferDefinitions);
		// then
		assertThat(nextSpecialOffers).isNotEmpty();
		DateTime expectedNextTime = activationTimeInThreeDays.plusDays(FREQUENCY_IN_DAYS).plusDays(FREQUENCY_IN_DAYS).plusDays(FREQUENCY_IN_DAYS);
		assertNextRefreshTime(nextSpecialOffers, expectedNextTime);
	}

	@Test
	public void givenSpecialOffersConfiguredWhenGetNextSpecialOffersThenNextSpecialOffersShouldBeReturnedCase6() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenTwoSpecialOfferDefinitions();
		increaseTimeInDays(24); //move to next day of activation time (3 days) + the day of second lapse frequency (20 days) + one extra day
		// when
		List<NextSpecialOffer> nextSpecialOffers = specialOfferBoard.getNextSpecialOffers(player, specialOfferDefinitions);
		// then
		assertThat(nextSpecialOffers).isNotEmpty();
		DateTime expectedNextTime = activationTimeInThreeDays.plusDays(FREQUENCY_IN_DAYS).plusDays(FREQUENCY_IN_DAYS).plusDays(FREQUENCY_IN_DAYS);
		assertNextRefreshTime(nextSpecialOffers, expectedNextTime);
	}

	@Test
	public void givenManySpecialOffersConfiguredWhenGetNextSpecialOffersThenNextSpecialOffersShouldBeReturned() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenScheduledSpecialOfferDefinitionsWithFilterByMaps();
		increaseTimeInDays(6); //move to next day of activation time (3 days) + the day of second lapse frequency (3 days)
		// when
		List<NextSpecialOffer> nextSpecialOffers = specialOfferBoard.getNextSpecialOffers(player, specialOfferDefinitions);
		// then
		assertThat(nextSpecialOffers).isNotEmpty();
		DateTime expectedNextTime = activationTimeInThreeDays.plusDays(3).plusDays(3);
		assertNextRefreshTime(nextSpecialOffers, expectedNextTime);
	}

	private void assertNextRefreshTime(List<NextSpecialOffer> nextSpecialOffers, DateTime expectedNextTime) {
		long expectedNexTimeInSeconds = ServerTime.fromDate(expectedNextTime);
		assertThat(nextSpecialOffers).extracting(NextSpecialOffer::getNextRefreshTimeInSeconds).containsOnly(expectedNexTimeInSeconds);
	}

	private SpecialOfferBoard givenAnEmptySpecialOfferBoard() {
		return new SpecialOfferBoard(timeProvider);
	}

	private List<SpecialOfferDefinition> givenTwoSpecialOfferDefinitions() {
		SpecialOfferDefinition epicChestOffer = aOnTimeEpicChestOfferDefinition();
		SpecialOfferDefinition starterPackOffer = anScheduledStarterPackOfferDefinition();
		return newArrayList(epicChestOffer, starterPackOffer);
	}

	private List<SpecialOfferDefinition> givenScheduledSpecialOfferDefinitionsWithFilterByMaps() {
		return newArrayList( //
				anScheduledEpicChestForMap0OfferDefinition(), //
				anScheduledEpicChestForMap1OfferDefinition(), //
				anScheduledEpicChestForMap2OfferDefinition());
	}

	private SpecialOfferDefinition aOnTimeEpicChestOfferDefinition() {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_EPIC_CHEST_ID, 100).build();
	}

	private SpecialOfferDefinition anScheduledEpicChestForMap0OfferDefinition() {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_EPIC_CHEST_BY_MAP_0_ID, activationTimeInThreeDays, 2, SECONDS_IN_ONE_DAYS, 0).build();
	}

	private SpecialOfferDefinition anScheduledEpicChestForMap1OfferDefinition() {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_EPIC_CHEST_BY_MAP_1_ID, activationTimeInThreeDays, 3, SECONDS_IN_ONE_DAYS, 1).build();
	}

	private SpecialOfferDefinition anScheduledEpicChestForMap2OfferDefinition() {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_EPIC_CHEST_BY_MAP_2_ID, activationTimeInThreeDays, 4, SECONDS_IN_ONE_DAYS, 2).build();
	}

	private SpecialOfferDefinition anScheduledStarterPackOfferDefinition() {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_STARTER_PACK_ID, activationTimeInThreeDays, FREQUENCY_IN_DAYS, 900) //
				.withAnyItem("GloriousChestId", "gloriousChestTypeId", RewardType.CHEST) //
				.withAnyItem("FistfulCoins800Id", "", RewardType.GOLD) //
				.withAnyItem("FistfulGems100Id", "", RewardType.GEMS) //
				.build();
	}

	private void increaseTimeInDays(int days) {
		DateTime timeIncreased = timeProvider.getDateTime().plusDays(days);
		timeProvider.changeTime(timeIncreased);
	}
}
