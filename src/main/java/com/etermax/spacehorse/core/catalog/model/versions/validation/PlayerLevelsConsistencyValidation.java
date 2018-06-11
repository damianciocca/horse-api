package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.PlayerLevelsCollection;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class PlayerLevelsConsistencyValidation implements CatalogValidation {

    @Override
    public boolean validate(Catalog catalog) {
        PlayerLevelsCollection playerLevelsCollection = catalog.getPlayerLevelsCollection();
        if (playerLevelsCollection.getMaxLevel() == 0) {
            throw new CatalogException("Missing player levels");
        }
        for (int level = 0; level < playerLevelsCollection.getMaxLevel(); level++) {
            if (!playerLevelsCollection.findByLevel(level).isPresent()) {
                throw new CatalogException("Missing player level " + level);
            }
        }
        return true;
    }

}
