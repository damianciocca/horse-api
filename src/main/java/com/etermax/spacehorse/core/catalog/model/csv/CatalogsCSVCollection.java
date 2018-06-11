package com.etermax.spacehorse.core.catalog.model.csv;

import java.util.ArrayList;
import java.util.Hashtable;

public class CatalogsCSVCollection {

	private Hashtable<String, CatalogCSV> catalogsByName = new Hashtable<>();

	public void add(CatalogCSV sheet) {
		catalogsByName.put(sheet.getName(), sheet);
	}

	public CatalogCSV getCatalog(String name) {
		if (catalogsByName.containsKey(name)) {
			return catalogsByName.get(name);
		}

		return null;
	}

	public ArrayList<CatalogCSV> getCatalogsList() {
		return new ArrayList<>(catalogsByName.values());
	}
}
