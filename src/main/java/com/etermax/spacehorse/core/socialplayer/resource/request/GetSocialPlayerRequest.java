package com.etermax.spacehorse.core.socialplayer.resource.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetSocialPlayerRequest {
	@JsonProperty("userId")
	private String userId;

	public GetSocialPlayerRequest(@JsonProperty("userId") String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
