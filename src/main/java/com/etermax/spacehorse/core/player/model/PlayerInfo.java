package com.etermax.spacehorse.core.player.model;


public class PlayerInfo {

	private final String userId;

	private final String userName;

	public PlayerInfo(String userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}
}
