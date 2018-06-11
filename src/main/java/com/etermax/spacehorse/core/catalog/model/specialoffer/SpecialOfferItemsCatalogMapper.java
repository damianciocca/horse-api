package com.etermax.spacehorse.core.catalog.model.specialoffer;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;
import com.etermax.spacehorse.core.catalog.resource.response.SpecialOffersItemResponse;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class SpecialOfferItemsCatalogMapper {

	public EntryContainerResponse<SpecialOffersItemResponse> mapFrom(Catalog catalog) {
		List<SpecialOffersItemResponse> specialOffersItemsResponses = catalog.getSpecialOfferItemsDefinitionsCollection().getEntries().stream()
				.map(SpecialOffersItemResponse::new).collect(Collectors.toList());
		return new EntryContainerResponse<>(specialOffersItemsResponses);

	}

	public List<SpecialOfferItemDefinition> mapFrom(CatalogResponse catalogResponse) {
		return catalogResponse.getSpecialOffersItemsCollection().getEntries().stream() //
				.map(toSpecialOfferItemDefinition()) //
				.collect(Collectors.toList());
	}

	private Function<SpecialOffersItemResponse, SpecialOfferItemDefinition> toSpecialOfferItemDefinition() {
		return specialOffersItemResponse -> new SpecialOfferItemDefinition(specialOffersItemResponse.getId(),//
				specialOffersItemResponse.getItemRewardTypeId(),//
				RewardType.valueOf(specialOffersItemResponse.getRewardType()),//
				specialOffersItemResponse.getQuantity(),//
				specialOffersItemResponse.getSpecialOfferId());
	}
}
