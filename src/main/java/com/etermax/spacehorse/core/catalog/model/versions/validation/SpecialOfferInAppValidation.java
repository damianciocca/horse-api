package com.etermax.spacehorse.core.catalog.model.versions.validation;

import java.util.function.Predicate;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferInAppDefinition;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class SpecialOfferInAppValidation implements CatalogValidation {

	@Override
	public boolean validate(Catalog catalog) {
		catalog.getSpecialOfferDefinitionsCollection().getEntries().forEach(specialOfferDefinition -> {
			if (specialOfferDefinition.isInAppPurchase()) {
				catalog.getSpecialOfferInAppDefinitionsCollection().getEntries().stream() //
						.filter(byInAppItem(specialOfferDefinition)) //
						.findFirst() //
						.orElseThrow(() -> throwCatalogException(specialOfferDefinition));
			}
		});
		return true;
	}

	private Predicate<SpecialOfferInAppDefinition> byInAppItem(SpecialOfferDefinition specialOfferDefinition) {
		return iap -> iap.getItemId().equals(specialOfferDefinition.getShopInAppItemId());
	}

	private CatalogException throwCatalogException(SpecialOfferDefinition specialOfferDefinition) {
		return new CatalogException("Special Offer In App not found for special offer definition id [ " + specialOfferDefinition.getId() + " ]");
	}

}
