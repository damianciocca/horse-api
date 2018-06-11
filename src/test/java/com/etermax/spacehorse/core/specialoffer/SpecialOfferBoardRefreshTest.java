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
import com.etermax.spacehorse.core.specialoffer.model.SpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.etermax.spacehorse.mock.SpecialOfferDefinitionScenarioBuilder;
import com.etermax.spacehorse.mock.SpecialOfferScenarioBuilder;
import com.google.common.collect.Lists;

public class SpecialOfferBoardRefreshTest {

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
		activationTimeInThreeDays = timeProvider.getDateTime().plusDays(3);
	}

	@Test
	public void givenNoAvailableOffersWhenRefreshThenNoOffersShouldBeShown() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = Lists.newArrayList();
		// when
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		// then
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).isEmpty();
	}

	@Test
	public void givenOneTimeSpecialOfferAvailableWhenRefreshThenOneSpecialOfferShouldBeShown() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenTwoSpecialOfferDefinitions();
		// when
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		// then
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).isNotEmpty();
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).extracting(SpecialOffer::getId).containsExactly(OFFER_EPIC_CHEST_ID);
	}

	@Test
	public void givenTwoSpecialOffersAvailableWhenRefreshThenBothSpecialOfferShouldBeShownCase1() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenTwoSpecialOfferDefinitions();
		increaseTimeInDays(3); // move to same day of activation time
		// when
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		// then
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).extracting(SpecialOffer::getId)
				.containsExactly(OFFER_STARTER_PACK_ID, OFFER_EPIC_CHEST_ID);
	}

	@Test
	public void givenTwoSpecialOffersAvailableWhenRefreshThenBothSpecialOfferShouldBeShownCase2() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenTwoSpecialOfferDefinitions();
		increaseTimeInDays(13); // move to the day of first lapse frequency (10 days)  + activation time (3 days)
		// when
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		// then
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).extracting(SpecialOffer::getId)
				.containsExactly(OFFER_STARTER_PACK_ID, OFFER_EPIC_CHEST_ID);
	}

	@Test
	public void givenTwoSpecialOffersAvailableWhenRefreshThenBothSpecialOfferShouldBeShownCase3() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenTwoSpecialOfferDefinitions();
		increaseTimeInDays(23); // move to the day of second lapse frequency (20 days)  + activation time (3 days)
		// when
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		// then
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).extracting(SpecialOffer::getId)
				.containsExactly(OFFER_STARTER_PACK_ID, OFFER_EPIC_CHEST_ID);
	}

	@Test
	public void givenTwoAvailableOffersWhenTheTimePassAndRefreshTwiceThenOneOfferShouldBeExpired() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		SpecialOfferDefinition epicChestOfferDefinition = aOnTimeEpicChestOfferDefinition(SECONDS_IN_ONE_DAYS * 3);
		specialOfferBoard.put(new SpecialOfferScenarioBuilder(epicChestOfferDefinition, timeProvider).build());
		List<SpecialOfferDefinition> specialOfferDefinitions = newArrayList(epicChestOfferDefinition, anScheduledStarterPackOfferDefinition());
		increaseTimeInDays(13);
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		// when
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		// then
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).extracting(SpecialOffer::getId).containsExactly(OFFER_STARTER_PACK_ID);
	}

	@Test
	public void givenTwoAvailableOffersWhenTimePassAndRefreshManyTimesThenNoOffersShouldBeShown() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		SpecialOfferDefinition epicChestOfferDefinition = aOnTimeEpicChestOfferDefinition(SECONDS_IN_ONE_DAYS * 3);
		specialOfferBoard.put(new SpecialOfferScenarioBuilder(epicChestOfferDefinition, timeProvider).build());
		List<SpecialOfferDefinition> specialOfferDefinitions = newArrayList(epicChestOfferDefinition, anScheduledStarterPackOfferDefinition());
		increaseTimeInDays(13);
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		increaseTimeInDays(1);
		// when
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		// then
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).isEmpty();
	}

	@Test
	public void givenOneAvailableOfferWhenRefreshAndNotExistsDefinitionsThenTheOfferShouldBeRemoved() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		SpecialOfferDefinition epicChestOfferDefinition = aOnTimeEpicChestOfferDefinition(SECONDS_IN_ONE_DAYS * 3);
		specialOfferBoard.put(new SpecialOfferScenarioBuilder(epicChestOfferDefinition, timeProvider).build());
		increaseTimeInDays(3); // move to same day of activation time
		// when
		specialOfferBoard.refresh(player, newArrayList());
		// then
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).isEmpty();
	}

	@Test
	public void givenTwoAvailableOffersWhenPlayerWithMap1RefreshThenTheBothOffersShouldBeShown() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenScheduledSpecialOfferDefinitionsWithFilterByMaps();
		increaseTimeInDays(3); // move to same day of activation time
		// when
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		// then
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).extracting(SpecialOffer::getId)
				.containsExactly(OFFER_EPIC_CHEST_BY_MAP_1_ID, OFFER_STARTER_PACK_ID);
	}

	@Test
	public void givenOneAvailableOffersWhenPlayerWithMap1RefreshThenTheOfferShouldBeShown() throws Exception {
		// given
		SpecialOfferBoard specialOfferBoard = givenAnEmptySpecialOfferBoard();
		List<SpecialOfferDefinition> specialOfferDefinitions = givenScheduledSpecialOfferDefinitionsWithFilterByMaps();
		increaseTimeInDays(3); // move to same day of activation time
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		increaseTimeInSeconds(901); // expire one of the offers
		// when
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		// then
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).extracting(SpecialOffer::getId).containsExactly(OFFER_EPIC_CHEST_BY_MAP_1_ID);
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
				anScheduledStarterPackOfferDefinition(), //
				anScheduledEpicChestForMap0OfferDefinition(), //
				anScheduledEpicChestForMap1OfferDefinition(), //
				anScheduledEpicChestForMap2OfferDefinition());
	}

	private SpecialOfferDefinition aOnTimeEpicChestOfferDefinition() {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_EPIC_CHEST_ID, 100).build();
	}

	private SpecialOfferDefinition aOnTimeEpicChestOfferDefinition(int durationInSeconds) {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_EPIC_CHEST_ID, durationInSeconds).build();
	}

	private SpecialOfferDefinition anScheduledEpicChestForMap0OfferDefinition() {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_EPIC_CHEST_BY_MAP_0_ID, activationTimeInThreeDays, 2, SECONDS_IN_ONE_DAYS, 0).build();
	}

	private SpecialOfferDefinition anScheduledEpicChestForMap1OfferDefinition() {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_EPIC_CHEST_BY_MAP_1_ID, activationTimeInThreeDays, 2, SECONDS_IN_ONE_DAYS, 1).build();
	}

	private SpecialOfferDefinition anScheduledEpicChestForMap2OfferDefinition() {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_EPIC_CHEST_BY_MAP_2_ID, activationTimeInThreeDays, 2, SECONDS_IN_ONE_DAYS, 2).build();
	}

	private SpecialOfferDefinition anScheduledStarterPackOfferDefinition() {
		return new SpecialOfferDefinitionScenarioBuilder(OFFER_STARTER_PACK_ID, activationTimeInThreeDays, 10, 900) //
				.withAnyItem("GloriousChestId", "gloriousChestTypeId", RewardType.CHEST) //
				.withAnyItem("FistfulCoins800Id", "", RewardType.GOLD) //
				.withAnyItem("FistfulGems100Id", "", RewardType.GEMS) //
				.build();
	}

	private void increaseTimeInDays(int days) {
		DateTime timeIncreased = timeProvider.getDateTime().plusDays(days);
		timeProvider.changeTime(timeIncreased);
	}

	private void increaseTimeInSeconds(int seconds) {
		DateTime timeIncreased = timeProvider.getDateTime().plusSeconds(seconds);
		timeProvider.changeTime(timeIncreased);
	}
}
