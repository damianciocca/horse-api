package com.etermax.spacehorse.core.battle.repository.dynamo;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.BattleResult;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.model.PlayerWinRateConfiguration;
import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class PlayerWinRateDynamoRepositoryTest {

	public static final String DEFAULT_ID = "id";
	private static final int CAPPED_MMR = 3000;

	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();
	private PlayerWinRateDynamoRepository repository;

	public PlayerWinRateDynamoRepositoryTest() {
		DynamoDao dao = new DynamoDao(RULE.getAmazonDynamoDB());
		this.repository = new PlayerWinRateDynamoRepository(dao);
	}

	@Before
	public void setUp() {
		RULE.createSimpleTable(DynamoPlayerWinRate.class);
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	@Test
	public void testUnableToFind() {
		PlayerWinRate playerWinRate1 = this.repository.findOrCrateDefault("unexistant");
		assertEquals(0, playerWinRate1.getLose());
		assertEquals(0, playerWinRate1.getTie());
		assertEquals(0, playerWinRate1.getWin());
	}

	@Test
	public void testAdd() {
		final String id = DEFAULT_ID;
		PlayerWinRate playerWinRate = new PlayerWinRate(id, 7, 3, 0);

		this.repository.add(playerWinRate);
		PlayerWinRate foundValue = this.repository.findOrCrateDefault(id);

		assertEquals(foundValue.getUserId(), playerWinRate.getUserId());
		assertEquals(foundValue.getLose(), playerWinRate.getLose());
		assertEquals(foundValue.getTie(), playerWinRate.getTie());
		assertEquals(foundValue.getWin(), playerWinRate.getWin());
	}

	@Test
	public void testUpdate() {
		final String id = DEFAULT_ID;
		this.repository.updateScore(id, BattleResult.WIN);

		PlayerWinRate foundValue = this.repository.findOrCrateDefault(id);

		assertEquals(foundValue.getUserId(), id);
		assertEquals(foundValue.getLose(), 0);
		assertEquals(foundValue.getTie(), 0);
		assertEquals(foundValue.getWin(), 1);
	}

	@Test
	public void testUpdateWin() {
		final String id = DEFAULT_ID;
		PlayerWinRate playerWinRate = new PlayerWinRate(id, 7, 3, 0);

		this.repository.add(playerWinRate);
		this.repository.updateScore(id, BattleResult.WIN);
		PlayerWinRate foundValue = this.repository.findOrCrateDefault(id);

		assertEquals(foundValue.getUserId(), playerWinRate.getUserId());
		assertEquals(foundValue.getLose(), playerWinRate.getLose());
		assertEquals(foundValue.getTie(), playerWinRate.getTie());
		assertEquals(foundValue.getWin(), playerWinRate.getWin() + 1);
	}

	@Test
	public void testUpdateLose() {
		final String id = DEFAULT_ID;
		PlayerWinRate playerWinRate = new PlayerWinRate(id, 7, 3, 0);

		this.repository.add(playerWinRate);
		this.repository.updateScore(id, BattleResult.LOSE);
		PlayerWinRate foundValue = this.repository.findOrCrateDefault(id);

		assertEquals(foundValue.getUserId(), playerWinRate.getUserId());
		assertEquals(foundValue.getLose(), playerWinRate.getLose() + 1);
		assertEquals(foundValue.getTie(), playerWinRate.getTie());
		assertEquals(foundValue.getWin(), playerWinRate.getWin());
	}

	@Test
	public void testUpdateTie() {
		final String id = DEFAULT_ID;
		PlayerWinRate playerWinRate = new PlayerWinRate(id, 7, 3, 0);

		this.repository.add(playerWinRate);
		this.repository.updateScore(id, BattleResult.TIE);
		PlayerWinRate foundValue = this.repository.findOrCrateDefault(id);

		assertEquals(foundValue.getUserId(), playerWinRate.getUserId());
		assertEquals(foundValue.getLose(), playerWinRate.getLose());
		assertEquals(foundValue.getTie(), playerWinRate.getTie() + 1);
		assertEquals(foundValue.getWin(), playerWinRate.getWin());
	}

	@Test
	public void testConditionalUpdateMmr() {
		givenADefaultWinRate();

		whenUpdatingMmr(0, 5);
		thenNewMmrIs(5);
	}

	@Test
	public void testConditionalUpdateMmrTwice() {
		givenADefaultWinRate();

		whenUpdatingMmr(0, 5);
		thenNewMmrIs(5);
		whenUpdatingMmr(5, 10);
		thenNewMmrIs(15);
	}

	@Test
	public void testConditionalUpdateWithWrongOldValueDoesntUpdate() {
		givenADefaultWinRate();

		whenUpdatingMmr(0, 5);
		thenNewMmrIs(5);
		whenUpdatingMmr(10, 10);
		thenNewMmrIs(5);
	}

	private void thenNewMmrIs(int mmr) {
		PlayerWinRate foundValue = this.repository.findOrCrateDefault(DEFAULT_ID);

		assertEquals(foundValue.getMmr(), mmr);
	}

	private void whenUpdatingMmr(Integer oldMmr, Integer deltaMmr) {
		this.repository.updateMmrOnlyIfOldValueIs(DEFAULT_ID, oldMmr, deltaMmr, PlayerWinRateConfiguration.create(CAPPED_MMR, true));
	}

	private void givenADefaultWinRate() {
		PlayerWinRate playerWinRate = new PlayerWinRate(DEFAULT_ID);
		this.repository.add(playerWinRate);
	}
}
