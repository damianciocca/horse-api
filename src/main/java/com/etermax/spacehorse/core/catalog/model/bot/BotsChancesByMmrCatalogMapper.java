package com.etermax.spacehorse.core.catalog.model.bot;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.BotsChancesByMmrDefinitionResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;

public class BotsChancesByMmrCatalogMapper {

	public EntryContainerResponse<BotsChancesByMmrDefinitionResponse> mapFrom(Catalog catalog) {
		List<BotsChancesByMmrDefinitionResponse> responses = getBotsChancesByMmrFrom(catalog).stream().map(toBotsChancesByMmrDefinitionResponse())
				.collect(Collectors.toList());
		return new EntryContainerResponse<>(responses);
	}

	public List<BotsChancesByMmrDefinition> mapFrom(CatalogResponse catalogResponse) {
		return getBotsChancesByMmrFrom(catalogResponse).stream().map(toBotsChancesByMmrDefinition()).collect(Collectors.toList());
	}

	private List<BotsChancesByMmrDefinitionResponse> getBotsChancesByMmrFrom(CatalogResponse catalogResponse) {
		return catalogResponse.getBotsChancesByMmrCollection().getEntries();
	}

	private List<BotsChancesByMmrDefinition> getBotsChancesByMmrFrom(Catalog catalog) {
		return catalog.getBotsChancesDefinitionsCollection().getEntries();
	}

	private Function<BotsChancesByMmrDefinitionResponse, BotsChancesByMmrDefinition> toBotsChancesByMmrDefinition() {
		return definitionResponse -> new BotsChancesByMmrDefinition( //
				definitionResponse.getId(), //
				definitionResponse.getMinMmr(),//
				definitionResponse.getMaxMmr(), //
				definitionResponse.getChance());
	}

	private Function<BotsChancesByMmrDefinition, BotsChancesByMmrDefinitionResponse> toBotsChancesByMmrDefinitionResponse() {
		return botsChancesByMmrDefinition -> new BotsChancesByMmrDefinitionResponse( //
				botsChancesByMmrDefinition.getId(), //
				botsChancesByMmrDefinition.getMinMmr(),//
				botsChancesByMmrDefinition.getMaxMmr(), //
				botsChancesByMmrDefinition.getChance());
	}
}
