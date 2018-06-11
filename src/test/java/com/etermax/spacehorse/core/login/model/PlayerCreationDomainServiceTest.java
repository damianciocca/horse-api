package com.etermax.spacehorse.core.login.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionFactory;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.achievements.repository.InMemoryAchievementCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.InMemorySpecialOfferBoardRepository;
import com.etermax.spacehorse.mock.MockUtils;

public class PlayerCreationDomainServiceTest {

	private static final String PLAYER_ID = "22";
	private PlayerRepository playerRepository;
	private ApplyRewardDomainService applyRewardDomainService;
	private Catalog catalog;
	private QuestBoardRepository questBoardRepository;
	private FixedServerTimeProvider timeProvider;
	private CaptainCollectionRepository captainCollectionRepository;
	private AchievementsFactory achievementsFactory;

	@Before
	public void setUp() {
		catalog = MockUtils.mockCatalog();
		playerRepository = mock(PlayerRepository.class);
		applyRewardDomainService = mock(ApplyRewardDomainService.class);
		questBoardRepository = mock(QuestBoardRepository.class);
		timeProvider = new FixedServerTimeProvider();
		captainCollectionRepository = mock(CaptainCollectionRepository.class);
		achievementsFactory = mock(AchievementsFactory.class);

	}

	@Test
	public void whenCreateNewPlayerTheThePlayerShouldBeCreatedSuccessfully() {
		// given
		long timeNowInSeconds = 100L;
		PlayerCreationDomainService playerCreationDomainService = new PlayerCreationDomainService(playerRepository, applyRewardDomainService,
				questBoardRepository, timeProvider, new InMemorySpecialOfferBoardRepository(timeProvider), captainCollectionRepository,
				new InMemoryAchievementCollectionRepository(), new AchievementCollectionFactory(), achievementsFactory);
		// when
		Player player = whenCreateNewPlayer(timeNowInSeconds, playerCreationDomainService);
		// then
		thenThePlayerWasSuccessfullyCreated(player);
	}

	private void thenThePlayerWasSuccessfullyCreated(Player player) {
		assertThat(player.getCatalogId().concat("-")).isEqualTo(catalog.getCatalogId().concat("-"));
	}

	private Player whenCreateNewPlayer(long timeNowInSeconds, PlayerCreationDomainService playerCreationDomainService) {
		return playerCreationDomainService.createPlayer(PLAYER_ID, ABTag.emptyABTag(), catalog, timeNowInSeconds);
	}

}
