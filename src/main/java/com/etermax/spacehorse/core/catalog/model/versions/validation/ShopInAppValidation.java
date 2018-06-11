package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ShopInAppDefinition;
import com.etermax.spacehorse.core.catalog.model.ShopItemDefinition;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

import java.util.Optional;

public class ShopInAppValidation implements CatalogValidation {

    @Override
    public boolean validate(Catalog catalog) {
        for (ShopItemDefinition shopItemDefinition : catalog.getShopItemsCollection().getEntries()) {
            if (shopItemDefinition.getInAppPurchase()) {
                Optional<ShopInAppDefinition> shopInAppDefinition = catalog.getShopInAppCollection().getEntries().stream().filter(iap -> iap.getItemId().equals(shopItemDefinition.getId())).findFirst();
                if (!shopInAppDefinition.isPresent()) {
                    throw new CatalogException("Shop Definition not found for Shop In App " + shopItemDefinition.getId());
                }
            }
        }
        for (ShopInAppDefinition shopInAppDefinition : catalog.getShopInAppCollection().getEntries()) {
            Optional<ShopItemDefinition> shopItemDefinition = catalog.getShopItemsCollection().findById(shopInAppDefinition.getItemId());
            if (!shopItemDefinition.isPresent()) {
                throw new CatalogException("Shop In App not found among Shop Definitions " + shopInAppDefinition.getId());
            }
        }
        return true;
    }

}
