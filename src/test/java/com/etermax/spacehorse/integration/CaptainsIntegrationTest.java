package com.etermax.spacehorse.integration;

import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD_ID;
import static com.google.common.collect.Lists.newArrayList;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.capitain.resource.BuyCaptainRequest;
import com.etermax.spacehorse.core.capitain.resource.SelectCaptainRequest;
import com.etermax.spacehorse.core.capitain.resource.skins.BuyCaptainSkinRequest;
import com.etermax.spacehorse.core.capitain.resource.skins.CaptainSlotRequest;
import com.etermax.spacehorse.core.capitain.resource.skins.UpdateCaptainSkinsRequest;
import com.etermax.spacehorse.core.player.model.Player;
import com.google.common.collect.Maps;

public class CaptainsIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(CaptainsIntegrationTest.class);

	private static final String URL_BUY_CAPTAIN = "/v1/captain/buyCaptain";
	private static final String URL_SELECT_CAPTAIN = "/v1/captain/selectCaptain";
	private static final String URL_BUY_CAPTAIN_SKIN = "/v1/captain/buyCaptainSkin";
	private static final String URL_UPDATE_CAPTAIN_SKINS = "/v1/captain/updateCaptainSkins";

	private static final String CAPTAIN_ID_REX = "captain-rex";
	private static final String CAPTAIN_ID_HELA = "captain-hela";

	private String sessionToken;
	private Player player;
	private Map<String, List<CaptainSlotRequest>> skinsByCaptains;

	@Before
	public void setUp() throws Exception {
		String responseAsJson = aNewEditorPlayerLogged();
		String playerId = extractPlayerIdFrom(responseAsJson);
		player = getPlayerRepository().find(playerId);
		sessionToken = extractSessionTokenFrom(responseAsJson);
		skinsByCaptains = Maps.newHashMap();
		skinsByCaptains.put(CAPTAIN_ID_REX, // estas keys salen del mockCatalog.json -> Skins defaults
				newArrayList( //
						new CaptainSlotRequest(1, CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST_ID), //
						new CaptainSlotRequest(0, CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS_ID), //
						new CaptainSlotRequest(2, CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD_ID)));
		skinsByCaptains.put(CAPTAIN_ID_HELA, //
				newArrayList( //
						new CaptainSlotRequest(1, CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST_ID), //
						new CaptainSlotRequest(0, CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS_ID), //
						new CaptainSlotRequest(2, CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD_ID)));
	}

	@Test
	public void givenANewPlayerWhenNoBuyAnyCaptainThenThePlayerShouldHaveOneOwnedCaptain() {
		// Given
		givenAPlayerInMapNumber2();
		// When - Then
		assertThat(getCaptainCollectionRepository().findOrDefaultBy(player).getCaptains()).hasSize(1);
		List<Captain> ownedCaptains = getCaptainCollectionRepository().findOrDefaultBy(player).getCaptains();
		assertThat(ownedCaptains.stream()
				.anyMatch(captain -> captain.getCaptainId().equals(CAPTAIN_ID_REX) || captain.getCaptainId().equals(CAPTAIN_ID_HELA))).isTrue();
	}

	@Test
	public void givenANewPlayerWhenBuyNewCaptainThenTheGemsShouldBeRemovedAndNewCaptainShouldBeAddedToCollection() {
		// Given
		givenAPlayerInMapNumber2WithManyGems();
		int playerGemsBeforeBuyCaptain = player.getInventory().getGems().getAmount();
		String captainId = givenACaptainIdToBuy();

		// when
		Response response = whenBuyCaptain(player.getUserId(), sessionToken, captainId);
		// then
		assertThatResponseWasOk(response);
		assertThatGemsWereDecreased(playerGemsBeforeBuyCaptain, captainId);
		assertThatANewCaptainWasAddedToCaptainsCollection(captainId);
		assertThatCaptainBoughtIsSelected(captainId);
	}

	@Test
	public void givenANewPlayerWhenSelectACaptainEISThenTheCaptainEISShouldBeSelected() {
		// Given
		givenAPlayerInMapNumber2();
		String captainId = givenACaptainIdToBuy();
		whenBuyCaptain(player.getUserId(), sessionToken, captainId);
		// when
		Response response = whenSelectCaptain(player.getUserId(), sessionToken, captainId);
		// then
		assertThatResponseWasOk(response);
		assertThat(getCaptainCollectionRepository().findOrDefaultBy(player).getSelectedCaptainId()).isEqualTo(captainId);
	}

	@Test
	public void givenOneRandomCaptainWithDefaultSkinsWhenBuyOtherNoDefaultSkinThenTheSkinShouldBeAddedOnCaptain() {
		// Given
		givenAPlayerInMapNumber2WithManyGems();
		String captainId = givenACaptainId();
		logger.info("-----> radonm captainId = " + captainId);
		CaptainSlotRequest slotsOfSkins = givenOtherNonDefaultSkinFor(captainId);
		int playerGemsBeforeBuyCaptain = player.getInventory().getGems().getAmount();
		// when
		Response response = whenBuyCaptainSkin(player.getUserId(), sessionToken, captainId, slotsOfSkins.getCaptainSkinId());
		// then
		assertThatResponseWasOk(response);
		List<Captain> captains = getCaptainCollectionRepository().findOrDefaultBy(player).getCaptains();
		assertThat(captains).hasSize(1);
		Captain captain = captains.get(0);
		assertThatOwnedSkinsWereUpdated(captainId, captain);
		assertThatGemsWereDecreased(playerGemsBeforeBuyCaptain, captainId);
	}

	@Test
	public void givenOneRandomCaptainWithDefaultSkinsWhenUpdateAllSkinsThenTheCaptainSkinsShouldBeUpdated() {
		// Given
		givenAPlayerInMapNumber2WithManyGems();
		String captainId = givenACaptainId();
		logger.info("-----> random captainId = " + captainId);
		List<CaptainSlotRequest> slotsOfSkins = givenCaptainSlotOfSkin(captainId);
		List<String> captainSkinIds = getCaptainSkinIdsFrom(slotsOfSkins);
		// when
		Response response = whenUpdateCaptainSkins(player.getUserId(), sessionToken, captainId, slotsOfSkins);
		// then
		assertThatResponseWasOk(response);
		Captain captain = getCaptainCollectionRepository().findOrDefaultBy(player).getCaptains().get(0);
		assertThatCaptainSlotsWereUpdated(captainSkinIds, captain);
		assertThat(captain.getOwnedSkins()).hasSize(captainSkinIds.size());
	}

	@Test
	public void givenOneRandomCaptainWithdefaultSkinsWhenUpdateSkinsWithInvalidSlotNumberThenTheResponseShouldBeFail() {
		// Given
		givenAPlayerInMapNumber2WithManyGems();
		String captainId = givenACaptainId();
		logger.info("-----> random captainId = " + captainId);
		List<CaptainSlotRequest> validSlotsOfSkins = givenCaptainSlotOfSkin(captainId);
		int unknownSlotNumber = 4;
		List<CaptainSlotRequest> invalidSlotsOfSkins = getInvalidSlotsOfSkins(unknownSlotNumber, validSlotsOfSkins);
		// when
		Response response = whenUpdateCaptainSkins(player.getUserId(), sessionToken, captainId, invalidSlotsOfSkins);
		// then
		assertThatResponseWasError(response);
	}

	private List<CaptainSlotRequest> getInvalidSlotsOfSkins(int unknownSlotNumber, List<CaptainSlotRequest> slotsOfSkins) {
		return newArrayList(new CaptainSlotRequest(unknownSlotNumber, slotsOfSkins.get(0).getCaptainSkinId()));
	}

	private List<String> getCaptainSkinIdsFrom(List<CaptainSlotRequest> captainSlotRequests) {
		return captainSlotRequests.stream().map(CaptainSlotRequest::getCaptainSkinId).collect(Collectors.toList());
	}

	private void givenAPlayerInMapNumber2() {
		player.setMapNumber(2);
		getPlayerRepository().update(player);
	}

	private void givenAPlayerInMapNumber2WithManyGems() {
		player.setMapNumber(2);
		player.getInventory().getGems().setAmount(1000);
		getPlayerRepository().update(player);
	}

	private String givenACaptainIdToBuy() {
		CaptainsCollection captainsCollection = getCaptainCollectionRepository().findOrDefaultBy(player);
		return captainsCollection.getSelectedCaptainId().equals(CAPTAIN_ID_HELA) ? CAPTAIN_ID_REX : CAPTAIN_ID_HELA;
	}

	private String givenACaptainId() {
		CaptainsCollection captainsCollection = getCaptainCollectionRepository().findOrDefaultBy(player);
		return captainsCollection.getSelectedCaptainId();
	}

	private List<CaptainSlotRequest> givenCaptainSlotOfSkin(String captainId) {
		return skinsByCaptains.get(captainId);
	}

	private CaptainSlotRequest givenOtherNonDefaultSkinFor(String captainId) {
		if (CAPTAIN_ID_REX.equals(captainId)) {
			return new CaptainSlotRequest(1, CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST2_ID);
		} else {
			return new CaptainSlotRequest(0, CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS2_ID);
		}
	}

	private Response whenBuyCaptain(String userId, String sessionToken, String captainId) {
		return client.target(getBuyCaptainEndpoint()).request().header(LOGIN_ID_HEADER, userId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(new BuyCaptainRequest(captainId), APPLICATION_JSON));
	}

	private Response whenSelectCaptain(String userId, String sessionToken, String captainId) {
		return client.target(getSelectCaptainEndpoint()).request().header(LOGIN_ID_HEADER, userId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(new SelectCaptainRequest(captainId), APPLICATION_JSON));
	}

	private Response whenBuyCaptainSkin(String userId, String sessionToken, String captainId, String captainSkinId) {
		return client.target(getBuyCaptainSkinEndpoint()).request().header(LOGIN_ID_HEADER, userId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(new BuyCaptainSkinRequest(captainId, captainSkinId), APPLICATION_JSON));
	}

	private Response whenUpdateCaptainSkins(String userId, String sessionToken, String captainId, List<CaptainSlotRequest> slotsOfSkins) {
		return client.target(getUpdateCaptainSkinEndpoint()).request().header(LOGIN_ID_HEADER, userId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(new UpdateCaptainSkinsRequest(captainId, slotsOfSkins), APPLICATION_JSON));
	}

	private String assertThatResponseWasOk(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		return responseAsJson;
	}

	private String assertThatResponseWasError(Response response) {
		assertThat(response.getStatus()).isNotEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		return responseAsJson;
	}

	private void assertThatANewCaptainWasAddedToCaptainsCollection(String captainId) {
		assertThat(getCaptainCollectionRepository().findOrDefaultBy(player).getCaptains().size()).isEqualTo(2);
		assertThat(getCaptainCollectionRepository().findOrDefaultBy(player).getCaptains()).extracting(Captain::getCaptainId).contains(captainId);
	}

	private void assertThatGemsWereDecreased(int playerGemsBeforeBuyCaptain, String captainId) {
		assertThat(getPlayerRepository().find(player.getUserId()).getInventory().getGems().getAmount()).isLessThan(playerGemsBeforeBuyCaptain);
		// the initial gems of player is 100
		if (captainId.equals(CAPTAIN_ID_REX)) {
			assertThat(getPlayerRepository().find(player.getUserId()).getInventory().getGems().getAmount()).isEqualTo(920);
		}
		if (captainId.equals(CAPTAIN_ID_HELA)) {
			assertThat(getPlayerRepository().find(player.getUserId()).getInventory().getGems().getAmount()).isEqualTo(940);
		}
	}

	private void assertThatCaptainBoughtIsSelected(String captainId) {
		assertThat(getCaptainCollectionRepository().findOrDefaultBy(player).getSelectedCaptainId()).isEqualTo(captainId);
	}

	private void assertThatOwnedSkinsWereUpdated(String expectedCaptainId, Captain captain) {
		if (CAPTAIN_ID_HELA.equals(expectedCaptainId)) {
			assertThat(captain.getOwnedSkins().size()).isEqualTo(4); //
			assertThat(captain.getOwnedSkins()).extracting(CaptainSkin::getCaptainSkinId)
					.contains(CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS_ID, CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST_ID, CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD_ID,
							CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS2_ID);
		} else {
			assertThat(captain.getOwnedSkins().size()).isEqualTo(4); //
			assertThat(captain.getOwnedSkins()).extracting(CaptainSkin::getCaptainSkinId)
					.contains(CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD_ID, CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS_ID, CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST_ID,
							CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST2_ID);
		}
	}

	private void assertThatCaptainSlotsWereUpdated(List<String> captainSkinIds, Captain captain) {
		captain.getCaptainSlots().forEach(captainSlot -> assertThat(captainSkinIds.contains(captainSlot.getCaptainSkinId())).isTrue());
	}

	private String getBuyCaptainEndpoint() {
		return getBaseUrl().concat(URL_BUY_CAPTAIN);
	}

	private String getSelectCaptainEndpoint() {
		return getBaseUrl().concat(URL_SELECT_CAPTAIN);
	}

	private String getBuyCaptainSkinEndpoint() {
		return getBaseUrl().concat(URL_BUY_CAPTAIN_SKIN);
	}

	private String getUpdateCaptainSkinEndpoint() {
		return getBaseUrl().concat(URL_UPDATE_CAPTAIN_SKINS);
	}
}
