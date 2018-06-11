package com.etermax.spacehorse.core.specialoffer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinitionSelector;
import com.etermax.spacehorse.mock.MockUtils;

public class SpecialOfferDefinitionSelectorTest {

	private Catalog catalog;

	@Before
	public void setUp() throws Exception {
		catalog = MockUtils.mockCatalog();
	}

	@Test
	public void whenExistProductIdThenTheSpecialOfferShouldBeFound() throws Exception {
		Optional<SpecialOfferDefinition> specialOfferDefinition = SpecialOfferDefinitionSelector
				.selectFromInAppProductId(catalog, "com.etermax.orbital1.starterPack1", MarketType.ANDROID);

		assertThat(specialOfferDefinition.isPresent()).isTrue();
		assertThat(specialOfferDefinition.get().getId()).isEqualTo("starter_pack1");
	}

	@Test
	public void whenNonExistProductIdThenTheSpecialOfferShouldNotBeFound() throws Exception {
		Optional<SpecialOfferDefinition> specialOfferDefinition = SpecialOfferDefinitionSelector.selectFromInAppProductId(catalog, "unknown", MarketType.ANDROID);

		assertThat(specialOfferDefinition.isPresent()).isFalse();
	}
}
