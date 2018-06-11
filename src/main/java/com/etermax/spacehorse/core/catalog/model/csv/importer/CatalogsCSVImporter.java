package com.etermax.spacehorse.core.catalog.model.csv.importer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import com.etermax.spacehorse.core.catalog.model.csv.CatalogCSV;
import com.etermax.spacehorse.core.catalog.model.csv.CatalogsCSVCollection;
import com.etermax.spacehorse.core.catalog.model.csv.field.BasicFieldDataType;
import com.etermax.spacehorse.core.catalog.model.csv.field.BasicFieldDataTypeUtils;
import com.etermax.spacehorse.core.catalog.model.csv.field.Field;
import com.etermax.spacehorse.core.catalog.model.csv.field.FieldDataType;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.Sheet;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.SheetsCollection;
import com.etermax.spacehorse.core.common.exception.ApiException;

public class CatalogsCSVImporter {
	static public final String MasterlistsName = "Masterlists";
	static public final String MasterlistsFieldsName = "MasterlistsFields";
	static public final String MasterlistsEnumsName = "MasterlistsEnums";

	public CatalogsCSVCollection processSheets(SheetsCollection sheets) {
		CatalogCSV masterlists = buildMasterlsitsCatalog(sheets.getSheet(MasterlistsName));
		CatalogCSV masterlistsFields = buildFieldsCatalog(sheets.getSheet(MasterlistsFieldsName));
		CatalogCSV masterlistsEnums = buildEnumsCatalog(sheets.getSheet(MasterlistsEnumsName));

		Hashtable<String, Hashtable<String, Integer>> enumsByName = buildEnums(masterlistsEnums);

		return buildCatalogs(buildMasterListsNames(masterlists), sheets, masterlistsFields, enumsByName);
	}

	private CatalogsCSVCollection buildCatalogs(List<String> masterListsNames, SheetsCollection sheets, CatalogCSV masterlistsFields,
			Hashtable<String, Hashtable<String, Integer>> enumsByName) {
		HashSet<String> masterListsNamesSet = new HashSet<>();

		masterListsNames.forEach(masterListsNamesSet::add);

		int sheetNameIndex = masterlistsFields.getFieldIndex("SheetName");
		int fieldNameIndex = masterlistsFields.getFieldIndex("FieldName");
		int fieldTypeIndex = masterlistsFields.getFieldIndex("FieldType");
		int enumTypeIndex = masterlistsFields.getFieldIndex("EnumType");

		Hashtable<String, ArrayList<Field>> fieldsBySheetName = new Hashtable<>();

		for (int row = 0; row < masterlistsFields.getRowsCount(); row++) {
			String sheetName = masterlistsFields.getString(row, sheetNameIndex);
			String fieldName = masterlistsFields.getString(row, fieldNameIndex);
			BasicFieldDataType basicFieldType = BasicFieldDataTypeUtils.fromInt(masterlistsFields.getEnum(row, fieldTypeIndex));
			String enumType = masterlistsFields.getString(row, enumTypeIndex);

			if (!masterListsNamesSet.contains(sheetName)) {
				throw new ApiException("Unknown SheetName " + sheetName + " in fields sheet");
			}

			FieldDataType fieldType;

			if (basicFieldType == BasicFieldDataType.Enum || basicFieldType == BasicFieldDataType.EnumArray) {
				if (enumsByName.get(enumType) == null) {
					throw new ApiException("Field " + fieldName + " of " + sheetName + " has an invalid EnumType value in fields sheet");
				}

				Hashtable<String, Integer> enumValues = enumsByName.get(enumType);
				fieldType = new FieldDataType(enumValues, basicFieldType);
			} else {
				fieldType = new FieldDataType(basicFieldType);
			}

			Field field = new Field(fieldName, fieldType);

			if (!fieldsBySheetName.containsKey(sheetName)) {
				fieldsBySheetName.put(sheetName, new ArrayList<>());
			}

			fieldsBySheetName.get(sheetName).add(field);
		}

		CatalogsCSVCollection catalogs = new CatalogsCSVCollection();

		for (String sheetName : fieldsBySheetName.keySet()) {
			ArrayList<Field> fields = fieldsBySheetName.get(sheetName);

			Sheet sheet = sheets.getSheet(sheetName);

			CatalogCSV catalog = buildCatalog(sheet, fields);

			catalogs.add(catalog);
		}

		return catalogs;
	}

