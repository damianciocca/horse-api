package com.etermax.spacehorse.core.specialoffer.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.etermax.spacehorse.core.servertime.model.ServerTime;

public class SpecialOfferHistory {

	public static final String EMPTY_GROUP_ID = "";
	private final String specialOfferId;
	private final String groupId;
	private final DateTime creationTime;

	public SpecialOfferHistory(String specialOfferId, String groupId, DateTime creationTime) {
		this.specialOfferId = specialOfferId;
		this.groupId = groupId;
		this.creationTime = creationTime;
	}

	public static SpecialOfferHistory restore(String specialOfferId, String groupId, long creationTimeInSeconds) {
		if (groupId == null) {
			groupId = EMPTY_GROUP_ID;
		}
		return new SpecialOfferHistory(specialOfferId, groupId, ServerTime.toDateTime(creationTimeInSeconds));
	}

	public String getSpecialOfferId() {
		return specialOfferId;
	}

	public String getGroupId() {
		return groupId;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
