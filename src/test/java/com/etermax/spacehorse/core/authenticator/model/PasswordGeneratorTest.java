package com.etermax.spacehorse.core.authenticator.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordGeneratorTest {

	@Test
	public void mustGenerateDifferentPasswords(){
		assertNotEquals(PasswordGenerator.generate(), PasswordGenerator.generate());
	}

}
