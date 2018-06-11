package com.etermax.spacehorse.core.login.resource.response.eventsnotification;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.specialoffer.model.NextSpecialOffer;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScheduledNotificationEventsResponse {

	@JsonProperty("specialOfferNotificationEvents")
	private List<NotificationEventResponse> specialOfferNotificationEvents;

	public ScheduledNotificationEventsResponse(List<NextSpecialOffer> nextSpecialOffers) {
		specialOfferNotificationEvents = nextSpecialOffers.stream().map(toSpecialOfferNotificationEvents()).collect(Collectors.toList());
	}

	private Function<NextSpecialOffer, NotificationEventResponse> toSpecialOfferNotificationEvents() {
		return nextSpecialOffer -> new NotificationEventResponse(nextSpecialOffer.getId(),
				nextSpecialOffer.getNextRefreshTimeInSeconds());
	}

	public List<NotificationEventResponse> getSpecialOfferNotificationEvents() {
		return specialOfferNotificationEvents;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
