package com.etermax.spacehorse.core.login.resource.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.login.resource.response.eventsnotification.ScheduledNotificationEventsResponse;
import com.etermax.spacehorse.core.player.resource.response.player.PlayerResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

	@JsonProperty("serverStatus")
	private ServerStatusResponse serverStatus = null;

	@JsonProperty("loginId")
	private String loginId = null;

	@JsonProperty("password")
	private String password = null;

	@JsonProperty("sessionToken")
	private String sessionToken = null;

	@JsonProperty("userRole")
	private Role userRole = null;

	@JsonProperty("catalogId")
	private String catalogId = null;

	@JsonProperty("player")
	private PlayerResponse player;

	@JsonProperty("serverTime")
	private Long serverTime;

	@JsonProperty("mmr")
	private Integer mmr;

	@JsonProperty("matchmakingServerUrl")
	private String matchmakingServerUrl;

	@JsonProperty("linkedWithSocialAccountId")
	private String linkedWithSocialAccountId;

	@JsonProperty("scheduledNotificationEvents")
	private ScheduledNotificationEventsResponse scheduledNotificationEvents;

	public LoginResponse(ServerStatusResponse serverStatus) {
		this.serverStatus = serverStatus;
	}

	public LoginResponse(String loginId, String password, String sessionToken, Role userRole, String catalogId, PlayerResponse player, Integer mmr,
			String matchmakingServerUrl, long serverTimeInSeconds, String linkedWithSocialAccountId,
			ScheduledNotificationEventsResponse scheduledNotificationEvents) {
		this.loginId = loginId;
		this.password = password;
		this.sessionToken = sessionToken;
		this.userRole = userRole;
		this.catalogId = catalogId;
		this.player = player;
		this.matchmakingServerUrl = matchmakingServerUrl;
		this.serverTime = serverTimeInSeconds;
		this.mmr = mmr;
		this.linkedWithSocialAccountId = linkedWithSocialAccountId;
		this.scheduledNotificationEvents = scheduledNotificationEvents;
	}

	public String getLoginId() {
		return loginId;
	}

	public String getPassword() {
		return password;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public Role getUserRole() {
		return userRole;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public Long getServerTime() {
		return serverTime;
	}

	public PlayerResponse getPlayer() {
		return player;
	}

	public Integer getMmr() {
		return mmr;
	}

	public String getLinkedWithSocialAccountId() {
		return linkedWithSocialAccountId;
	}

	public ServerStatusResponse getServerStatus() {
		return serverStatus;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

	public String getMatchmakingServerUrl() {
		return matchmakingServerUrl;
	}

	public ScheduledNotificationEventsResponse getScheduledNotificationEvents() {
		return scheduledNotificationEvents;
	}
}
