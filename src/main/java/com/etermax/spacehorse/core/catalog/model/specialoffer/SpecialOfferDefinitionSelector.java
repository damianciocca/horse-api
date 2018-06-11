package com.etermax.spacehorse.core.catalog.model.specialoffer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.MarketType;

public class SpecialOfferDefinitionSelector {

	public static Optional<SpecialOfferDefinition> selectFromInAppProductId(Catalog catalog, String productId, MarketType marketType) {

		List<SpecialOfferInAppDefinition> inAppDefinitions = catalog.getSpecialOfferInAppDefinitionsCollection().getEntries();
		List<SpecialOfferDefinition> offerDefinitions = catalog.getSpecialOfferDefinitionsCollection().getEntries();

		Optional<SpecialOfferInAppDefinition> inAppDefinition = inAppDefinitions.stream()
				.filter(inApp -> inApp.getMarketType().equals(marketType) && Objects.equals(inApp.getProductId(), productId)).findFirst();

		if (inAppDefinition.isPresent()) {
			return offerDefinitions.stream().filter(offerDefinition -> offerDefinition.isInAppPurchase() && offerDefinition.getShopInAppItemId()
					.equals(inAppDefinition.get().getItemId())).findFirst();
		}

		return Optional.empty();

	}
}
