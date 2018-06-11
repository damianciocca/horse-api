package com.etermax.spacehorse.core.captain;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.capitain.exceptions.CaptainAlreadyFoundException;
import com.etermax.spacehorse.core.capitain.exceptions.CaptainNotFoundException;
import com.etermax.spacehorse.core.capitain.exceptions.skins.CaptainSkinAlreadyFoundException;
import com.etermax.spacehorse.core.capitain.exceptions.skins.CaptainSkinNotBelongsToCaptainException;
import com.etermax.spacehorse.core.capitain.exceptions.skins.DuplicatedCaptainSlotException;
import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.google.common.collect.Maps;

public class CaptainsCollectionTest {

	private static final String CAPTAIN_ID_1 = "captain-id-1";
	private static final String CAPTAIN_ID_2 = "captain-id-2";
	private static final String CAPTAIN_SKIN_ID_1 = "captain-sking-id-1";
	private static final String CAPTAIN_SKIN_ID_2 = "captain-sking-id-2";
	private static final String CAPTAIN_SKIN_ID_3 = "captain-sking-id-3";
	private Player player;

	@Before
	public void setUp() throws Exception {
		player = new PlayerScenarioBuilder("10").withMapNumber(1).build();
	}

	/**
	 * addCaptain
	 */
	@Test
	public void givenAnEmptyCollectionWhenUnlockNewCaptainThenTheCollectionShouldHaveOneCaptain() throws Exception {
		// given
		List<Captain> emptyCaptains = newArrayList();
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), emptyCaptains, CAPTAIN_ID_1);
		// when
		captainsCollection.addCaptain(player, aCaptain(CAPTAIN_ID_1));
		// then
		assertThat(captainsCollection.getCaptains()).extracting(Captain::getCaptainId).containsExactly(CAPTAIN_ID_1);
	}

	@Test
	public void givenACollectionWithOneCaptainWhenUnlockAnotherCaptainThenTheCollectionShouldHaveTwoCaptains() throws Exception {
		// given
		List<Captain> captains = newArrayList(aCaptain(CAPTAIN_ID_2));
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), captains, CAPTAIN_ID_1);
		// when
		captainsCollection.addCaptain(player, aCaptain(CAPTAIN_ID_1));
		// then
		assertThat(captainsCollection.getCaptains()).extracting(Captain::getCaptainId).containsExactly(CAPTAIN_ID_2, CAPTAIN_ID_1);
	}

	@Test
	public void givenACollectionWithOneCaptainWhenAddTheSameCaptainAgainThenAnExceptionShouldBeThrown() throws Exception {
		// given
		List<Captain> emptyCaptains = newArrayList();
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), emptyCaptains, CAPTAIN_ID_1);
		captainsCollection.addCaptain(player, aCaptain(CAPTAIN_ID_1));
		// when - then
		assertThatThrownBy(() -> captainsCollection.addCaptain(player, aCaptain(CAPTAIN_ID_1))).isInstanceOf(CaptainAlreadyFoundException.class);

	}

	/**
	 * selectCaptain
	 */

	@Test
	public void givenAnEmptyCollectionWhenSelectNewCaptainThenAnExceptionShouldBeThrown() throws Exception {
		// given
		List<Captain> emptyCaptains = newArrayList();
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), emptyCaptains, CAPTAIN_ID_1);
		// when - then
		assertThatThrownBy(() -> captainsCollection.selectCaptain(aCaptain(CAPTAIN_ID_1).getCaptainId())).isInstanceOf(CaptainNotFoundException.class);
	}

	@Test
	public void givenACollectionWithOneCaptainWhenSelectTheCaptainThenTheCaptainShouldBeSelected() throws Exception {
		// given
		List<Captain> captains = newArrayList(aCaptain(CAPTAIN_ID_2));
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), captains, CAPTAIN_ID_1);
		// when
		captainsCollection.selectCaptain(aCaptain(CAPTAIN_ID_2).getCaptainId());
		// then
		assertThat(captainsCollection.getSelectedCaptainId()).isEqualTo(CAPTAIN_ID_2);
	}

	@Test
	public void givenACollectionWithTwoCaptainsWhenSelectOneOfThemThenTheCaptainShouldBeSelected() throws Exception {
		// given
		List<Captain> captains = newArrayList(aCaptain(CAPTAIN_ID_1), aCaptain(CAPTAIN_ID_2));
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), captains, CAPTAIN_ID_2);
		captainsCollection.selectCaptain(aCaptain(CAPTAIN_ID_2).getCaptainId());
		// when
		captainsCollection.selectCaptain(aCaptain(CAPTAIN_ID_1).getCaptainId());
		// then
		assertThat(captainsCollection.getSelectedCaptainId()).isEqualTo(CAPTAIN_ID_1);
	}

	@Test
	public void givenACollectionWithOneCaptainWhenSelectAnotherCaptainThenAnExceptionShouldBeThrown() throws Exception {
		// given
		List<Captain> captains = newArrayList(aCaptain(CAPTAIN_ID_2));
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), captains, CAPTAIN_ID_2);
		// when - then
		assertThatThrownBy(() -> captainsCollection.selectCaptain(aCaptain(CAPTAIN_ID_1).getCaptainId())).isInstanceOf(CaptainNotFoundException.class);
	}

	/**
	 * addCaptainSkin
	 */

	@Test
	public void givenOneCaptainWhenAddOneSkinToCaptainThenTheSkinShouldBeAddedAsOwnedSkin() throws Exception {
		// given
		List<Captain> captains = newArrayList(aCaptain(CAPTAIN_ID_2));
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), captains, CAPTAIN_ID_2);
		CaptainSkin captainSkin = aCaptainSkin(CAPTAIN_SKIN_ID_1, CAPTAIN_ID_2, 1);
		// when
		Captain captain = captainsCollection.addCaptainSkin(player, CAPTAIN_ID_2, captainSkin);
		// then
		assertThat(captain.getCaptainSlots()).isEmpty();
		assertThat(captain.getOwnedSkins()).extracting(CaptainSkin::getCaptainSkinId).contains(CAPTAIN_SKIN_ID_1);
	}

	@Test
	public void givenOneCaptainWhenAddOneSkinToAnotherCaptainThenAnExceptionShouldBeThrown() throws Exception {
		// given
		List<Captain> captains = newArrayList(aCaptain(CAPTAIN_ID_2));
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), captains, CAPTAIN_ID_2);
		CaptainSkin captainSkin = aCaptainSkin(CAPTAIN_SKIN_ID_1, CAPTAIN_ID_2, 1);
		// when - then
		assertThatThrownBy(() -> captainsCollection.addCaptainSkin(player, CAPTAIN_ID_1, captainSkin)).isInstanceOf(CaptainNotFoundException.class);
	}

	@Test
	public void givenOneCaptainWithOneSkinWhenAddTheSameSkinToCaptainThenAnExceptionShouldBeThrown() throws Exception {
		// given
		List<Captain> captains = newArrayList(aCaptain(CAPTAIN_ID_2));
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), captains, CAPTAIN_ID_2);
		CaptainSkin captainSkin = aCaptainSkin(CAPTAIN_SKIN_ID_1, CAPTAIN_ID_2, 1);
		captainsCollection.addCaptainSkin(player, CAPTAIN_ID_2, captainSkin);
		// when - then
		assertThatThrownBy(() -> captainsCollection.addCaptainSkin(player, CAPTAIN_ID_2, captainSkin))
				.isInstanceOf(CaptainSkinAlreadyFoundException.class);
	}

	@Test
	public void givenOneCaptainWhenAddOneSkinBelongsToAnotherCaptainThenAnExceptionShouldBeThrown() throws Exception {
		// given
		List<Captain> captains = newArrayList(aCaptain(CAPTAIN_ID_1), aCaptain(CAPTAIN_ID_2));
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), captains, CAPTAIN_ID_2);
		CaptainSkin captainSkin = aCaptainSkin(CAPTAIN_SKIN_ID_1, CAPTAIN_ID_1, 1);
		// when - then
		assertThatThrownBy(() -> captainsCollection.addCaptainSkin(player, CAPTAIN_ID_2, captainSkin))
				.isInstanceOf(CaptainSkinNotBelongsToCaptainException.class);
	}

	/**
	 * updateCaptainSkin
	 */

	@Test
	public void givenACaptainWithThreeSkinsWhenSelectTwoThenTheCaptainsWasUpdatedWithTwoSkins() throws Exception {
		// given
		List<Captain> captains = newArrayList(aCaptain(CAPTAIN_ID_1), aCaptain(CAPTAIN_ID_2));
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), captains, CAPTAIN_ID_2);

		CaptainSkin captainSkin1 = aCaptainSkin(CAPTAIN_SKIN_ID_1, CAPTAIN_ID_2, 1);
		CaptainSkin captainSkin2 = aCaptainSkin(CAPTAIN_SKIN_ID_2, CAPTAIN_ID_2, 2);
		CaptainSkin captainSkin3 = aCaptainSkin(CAPTAIN_SKIN_ID_3, CAPTAIN_ID_2, 0);

		captainsCollection.addCaptainSkin(player, CAPTAIN_ID_2, captainSkin1);
		captainsCollection.addCaptainSkin(player, CAPTAIN_ID_2, captainSkin2);
		captainsCollection.addCaptainSkin(player, CAPTAIN_ID_2, captainSkin3);
		// when
		Captain captain = captainsCollection.updateCaptainSkins(CAPTAIN_ID_2, newArrayList(captainSkin1, captainSkin2));
		// then
		assertThatCaptainWasUpdatedWithTwoSelectedSkins(captainSkin1, captainSkin2, captain);
	}

	@Test
	public void whenTriesToUpdateSkinsWithDuplicatedSlotsThenAnExceptionShouldBethrown() throws Exception {
		// given
		List<Captain> captains = newArrayList(aCaptain(CAPTAIN_ID_1), aCaptain(CAPTAIN_ID_2));
		CaptainsCollection captainsCollection = new CaptainsCollection(player.getUserId(), captains, CAPTAIN_ID_2);

		int sameSlotNumber = 1;
		CaptainSkin captainSkin1 = aCaptainSkin(CAPTAIN_SKIN_ID_1, CAPTAIN_ID_2, sameSlotNumber);
		CaptainSkin captainSkin2 = aCaptainSkin(CAPTAIN_SKIN_ID_2, CAPTAIN_ID_2, sameSlotNumber);

		captainsCollection.addCaptainSkin(player, CAPTAIN_ID_2, captainSkin1);
		captainsCollection.addCaptainSkin(player, CAPTAIN_ID_2, captainSkin2);
		// when - then
		assertThatThrownBy(() -> captainsCollection.updateCaptainSkins(CAPTAIN_ID_2, newArrayList(captainSkin1, captainSkin2)))
				.isInstanceOf(DuplicatedCaptainSlotException.class);
		;
	}

	private void assertThatCaptainWasUpdatedWithTwoSelectedSkins(CaptainSkin captainSkin1, CaptainSkin captainSkin2, Captain captain) {
		assertThat(captain.getCaptainSlots()).hasSize(2);
		assertThat(captain.getCaptainSlotBy(1).getCaptainSkinId()).isEqualTo(CAPTAIN_SKIN_ID_1);
		assertThat(captain.getCaptainSlotBy(1).getCaptainSkin()).isEqualTo(captainSkin1);
		assertThat(captain.getCaptainSlotBy(2).getCaptainSkinId()).isEqualTo(CAPTAIN_SKIN_ID_2);
		assertThat(captain.getCaptainSlotBy(2).getCaptainSkin()).isEqualTo(captainSkin2);

		assertThat(captain.getOwnedSkins()).extracting(CaptainSkin::getCaptainSkinId)
				.contains(CAPTAIN_SKIN_ID_1, CAPTAIN_SKIN_ID_2, CAPTAIN_SKIN_ID_3);
	}

	private CaptainSkin aCaptainSkin(String captainSkinId, String captainId, int slotNumber) {
		return new CaptainSkinScenarioBuilder(captainId).withSkin(captainSkinId, slotNumber).build().get(0);
	}

	private Captain aCaptain(String captainId) {
		return new Captain(captainId, new CaptainDefinition(captainId, 1, 10, 0), Maps.newHashMap(), Maps.newHashMap());
	}

}
