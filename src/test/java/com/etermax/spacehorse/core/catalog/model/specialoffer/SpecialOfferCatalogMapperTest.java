package com.etermax.spacehorse.core.catalog.model.specialoffer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.SpecialOfferDefinitionResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.mock.MockUtils;

public class SpecialOfferCatalogMapperTest {

	private Catalog catalog;
	private CatalogResponse catalogResponse;

	@Before
	public void setUp() throws Exception {
		catalog = MockUtils.mockCatalog();
		catalogResponse = new CatalogResponse(catalog);
	}

	@Test
	public void whenMapFromCatalogResponseThenSpecialOfferDefinitionsShouldBeCreated() throws Exception {
		// given
		SpecialOfferCatalogMapper mapper = new SpecialOfferCatalogMapper();
		// when
		List<SpecialOfferDefinition> definitions = mapper.mapFrom(catalogResponse);
		// then
		assertThat(definitions).hasSize(7);
		assertThatScheduledSpecialOffersShouldHaveACorrectActivationTime(definitions);
	}

	@Test
	public void whenMapFromCatalogThenSpecialOfferDefinitionsResponseShouldBeCreated() throws Exception {
		// given
		SpecialOfferCatalogMapper mapper = new SpecialOfferCatalogMapper();
		// when
		List<SpecialOfferDefinitionResponse> responses = mapper.mapFrom(catalog).getEntries();
		// then
		assertThat(responses).hasSize(7);
	}

	private void assertThatScheduledSpecialOffersShouldHaveACorrectActivationTime(List<SpecialOfferDefinition> definitions) {
		for (SpecialOfferDefinition definition : definitions) {
			if (definition.isScheduledFilterEnabled()) {
				assertThat(definition.getActivationTime()).isEqualTo(ServerTime.roundToStartOfDay(definition.getActivationTime()));
			}
		}
	}
}
