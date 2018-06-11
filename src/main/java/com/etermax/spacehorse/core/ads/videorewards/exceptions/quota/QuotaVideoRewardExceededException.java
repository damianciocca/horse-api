package com.etermax.spacehorse.core.ads.videorewards.exceptions.quota;

import static java.lang.String.format;

import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.common.exception.ApiException;

public class QuotaVideoRewardExceededException extends ApiException {

	public QuotaVideoRewardExceededException(QuotaVideoReward quotaVideoReward) {
		super(format("The quota of video rewards was exceeded for user id [ %s ] and placeName [ %s ]. Actual [ %s ]", quotaVideoReward.getUserId(),
				quotaVideoReward.getPlaceName(), quotaVideoReward));
	}
}
