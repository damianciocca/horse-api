package com.etermax.spacehorse.core.authenticator.model;

public class Credentials {

	private String loginId;

	private String sessionToken;

	public Credentials(String loginId, String sessionToken) {
		this.loginId = loginId;
		this.sessionToken = sessionToken;
	}

	public String getLoginId() {
		return loginId;
	}

	public String getSessionToken() {
		return sessionToken;
	}

}
