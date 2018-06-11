package com.etermax.spacehorse.core.catalog.model.csv;

import java.util.ArrayList;

import com.etermax.spacehorse.core.catalog.model.csv.field.BasicFieldDataType;
import com.etermax.spacehorse.core.catalog.model.csv.field.Field;
import com.etermax.spacehorse.core.catalog.model.csv.field.Fint;
import com.etermax.spacehorse.core.common.exception.ApiException;

public class CatalogCSV {

	private String name = "";
	private ArrayList<Field> fields;
	private ArrayList<Object[]> rows = new ArrayList<>();

	public String getName() {
		return name;
	}

	public int getRowsCount() {
		return rows.size();
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public CatalogCSV(String name, ArrayList<Field> fields) {
		this.name = name;
		this.fields = fields;
	}

	public int getFieldIndex(String fieldName) {

		for (int i = 0; i < fields.size(); i++)
			if (fields.get(i).getName().equals(fieldName)) {
				return i;
			}

		throw new ApiException("Field " + fieldName + " not found in " + getName());
	}

	public Field getField(String fieldName) {
		return fields.get(getFieldIndex(fieldName));
	}

	public int addRow() {
		Object[] row = new Object[fields.size()];
		for (int i = 0; i < fields.size(); i++)
			row[i] = fields.get(i).getDefaultValue();
		rows.add(row);
		return rows.size() - 1;
	}

	public void setData(int row, int field, Object data) {
		try {
			data = fields.get(field).validateAndConvertData(data);

			rows.get(row)[field] = data;
		} catch (Exception ex) {
			throw new ApiException(
					"Error trying to setAmount field " + fields.get(field).getName() + " of row " + row + " of " + getName() + ": " + ex.getMessage(),
					ex);
		}
	}

	public Object getData(int row, int field) {
		return rows.get(row)[field];
	}

	public int getInt(int row, int field) {
		validateFieldType(field, BasicFieldDataType.Int);
		return (int) getData(row, field);
	}

	public ArrayList<Integer> getIntArray(int row, int field) {
		validateFieldType(field, BasicFieldDataType.IntArray);
		return (ArrayList<Integer>) getData(row, field);
	}

	public boolean getBool(int row, int field) {
		validateFieldType(field, BasicFieldDataType.Bool);
		return (boolean) getData(row, field);
	}

	public String getString(int row, int field) {
		validateFieldType(field, BasicFieldDataType.String);
		return (String) getData(row, field);
	}

	public ArrayList<String> getStringArray(int row, int field) {
		validateFieldType(field, BasicFieldDataType.StringArray);
		return (ArrayList<String>) getData(row, field);
	}

	public Fint getFint(int row, int field) {
		validateFieldType(field, BasicFieldDataType.Fint);
		return (Fint) getData(row, field);
	}

	public int getEnum(int row, int field) {
		validateFieldType(field, BasicFieldDataType.Enum);
		return (int) getData(row, field);
	}

	public ArrayList<Integer> getEnumArray(int row, int field) {
		validateFieldType(field, BasicFieldDataType.EnumArray);
		return (ArrayList<Integer>) getData(row, field);
	}

	private void validateFieldType(int field, BasicFieldDataType basicDataType) {
		if (fields.get(field).getDataType().getBasicDataType() != basicDataType) {
			throw new ApiException(
					"Requested field " + fields.get(field).getName() + " of " + getName() + " isn't a " + basicDataType + ", it's a " + fields
							.get(field).getDataType().getBasicDataType());
		}
	}
}
