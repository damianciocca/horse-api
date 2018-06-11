package com.etermax.spacehorse.core.abtest.model;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Optional;

public class ABTag {
	private static final String EMPTY_CAMPAIGN_ID = "";
	private static final String EMPTY_SEGMENT_ID = "";

	private String campaignId;
	private String segmentId;

	public ABTag(String abTag) {
		if (isBlank(abTag)) {
			this.campaignId=EMPTY_CAMPAIGN_ID;
			this.segmentId=EMPTY_SEGMENT_ID;
		} else {
			String[] splitedTag = abTag.split("-");
			this.campaignId = splitedTag[0];
			this.segmentId = splitedTag[1];
		}
	}

	public ABTag(String campaignId, String segmentId) {
		this.campaignId = campaignId;
		this.segmentId = segmentId;
	}

	private ABTag() {
		campaignId = EMPTY_CAMPAIGN_ID;
		segmentId = EMPTY_SEGMENT_ID;
	}

	public static ABTag buildFromPostulation(Optional<String> rawABTag) {
		return rawABTag.map(rawTagWithGameId -> {
			String[] splitedTag = rawTagWithGameId.split("-");
			String campaignId = splitedTag[1];
			String segmentId = splitedTag[2];
			return new ABTag(campaignId, segmentId);
		}).orElse(ABTag.emptyABTag());
	}

	public static ABTag emptyABTag() {
		return new ABTag();
	}

	@Override
	public String toString() {
		if (isEmptyABTag()) {
			return "";
		} else {
			return campaignId + "-" + segmentId;
		}
	}

	public boolean isEmptyABTag() {
		return isBlank(this.campaignId) || isBlank(this.segmentId);
	}

	public String getSegmentId() {
		return this.segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ABTag abTag = (ABTag) o;

		if (campaignId != null ? !campaignId.equals(abTag.campaignId) : abTag.campaignId != null) {
			return false;
		}
		return segmentId != null ? segmentId.equals(abTag.segmentId) : abTag.segmentId == null;
	}

	@Override
	public int hashCode() {
		int result = campaignId != null ? campaignId.hashCode() : 0;
		result = 31 * result + (segmentId != null ? segmentId.hashCode() : 0);
		return result;
	}
}
