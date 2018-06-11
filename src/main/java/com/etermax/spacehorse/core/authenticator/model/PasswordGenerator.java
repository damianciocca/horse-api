package com.etermax.spacehorse.core.authenticator.model;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PasswordGenerator {

	private static final int NUM_BITS = 130;

	private static final int RADIX = 32;

	public static String generate() {
		return new BigInteger(NUM_BITS, new SecureRandom()).toString(RADIX);
	}

}
