package com.etermax.spacehorse.core.user.model;

import org.junit.Assert;
import org.junit.Test;

public class SHA256DigesterTest {

	public static final String PASSWORD = "1234";

	@Test
	public void mustBeEqualWhenDigestSamePassword(){
		Assert.assertEquals(SHA256Digester.digest(PASSWORD), SHA256Digester.digest(PASSWORD));
	}

}
