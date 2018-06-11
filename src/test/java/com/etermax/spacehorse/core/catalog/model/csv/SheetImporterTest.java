package com.etermax.spacehorse.core.catalog.model.csv;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.csv.sheet.Sheet;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.importer.SheetImporterCSV;

public class SheetImporterTest {

	final String SHEET_NAME = "TestName1";
	final String[] HEADERS_NAMES = new String[] { "Field1", "Field2", "Field3" };
	final String[][] ROW_VALUES = new String[][] { new String[] { "val1", "val2", "val3" }, new String[] { "val4", "val5", "val6" },
			new String[] { "val7", "val8", "val9" } };

	private SheetImporterCSV importer;
	private Sheet sheet;
	private String csv;

	@After
	public void tearDown() {
		importer = null;
		sheet = null;
		csv = null;
	}

	@Test
	public void anEmptCsvProducesAnEmptySheet() {
		givenAnImporter();
		givenAnEmptyCsv();

		whenImporting();

		thenTheSheetIsEmpty();
	}

	@Test
	public void aSingleSingleCsvProducesASheetWithTheSameName() {
		givenAnImporter();
		givenAnCsvWithOnlyAName();

		whenImporting();

		thenTheSheetHasOnlyAName();
	}

	@Test
	public void aCsvWithANameAndAHeaderProducesAValidSheet() {
		givenAnImporter();
		givenAnCsvWithANameAndAHeader();

		whenImporting();

		thenTheSheetHasANameAndValidHeaders();
	}

	@Test
	public void aCsvWithANameAndAHeaderUsingDataSeparatorsProducesAValidSheet() {
		givenAnImporter();
		givenAnCsvWithANameAndAHeaderUsingDataSeparators();

		whenImporting();

		thenTheSheetHasANameAndValidHeaders();
	}

	@Test
	public void aFullCsvProducesAValidSheet() {
		givenAnImporter();
		givenAFullCsv();

		whenImporting();

		thenTheSheetHasFullValues();
	}

	private void thenTheSheetHasFullValues() {
		assertThat(sheet.getName(), equalTo(SHEET_NAME));
		assertThat(sheet.getHeaders(), hasItems(HEADERS_NAMES));
		assertThat(sheet.getRows().size(), equalTo(ROW_VALUES.length));

		for (int i = 0; i < ROW_VALUES.length; i++)
			assertThat(sheet.getRows().get(i), hasItems(ROW_VALUES[i]));
	}

	private void givenAFullCsv() {
		givenAnCsvWithANameAndAHeader();
		csv += SheetImporterCSV.DefaultRowSeperator;

		csv += String.join(SheetImporterCSV.DefaultRowSeperator,
				Arrays.stream(ROW_VALUES).map(strings -> String.join(SheetImporterCSV.DefaultFieldSeparator, strings)).toArray(String[]::new));
	}

	private void thenTheSheetHasANameAndValidHeaders() {
		assertThat(sheet.getName(), equalTo(SHEET_NAME));
		assertThat(sheet.getHeaders(), CoreMatchers.hasItems(HEADERS_NAMES));
		assertThat(sheet.getRows().size(), equalTo(0));
	}

	private void givenAnCsvWithANameAndAHeaderUsingDataSeparators() {
		csv = SHEET_NAME + SheetImporterCSV.DefaultRowSeperator;
		csv += String.join(SheetImporterCSV.DefaultFieldSeparator,
				Arrays.stream(HEADERS_NAMES).map(s -> SheetImporterCSV.DefaultDataSeparator + s + SheetImporterCSV.DefaultDataSeparator)
						.toArray(String[]::new));
	}

	private void givenAnCsvWithANameAndAHeader() {
		csv = SHEET_NAME + SheetImporterCSV.DefaultRowSeperator + String.join(SheetImporterCSV.DefaultFieldSeparator, HEADERS_NAMES);
	}

	private void thenTheSheetHasOnlyAName() {
		assertThat(sheet.getName(), equalTo(SHEET_NAME));
		assertThat(sheet.getHeaders().size(), equalTo(0));
		assertThat(sheet.getRows().size(), equalTo(0));
	}

	private void givenAnCsvWithOnlyAName() {
		csv = SHEET_NAME;
	}

	private void thenTheSheetIsEmpty() {
		assertThat(sheet.getName(), equalTo(""));
		assertThat(sheet.getHeaders().size(), equalTo(0));
		assertThat(sheet.getRows().size(), equalTo(0));
	}

	private void whenImporting() {
		sheet = importer.importSheet(csv);
	}

	private void givenAnEmptyCsv() {
		csv = "";
	}

	private void givenAnImporter() {
		importer = new SheetImporterCSV();
	}
}
