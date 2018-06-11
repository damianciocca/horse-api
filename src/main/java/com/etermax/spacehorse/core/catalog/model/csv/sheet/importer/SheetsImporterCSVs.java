package com.etermax.spacehorse.core.catalog.model.csv.sheet.importer;

import java.util.regex.Pattern;

import com.etermax.spacehorse.core.catalog.model.csv.sheet.Sheet;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.SheetsCollection;

public class SheetsImporterCSVs {

	static public final String SheetsCsvSeparator = "**8a72f76159b712cda09a19745318a11b**d3a268491898ab36874aab2858816f46**3e2c9348bfbaec2d26cab0c2b379270d**";
	static private final String SheetsCsvSeparatorPattern = Pattern.quote(SheetsCsvSeparator);

	public SheetsCollection importSheets(String csvs) {
		String[] splitCSVs = csvs.split(SheetsCsvSeparatorPattern);

		SheetsCollection sheets = new SheetsCollection();

		SheetImporterCSV sheetImporter = new SheetImporterCSV();

		for (String csv : splitCSVs) {
			Sheet sheet = sheetImporter.importSheet(csv);

			sheets.add(sheet);
		}

		return sheets;
	}
}
