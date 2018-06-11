package com.etermax.spacehorse.core.login.resource.response.eventsnotification;

import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationEventResponse {

	@JsonProperty("id")
	private String id;

	@JsonProperty("scheduledTriggerTimeInSeconds")
	private long scheduledTriggerTimeInSeconds;

	public NotificationEventResponse(String id, long scheduledTriggerTimeInSeconds) {
		this.id = id;
		this.scheduledTriggerTimeInSeconds = scheduledTriggerTimeInSeconds;
	}

	@Override
	public String toString() {
		return "NotificationEventResponse{" + "ID =" + id + " scheduledTriggerTimeInSeconds =" + ServerTime.toDateTime(scheduledTriggerTimeInSeconds);
	}
}
