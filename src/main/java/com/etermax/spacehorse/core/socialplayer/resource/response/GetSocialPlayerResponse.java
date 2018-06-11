package com.etermax.spacehorse.core.socialplayer.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetSocialPlayerResponse {
	@JsonProperty("socialPlayer")
	public SocialPlayerResponse socialPlayer;

	public GetSocialPlayerResponse(SocialPlayerResponse socialPlayer) {
		this.socialPlayer = socialPlayer;
	}

	public SocialPlayerResponse getSocialPlayer() {
		return socialPlayer;
	}
}
