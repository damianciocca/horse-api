package com.etermax.spacehorse.core.catalog.model.csv.field;

import java.util.ArrayList;
import java.util.Hashtable;

public class FieldDataType {

	private BasicFieldDataType basicDataType;
	private Hashtable<String, Integer> enumDictionary;
	private int lowestEnumValue;

	public BasicFieldDataType getBasicDataType() {
		return basicDataType;
	}

	public Hashtable<String, Integer> getEnumDictionary() {
		return enumDictionary;
	}

	public int getLowestEnumValue() {
		return lowestEnumValue;
	}

	public FieldDataType(BasicFieldDataType basicDataType) {
		this.basicDataType = basicDataType;
	}

	public FieldDataType(Hashtable<String, Integer> enumDictionary, BasicFieldDataType basicDataType) {
		this.basicDataType = basicDataType;
		this.enumDictionary = enumDictionary;

		ArrayList<Integer> sortedEnumValues = new ArrayList<>(enumDictionary.values());
		sortedEnumValues.sort(null);
		lowestEnumValue = sortedEnumValues.get(0);
	}
}
