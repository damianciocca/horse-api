package com.etermax.spacehorse.mock;

import java.io.File;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MockCatalog {

	private static final String FILE_NAME = "mockCatalog.json";

	private MockCatalog() {
	}

	public static Catalog buildCatalog() {
		return new Catalog((CatalogResponse) (newFromResource(FILE_NAME, CatalogResponse.class)));
	}

	private static Object newFromResource(String resourceName, Class objectClass) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			File jsonFile = new File(objectClass.getClassLoader().getResource(resourceName).getFile());
			return mapper.readValue(jsonFile, objectClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
