package com.etermax.spacehorse.core.captain;

import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_JADE_HEAD_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_JADE_CHEST_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_JADE_ARMS_ID;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.CaptainFactory;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;
import com.etermax.spacehorse.mock.MockUtils;

public class CaptainFactoryTest {

	private final static String CAPTAIN_REX = "captain-rex";
	private final static String CAPTAIN_HELA = "captain-hela";
	private final static String CAPTAIN_JADE = "captain_jade";
	private Catalog catalog;
	private CaptainDefinition captainDefinitionForRex;
	private List<CaptainSkinDefinition> captainSkinDefinitions;
	private CaptainDefinition captainDefinitionForHela;
	private boolean defaultSkinForCaptainIsAsserted = false;
	private boolean alternativeSkinForCaptainIsAsserted = false;
	private CaptainDefinition captainDefinitionJade;

	@Before
	public void setUp() throws Exception {
		catalog = MockUtils.mockCatalog();
		captainDefinitionForRex = catalog.getCaptainDefinitionsCollection().findByIdOrFail(CAPTAIN_REX);
		captainDefinitionForHela = catalog.getCaptainDefinitionsCollection().findByIdOrFail(CAPTAIN_HELA);
		captainDefinitionJade = catalog.getCaptainDefinitionsCollection().findByIdOrFail(CAPTAIN_JADE);
		captainSkinDefinitions = catalog.getCaptainSkinDefinitionsCollection().getEntries();
	}

	@Test
	public void whenCreateCaptainRexThenTheSkinsAndSlotsShouldBeCreatedByDefaultUsingDefaultSkins() {
		// given
		CaptainFactory captainFactory = new CaptainFactory();
		// when
		Captain captain = captainFactory.createWithDefaultSkin(CAPTAIN_REX, captainDefinitionForRex, captainSkinDefinitions);

		// then
		assertThatCaptainRexIsCreatedWithDefaultSkinsAndSlots(captain);

	}

	@Test
	public void whenCreateCaptainHelaThenTheSkinsAndSlotsShouldBeCreatedByDefaultUsingDefaultSkins() {
		// given
		CaptainFactory captainFactory = new CaptainFactory();
		// when
		Captain captain = captainFactory.createWithDefaultSkin(CAPTAIN_HELA, captainDefinitionForHela, captainSkinDefinitions);
		// then
		assertThatCaptainHelaIsCreatedWithDefaultSkinsAndSlots(captain);
	}

	@Test
	public void createACaptainRexWithRandomSkin() {
		// given
		CaptainFactory captainFactory = new CaptainFactory();
		Consumer<Captain> consumer = captain -> assertThatCaptainHasCorrectRandomSkin(captain, "captain_rex_rex_head_2",
				expectedDefaultSkinsForRex(), expectedAlternativeSkinsForRex());

		whenCreateRandomSkinForCaptainOneHundredTimes(captainFactory, CAPTAIN_REX, captainDefinitionForRex, consumer);

		thenDefaultAndAlternativeSkinWereReturned();
	}

	@Test
	public void createACaptainHelaWithRandomSkin() {
		// given
		CaptainFactory captainFactory = new CaptainFactory();
		Consumer<Captain> consumer = captain -> assertThatCaptainHasCorrectRandomSkin(captain, "captain_hela_hela_head_2",
				expectedDefaultSkinsForHela(), expectedAlternativeSkinsForHela());

		whenCreateRandomSkinForCaptainOneHundredTimes(captainFactory, CAPTAIN_HELA, captainDefinitionForHela, consumer);

		thenDefaultAndAlternativeSkinWereReturned();
	}

	@Test
	public void createACaptainJadeWithRandomSkinAlwaysReturnDefaultSkin() {
		// given
		CaptainFactory captainFactory = new CaptainFactory();

		for(int i= 0; i<= 100 ; i++) {
			//where
			Captain captain = captainFactory.createWithRandomSkin(CAPTAIN_JADE, captainDefinitionJade, captainSkinDefinitions);
			//then
			assertThatCaptainJadeIsCreatedWithDefaultSkinsAndSlots(captain);
		}
	}

