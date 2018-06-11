package com.etermax.spacehorse.core.ads.videorewards.quota.repository;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardFactory;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.google.common.collect.Maps;

public class InMemoryQuotaVideoRewardRepository implements QuotaVideoRewardRepository {

	private final QuotaVideoRewardFactory quotaVideoRewardFactory;

	private Map<QuotaVideoRewardKey, QuotaVideoReward> quotaByPlayers = Maps.newHashMap();

	public InMemoryQuotaVideoRewardRepository(ServerTimeProvider timeProvider) {
		this.quotaVideoRewardFactory = new QuotaVideoRewardFactory(timeProvider);
	}

	@Override
	public QuotaVideoReward findOrDefaultBy(Player player, String placeName) {
		QuotaVideoRewardKey key = new QuotaVideoRewardKey(player.getUserId(), placeName);
		if (existQuotaFor(key)) {
			return quotaByPlayers.get(key);
		}
		QuotaVideoReward quotaVideoReward = quotaVideoRewardFactory.create(player.getUserId(), placeName);
		addOrUpdate(quotaVideoReward);
		return quotaVideoReward;
	}

	@Override
	public void addOrUpdate(QuotaVideoReward quotaVideoReward) {
		QuotaVideoRewardKey key = new QuotaVideoRewardKey(quotaVideoReward.getUserId(), quotaVideoReward.getPlaceName());
		quotaByPlayers.put(key, quotaVideoReward);
	}

	private boolean existQuotaFor(QuotaVideoRewardKey quotaVideoRewardKey) {
		return quotaByPlayers.containsKey(quotaVideoRewardKey);
	}

	private class QuotaVideoRewardKey {
		private final String userId;
		private final String placeName;

		private QuotaVideoRewardKey(String userId, String placeName) {
			this.userId = userId;
			this.placeName = placeName;
		}

		public String getUserId() {
			return userId;
		}

		public String getPlaceName() {
			return placeName;
		}

		@Override
		public boolean equals(Object other) {
			return EqualsBuilder.reflectionEquals(this, other);
		}

		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}
	}
}