	private ArrayList<String> buildMasterListsNames(CatalogCSV masterlists) {
		ArrayList<String> names = new ArrayList<>();

		int sheetNameIndex = masterlists.getFieldIndex("SheetName");

		for (int i = 0; i < masterlists.getRowsCount(); i++)
			names.add(masterlists.getString(i, sheetNameIndex));

		return names;
	}

	private CatalogCSV buildMasterlsitsCatalog(Sheet data) {
		ArrayList<Field> fields = new ArrayList<>();

		fields.add(new Field("SheetName", new FieldDataType(BasicFieldDataType.String)));

		return buildCatalog(data, fields);
	}

	private CatalogCSV buildFieldsCatalog(Sheet data) {
		ArrayList<Field> fields = new ArrayList<>();

		fields.add(new Field("SheetName", new FieldDataType(BasicFieldDataType.String)));
		fields.add(new Field("FieldName", new FieldDataType(BasicFieldDataType.String)));

		Hashtable<String, Integer> fieldTypeEnum = new Hashtable<>();
		fieldTypeEnum.put("int", BasicFieldDataTypeUtils.VAL_INT);
		fieldTypeEnum.put("int[]", BasicFieldDataTypeUtils.VAL_INT_ARRAY);
		fieldTypeEnum.put("string", BasicFieldDataTypeUtils.VAL_STRING);
		fieldTypeEnum.put("string[]", BasicFieldDataTypeUtils.VAL_STRING_ARRAY);
		fieldTypeEnum.put("bool", BasicFieldDataTypeUtils.VAL_BOOL);
		fieldTypeEnum.put("fint", BasicFieldDataTypeUtils.VAL_FINT);
		fieldTypeEnum.put("enum", BasicFieldDataTypeUtils.VAL_ENUM);
		fieldTypeEnum.put("enum[]", BasicFieldDataTypeUtils.VAL_ENUM_ARRAY);

		fields.add(new Field("FieldType", new FieldDataType(fieldTypeEnum, BasicFieldDataType.Enum)));
		fields.add(new Field("EnumType", new FieldDataType(BasicFieldDataType.String)));

		return buildCatalog(data, fields);
	}

	private CatalogCSV buildEnumsCatalog(Sheet data) {
		ArrayList<Field> fields = new ArrayList<>();

		fields.add(new Field("EnumId", new FieldDataType(BasicFieldDataType.String)));
		fields.add(new Field("Id", new FieldDataType(BasicFieldDataType.String)));
		fields.add(new Field("Value", new FieldDataType(BasicFieldDataType.Int)));

		return buildCatalog(data, fields);
	}

	private CatalogCSV buildCatalog(Sheet data, ArrayList<Field> fields) {
		CatalogCSV catalog = new CatalogCSV(data.getName(), fields);

		int[] rowIndexByFieldIndex = new int[fields.size()];
		for (int i = 0; i < fields.size(); i++)
			rowIndexByFieldIndex[i] = data.getHeaderIndex(fields.get(i).getName());

		for (ArrayList<String> row : data.getRows()) {
			int catalogRowIndex = catalog.addRow();

			for (int i = 0; i < fields.size(); i++) {
				String rawData = row.get(rowIndexByFieldIndex[i]);
				catalog.setData(catalogRowIndex, i, rawData);
			}
		}

		return catalog;
	}

	private Hashtable<String, Hashtable<String, Integer>> buildEnums(CatalogCSV masterlistsEnums) {
		Hashtable<String, Hashtable<String, Integer>> enumsByName = new Hashtable<>();

		//EnumId,Id,Value
		int enumIdIndex = masterlistsEnums.getFieldIndex("EnumId");
		int idIndex = masterlistsEnums.getFieldIndex("Id");
		int valueIndex = masterlistsEnums.getFieldIndex("Value");

		for (int row = 0; row < masterlistsEnums.getRowsCount(); row++) {
			String enumId = masterlistsEnums.getString(row, enumIdIndex);
			String id = masterlistsEnums.getString(row, idIndex);
			int value = masterlistsEnums.getInt(row, valueIndex);

			if (!enumsByName.containsKey(enumId)) {
				enumsByName.put(enumId, new Hashtable<>());
			}

			enumsByName.get(enumId).put(id, value);
		}

		return enumsByName;
	}
}
