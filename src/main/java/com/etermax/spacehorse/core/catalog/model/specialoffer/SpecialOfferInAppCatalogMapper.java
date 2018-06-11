package com.etermax.spacehorse.core.catalog.model.specialoffer;

import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;
import com.etermax.spacehorse.core.catalog.resource.response.SpecialOfferInAppDefinitionResponse;

public class SpecialOfferInAppCatalogMapper {

	public EntryContainerResponse<SpecialOfferInAppDefinitionResponse> mapFrom(Catalog catalog) {
		List<SpecialOfferInAppDefinitionResponse> responses = catalog.getSpecialOfferInAppDefinitionsCollection().getEntries().stream()
				.map(SpecialOfferInAppDefinitionResponse::new).collect(Collectors.toList());
		return new EntryContainerResponse<>(responses);
	}

	public List<SpecialOfferInAppDefinition> mapFrom(CatalogResponse catalogResponse) {
		return catalogResponse.getSpecialOfferInAppCollection().getEntries().stream().map(SpecialOfferInAppDefinition::new)
				.collect(Collectors.toList());
	}

}
