package com.etermax.spacehorse.core.catalog.model.card;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.specialoffer.CardCatalogMapper;
import com.etermax.spacehorse.core.catalog.resource.response.CardDefinitionResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;
import com.etermax.spacehorse.mock.MockUtils;

public class CardCatalogMapperTest {

	private Catalog catalog;
	private CardCatalogMapper cardCatalogMapper;
	private EntryContainerResponse<CardDefinitionResponse> cardDefinitionResponseEntryContainerResponse;
	private CatalogResponse catalogResponse;

	@Before
	public void setUp() throws Exception {
		catalog = MockUtils.mockCatalog();
		catalogResponse = new CatalogResponse(catalog);
	}

	@Test
	public void whenMapFromCatalogThenCardDefinitionResponsesShouldBeCreated() {
		givenACardCatalogMapper();

		whenMapCatalog();

		thenCardDefinitionResponsesAreExpectedWithCorrectFields(cardDefinitionResponseEntryContainerResponse);
	}

	@Test
	public void whenMapFromCatalogResponseThenTheCardDefinitionsShouldBeCreated() {
		givenACardCatalogMapper();

		List<CardDefinition> cardDefinitions = cardCatalogMapper.mapFrom(catalogResponse);

		thenCardDefinitionsAreExpectedWithCorrectFields(cardDefinitions);
	}

	private void thenCardDefinitionsAreExpectedWithCorrectFields(List<CardDefinition> cardDefinitions) {
		assertThat(cardDefinitions).hasSize(34);
		Assertions.assertThat(cardDefinitions) //
				.extracting(//
						CardDefinition::getId, //
						CardDefinition::getAvailableFromMapId, //
						CardDefinition::getCardAction, //
						CardDefinition::getCardRarity, //
						CardDefinition::getCardTarget, //
						CardDefinition::getEnabled, //
						CardDefinition::getEnergyCost, //
						CardDefinition::getPowerUpId, //
						CardDefinition::getTargetTeam,//
						CardDefinition::getActivationTime) //
				.containsOnly( //
						tuple("card_aqua_burst", 0, 2, CardRarity.COMMON, 1, true, 4, "aqua_burst", 1, Optional.empty()), //
						tuple("card_electric_duo", 0, 1, CardRarity.COMMON, 2, true, 3, "", 0, Optional.empty()), //
						tuple("card_enforcer", 0, 1, CardRarity.COMMON, 2, true, 3, "", 0, Optional.empty()), //
						tuple("card_flying_headstrongs", 0, 1, CardRarity.COMMON, 2, true, 3, "", 0, Optional.empty()), //
						tuple("card_headstrongs", 0, 1, CardRarity.COMMON, 2, true, 2, "", 0, Optional.empty()), //
						tuple("card_laser", 0, 2, CardRarity.COMMON, 1, true, 3, "laser", 1, Optional.empty()), //
						tuple("card_spitter", 0, 1, CardRarity.COMMON, 2, true, 3, "", 0, Optional.empty()), //
						tuple("card_hell_bite", 0, 1, CardRarity.RARE, 2, true, 4, "", 0, Optional.empty()), //
						tuple("card_mastodon", 0, 1, CardRarity.RARE, 2, true, 5, "", 0, Optional.empty()), //
						tuple("card_punk_shooter", 0, 1, CardRarity.RARE, 2, true, 4, "", 0, Optional.empty()), //
						tuple("card_biter_horde", 0, 1, CardRarity.EPIC, 2, true, 3, "", 0, Optional.empty()), //
						tuple("card_buzzer", 0, 1, CardRarity.EPIC, 2, true, 4, "", 0, Optional.empty()), //
						tuple("card_falling_chasers", 0, 2, CardRarity.EPIC, 1, true, 6, "falling_chasers", 1, Optional.empty()), //
						tuple("card_firebellies", 1, 1, CardRarity.COMMON, 2, true, 2, "", 0, Optional.empty()), //
						tuple("card_glowflies", 1, 1, CardRarity.COMMON, 2, true, 2, "", 0, Optional.empty()), //
						tuple("card_ice_splash", 1, 2, CardRarity.COMMON, 1, true, 2, "ice_splash", 1, Optional.empty()), //
						tuple("card_arachnostorm", 1, 1, CardRarity.RARE, 2, true, 4, "", 0, Optional.empty()), //
						tuple("card_chaser", 1, 2, CardRarity.EPIC, 4, true, 6, "chaser", 0, Optional.empty()), //
						tuple("card_lavafists", 2, 1, CardRarity.COMMON, 2, true, 6, "", 0, Optional.empty()), //
						tuple("card_biters", 2, 1, CardRarity.COMMON, 2, true, 1, "", 0, Optional.empty()), //
						tuple("card_flying_lavafists", 2, 1, CardRarity.RARE, 2, true, 3, "", 0, Optional.empty()), //
						tuple("card_boomerang", 2, 2, CardRarity.EPIC, 1, true, 2, "boomerang", 1, Optional.empty()), //
						tuple("card_oil_tank", 3, 1, CardRarity.RARE, 2, true, 2, "", 0, Optional.empty()), //
						tuple("card_klaw", 3, 1, CardRarity.EPIC, 2, true, 7, "", 0, Optional.empty()), //
						tuple("card_big_bertha", -1, 1, CardRarity.COMMON, 5, false, 3, "", 0, Optional.empty()), //
						tuple("card_bomber", -1, 1, CardRarity.COMMON, 2, false, 3, "", 0, Optional.empty()), //
						tuple("card_shield", -1, 2, CardRarity.COMMON, 3, false, 3, "shield", 0, Optional.empty()), //
						tuple("card_wall", -1, 1, CardRarity.RARE, 5, false, 2, "", 0, Optional.empty()), //
						tuple("card_shipyard", -1, 1, CardRarity.RARE, 5, false, 5, "", 0, Optional.empty()), //
						tuple("card_decoy", -1, 2, CardRarity.EPIC, 4, false, 5, "decoy", 0, Optional.empty()), //
						tuple("card_freeze", -1, 2, CardRarity.EPIC, 1, false, 4, "freeze", 1, Optional.empty()), //
						tuple("card_gatling", -1, 1, CardRarity.EPIC, 5, false, 6, "", 0, Optional.empty()), //
						tuple("card_sandstorm", -1, 2, CardRarity.EPIC, 1, false, 3, "sandstorm", 1, Optional.empty()), //
						tuple("card_as_booster", -1, 2, CardRarity.EPIC, 1, false, 2, "haste", 0, Optional.empty()) //

				);
	}

