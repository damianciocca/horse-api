package com.etermax.spacehorse.integration;

import com.etermax.spacehorse.app.SpaceHorseApplication;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class SpaceHorseApplicationTest extends SpaceHorseApplication {

	public CatalogRepository getCatalogRepository() {
		return horseRaceRepositories.getCatalogRepository();
	}

	public PlayerRepository getPlayerRepository() {
		return horseRaceRepositories.getPlayerRepository();
	}

	@Override
	protected ServerTimeProvider createServerTimeProvider() {
		return new FixedServerTimeProvider();
	}

	@Override
	protected ServerTimeProvider getServerTimeProvider() {
		return super.getServerTimeProvider();
	}

	public QuestBoardRepository getQuestBoardRepository() {
		return horseRaceRepositories.getQuestBoardRepository();
	}

	public CaptainCollectionRepository getCaptainsCollectionRepository() {
		return horseRaceRepositories.getCaptainCollectionRepository();
	}

	public AchievementCollectionRepository getAchievementCollectionRepository() {
		return horseRaceRepositories.getAchievementCollectionRepository();
	}
}
