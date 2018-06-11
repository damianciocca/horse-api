package com.etermax.spacehorse.core.user.model;

import org.apache.commons.codec.digest.DigestUtils;

public class SHA256Digester {

	public static final String SALT = "c9254f7f8fc9a2316406289af8acc2c542d653b1198bc8e998f28bd75f1d2b37";

	public static String digest(String password) {
		return DigestUtils.sha256Hex(password + SALT);
	}

}
