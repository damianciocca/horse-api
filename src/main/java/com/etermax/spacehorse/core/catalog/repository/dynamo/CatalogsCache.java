package com.etermax.spacehorse.core.catalog.repository.dynamo;

import java.util.HashMap;
import java.util.Map;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;

public class CatalogsCache {
	String activeCatalogId;
	Map<ABTag, Catalog> catalogsWithDelta;

	public CatalogsCache() {
		catalogsWithDelta = new HashMap<>();
	}

	public synchronized Catalog getActiveCatalogWithTag(String catalogId, ABTag abTag) {
		if (!catalogId.equals(activeCatalogId)) {
			return null;
		}
		if (catalogsWithDelta.containsKey(abTag)) {
			return catalogsWithDelta.get(abTag);
		} else {
			return null;
		}
	}

	public synchronized void put(ABTag abTag, Catalog catalog) {
		if (activeCatalogId != null && !activeCatalogId.equals(catalog.getCatalogId())) {
			activeCatalogId = null;
			catalogsWithDelta.clear();
		}

		activeCatalogId = catalog.getCatalogId();
		catalogsWithDelta.put(abTag, catalog);
	}
}
