package com.etermax.spacehorse.core.specialoffer.repository;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.exception.SpecialOfferBoardNotFoundExpception;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class DynamoSpecialOfferBoardRepository implements SpecialOfferBoardRepository {

	private final DynamoDao<DynamoSpecialOfferBoard> dynamoDao;
	private final ServerTimeProvider timeProvider;
	private final CatalogRepository catalogRepository;

	public DynamoSpecialOfferBoardRepository(DynamoDao dynamoDao, ServerTimeProvider timeProvider, CatalogRepository catalogRepository) {
		this.dynamoDao = dynamoDao;
		this.timeProvider = timeProvider;
		this.catalogRepository = catalogRepository;
	}

	@Override
	public SpecialOfferBoard findBy(Player player) {
		DynamoSpecialOfferBoard dynamoSpecialOfferBoard = findSpecialOfferBoardBy(player);
		if (notExists(dynamoSpecialOfferBoard)) {
			throw new SpecialOfferBoardNotFoundExpception(player.getUserId());
		}
		return DynamoSpecialOfferBoard.mapFromDynamoSpecialOfferBoard(dynamoSpecialOfferBoard, timeProvider, getSpecialOfferDefinitions(player));
	}

	@Override
	public SpecialOfferBoard findOrDefaultBy(Player player) {
		DynamoSpecialOfferBoard dynamoSpecialOfferBoard = findSpecialOfferBoardBy(player);
		if (notExists(dynamoSpecialOfferBoard)) {
			SpecialOfferBoard newSpecialOfferBoard = new SpecialOfferBoard(timeProvider);
			addOrUpdate(player, newSpecialOfferBoard);
			return newSpecialOfferBoard;
		}
		return DynamoSpecialOfferBoard.mapFromDynamoSpecialOfferBoard(dynamoSpecialOfferBoard, timeProvider, getSpecialOfferDefinitions(player));
	}

	@Override
	public void addOrUpdate(Player player, SpecialOfferBoard specialOfferBoard) {
		DynamoSpecialOfferBoard dynamoSpecialOfferBoard = DynamoSpecialOfferBoard.mapFromSpecialOfferBoard(player.getUserId(), specialOfferBoard);
		dynamoDao.add(dynamoSpecialOfferBoard);
	}

	private DynamoSpecialOfferBoard findSpecialOfferBoardBy(Player player) {
		return dynamoDao.find(DynamoSpecialOfferBoard.class, player.getUserId());
	}

	private CatalogEntriesCollection<SpecialOfferDefinition> getSpecialOfferDefinitions(Player player) {
		Catalog catalog = getActiveCatalog(player.getAbTag());
		return catalog.getSpecialOfferDefinitionsCollection();
	}

	private Catalog getActiveCatalog(ABTag abTag) {
		return catalogRepository.getActiveCatalogWithTag(abTag);
	}

	private boolean notExists(DynamoSpecialOfferBoard dynamoSpecialOfferBoard) {
		return dynamoSpecialOfferBoard == null;
	}
}
