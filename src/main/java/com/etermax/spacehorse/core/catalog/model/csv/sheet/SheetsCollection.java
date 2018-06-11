package com.etermax.spacehorse.core.catalog.model.csv.sheet;

import java.util.ArrayList;
import java.util.Hashtable;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class SheetsCollection {
	private Hashtable<String, Sheet> sheetsByName = new Hashtable<>();

	public SheetsCollection() {

	}

	public void add(Sheet sheet) {
		sheetsByName.put(sheet.getName(), sheet);
	}

	public Sheet getSheet(String name) {
		if (sheetsByName.containsKey(name)) {
			return sheetsByName.get(name);
		}

		throw new ApiException("Sheet not found: " + name);
	}

	public ArrayList<Sheet> getSheetsList() {
		return new ArrayList<>(sheetsByName.values());
	}
}
