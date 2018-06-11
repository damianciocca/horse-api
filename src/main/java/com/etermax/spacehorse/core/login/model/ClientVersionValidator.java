package com.etermax.spacehorse.core.login.model;

import com.etermax.spacehorse.core.login.error.InvalidClientException;

public class ClientVersionValidator {

	private final int expectedVersion;

	public ClientVersionValidator(int expectedVersion) {
		this.expectedVersion = expectedVersion;
	}

	public void validate(Integer clientVersion) {
		if (clientVersion < expectedVersion) {
			throw new InvalidClientException("Invalid client version, expected version " + expectedVersion);
		}
	}

}
