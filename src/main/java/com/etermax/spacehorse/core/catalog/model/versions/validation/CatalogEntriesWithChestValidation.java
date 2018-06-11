package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.CatalogEntryWithChestInformation;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class CatalogEntriesWithChestValidation implements CatalogValidation {

    private CatalogEntriesCollection catalogEntriesCollection;

    public CatalogEntriesWithChestValidation(CatalogEntriesCollection catalogEntriesCollection) {
        this.catalogEntriesCollection = catalogEntriesCollection;
    }

    @Override
    public boolean validate(Catalog catalog) {
        for (Object catalogEntry : this.catalogEntriesCollection.getEntries()) {
            CatalogEntryWithChestInformation entry = (CatalogEntryWithChestInformation) catalogEntry;
            catalog.getChestDefinitionsCollection().findById(entry.getChestId()).orElseThrow(() ->
                    new CatalogException("Chest id " + entry.getChestId() + " in " + this.catalogEntriesCollection.getClassName() + " not found among chest definitions"));
        }
        return true;
    }

}
