package com.etermax.spacehorse.core.ads.videorewards.repository.quota;

import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardFactory;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.repository.DynamoQuestBoard;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class DynamoQuotaVideoRewardRepository implements QuotaVideoRewardRepository {

	private final DynamoDao<DynamoQuestBoard> dynamoDao;
	private final ServerTimeProvider timeProvider;
	private final QuotaVideoRewardFactory quotaVideoRewardFactory;

	public DynamoQuotaVideoRewardRepository(DynamoDao dynamoDao, ServerTimeProvider timeProvider) {
		this.dynamoDao = dynamoDao;
		this.timeProvider = timeProvider;
		this.quotaVideoRewardFactory = new QuotaVideoRewardFactory(timeProvider);
	}

	@Override
	public QuotaVideoReward findOrDefaultBy(Player player, String placeName) {
		DynamoQuotaVideoReward entity = new DynamoQuotaVideoReward();
		entity.setUserId(player.getUserId());
		entity.setPlaceName(placeName);

		DynamoQuotaVideoReward dynamoQuotaVideoReward = (DynamoQuotaVideoReward) dynamoDao.find(entity);
		return findOrCreate(player.getUserId(), dynamoQuotaVideoReward, placeName);
	}

	@Override
	public void addOrUpdate(QuotaVideoReward quotaVideoReward) {
		DynamoQuotaVideoReward dynamoQuotaVideoReward = DynamoQuotaVideoReward
				.toDynamoQuotaVideReward(quotaVideoReward.getUserId(), quotaVideoReward);
		dynamoDao.add(dynamoQuotaVideoReward);

	}

	private QuotaVideoReward findOrCreate(String userId, DynamoQuotaVideoReward dynamoQuotaVideoReward, String placeName) {
		if (notExists(dynamoQuotaVideoReward)) {
			QuotaVideoReward quotaVideoReward = quotaVideoRewardFactory.create(userId, placeName);
			addOrUpdate(quotaVideoReward);
			return quotaVideoReward;
		}
		return DynamoQuotaVideoReward.toQuotaVideoReward(dynamoQuotaVideoReward, timeProvider);
	}

	private boolean notExists(DynamoQuotaVideoReward dynamoQuestBoard) {
		return dynamoQuestBoard == null;
	}

}