	private void whenMapCatalog() {
		cardDefinitionResponseEntryContainerResponse = cardCatalogMapper.mapFrom(catalog);
	}

	private void givenACardCatalogMapper() {
		cardCatalogMapper = new CardCatalogMapper();
	}

	private void thenCardDefinitionResponsesAreExpectedWithCorrectFields(EntryContainerResponse<CardDefinitionResponse> cardDefinitionResponseEntryContainerResponse) {
		List<CardDefinitionResponse> definitionResponses = cardDefinitionResponseEntryContainerResponse.getEntries();
		assertThat(definitionResponses).hasSize(34);
		Assertions.assertThat(definitionResponses) //
				.extracting(//
						CardDefinitionResponse::getId, //
						CardDefinitionResponse::getAvailableFromMapId, //
						CardDefinitionResponse::getCardAction, //
						CardDefinitionResponse::getCardRarity, //
						CardDefinitionResponse::getCardTarget, //
						CardDefinitionResponse::getEnabled, //
						CardDefinitionResponse::getEnergyCost, //
						CardDefinitionResponse::getPowerUpId, //
						CardDefinitionResponse::getTargetTeam,//
						CardDefinitionResponse::getActivationTime) //
				.containsOnly( //
						tuple("card_aqua_burst", 0, 2, 0, 1, true, 4, "aqua_burst", 1, null), //
						tuple("card_electric_duo", 0, 1, 0, 2, true, 3, "", 0, null), //
						tuple("card_enforcer", 0, 1, 0, 2, true, 3, "", 0, null), //
						tuple("card_flying_headstrongs", 0, 1, 0, 2, true, 3, "", 0, null), //
						tuple("card_headstrongs", 0, 1, 0, 2, true, 2, "", 0, null), //
						tuple("card_laser", 0, 2, 0, 1, true, 3, "laser", 1, null), //
						tuple("card_spitter", 0, 1, 0, 2, true, 3, "", 0, null), //
						tuple("card_hell_bite", 0, 1, 1, 2, true, 4, "", 0, null), //
						tuple("card_mastodon", 0, 1, 1, 2, true, 5, "", 0, null), //
						tuple("card_punk_shooter", 0, 1, 1, 2, true, 4, "", 0, null), //
						tuple("card_biter_horde", 0, 1, 2, 2, true, 3, "", 0, null), //
						tuple("card_buzzer", 0, 1, 2, 2, true, 4, "", 0, null), //
						tuple("card_falling_chasers", 0, 2, 2, 1, true, 6, "falling_chasers", 1, null), //
						tuple("card_firebellies", 1, 1, 0, 2, true, 2, "", 0, null), //
						tuple("card_glowflies", 1, 1, 0, 2, true, 2, "", 0, null), //
						tuple("card_ice_splash", 1, 2, 0, 1, true, 2, "ice_splash", 1, null), //
						tuple("card_arachnostorm", 1, 1, 1, 2, true, 4, "", 0, null), //
						tuple("card_chaser", 1, 2, 2, 4, true, 6, "chaser", 0, null), //
						tuple("card_lavafists", 2, 1, 0, 2, true, 6, "", 0, null), //
						tuple("card_biters", 2, 1, 0, 2, true, 1, "", 0, null), //
						tuple("card_flying_lavafists", 2, 1, 1, 2, true, 3, "", 0, null), //
						tuple("card_boomerang", 2, 2, 2, 1, true, 2, "boomerang", 1, null), //
						tuple("card_oil_tank", 3, 1, 1, 2, true, 2, "", 0, null), //
						tuple("card_klaw", 3, 1, 2, 2, true, 7, "", 0, null), //
						tuple("card_big_bertha", -1, 1, 0, 5, false, 3, "", 0, null), //
						tuple("card_bomber", -1, 1, 0, 2, false, 3, "", 0, null), //
						tuple("card_shield", -1, 2, 0, 3, false, 3, "shield", 0, null), //
						tuple("card_wall", -1, 1, 1, 5, false, 2, "", 0, null), //
						tuple("card_shipyard", -1, 1, 1, 5, false, 5, "", 0, null), //
						tuple("card_decoy", -1, 2, 2, 4, false, 5, "decoy", 0, null), //
						tuple("card_freeze", -1, 2, 2, 1, false, 4, "freeze", 1, null), //
						tuple("card_gatling", -1, 1, 2, 5, false, 6, "", 0, null), //
						tuple("card_sandstorm", -1, 2, 2, 1, false, 3, "sandstorm", 1, null), //
						tuple("card_as_booster", -1, 2, 2, 1, false, 2, "haste", 0, null) //

				);
	}

}