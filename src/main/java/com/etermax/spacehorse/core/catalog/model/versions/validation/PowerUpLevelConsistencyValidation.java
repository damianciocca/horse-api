package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.PowerUpDefinition;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class PowerUpLevelConsistencyValidation implements CatalogValidation {

    @Override
    public boolean validate(Catalog catalog) {
        for (PowerUpDefinition powerUp : catalog.getPowerUpDefinitionsCollection().getEntries()) {
            if (!catalog.getPowerUpLevelDefinitionsCollection().getEntries().stream()
                    .filter(powerUpLevel -> powerUpLevel.getPowerUpId().equals(powerUp.getId())).findFirst().isPresent()) {
                throw new CatalogException("No Power Up Level found for Power Up " + powerUp.getId());
            }
        }
        return true;
    }

}
