package com.etermax.spacehorse.core.achievements.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.model.Achievement.AchievementState;
import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.etermax.spacehorse.mock.AchievementScenarioBuilder;
import com.etermax.spacehorse.mock.MockCatalog;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.google.common.collect.Maps;

public class DynamoAchievementCollectionRepositoryTest {

	private static final String USER_ID = "10";
	private static final String OTHER_USER_ID = "100";
	private static final String ACHIEVEMENT_PURCHASE_CAPTAIN_ID = "achievement_PurchaseCaptain";
	private static final String ACHIEVEMENT_PLAY_BATTLE_ID = "achievement_PlayBattle";
	private static final String ACHIEVEMENT_UNLOCK_CARD_ID = "achievement_UnlockCard";
	private static final String ACHIEVEMENT_PLAY_BATTLE_PRACTICE_ID = "achievement_PlayBattlePractice";

	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();
	private DynamoAchievementCollectionRepository repository;
	private Player player;
	private Player otherPlayer;

	@Before
	public void setUp() {
		RULE.createSimpleTable(DynamoAchievementCollection.class);
		DynamoDao dao = new DynamoDao(RULE.getAmazonDynamoDB());
		CatalogRepository catalogRepository = aCatalogRepository();
		player = new PlayerScenarioBuilder(USER_ID).build();
		otherPlayer = new PlayerScenarioBuilder(OTHER_USER_ID).build();
		repository = new DynamoAchievementCollectionRepository(dao, catalogRepository);
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	@Test
	public void whenFindAnAchievementCollectionForNewPlayerThenADefaultAchievementsShouldBeCreated() {
		// when
		AchievementCollection achievementCollection = repository.findOrDefaultBy(player);
		// then
		assertThat(achievementCollection).isNotNull();
		thenAssertThatAchievementCollectionHasAlwaysTwoAchievementsInValidStates(achievementCollection, AchievementState.INITIAL,
				AchievementState.INITIAL, AchievementState.INITIAL, AchievementState.INITIAL);
	}

	@Test
	public void whenAddOrUpdateOneAchievementThenTheAchievementCollectionsShouldBeUpdated() {
		//given
		AchievementCollection achievementCollection = givenAnAchievementCollectionWithOneAchievementInStateInitial();
		// when
		repository.addOrUpdate(player.getUserId(), achievementCollection);
		// when - then
		achievementCollection = repository.findOrDefaultBy(player);
		assertThat(achievementCollection).isNotNull();
		thenAssertThatAchievementCollectionHasAlwaysTwoAchievementsInValidStates(achievementCollection, AchievementState.INITIAL,
				AchievementState.INITIAL, AchievementState.INITIAL, AchievementState.INITIAL); // the others achievements is retrieved from catalog.
	}

	@Test
	public void whenFindAnAchievementCollectionThenTheAchievementCollectionsShouldBeRetrievedSuccessfully() {
		//given
		givenAPersistedAchievementCollectionWithOneAchievementInStateInitialFor(player);
		givenAPersistedAchievementCollectionWithOneAchievementInStateReadyToByClaimFor(otherPlayer);
		// when
		AchievementCollection achievementCollection = repository.findOrDefaultBy(otherPlayer);
		// when - then
		assertThat(achievementCollection).isNotNull();
		thenAssertThatAchievementCollectionHasAlwaysTwoAchievementsInValidStates(achievementCollection, AchievementState.READY_TO_CLAIM,
				AchievementState.INITIAL, AchievementState.INITIAL, AchievementState.INITIAL);
	}

	private AchievementCollection givenAnAchievementCollectionWithOneAchievementInStateInitial() {
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		return new AchievementCollection(player.getUserId(), achievements);
	}

	private void givenAPersistedAchievementCollectionWithOneAchievementInStateInitialFor(Player player) {
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		repository.addOrUpdate(player.getUserId(), new AchievementCollection(this.player.getUserId(), achievements));
	}

	private void givenAPersistedAchievementCollectionWithOneAchievementInStateReadyToByClaimFor(Player player) {
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		repository.addOrUpdate(player.getUserId(), new AchievementCollection(this.otherPlayer.getUserId(), achievements));
		AchievementCollection achievementCollection = repository.findOrDefaultBy(this.otherPlayer);
		achievementCollection.achievementAccomplished(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		repository.addOrUpdate(this.otherPlayer.getUserId(), achievementCollection);
	}

	private Map<String, Achievement> givenOneAchievementInStateInitial() {
		Map<String, Achievement> achievements = Maps.newHashMap();
		Achievement achievement = new AchievementScenarioBuilder().buildDefaultAchievement();
		achievements.put(ACHIEVEMENT_PURCHASE_CAPTAIN_ID, achievement);
		return achievements;
	}

	private void thenAssertThatAchievementCollectionHasAlwaysTwoAchievementsInValidStates(AchievementCollection achievementCollection,
			AchievementState purchaseCaptainAchievementState, AchievementState playBattleAchievementState, AchievementState
			cardUnlockAchievementState, AchievementState playBattlePracticeAchievementState) {
		assertThat(achievementCollection.getAchievements()) //
				.extracting( //
						Achievement::getAchievementId, //
						Achievement::getStateAsTxt)//
				.containsOnly(//
						tuple(ACHIEVEMENT_PURCHASE_CAPTAIN_ID, purchaseCaptainAchievementState.getState()), //
						tuple(ACHIEVEMENT_PLAY_BATTLE_ID, playBattleAchievementState.getState()), //
						tuple(ACHIEVEMENT_UNLOCK_CARD_ID, cardUnlockAchievementState.getState()), //
						tuple(ACHIEVEMENT_PLAY_BATTLE_PRACTICE_ID, playBattlePracticeAchievementState.getState()));//
	}

	private CatalogRepository aCatalogRepository() {
		CatalogRepository catalogRepository = mock(CatalogRepository.class);
		Catalog catalog = MockCatalog.buildCatalog(); // The catalog have two achievements configured. So keep it in mind in order to find in repo
		when(catalogRepository.getActiveCatalogWithTag(any())).thenReturn(catalog);
		return catalogRepository;
	}

}
