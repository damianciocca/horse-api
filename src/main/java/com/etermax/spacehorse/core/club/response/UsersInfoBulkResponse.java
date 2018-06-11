package com.etermax.spacehorse.core.club.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsersInfoBulkResponse {
	@JsonProperty("users")
	private List<PlayerInfoResponse> users;

	public UsersInfoBulkResponse(List<PlayerInfoResponse> users) {
		this.users = users;
	}

	public List<PlayerInfoResponse> getUsers() {
		return users;
	}
}
