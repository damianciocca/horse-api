package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class CatalogEntriesCollectionValidation implements CatalogValidation {

    protected CatalogEntriesCollection catalogEntriesCollection;

    public CatalogEntriesCollectionValidation(CatalogEntriesCollection catalogEntriesCollection) {
        this.catalogEntriesCollection = catalogEntriesCollection;
    }

    @Override
    public boolean validate(Catalog catalog) {
        if (catalogEntriesCollection.getEntries().size() == 0) {
            throw new CatalogException("Collection can't be empty " + catalogEntriesCollection.getClass().getName());
        }
        if (catalogEntriesCollection.containsDuplicates()) {
            throw new CatalogException("Duplicated entries in " + catalogEntriesCollection.getClassName());
        }
        catalogEntriesCollection.getEntries().forEach(t -> ((CatalogEntry)t).validate(catalog)); // up-casting
        return true;
    }

}
