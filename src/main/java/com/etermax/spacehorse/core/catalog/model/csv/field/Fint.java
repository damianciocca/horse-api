package com.etermax.spacehorse.core.catalog.model.csv.field;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class Fint {
	private long raw;

	static public final Fint zero = new Fint(0);
	static public final int SHIFT_AMOUNT = 12; //12 is 4096

	private final long rawOne = 1 << SHIFT_AMOUNT;

	public long getRaw() {
		return raw;
	}

	public Fint(long raw) {
		this.raw = raw;
	}

	public static Fint createFromString(String strValue) {
		long intPart = 0;

		long decPart = 0;
		long decDivisor = 1;

		boolean inDecimals = false;
		boolean negative = false;

		int decimals = 0;
		int maxDecimals = 5;

		for (int i = 0; i < strValue.length(); i++) {
			char c = strValue.charAt(i);
			switch (c) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					if (inDecimals) {
						if (decimals < maxDecimals) {
							long newDecPart = (decPart * 10L) + (long) (c - '0');
							if (newDecPart < decPart) {
								throw new ApiException("Invalid FintResponse format (overflow): " + strValue);
							}
							decPart = newDecPart;
							decDivisor *= 10;
							decimals++;
						}
					} else {
						long newIntPart = (intPart * 10L) + (long) (c - '0');
						if (newIntPart < intPart) {
							throw new ApiException("Invalid FintResponse format (overflow): " + strValue);
						}
						intPart = newIntPart;
					}
					break;

				case '-':
					if (i == 0) {
						negative = true;
					} else {
						throw new ApiException("Invalid FintResponse format: " + strValue);
					}
					break;

				case '+':
					if (i == 0) {
						negative = false;
					} else {
						throw new ApiException("Invalid FintResponse format: " + strValue);
					}
					break;

				case '.':
					if (!inDecimals) {
						inDecimals = true;
					} else {
						throw new ApiException("Invalid FintResponse format: " + strValue);
					}
					break;
			}
		}

		Fint fInt = new Fint(0);
		fInt.raw = intPart;
		fInt.raw = fInt.raw << SHIFT_AMOUNT;
		fInt.raw =
				fInt.raw | ((decPart * (1L << SHIFT_AMOUNT)) / decDivisor); //This is magic, we spent 2 hs with mariano coming up with this solution
		if (negative) {
			fInt.raw = -fInt.raw;
		}

		return fInt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Fint fint = (Fint) o;

		return raw == fint.raw;
	}

	@Override
	public int hashCode() {
		return (int) (raw ^ (raw >>> 32));
	}

	public static Fint createFromInt(int intValue) {
		Fint fInt = new Fint(0);
		fInt.raw = intValue;
		fInt.raw = fInt.raw << SHIFT_AMOUNT;
		return fInt;
	}

	public static Fint createFromFraction(int numerator, int denominator) {
		return Fint.div(Fint.createFromInt(numerator), Fint.createFromInt(denominator));
	}

	public static Fint div(Fint one, Fint other) {
		return new Fint((one.raw << SHIFT_AMOUNT) / (other.raw));
	}

	public float toFloat() {
		return (float)this.raw / (float) rawOne;
	}
}
