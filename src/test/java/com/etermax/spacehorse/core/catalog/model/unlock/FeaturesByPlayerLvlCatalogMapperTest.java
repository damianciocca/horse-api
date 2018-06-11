package com.etermax.spacehorse.core.catalog.model.unlock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.FeaturesByPlayerLvlDefinitionResponse;
import com.etermax.spacehorse.mock.MockUtils;

public class FeaturesByPlayerLvlCatalogMapperTest {

	private Catalog catalog;
	private CatalogResponse catalogResponse;

	@Before
	public void setUp() throws Exception {
		catalog = MockUtils.mockCatalog();
		catalogResponse = new CatalogResponse(catalog);
	}

	@Test
	public void whenMapFromCatalogThenTheFeaturesByPlayerLvlDefinitionResponseShouldBeCreated() throws Exception {
		// given

		FeaturesByPlayerLvlCatalogMapper mapper = new FeaturesByPlayerLvlCatalogMapper();
		// when
		List<FeaturesByPlayerLvlDefinitionResponse> featuresByPlayerLvlDefinitionResponses = mapper.mapFrom(catalog).getEntries();
		// then
		assertThatResponseShouldBeCreatedSuccessfully(featuresByPlayerLvlDefinitionResponses);
	}

	@Test
	public void whenMapFromCatalogResponseThenTheFeaturesByPlayerLvlDefinitionsShouldBeCreated() throws Exception {
		// given
		FeaturesByPlayerLvlCatalogMapper mapper = new FeaturesByPlayerLvlCatalogMapper();
		// when
		List<FeaturesByPlayerLvlDefinition> featuresByPlayerLvlDefinitions = mapper.mapFrom(catalogResponse);
		// then
		assertThatDefinitionShouldBeCreatedSuccessfully(featuresByPlayerLvlDefinitions);
	}

	private void assertThatResponseShouldBeCreatedSuccessfully(List<FeaturesByPlayerLvlDefinitionResponse> featuresByPlayerLvlDefinitionResponses) {
		assertThat(featuresByPlayerLvlDefinitionResponses).hasSize(10);
		assertThat(featuresByPlayerLvlDefinitionResponses) //
				.extracting(//
						FeaturesByPlayerLvlDefinitionResponse::getId, //
						FeaturesByPlayerLvlDefinitionResponse::getFeatureType, //
						FeaturesByPlayerLvlDefinitionResponse::getAvailableOnLevel) //
				.containsOnly( //
						// nivel 0
						tuple("CourierSlot_MaxSlots_2", "CourierSlot", 0), //
						tuple("QuestSlot_EASY", "QuestSlot", 0), //
						tuple("DeckSlot_1", "DeckSlot", 0), //
						// nivel 3
						tuple("CourierSlot_MaxSlots_3", "CourierSlot", 3), //
						// nivel 4
						tuple("QuestSlot_MEDIUM", "QuestSlot", 4), //
						// nivel 5
						tuple("CourierSlot_MaxSlots_4", "CourierSlot", 5), //
						// nivel 6
						tuple("DeckSlot_2", "DeckSlot", 6), //
						tuple("Clubs", "Club", 6), //
						// nivel 7
						tuple("QuestSlot_HARD", "QuestSlot", 7),//
						// nivel 8
						tuple("DeckSlot_3", "DeckSlot", 8) //
				);
	}

	private void assertThatDefinitionShouldBeCreatedSuccessfully(List<FeaturesByPlayerLvlDefinition> featuresByPlayerLvlDefinitions) {
		assertThat(featuresByPlayerLvlDefinitions).hasSize(10);
		assertThat(featuresByPlayerLvlDefinitions) //
				.extracting(//
						FeaturesByPlayerLvlDefinition::getId, //
						FeaturesByPlayerLvlDefinition::getFeatureType, //
						FeaturesByPlayerLvlDefinition::getAvailableOnLevel) //
				.containsOnly( //
						// nivel 0
						tuple("CourierSlot_MaxSlots_2", "CourierSlot", 0), //
						tuple("QuestSlot_EASY", "QuestSlot", 0), //
						tuple("DeckSlot_1", "DeckSlot", 0), //
						// nivel 3
						tuple("CourierSlot_MaxSlots_3", "CourierSlot", 3), //
						// nivel 4
						tuple("QuestSlot_MEDIUM", "QuestSlot", 4), //
						// nivel 5
						tuple("CourierSlot_MaxSlots_4", "CourierSlot", 5), //
						// nivel 6
						tuple("DeckSlot_2", "DeckSlot", 6), //
						tuple("Clubs", "Club", 6), //
						// nivel 7
						tuple("QuestSlot_HARD", "QuestSlot", 7),//
						// nivel 8
						tuple("DeckSlot_3", "DeckSlot", 8) //
				);
	}
}
