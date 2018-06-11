package com.etermax.spacehorse.core.catalog.model.csv.field;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class BasicFieldDataTypeUtils {

	static public final int VAL_STRING = 0;
	static public final int VAL_STRING_ARRAY = 1;
	static public final int VAL_INT = 2;
	static public final int VAL_INT_ARRAY = 3;
	static public final int VAL_BOOL = 4;
	static public final int VAL_FINT = 5;
	static public final int VAL_ENUM = 6;
	static public final int VAL_ENUM_ARRAY = 7;

	static public BasicFieldDataType fromInt(int n) {
		switch (n) {
			case VAL_STRING:
				return BasicFieldDataType.String;

			case VAL_STRING_ARRAY:
				return BasicFieldDataType.StringArray;

			case VAL_INT:
				return BasicFieldDataType.Int;

			case VAL_INT_ARRAY:
				return BasicFieldDataType.IntArray;

			case VAL_BOOL:
				return BasicFieldDataType.Bool;

			case VAL_FINT:
				return BasicFieldDataType.Fint;

			case VAL_ENUM:
				return BasicFieldDataType.Enum;

			case VAL_ENUM_ARRAY:
				return BasicFieldDataType.EnumArray;
		}

		throw new ApiException("Unknown data type with number " + n);
	}
}
