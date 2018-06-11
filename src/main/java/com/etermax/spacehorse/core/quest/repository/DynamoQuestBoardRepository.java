package com.etermax.spacehorse.core.quest.repository;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class DynamoQuestBoardRepository implements QuestBoardRepository {

	private final DynamoDao<DynamoQuestBoard> dynamoDao;
	private final ServerTimeProvider timeProvider;
	private final CatalogRepository catalogRepository;

	public DynamoQuestBoardRepository(DynamoDao dynamoDao, ServerTimeProvider timeProvider, CatalogRepository catalogRepository) {
		this.dynamoDao = dynamoDao;
		this.timeProvider = timeProvider;
		this.catalogRepository = catalogRepository;
	}

	@Override
	public QuestBoard findOrDefaultBy(Player player) {
		DynamoQuestBoard dynamoQuestBoard = dynamoDao.find(DynamoQuestBoard.class, player.getUserId());
		Catalog catalog = getActiveCatalog(player.getAbTag());
		QuestBoard questBoard = findOrCreate(player.getUserId(), dynamoQuestBoard, catalog);
		return questBoard;
	}

	@Override
	public void addOrUpdate(String userId, QuestBoard questBoard) {
		DynamoQuestBoard dynamoQuestBoard = new DynamoQuestBoard(userId, questBoard);
		dynamoDao.add(dynamoQuestBoard);
	}

	private QuestBoardConfiguration createQuestBoardConfigurationFrom(Catalog catalog) {
		GameConstants gameConstants = catalog.getGameConstants();
		return new QuestBoardConfiguration(catalog.getGameConstants().getSkipTimeForQuestBoard(), gameConstants.getGemsCostToSkipQuest(),
				gameConstants.getNextQuestRemainingTimeDividerFactor(), gameConstants.getNextQuestRemainingTimeGemsCostFactor());
	}

	private QuestBoard findOrCreate(String userId, DynamoQuestBoard dynamoQuestBoard, Catalog catalog) {
		QuestBoardConfiguration configuration = createQuestBoardConfigurationFrom(catalog);
		if (ifNotExists(dynamoQuestBoard)) {
			Quest dailyQuest = new Quest();
			QuestBoard newQuestBoard = new QuestBoard(timeProvider, configuration, dailyQuest);
			addOrUpdate(userId, newQuestBoard);
			return newQuestBoard;
		}
		QuestBoard questBoard = dynamoQuestBoard.toQuestBoard(timeProvider, configuration);
		questBoard.onQuestDefinitionChanged(catalog.getQuestCollection(), catalog.getDailyQuestCollection());
		return questBoard;
	}

	private Catalog getActiveCatalog(ABTag abTag) {
		return catalogRepository.getActiveCatalogWithTag(abTag);
	}

	private boolean ifNotExists(DynamoQuestBoard dynamoQuestBoard) {
		return dynamoQuestBoard == null;
	}
}
