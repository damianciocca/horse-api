package com.etermax.spacehorse.core.catalog.model.csv;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.Hashtable;

import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.csv.exporter.CatalogCSVExporterJson;
import com.etermax.spacehorse.core.catalog.model.csv.importer.CatalogsCSVImporter;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.SheetsCollection;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.importer.SheetsImporterCSVs;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

public class CatalogCSVExporterTest extends CatalogCSVTest {

	Hashtable<String, String> catalogsJson;
	String singleCatalogsJson;

	@Override
	public void after() {
		super.after();

		catalogsJson = null;
		singleCatalogsJson = null;
	}

	@Test
	public void aFullCsvProducesValidJsons() {

		givenCatalogsFromFullTestCsv();

		whenExportingSheetsToJsons();

		thenTheJsonsAreValid();
	}

	@Test
	public void aFullCsvProducesASingleValidJson() {

		givenCatalogsFromFullTestCsv();

		whenExportingSheetsToSingleJson();

		thenTheSingleJsonIsValid();
	}

	private void givenCatalogsFromFullTestCsv() {

		givenAFullTestCsv();

		SheetsCollection sheets = new SheetsImporterCSVs().importSheets(csv);

		catalogs = new CatalogsCSVImporter().processSheets(sheets);
	}

	private void thenTheSingleJsonIsValid() {
		assertTrue(isValidJson(singleCatalogsJson));
	}

	private void thenTheJsonsAreValid() {
		assertThat(catalogsJson.keySet(), hasItems(FULL_SHEETS_NAMES));

		catalogsJson.keySet().forEach(name -> assertTrue(isValidJson(catalogsJson.get(name))));
	}

	private boolean isValidJson(String json) {
		Object obj;
		try {
			obj = JSONValue.parseStrict(json);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		return obj != null && obj instanceof JSONObject;
	}

	private void whenExportingSheetsToSingleJson() {
		CatalogCSVExporterJson jsonExporter = new CatalogCSVExporterJson();

		singleCatalogsJson = jsonExporter.export(catalogs);
	}

	private void whenExportingSheetsToJsons() {
		CatalogCSVExporterJson jsonExporter = new CatalogCSVExporterJson();

		catalogsJson = new Hashtable<>();

		for (CatalogCSV catalog : catalogs.getCatalogsList()) {
			String json = jsonExporter.export(catalog);
			catalogsJson.put(catalog.getName(), json);
		}
	}
}
