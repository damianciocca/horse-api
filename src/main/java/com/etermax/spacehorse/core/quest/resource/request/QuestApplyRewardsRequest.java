package com.etermax.spacehorse.core.quest.resource.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestApplyRewardsRequest {

	@JsonProperty("slotId")
	private String slotId;

	public QuestApplyRewardsRequest(@JsonProperty("slotId") String slotId) {
		this.slotId = slotId;
	}

	public String getSlotId() {
		return slotId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
