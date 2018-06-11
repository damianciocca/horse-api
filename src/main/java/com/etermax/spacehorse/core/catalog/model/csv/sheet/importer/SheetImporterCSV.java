package com.etermax.spacehorse.core.catalog.model.csv.sheet.importer;

import java.util.ArrayList;
import java.util.Collections;

import com.etermax.spacehorse.core.catalog.model.csv.sheet.Sheet;

public class SheetImporterCSV {

	static public final String DefaultRowSeperator = "\n";
	static public final String DefaultFieldSeparator = ",";
	static public final String DefaultDataSeparator = "\"";

	static private final char EscapePrefix = '\\';

	static private final char RowsSeparato1 = '\r';
	static private final char RowsSeparato2 = '\n';

	static private final char FieldSeparator = ',';
	static private final char FieldDataSeparator = '\"';

	public Sheet importSheet(String csv) {
		String[] lines = splitCsvInLines(csv);

		if (lines.length == 0) {
			return new Sheet();
		}

		String sheetName = lines[0];

		Sheet sheet = new Sheet();
		sheet.setName(sheetName);

		if (lines.length > 1) {
			String[] headers = splitRowInFields(lines[1]);

			for (String header : headers)
				sheet.addHeader(header);

			for (int i = 2; i < lines.length; i++) {
				ArrayList<String> row = sheet.addRow();

				String[] rowData = splitRowInFields(lines[i]);

				Collections.addAll(row, rowData);
			}
		}

		return sheet;
	}

	static private String[] splitCsvInLines(String csv) {
		if (csv.length() == 0) {
			return new String[0];
		}

		StringBuilder sb = new StringBuilder();
		ArrayList<String> lines = new ArrayList<>();

		boolean insideValue = false;

		for (int i = 0; i < csv.length(); i++) {
			char c = csv.charAt(i);

			switch (c) {
				case FieldDataSeparator:
					sb.append(c);
					insideValue = !insideValue;
					break;

				case EscapePrefix:
					if (insideValue) {
						sb.append(c);
						if (i + 1 < csv.length()) {
							sb.append(csv.charAt(i + 1));
							i++;
						}
					} else {
						sb.append(c);
					}
					break;

				case RowsSeparato1:
				case RowsSeparato2:
					if (insideValue) {
						sb.append(c);
					} else {
						lines.add(sb.toString());
						sb.setLength(0);
					}
					break;

				default:
					sb.append(c);
					break;
			}
		}

		if (sb.length() > 0) {
			lines.add(sb.toString());
			sb.setLength(0);
		}

		for (int i = lines.size() - 1; i >= 0; i--)
			if (lines.get(i).length() == 0) {
				lines.remove(i);
			}

		return lines.toArray(new String[lines.size()]);
	}

	static private String[] splitRowInFields(String row) {
		if (row.length() == 0) {
			return new String[0];
		}

		ArrayList<String> fields = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		boolean insideValue = false;
		boolean expectDataSeparatorEnd = false;
		boolean emptyStringValid = false;

		for (int i = 0; i < row.length(); i++) {
			char c = row.charAt(i);

			switch (c) {
				case FieldDataSeparator:
					if (!insideValue) {
						insideValue = true;
						expectDataSeparatorEnd = true;
					} else if (expectDataSeparatorEnd) {
						fields.add(sb.toString());
						sb.setLength(0);
						insideValue = false;
						emptyStringValid = false;
					} else {
						sb.append(c);
					}
					break;

				case EscapePrefix:
					if (insideValue && expectDataSeparatorEnd) {
						if (i + 1 < row.length()) {
							sb.append(row.charAt(i + 1));
							i++;
						} else {
							sb.append(c);
						}
					} else {
						sb.append(c);
					}
					break;

				case FieldSeparator:
					emptyStringValid = true;
					if (insideValue) {
						if (expectDataSeparatorEnd) {
							sb.append(c);
						} else {
							fields.add(sb.toString());
							sb.setLength(0);
							insideValue = false;
						}
					}
					break;

				default:
					if (insideValue) {
						sb.append(c);
					} else {
						expectDataSeparatorEnd = false;
						insideValue = true;
						sb.append(c);
					}
					break;
			}
		}

		if (sb.length() > 0 || emptyStringValid) {
			fields.add(sb.toString());
			sb.setLength(0);
		}

		return fields.toArray(new String[fields.size()]);
	}
}
