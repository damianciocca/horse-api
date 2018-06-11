package com.etermax.spacehorse.core.catalog.model.csv;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.csv.field.BasicFieldDataType;
import com.etermax.spacehorse.core.catalog.model.csv.field.Fint;
import com.etermax.spacehorse.core.catalog.model.csv.importer.CatalogsCSVImporter;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.SheetsCollection;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.importer.SheetsImporterCSVs;

public class CatalogCSVImporterTest extends CatalogCSVTest {

	SheetsCollection sheets;
	CatalogsCSVImporter importer;

	@Override
	public void after() {
		super.after();

		importer = null;
		sheets = null;
	}

	@Test
	public void aCsvWithOneFieldOfEachTypeProducesAValidSheet() {
		givenACsvWithOneFieldOfEachType();
		givenSheetsImportedFromCsv();

		givenACatalogsImporter();

		whenImportingCatalogsFromSheets();

		CatalogCSV catalog = catalogs.getCatalog("Sheet1");

		assertThat(catalog.getField("FieldInt").getDataType().getBasicDataType(), equalTo(BasicFieldDataType.Int));
		assertThat(catalog.getField("FieldIntArray").getDataType().getBasicDataType(), equalTo(BasicFieldDataType.IntArray));
		assertThat(catalog.getField("FieldString").getDataType().getBasicDataType(), equalTo(BasicFieldDataType.String));
		assertThat(catalog.getField("FieldStringArray").getDataType().getBasicDataType(), equalTo(BasicFieldDataType.StringArray));
		assertThat(catalog.getField("FieldBool").getDataType().getBasicDataType(), equalTo(BasicFieldDataType.Bool));
		assertThat(catalog.getField("FieldFint").getDataType().getBasicDataType(), equalTo(BasicFieldDataType.Fint));
		assertThat(catalog.getField("FieldEnum").getDataType().getBasicDataType(), equalTo(BasicFieldDataType.Enum));
		assertThat(catalog.getField("FieldEnumArray").getDataType().getBasicDataType(), equalTo(BasicFieldDataType.EnumArray));

		assertThat(catalog.getRowsCount(), equalTo(1));

		assertThat(catalog.getInt(0, 0), equalTo(1));
		assertThat(catalog.getIntArray(0, 1), hasItems(2, 3, 4));
		assertThat(catalog.getString(0, 2), equalTo("fede"));
		assertThat(catalog.getStringArray(0, 3), hasItems("fede1", "fede2", "fede3"));
		assertThat(catalog.getBool(0, 4), equalTo(true));
		assertThat(catalog.getFint(0, 5), equalTo(Fint.createFromString("123.25")));
		assertThat(catalog.getEnum(0, 6), equalTo(0));
		assertThat(catalog.getEnumArray(0, 7), hasItems(1, 2));

	}

	@Test
	public void aFullCsvProducesValidSheets() {

		givenAFullTestCsv();
		givenSheetsImportedFromCsv();

		givenACatalogsImporter();

		whenImportingCatalogsFromSheets();

		thenTheCatalogsAreValid();
	}

	protected void whenImportingCatalogsFromSheets() {
		catalogs = importer.processSheets(sheets);
	}

	protected void givenACatalogsImporter() {
		importer = new CatalogsCSVImporter();
	}

	protected void givenSheetsImportedFromCsv() {

		SheetsImporterCSVs sheetsImporter = new SheetsImporterCSVs();

		sheets = sheetsImporter.importSheets(csv);
	}

}
