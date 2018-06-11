package com.etermax.spacehorse.core.catalog.model.csv.exporter;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.catalog.model.csv.CatalogCSV;
import com.etermax.spacehorse.core.catalog.model.csv.CatalogsCSVCollection;
import com.etermax.spacehorse.core.catalog.model.csv.field.Field;
import com.etermax.spacehorse.core.catalog.model.csv.field.FieldDataType;

public class CatalogCSVExporterJson {

	public boolean nice = false;
	private StringBuilder tmpJson = new StringBuilder();

	public String export(CatalogsCSVCollection catalogs) {

		tmpJson.setLength(0);

		tmpJson.append("{\n");

		ArrayList<CatalogCSV> cats = catalogs.getCatalogsList();

		for (int i = 0; i < cats.size(); i++) {

			CatalogCSV catalog = cats.get(i);

			if (i > 0) {
				tmpJson.append(",");
				tmpJson.append("\n");
			}

			tmpJson.append("\"");
			tmpJson.append(escapeString(catalog.getName()));
			tmpJson.append("\"");
			tmpJson.append(":\n");

			export(catalog, tmpJson);
		}

		tmpJson.append("}\n");

		return tmpJson.toString();
	}

	public String export(CatalogCSV catalog) {
		tmpJson.setLength(0);

		export(catalog, tmpJson);

		return tmpJson.toString();
	}

	private void export(CatalogCSV catalog, StringBuilder json) {

		ArrayList<Field> fields = catalog.getFields();

		json.append("{");
		if (nice) {
			json.append("\n");
		}

		json.append("\"entries\":");
		if (nice) {
			json.append("\n");
		}

		json.append("[");
		json.append("\n");

		for (int row = 0; row < catalog.getRowsCount(); row++) {
			if (row > 0) {
				json.append(",");
				json.append("\n");
			}

			json.append("{");
			if (nice) {
				json.append("\n");
			}

			for (int fieldIndex = 0; fieldIndex < fields.size(); fieldIndex++) {
				if (fieldIndex > 0) {
					json.append(",");
					if (nice) {
						json.append("\n");
					}
				}

				if (nice) {
					json.append("    ");
				}

				Field field = fields.get(fieldIndex);
				FieldDataType dataType = field.getDataType();

				json.append("\"");
				json.append(field.getName());
				json.append("\":");

				switch (dataType.getBasicDataType()) {
					case String:
						json.append("\"");
						json.append(escapeString(catalog.getString(row, fieldIndex)));
						json.append("\"");
						break;

					case StringArray: {
						List<String> strings = catalog.getStringArray(row, fieldIndex);

						json.append("[");

						for (int i = 0; i < strings.size(); i++) {
							if (i > 0) {
								json.append(",");
							}
							json.append("\"");
							json.append(escapeString(strings.get(i)));
							json.append("\"");
						}

						json.append("]");
						break;
					}

					case Int:
						json.append(catalog.getInt(row, fieldIndex));
						break;

					case IntArray: {
						List<Integer> ints = catalog.getIntArray(row, fieldIndex);

						json.append("[");

						for (int i = 0; i < ints.size(); i++) {
							if (i > 0) {
								json.append(",");
							}
							json.append(ints.get(i));
						}

						json.append("]");
						break;
					}

					case Bool:
						if (catalog.getBool(row, fieldIndex)) {
							json.append("true");
						} else {
							json.append("false");
						}
						break;

					case Fint:
						json.append("{");
						json.append("\"raw\":");
						json.append(catalog.getFint(row, fieldIndex).getRaw());
						json.append("}");
						break;

					case Enum:
						json.append(catalog.getEnum(row, fieldIndex));
						break;

					case EnumArray: {
						List<Integer> ints = catalog.getEnumArray(row, fieldIndex);

						json.append("[");

						for (int i = 0; i < ints.size(); i++) {
							if (i > 0) {
								json.append(",");
							}
							json.append(ints.get(i));
						}

						json.append("]");
						break;
					}
				}
			}

			if (nice) {
				json.append("\n");
			}
			json.append("}");
		}

		json.append("\n");

		json.append("]");
		if (nice) {
			json.append("\n");
		}

		json.append("}");
		json.append("\n");
	}

	static private String escapeString(String str) {
		str = str.replace("\\", "\\\\");
		str = str.replace("\"", "\\\"");
		str = str.replace("/", "\\/");
		str = str.replace("\b", "\\b");
		str = str.replace("\f", "\\f");
		str = str.replace("\n", "\\n");
		str = str.replace("\r", "\\r");
		str = str.replace("\t", "\\t");

		return str;
	}

}
