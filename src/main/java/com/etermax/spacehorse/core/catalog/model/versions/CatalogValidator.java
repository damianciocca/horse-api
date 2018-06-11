package com.etermax.spacehorse.core.catalog.model.versions;

import com.etermax.spacehorse.core.catalog.model.Catalog;

import java.util.ArrayList;
import java.util.List;

public class CatalogValidator implements CatalogValidation {

    private List<CatalogValidation> catalogValidationList;

    public CatalogValidator() {
        this.catalogValidationList = new ArrayList<>();
    }

    public CatalogValidator add(CatalogValidation catalogValidation) {
        this.catalogValidationList.add(catalogValidation);
        return this;
    }

    @Override
    public boolean validate(Catalog catalog) {
        catalogValidationList.parallelStream().forEach(v -> v.validate(catalog));
        return true;
    }

}
