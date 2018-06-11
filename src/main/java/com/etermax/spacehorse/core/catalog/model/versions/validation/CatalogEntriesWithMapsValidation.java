package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.*;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

import java.util.HashMap;

public class CatalogEntriesWithMapsValidation implements CatalogValidation {

    private CatalogEntriesCollection catalogEntriesCollection;

    public CatalogEntriesWithMapsValidation(CatalogEntriesCollection catalogEntriesCollection) {
        this.catalogEntriesCollection = catalogEntriesCollection;
    }

    @Override
    public boolean validate(Catalog catalog) {
        HashMap<Integer, Integer> mapsAmounts = new HashMap<>();
        for (Object catalogEntry : this.catalogEntriesCollection.getEntries()) {
            CatalogEntryWithMapInformation entry = (CatalogEntryWithMapInformation) catalogEntry;
            if (!mapsAmounts.containsKey(entry.getMapNumber())) {
                mapsAmounts.put(entry.getMapNumber(), 1);
            } else {
                mapsAmounts.put(entry.getMapNumber(), mapsAmounts.get(entry.getMapNumber()) + 1);
            }
        }
        for (MapDefinition mapDefinition : catalog.getMapsCollection().getEntries()) {
            if (!mapsAmounts.containsKey(mapDefinition.getMapNumber())) {
                throw new CatalogException("Catalog entries are missing information for map number "
                        + mapDefinition.getMapNumber() + " in collection " + catalogEntriesCollection.getClassName());
            }
        }
        return true;
    }

}
