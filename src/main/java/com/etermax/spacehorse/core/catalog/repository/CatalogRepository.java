package com.etermax.spacehorse.core.catalog.repository;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogIsActive;

import java.util.List;

public interface CatalogRepository {

	void add(Catalog catalog);
	Catalog find(String id);
	void update(Catalog catalog);
	List<CatalogIsActive> listLatestCatalogs(int limit);
    Catalog getActiveCatalogWithTag(ABTag abTag);

	Catalog findByIdWithTag(String catalogId, ABTag abTag);
}
