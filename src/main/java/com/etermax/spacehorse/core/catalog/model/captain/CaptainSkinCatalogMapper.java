package com.etermax.spacehorse.core.catalog.model.captain;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CaptainSkinDefinitionResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;

public class CaptainSkinCatalogMapper {

	public EntryContainerResponse<CaptainSkinDefinitionResponse> mapFrom(Catalog catalog) {
		List<CaptainSkinDefinitionResponse> responses = catalog.getCaptainSkinDefinitionsCollection().getEntries().stream()
				.map(toCaptainSkinDefinitionResponse()).collect(Collectors.toList());
		return new EntryContainerResponse<>(responses);
	}

	public List<CaptainSkinDefinition> mapFrom(CatalogResponse catalogResponse) {
		return catalogResponse.getCaptainsSkinsCollection().getEntries().stream().map(toCaptainSkinDefinition()).collect(Collectors.toList());
	}

	private Function<CaptainSkinDefinitionResponse, CaptainSkinDefinition> toCaptainSkinDefinition() {
		return definitionSkinResponse -> new CaptainSkinDefinition( //
				definitionSkinResponse.getId(), //
				definitionSkinResponse.getCaptainId(),//
				definitionSkinResponse.getSkinId(),//
				new CaptainSkinDefinition.Slot(definitionSkinResponse.getSlotNumber()),//
				definitionSkinResponse.getGoldPrice(), //
				definitionSkinResponse.getGemsPrice(), //
				definitionSkinResponse.getIsDefault());
	}

	private Function<CaptainSkinDefinition, CaptainSkinDefinitionResponse> toCaptainSkinDefinitionResponse() {
		return captainSkinDefinition -> new CaptainSkinDefinitionResponse( //
				captainSkinDefinition.getId(), //
				captainSkinDefinition.getCaptainId(),//
				captainSkinDefinition.getSkinId(), //
				captainSkinDefinition.getSlotNumber(),//
				captainSkinDefinition.getGoldPrice(),//
				captainSkinDefinition.getGemsPrice(), //
				captainSkinDefinition.isDefault());
	}

}
