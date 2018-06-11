package com.etermax.spacehorse.core.captain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.capitain.exceptions.skins.CaptainSkinNotBelongsToCaptainSlotException;
import com.etermax.spacehorse.core.capitain.model.CaptainSlotsValidator;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder;
import com.google.common.collect.Maps;

public class CaptainSlotsValidatorTest {

	private static final int SLOT_NUMBER_0 = 0;
	private static final int SLOT_NUMBER_1 = 1;
	private static final int SLOT_NUMBER_2 = 2;
	private static final String SKIN_ID_0 = "skin-id-0";
	private static final String SKIN_ID_1 = "skin-id-1";
	private static final String SKIN_ID_2 = "skin-id-2";
	private static final String CAPTAIN_ID = "captain-id";
	private Map<Integer, String> skinIdsBySlots;

	@Before
	public void setUp() throws Exception {
		skinIdsBySlots = Maps.newHashMap();
		skinIdsBySlots.put(0, SKIN_ID_0);
		skinIdsBySlots.put(1, SKIN_ID_1);
		skinIdsBySlots.put(2, SKIN_ID_2);
	}

	@Test
	public void givenSkinsWithAppropriateSlotsThenTheValidatorShouldBePassedOk() throws Exception {
		// given
		CaptainSlotsValidator captainSlotsValidator = new CaptainSlotsValidator();
		List<CaptainSkin> captainSkins = new CaptainSkinScenarioBuilder(CAPTAIN_ID)//
				.withSkin(SKIN_ID_0, SLOT_NUMBER_0) //
				.withSkin(SKIN_ID_1, SLOT_NUMBER_1)//
				.withSkin(SKIN_ID_2, SLOT_NUMBER_2)//
				.build();
		// when - then
		captainSlotsValidator.validateSlotOfSkins(CAPTAIN_ID, skinIdsBySlots, captainSkins);
	}

	@Test
	public void givenSkinsInDifferentSlotsThenTheValidatorShouldBeThrowException() throws Exception {
		// given
		CaptainSlotsValidator captainSlotsValidator = new CaptainSlotsValidator();
		List<CaptainSkin> captainSkins = new CaptainSkinScenarioBuilder(CAPTAIN_ID)//
				.withSkin(SKIN_ID_0, SLOT_NUMBER_1) //
				.withSkin(SKIN_ID_1, SLOT_NUMBER_2)//
				.withSkin(SKIN_ID_2, SLOT_NUMBER_0)//
				.build();
		// when - then
		assertThatThrownBy(() -> captainSlotsValidator.validateSlotOfSkins(CAPTAIN_ID, skinIdsBySlots, captainSkins))
				.isInstanceOf(CaptainSkinNotBelongsToCaptainSlotException.class);
	}

	@Test
	public void givenSkinsOnUnknownSlotsThenTheValidatorShouldBeThrowException() throws Exception {
		// given
		skinIdsBySlots = Maps.newHashMap();
		skinIdsBySlots.put(0, SKIN_ID_1);
		CaptainSlotsValidator captainSlotsValidator = new CaptainSlotsValidator();
		List<CaptainSkin> captainSkins = new CaptainSkinScenarioBuilder(CAPTAIN_ID)//
				.withSkin(SKIN_ID_0, SLOT_NUMBER_1) //
				.withSkin(SKIN_ID_1, SLOT_NUMBER_2)//
				.withSkin(SKIN_ID_2, SLOT_NUMBER_0)//
				.build();
		// when - then
		assertThatThrownBy(() -> captainSlotsValidator.validateSlotOfSkins(CAPTAIN_ID, skinIdsBySlots, captainSkins))
				.isInstanceOf(CaptainSkinNotBelongsToCaptainSlotException.class);
	}
}

