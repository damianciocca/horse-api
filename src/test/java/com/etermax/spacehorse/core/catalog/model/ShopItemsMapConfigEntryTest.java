package com.etermax.spacehorse.core.catalog.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.TestUtils;
import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.resource.response.ShopItemsMapConfigEntryResponse;
import com.etermax.spacehorse.mock.MockUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShopItemsMapConfigEntryTest {

	private Catalog catalog;

	@Before
	public void setUp() {
		catalog = MockUtils.mockCatalog();
	}

	@Test
	public void testValidateValidEntryDoesNotThrowAnyException() {
		//Given
		ShopItemsMapConfigEntry shopItemsMapConfigEntry = givenAShopItemsMapConfigEntry
				("com/etermax/spacehorse/catalog/response/shop-items-map-config-entry-response.json");

		//Then
		assertThatCode(() -> shopItemsMapConfigEntry.validate(catalog)).doesNotThrowAnyException();
	}

	@Test
	public void testValidateFailsWhenItemDoesNotExist() {
		//Given
		ShopItemsMapConfigEntry shopItemsMapConfigEntry = givenAShopItemsMapConfigEntry
				("com/etermax/spacehorse/catalog/response/invalid-shop-items-map-config-entry-response.json");

		//When
		Throwable thrown = catchThrowable(() -> shopItemsMapConfigEntry.validate(catalog));

		//Then
		assertThat(thrown).isInstanceOf(CatalogException.class)
				.hasMessage("Error validating ShopItemsMapConfigEntry sarasa-1: ItemId: sarasa not found among ShopItems.");
	}

	@Test
	public void testValidateFailsWhenItemMapNumberIsInvalid() {
		//Given
		ShopItemsMapConfigEntry shopItemsMapConfigEntry = givenAShopItemsMapConfigEntry
				("com/etermax/spacehorse/catalog/response/invalid-map-shop-items-map-config-entry-response.json");

		//When
		Throwable thrown = catchThrowable(() -> shopItemsMapConfigEntry.validate(catalog));

		//Then
		assertThat(thrown).isInstanceOf(CatalogException.class)
				.hasMessage("Error validating ShopItemsMapConfigEntry courier_Mythical-11: Invalid map number: 11.");
	}

	private ShopItemsMapConfigEntry givenAShopItemsMapConfigEntry(String filePath) {
		JsonNode jsonNode = TestUtils.readJson(filePath);
		ShopItemsMapConfigEntryResponse shopItemsMapConfigEntryResponse = TestUtils
				.mapJsonNodeToObject(jsonNode, ShopItemsMapConfigEntryResponse.class);
		return new ShopItemsMapConfigEntry(shopItemsMapConfigEntryResponse);
	}

}
