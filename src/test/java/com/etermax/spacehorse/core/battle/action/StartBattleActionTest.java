package com.etermax.spacehorse.core.battle.action;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.DefaultBattleFactory;
import com.etermax.spacehorse.core.battle.repository.dynamo.BattleDynamoRepository;
import com.etermax.spacehorse.core.battle.repository.dynamo.DynamoBattle;
import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.etermax.spacehorse.mock.BattleScenarioBuilder;

public class StartBattleActionTest {

	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();
	private BattleDynamoRepository battleDynamoRepository;
	private StartBattleAction action;
	private FixedServerTimeProvider serverTimeProvider;

	@Before
	public void setUp() {
		RULE.createSimpleTable(DynamoBattle.class);
		battleDynamoRepository = new BattleDynamoRepository(new DynamoDao(RULE.getAmazonDynamoDB()), new DefaultBattleFactory());
		serverTimeProvider = new FixedServerTimeProvider();
		action = new StartBattleAction(battleDynamoRepository, serverTimeProvider);
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	@Test
	public void whenStartANonStartedBattleThenTheBattleShouldBeStarted() throws Exception {

		Battle battleNotStarted = givenANotStartedBattle();

		boolean started = whenStartBattle(battleNotStarted);

		assertThat(started).isTrue();
		thenAssertThatBattleIsStarted(battleNotStarted);
	}

	@Test
	public void whenStartAnAlreadyStartedBattleThenTheBattleShouldNoBeStarted() throws Exception {

		Battle battleStarted = givenAAlreadyStartedBattle();

		boolean started = whenStartBattle(battleStarted);

		assertThat(started).isFalse();
		thenAssertThatBattleIsStarted(battleStarted);
	}

	private Battle givenAAlreadyStartedBattle() {
		Battle battle = givenANotStartedBattle();
		action.startBattle(battle.getBattleId());
		return battle;
	}

	private boolean whenStartBattle(Battle battleNotStarted) {
		return action.startBattle(battleNotStarted.getBattleId());
	}

	private void thenAssertThatBattleIsStarted(Battle battle) {
		Optional<Battle> battleOptional = battleDynamoRepository.find(battle.getBattleId());
		assertThat(battleOptional).isPresent();
		assertThat(battleOptional.get().getStarted()).isTrue();
		assertThat(battleOptional.get().getStartedAt()).isEqualTo(serverTimeProvider.getDate());
	}

	private Battle givenANotStartedBattle() {
		Battle battle = new BattleScenarioBuilder(serverTimeProvider).build();
		return battleDynamoRepository.add(battle);
	}
}