	private void thenDefaultAndAlternativeSkinWereReturned() {
		assertThat(alternativeSkinForCaptainIsAsserted).isTrue();
		assertThat(defaultSkinForCaptainIsAsserted).isTrue();
	}

	private void whenCreateRandomSkinForCaptainOneHundredTimes(CaptainFactory captainFactory, String captainId, CaptainDefinition captainDefinition,
			Consumer<Captain> assertCaptainSkinConsumer) {
		for (int i = 0; i < 100; i++) {
			Captain captain = captainFactory.createWithRandomSkin(captainId, captainDefinition, captainSkinDefinitions);

			assertCaptainSkinConsumer.accept(captain);
		}
	}

	private void assertThatCaptainHasCorrectRandomSkin(Captain captain, String skinId, String[] expectedDefaultSkin,
			String[] expectedAlternativeSkin) {
		assertThat(captain.getOwnedSkins()).hasSize(3);

		if (captain.getOwnedSkins().stream().anyMatch(captainSkin -> captainSkin.getCaptainSkinId().equals(skinId))) {
			assertThat(captain.getOwnedSkins()) //
					.extracting(CaptainSkin::getCaptainSkinId)//
					.containsOnly(expectedDefaultSkin);
			defaultSkinForCaptainIsAsserted = true;
		} else {
			assertThat(captain.getOwnedSkins()) //
					.extracting(CaptainSkin::getCaptainSkinId)//
					.containsOnly(expectedAlternativeSkin);
			alternativeSkinForCaptainIsAsserted = true;
		}

		assertThat(captain.getOwnedSkins()) //
				.extracting(CaptainSkin::getSlotNumber) //
				.containsOnly(0, 1, 2);
	}

	private void assertThatCaptainJadeIsCreatedWithDefaultSkinsAndSlots(Captain captain) {
		assertThat(captain.getOwnedSkins()).hasSize(3);

		assertThat(captain.getOwnedSkins()) //
				.extracting(CaptainSkin::getCaptainSkinId)//
				.containsOnly(expectedDefaultSkinsForJade());

		assertThat(captain.getOwnedSkins()) //
				.extracting(CaptainSkin::getSlotNumber) //
				.containsOnly(0, 1, 2);
	}

	private void assertThatCaptainRexIsCreatedWithDefaultSkinsAndSlots(Captain captain) {
		assertThat(captain.getOwnedSkins()).hasSize(3);

		assertThat(captain.getOwnedSkins()) //
				.extracting(CaptainSkin::getCaptainSkinId)//
				.containsOnly(expectedDefaultSkinsForRex());

		assertThat(captain.getOwnedSkins()) //
				.extracting(CaptainSkin::getSlotNumber) //
				.containsOnly(0, 1, 2);
	}

	private void assertThatCaptainHelaIsCreatedWithDefaultSkinsAndSlots(Captain captain) {
		assertThat(captain.getOwnedSkins()).hasSize(3);

		assertThat(captain.getOwnedSkins()) //
				.extracting(CaptainSkin::getCaptainSkinId)//
				.containsOnly(expectedDefaultSkinsForHela());

		assertThat(captain.getOwnedSkins()) //
				.extracting(CaptainSkin::getSlotNumber) //
				.containsOnly(0, 1, 2);
	}

	private String[] expectedDefaultSkinsForRex() {
		return new String[] { CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS_ID, //
				CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST_ID, //
				CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD_ID };
	}

	private String[] expectedDefaultSkinsForJade() {
		return new String[] { CAPTAIN_SKIN_DEFAULT_FOR_JADE_ARMS_ID, //
				CAPTAIN_SKIN_DEFAULT_FOR_JADE_CHEST_ID, //
				CAPTAIN_SKIN_DEFAULT_FOR_JADE_HEAD_ID };
	}

	private String[] expectedAlternativeSkinsForRex() {
		return new String[] { CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS2_ID, //
				CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST2_ID, //
				CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD2_ID };
	}

	private String[] expectedDefaultSkinsForHela() {
		return new String[] { CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS_ID, //
				CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST_ID, //
				CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD_ID };
	}

	private String[] expectedAlternativeSkinsForHela() {
		return new String[] { CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS2_ID, //
				CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST2_ID, //
				CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD2_ID };
	}
}