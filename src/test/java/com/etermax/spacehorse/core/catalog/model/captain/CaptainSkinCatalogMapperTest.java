package com.etermax.spacehorse.core.catalog.model.captain;

import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD_ID;

import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD2_ID;

import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_JADE_HEAD_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_JADE_CHEST_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_JADE_ARMS_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_JADE_HEAD2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_JADE_CHEST2_ID;
import static com.etermax.spacehorse.mock.CaptainSkinScenarioBuilder.CAPTAIN_SKIN_DEFAULT_FOR_JADE_ARMS2_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CaptainSkinDefinitionResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.mock.MockUtils;

public class CaptainSkinCatalogMapperTest {

	private static final String CAPTAIN_REX = "captain-rex";
	private static final String CAPTAIN_HELA = "captain-hela";
	private static final String CAPTAIN_JADE = "captain_jade";
	private Catalog catalog;
	private CatalogResponse catalogResponse;

	@Before
	public void setUp() throws Exception {
		catalog = MockUtils.mockCatalog();
		catalogResponse = new CatalogResponse(catalog);
	}

	@Test
	public void whenMapFromCatalogThenTheCaptainSkinDefinitionResponseShouldBeCreated() {
		// given
		CaptainSkinCatalogMapper mapper = new CaptainSkinCatalogMapper();
		// when
		List<CaptainSkinDefinitionResponse> skinDefinitionResponses = mapper.mapFrom(catalog).getEntries();
		// then
		assertThatResponseShouldBeCreatedSuccessfully(skinDefinitionResponses);
	}

	@Test
	public void whenMapFromCatalogResponseThenCaptainSkinDefinitionsShouldBeCreated() {
		// given
		CaptainSkinCatalogMapper mapper = new CaptainSkinCatalogMapper();
		// when
		List<CaptainSkinDefinition> captainSkinDefinitions = mapper.mapFrom(catalogResponse);
		// then
		assertThatDefinitionShouldBeCreatedSuccessfully(captainSkinDefinitions);
	}

	private void assertThatDefinitionShouldBeCreatedSuccessfully(List<CaptainSkinDefinition> captainSkinDefinitions) {
		assertThat(captainSkinDefinitions).hasSize(18);
		assertThat(captainSkinDefinitions) //
				.extracting(//
						CaptainSkinDefinition::getId, //
						CaptainSkinDefinition::getCaptainId, //
						CaptainSkinDefinition::getSlotNumber, //
						CaptainSkinDefinition::isDefault) //
				.containsOnly( //
						// nivel 0
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS_ID, CAPTAIN_REX, 0, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST_ID, CAPTAIN_REX, 1, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD_ID, CAPTAIN_REX, 2, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS2_ID, CAPTAIN_REX, 0, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST2_ID, CAPTAIN_REX, 1, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD2_ID, CAPTAIN_REX, 2, false), //

						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS_ID, CAPTAIN_HELA, 0, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST_ID, CAPTAIN_HELA, 1, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD_ID, CAPTAIN_HELA, 2, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS2_ID, CAPTAIN_HELA, 0, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST2_ID, CAPTAIN_HELA, 1, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD2_ID, CAPTAIN_HELA, 2, false), //

						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_ARMS_ID, CAPTAIN_JADE, 0, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_CHEST_ID, CAPTAIN_JADE, 1, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_HEAD_ID, CAPTAIN_JADE, 2, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_ARMS2_ID, CAPTAIN_JADE, 0, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_CHEST2_ID, CAPTAIN_JADE, 1, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_HEAD2_ID, CAPTAIN_JADE, 2, false) //
				);
	}

	private void assertThatResponseShouldBeCreatedSuccessfully(List<CaptainSkinDefinitionResponse> skinDefinitionResponses) {
		assertThat(skinDefinitionResponses).hasSize(18);
		assertThat(skinDefinitionResponses) //
				.extracting(//
						CaptainSkinDefinitionResponse::getId, //
						CaptainSkinDefinitionResponse::getCaptainId, //
						CaptainSkinDefinitionResponse::getSlotNumber, //
						CaptainSkinDefinitionResponse::getIsDefault) //
				.containsOnly( //
						// nivel 0
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS_ID, CAPTAIN_REX, 0, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST_ID, CAPTAIN_REX, 1, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD_ID, CAPTAIN_REX, 2, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS2_ID, CAPTAIN_REX, 0, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST2_ID, CAPTAIN_REX, 1, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD2_ID, CAPTAIN_REX, 2, false), //

						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS_ID, CAPTAIN_HELA, 0, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST_ID, CAPTAIN_HELA, 1, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD_ID, CAPTAIN_HELA, 2, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS2_ID, CAPTAIN_HELA, 0, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST2_ID, CAPTAIN_HELA, 1, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD2_ID, CAPTAIN_HELA, 2, false), //

						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_ARMS_ID, CAPTAIN_JADE, 0, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_CHEST_ID, CAPTAIN_JADE, 1, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_HEAD_ID, CAPTAIN_JADE, 2, true), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_ARMS2_ID, CAPTAIN_JADE, 0, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_CHEST2_ID, CAPTAIN_JADE, 1, false), //
						tuple(CAPTAIN_SKIN_DEFAULT_FOR_JADE_HEAD2_ID, CAPTAIN_JADE, 2, false) //
				);
	}

}
