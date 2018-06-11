package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.UnitDefinition;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class UnitLevelConsistencyValidation implements CatalogValidation {

    @Override
    public boolean validate(Catalog catalog) {
        for (UnitDefinition unitDefinition : catalog.getUnitDefinitionsCollection().getEntries()) {
            if (!catalog.getUnitLevelDefinitionsCollection().getEntries().stream()
                    .filter(unitLevel -> unitLevel.getUnitId().equals(unitDefinition.getId())).findFirst().isPresent()) {
                throw new CatalogException("No Unit Level found for Unit " + unitDefinition.getId());
            }
        }
        return true;
    }

}
