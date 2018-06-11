package com.etermax.spacehorse.core.catalog.model.captain;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CaptainDefinitionResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;

public class CaptainCatalogMapper {

	public EntryContainerResponse<CaptainDefinitionResponse> mapFrom(Catalog catalog) {
		List<CaptainDefinitionResponse> responses = catalog.getCaptainDefinitionsCollection().getEntries().stream().map(toCaptainDefinitionResponse())
				.collect(Collectors.toList());
		return new EntryContainerResponse<>(responses);
	}

	public List<CaptainDefinition> mapFrom(CatalogResponse catalogResponse) {
		return catalogResponse.getCaptainsCollection().getEntries().stream().map(toCaptainDefinition()).collect(Collectors.toList());
	}

	private Function<CaptainDefinitionResponse, CaptainDefinition> toCaptainDefinition() {
		return definitionResponse -> new CaptainDefinition(definitionResponse.getId(), definitionResponse.getMapNumber(),
				definitionResponse.getGoldPrice(), definitionResponse.getGemsPrice());
	}

	private Function<CaptainDefinition, CaptainDefinitionResponse> toCaptainDefinitionResponse() {
		return captainDefinition -> new CaptainDefinitionResponse(captainDefinition.getId(), captainDefinition.getMapNumber(),
				captainDefinition.getGoldPrice(), captainDefinition.getGemsPrice());
	}

}
