package com.etermax.spacehorse.core.catalog.model.csv.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class Field {

	private String name;
	private FieldDataType dataType;

	public String getName() {
		return name;
	}

	public FieldDataType getDataType() {
		return dataType;
	}

	public Field(String name, FieldDataType dataType) {
		this.name = name;
		this.dataType = dataType;
	}

	public Object getDefaultValue() {

		switch (dataType.getBasicDataType()) {
			case String:
				return "";

			case StringArray:
				return new ArrayList<String>();

			case Int:
				return 0;

			case IntArray:
				return new ArrayList<Integer>();

			case Bool:
				return false;

			case Fint:
				return Fint.zero;

			case Enum:
				return dataType.getLowestEnumValue();

			case EnumArray:
				return new ArrayList<Integer>();

			default:
				throw new ApiException("Unknown basic field data type: " + dataType.getBasicDataType());
		}
	}

	static private final String ArraySeparator = Pattern.quote(",");

	static private String[] splitStringArray(String str) {

		if (str.length() == 0) {
			return new String[0];
		}

		return str.split(ArraySeparator);
	}

	public Object validateAndConvertData(Object data) {
		switch (dataType.getBasicDataType()) {

			case String:
				if (data instanceof String) {
					return data;
				} else {
					throw new ApiException("Invalid string value: " + data);
				}

			case StringArray:
				if (data instanceof ArrayList) {
					return data;
				} else if (data instanceof String) {
					String str = (String) data;
					String[] strs = splitStringArray(str);
					return new ArrayList<>(Arrays.asList(strs));
				} else {
					throw new ApiException("Invalid string value: " + data);
				}

			case Int:
				if (data instanceof String) {
					return Integer.parseInt((String) data);
				} else if (data instanceof Integer) {
					return data;
				} else {
					throw new ApiException("Invalid int value: " + data);
				}

			case IntArray:
				if (data instanceof ArrayList) {
					return data;
				} else if (data instanceof String) {
					String str = (String) data;
					String[] strs = splitStringArray(str);
					Integer[] ints = new Integer[strs.length];
					for (int i = 0; i < strs.length; i++)
						ints[i] = Integer.parseInt(strs[i]);
					return new ArrayList<>(Arrays.asList(ints));
				} else {
					throw new ApiException("Invalid string value: " + data);
				}

			case Bool:
				if (data instanceof String) {
					return ((String) data).toLowerCase().trim().equals("true");
				} else if (data instanceof Integer) {
					return (int) data != 0;
				} else {
					throw new ApiException("Invalid bool value: " + data);
				}

			case Fint:
				if (data instanceof String) {
					return Fint.createFromString((String) data);
				} else if (data instanceof Integer) {
					return Fint.createFromInt((int) data);
				} else {
					throw new ApiException("Invalid FintResponse value: " + data);
				}

			case Enum:
				if (data instanceof String && dataType.getEnumDictionary().containsKey(data)) {
					return dataType.getEnumDictionary().get(data);
				} else if (data instanceof Integer) {
					return data;
				} else {
					throw new ApiException("Invalid enum value: " + data);
				}

			case EnumArray:
				if (data instanceof ArrayList) {
					return data;
				} else if (data instanceof String) {
					String str = (String) data;
					String[] strs = splitStringArray(str);
					Integer[] ints = new Integer[strs.length];
					for (int i = 0; i < strs.length; i++) {
						if (dataType.getEnumDictionary().containsKey(strs[i])) {
							ints[i] = dataType.getEnumDictionary().get(strs[i]);
						} else {
							throw new ApiException("Invalid enum value: " + strs[i]);
						}
					}
					return new ArrayList<>(Arrays.asList(ints));
				} else {
					throw new ApiException("Invalid string value: " + data);
				}

			default:
				throw new ApiException("Unknown basic field data type: " + dataType.getBasicDataType());
		}
	}

}
