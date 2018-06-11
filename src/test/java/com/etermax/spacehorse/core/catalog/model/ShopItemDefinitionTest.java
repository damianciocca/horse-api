package com.etermax.spacehorse.core.catalog.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.TestUtils;
import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.resource.response.ShopItemEntryResponse;
import com.etermax.spacehorse.mock.MockUtils;
import com.fasterxml.jackson.databind.JsonNode;

public class ShopItemDefinitionTest {

	private Catalog catalog;

	@Before
	public void setUp() {
		catalog = MockUtils.mockCatalog();
	}

	@Test
	public void testValidateShopItemDefinition() {
		//Given
		ShopItemDefinition shopItemDefinition = givenAShopItemDefinition(
				"com/etermax/spacehorse/catalog/response/shop-item-definition-response.json");

		//Then
		assertThatCode(() -> shopItemDefinition.validate(catalog)).doesNotThrowAnyException();
	}

	@Test
	public void testValidateInvalidQuantityShopItemDefinition() {
		//Given
		ShopItemDefinition shopItemDefinition = givenAShopItemDefinition(
				"com/etermax/spacehorse/catalog/response/invalid-quantity-shop-item-definition-response.json");

		//When
		Throwable thrown = catchThrowable(() -> shopItemDefinition.validate(catalog));

		//Then
		assertThat(thrown).isInstanceOf(CatalogException.class)
				.hasMessage("Error validating ShopItemDefinition ChestGlorious: itemQuantity =< 0");
	}

	@Test
	public void testValidateInvalidGemPriceShopItemDefinition() {
		//Given
		ShopItemDefinition shopItemDefinition = givenAShopItemDefinition(
				"com/etermax/spacehorse/catalog/response/invalid-gem-price-shop-item-definition-response.json");

		//When
		Throwable thrown = catchThrowable(() -> shopItemDefinition.validate(catalog));

		//Then
		assertThat(thrown).isInstanceOf(CatalogException.class)
				.hasMessage("Error validating ShopItemDefinition ChestGlorious: itemGemPrice =< 0");
	}

	private ShopItemDefinition givenAShopItemDefinition(String filePath) {
		JsonNode jsonNode = TestUtils.readJson(filePath);
		ShopItemEntryResponse shopItemEntryResponse = TestUtils
				.mapJsonNodeToObject(jsonNode, ShopItemEntryResponse.class);
		return new ShopItemDefinition(shopItemEntryResponse);
	}

}
