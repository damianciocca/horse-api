package com.etermax.spacehorse.core.catalog.model.bot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.BotsChancesByMmrDefinitionResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.mock.MockUtils;

public class BotsChancesByMmrCatalogMapperTest {

	private Catalog catalog;
	private CatalogResponse catalogResponse;

	@Before
	public void setUp() throws Exception {
		catalog = MockUtils.mockCatalog();
		catalogResponse = new CatalogResponse(catalog);
	}

	@Test
	public void whenMapFromCatalogThenTheDefinitionResponseShouldBeCreated() throws Exception {
		// given
		BotsChancesByMmrCatalogMapper mapper = new BotsChancesByMmrCatalogMapper();
		// when
		List<BotsChancesByMmrDefinitionResponse> responses = mapper.mapFrom(catalog).getEntries();
		// then
		assertThatResponseShouldBeCreatedSuccessfully(responses);
	}

	@Test
	public void whenMapFromCatalogResponseThenDefinitionsShouldBeCreated() throws Exception {
		// given
		BotsChancesByMmrCatalogMapper mapper = new BotsChancesByMmrCatalogMapper();
		// when
		List<BotsChancesByMmrDefinition> definitions = mapper.mapFrom(catalogResponse);
		// then
		assertThatDefinitionShouldBeCreatedSuccessfully(definitions);
	}

	private void assertThatDefinitionShouldBeCreatedSuccessfully(List<BotsChancesByMmrDefinition> definitions) {
		assertThat(definitions).hasSize(3);
		assertThat(definitions) //
				.extracting(//
						BotsChancesByMmrDefinition::getId, //
						BotsChancesByMmrDefinition::getMinMmr, //
						BotsChancesByMmrDefinition::getMaxMmr, //
						BotsChancesByMmrDefinition::getChance) //
				.containsOnly( //
						expectedValues() //
				);
	}

	private void assertThatResponseShouldBeCreatedSuccessfully(List<BotsChancesByMmrDefinitionResponse> responses) {
		assertThat(responses).hasSize(3);
		assertThat(responses) //
				.extracting(//
						BotsChancesByMmrDefinitionResponse::getId, //
						BotsChancesByMmrDefinitionResponse::getMinMmr, //
						BotsChancesByMmrDefinitionResponse::getMaxMmr, //
						BotsChancesByMmrDefinitionResponse::getChance) //
				.containsOnly( //
						expectedValues()//
				);
	}

	private Tuple[] expectedValues() {
		return new Tuple[] { //
				tuple("chance-for-100-to-200", 100, 200, 80), //
				tuple("chance-for-201-to-300", 201, 300, 60), //
				tuple("chance-for-301-to-500", 301, 500, 50) //
		};
	}
}
